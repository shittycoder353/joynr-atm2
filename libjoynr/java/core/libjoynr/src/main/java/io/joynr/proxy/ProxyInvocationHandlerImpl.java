package io.joynr.proxy;

/*
 * #%L
 * %%
 * Copyright (C) 2011 - 2017 BMW Car IT GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.joynr.Async;
import io.joynr.Sync;
import io.joynr.arbitration.ArbitrationResult;
import io.joynr.arbitration.DiscoveryQos;
import io.joynr.dispatcher.rpc.JoynrBroadcastSubscriptionInterface;
import io.joynr.dispatcher.rpc.JoynrSubscriptionInterface;
import io.joynr.dispatcher.rpc.annotation.FireAndForget;
import io.joynr.dispatcher.rpc.annotation.JoynrMulticast;
import io.joynr.dispatcher.rpc.annotation.JoynrRpcBroadcast;
import io.joynr.exceptions.DiscoveryException;
import io.joynr.exceptions.JoynrException;
import io.joynr.exceptions.JoynrIllegalStateException;
import io.joynr.exceptions.JoynrRuntimeException;
import io.joynr.messaging.MessagingQos;
import io.joynr.proxy.invocation.AttributeSubscribeInvocation;
import io.joynr.proxy.invocation.BroadcastSubscribeInvocation;
import io.joynr.proxy.invocation.Invocation;
import io.joynr.proxy.invocation.MethodInvocation;
import io.joynr.proxy.invocation.MulticastSubscribeInvocation;
import io.joynr.proxy.invocation.SubscriptionInvocation;
import io.joynr.proxy.invocation.UnsubscribeInvocation;
import joynr.MethodMetaInformation;
import joynr.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyInvocationHandlerImpl extends ProxyInvocationHandler {
    private ConnectorFactory connectorFactory;
    private final MessagingQos qosSettings;
    private ConnectorStatus connectorStatus;
    private Lock connectorStatusLock = new ReentrantLock();
    private Condition connectorSuccessfullyFinished = connectorStatusLock.newCondition();
    private DiscoveryQos discoveryQos;
    private ConnectorInvocationHandler connector;
    private final String proxyParticipantId;
    private ConcurrentLinkedQueue<MethodInvocation<?>> queuedRpcList = new ConcurrentLinkedQueue<MethodInvocation<?>>();
    private ConcurrentLinkedQueue<SubscriptionAction> queuedSubscriptionInvocationList = new ConcurrentLinkedQueue<SubscriptionAction>();
    private ConcurrentLinkedQueue<UnsubscribeInvocation> queuedUnsubscripeInvocationList = new ConcurrentLinkedQueue<UnsubscribeInvocation>();
    private String interfaceName;
    private Set<String> domains;

    private static final Logger logger = LoggerFactory.getLogger(ProxyInvocationHandlerImpl.class);

    @Inject
    public ProxyInvocationHandlerImpl(@Assisted("domains") Set<String> domains,
                                      @Assisted("interfaceName") String interfaceName,
                                      @Assisted("proxyParticipantId") String proxyParticipantId,
                                      @Assisted DiscoveryQos discoveryQos,
                                      @Assisted MessagingQos messagingQos,
                                      ConnectorFactory connectorFactory) {
        this.domains = domains;
        this.proxyParticipantId = proxyParticipantId;
        this.interfaceName = interfaceName;
        this.discoveryQos = discoveryQos;
        this.qosSettings = messagingQos;
        this.connectorFactory = connectorFactory;
        this.connectorStatus = ConnectorStatus.ConnectorNotAvailabe;
    }

    private static interface ConnectorCaller {
        Object call(Method method, Object[] args) throws ApplicationException;
    }

    /**
     * executeSyncMethod is called whenever a method of the synchronous interface which is provided by the proxy is
     * called. The ProxyInvocationHandler will check the arbitration status before the call is delegated to the
     * connector. If the arbitration is still in progress the synchronous call will block until the arbitration was
     * successful or the timeout elapsed.
     *
     * @throws ApplicationException
     *
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @CheckForNull
    private Object executeSyncMethod(Method method, Object[] args) throws ApplicationException {
        return executeMethodWithCaller(method, args, new ConnectorCaller() {
            @Override
            public Object call(Method method, Object[] args) throws ApplicationException {
                return connector.executeSyncMethod(method, args);
            }
        });
    }

    @CheckForNull
    private Object executeOneWayMethod(Method method, Object[] args) throws ApplicationException {
        return executeMethodWithCaller(method, args, new ConnectorCaller() {

            @Override
            public Object call(Method method, Object[] args) {
                connector.executeOneWayMethod(method, args);
                return null;
            }
        });
    }

    private Object executeMethodWithCaller(Method method, Object[] args, ConnectorCaller connectorCaller)
                                                                                                         throws ApplicationException {
        try {
            if (waitForConnectorFinished()) {
                if (connector == null) {
                    throw new IllegalStateException("connector was null although arbitration finished successfully");
                }
                return connectorCaller.call(method, args);
            }
        } catch (ApplicationException | JoynrRuntimeException e) {
            throw e;

        } catch (Exception e) {
            throw new JoynrRuntimeException(e);
        }

        if (throwable != null) {
            if (throwable instanceof JoynrRuntimeException) {
                throw (JoynrRuntimeException) throwable;
            } else {
                throw new JoynrRuntimeException(throwable);
            }
        } else {
            throw new DiscoveryException("Arbitration and Connector failed: domain: " + domains + " interface: "
                    + interfaceName + " qos: " + discoveryQos + ": Arbitration could not be finished in time.");
        }
    }

    /**
     * Checks the connector status before a method call is executed. Instantly returns True if the connector already
     * finished successfully , otherwise it will block up to the amount of milliseconds defined by the
     * arbitrationTimeout or until the ProxyInvocationHandler is notified about a successful connection.
     *
     * @return True if the connector was finished successfully in time, False if the connector failed or could not be
     *         finished in time.
     * @throws InterruptedException in case thread is interrupted
     */
    public boolean waitForConnectorFinished() throws InterruptedException {
        connectorStatusLock.lock();
        try {
            if (connectorStatus == ConnectorStatus.ConnectorSuccesful) {
                return true;
            }

            return connectorSuccessfullyFinished.await(discoveryQos.getDiscoveryTimeoutMs(), TimeUnit.MILLISECONDS);

        } finally {
            connectorStatusLock.unlock();
        }

    }

    /**
     * Checks if the connector was set successfully. Returns immediately and does not block until the connector is
     * finished.
     *
     * @return true if a connector was successfully set.
     */
    public boolean isConnectorReady() {
        connectorStatusLock.lock();
        try {
            if (connectorStatus == ConnectorStatus.ConnectorSuccesful) {
                return true;
            }
            return false;

        } finally {
            connectorStatusLock.unlock();
        }

    }

    private void sendQueuedSubscriptionInvocations() {
        while (true) {
            SubscriptionAction currentSubscriptionAction = queuedSubscriptionInvocationList.poll();
            if (currentSubscriptionAction == null) {
                return;
            }
            try {
                currentSubscriptionAction.subscribe();
            } catch (JoynrRuntimeException e) {
                currentSubscriptionAction.fail(e);
            } catch (Exception e) {
                currentSubscriptionAction.fail(new JoynrRuntimeException(e));
            }
        }
    }

    private void sendQueuedUnsubscribeInvocations() {
        while (true) {
            UnsubscribeInvocation unsubscribeInvocation = queuedUnsubscripeInvocationList.poll();
            if (unsubscribeInvocation == null) {
                return;
            }
            try {
                connector.executeSubscriptionMethod(unsubscribeInvocation);
            } catch (JoynrRuntimeException e) {
                unsubscribeInvocation.getFuture().onFailure(e);
            } catch (Exception e) {
                unsubscribeInvocation.getFuture().onFailure(new JoynrRuntimeException(e));
            }
        }
    }

    private void setFutureErrorState(Invocation<?> invocation, JoynrRuntimeException e) {
        invocation.getFuture().onFailure(e);
    }

    /**
     * Executes previously queued remote calls. This method is called when arbitration is completed.
     */
    private void sendQueuedInvocations() {
        while (true) {
            MethodInvocation<?> currentRPC = queuedRpcList.poll();
            if (currentRPC == null) {
                return;
            }

            try {
                connector.executeAsyncMethod(currentRPC.getMethod(), currentRPC.getArgs(), currentRPC.getFuture());
            } catch (JoynrRuntimeException e) {
                currentRPC.getFuture().onFailure(e);
            } catch (Exception e) {
                currentRPC.getFuture().onFailure(new JoynrRuntimeException(e));
            }

        }

    }

    /**
     * Sets the connector for this ProxyInvocationHandler after the DiscoveryAgent got notified about a successful
     * arbitration. Should be called from the DiscoveryAgent
     *
     * @param result
     *            from the previously invoked arbitration
     */
    @Override
    public void createConnector(ArbitrationResult result) {
        connector = connectorFactory.create(proxyParticipantId, result, qosSettings);
        connectorStatusLock.lock();
        try {
            connectorStatus = ConnectorStatus.ConnectorSuccesful;
            connectorSuccessfullyFinished.signalAll();

            if (connector != null) {
                sendQueuedInvocations();
                sendQueuedSubscriptionInvocations();
                sendQueuedUnsubscribeInvocations();
            }
        } finally {
            connectorStatusLock.unlock();
        }
    }

    @CheckForNull
    private Object executeSubscriptionMethod(Method method, Object[] args) {
        Future<String> future = new Future<String>();
        if (method.getName().startsWith("subscribeTo")) {
            if (JoynrSubscriptionInterface.class.isAssignableFrom(method.getDeclaringClass())) {
                executeAttributeSubscriptionMethod(method, args, future);
            } else if (method.getAnnotation(JoynrRpcBroadcast.class) != null) {
                executeBroadcastSubscriptionMethod(method, args, future);
            } else if (method.getAnnotation(JoynrMulticast.class) != null) {
                executeMulticastSubscriptionMethod(method, args, future);
            } else {
                throw new JoynrRuntimeException("Method "
                        + method
                        + " not declared in JoynrSubscriptionInterface or annotated with either @JoynrRpcBroadcast or @JoynrMulticast.");
            }
            return future;
        } else if (method.getName().startsWith("unsubscribeFrom")) {
            return unsubscribe(new UnsubscribeInvocation(method, args, future)).getSubscriptionId();
        } else {
            throw new JoynrIllegalStateException("Called unknown method in one of the subscription interfaces.");
        }
    }

    private static abstract class SubscriptionAction {
        private Future<String> future;

        private SubscriptionAction(Future<String> future) {
            this.future = future;
        }

        protected abstract void subscribe();

        private void fail(JoynrException joynrException) {
            future.onFailure(joynrException);
        }
    }

    private void executeAttributeSubscriptionMethod(Method method, Object[] args, Future<String> future) {
        final AttributeSubscribeInvocation attributeSubscription = new AttributeSubscribeInvocation(method,
                                                                                                    args,
                                                                                                    future);
        queueOrExecuteSubscriptionInvocation(attributeSubscription, new SubscriptionAction(future) {
            @Override
            public void subscribe() {
                connector.executeSubscriptionMethod(attributeSubscription);
            }
        });

    }

    private void executeBroadcastSubscriptionMethod(Method method, Object[] args, Future<String> future) {
        final BroadcastSubscribeInvocation broadcastSubscription = new BroadcastSubscribeInvocation(method,
                                                                                                    args,
                                                                                                    future);
        queueOrExecuteSubscriptionInvocation(broadcastSubscription, new SubscriptionAction(future) {
            @Override
            public void subscribe() {
                connector.executeSubscriptionMethod(broadcastSubscription);
            }
        });
    }

    private void executeMulticastSubscriptionMethod(Method method, Object[] args, Future<String> future) {
        final MulticastSubscribeInvocation multicastSubscription = new MulticastSubscribeInvocation(method,
                                                                                                    args,
                                                                                                    future);
        queueOrExecuteSubscriptionInvocation(multicastSubscription, new SubscriptionAction(future) {
            @Override
            public void subscribe() {
                connector.executeSubscriptionMethod(multicastSubscription);
            }
        });
    }

    private void queueOrExecuteSubscriptionInvocation(SubscriptionInvocation subscriptionInvocation,
                                                      SubscriptionAction subscriptionMethodExecutor) {
        connectorStatusLock.lock();
        try {
            if (!isConnectorReady()) {
                queuedSubscriptionInvocationList.offer(subscriptionMethodExecutor);
                return;
            }
        } finally {
            connectorStatusLock.unlock();
        }
        try {
            subscriptionMethodExecutor.subscribe();
        } catch (JoynrRuntimeException e) {
            logger.error("error executing subscription: {} : {}",
                         subscriptionInvocation.getSubscriptionName(),
                         e.getMessage());
            setFutureErrorState(subscriptionInvocation, e);
        } catch (Exception e) {
            logger.error("error executing subscription: {} : {}",
                         subscriptionInvocation.getSubscriptionName(),
                         e.getMessage());
            setFutureErrorState(subscriptionInvocation, new JoynrRuntimeException(e));
        }
    }

    private <T> Object executeAsyncMethod(Method method, Object[] args) throws IllegalAccessException, Exception {
        @SuppressWarnings("unchecked")
        Future<T> future = (Future<T>) method.getReturnType().getConstructor().newInstance();

        connectorStatusLock.lock();
        try {
            if (!isConnectorReady()) {
                // waiting for arbitration -> queue invocation
                queuedRpcList.offer(new MethodInvocation<T>(method, args, future));
                return future;
            }
        } finally {
            connectorStatusLock.unlock();
        }

        // arbitration already successfully finished -> send invocation
        return connector.executeAsyncMethod(method, args, future);
    }

    private UnsubscribeInvocation unsubscribe(UnsubscribeInvocation unsubscribeInvocation) {
        connectorStatusLock.lock();
        try {
            if (!isConnectorReady()) {
                queuedUnsubscripeInvocationList.offer(unsubscribeInvocation);
                return unsubscribeInvocation;
            }
        } finally {
            connectorStatusLock.unlock();
        }

        try {
            connector.executeSubscriptionMethod(unsubscribeInvocation);
        } catch (JoynrRuntimeException e) {
            logger.error("error executing unsubscription: {} : {}",
                         unsubscribeInvocation.getSubscriptionId(),
                         e.getMessage());
            setFutureErrorState(unsubscribeInvocation, e);
        } catch (Exception e) {
            logger.error("error executing unsubscription: {} : {}",
                         unsubscribeInvocation.getSubscriptionId(),
                         e.getMessage());
            setFutureErrorState(unsubscribeInvocation, new JoynrRuntimeException(e));
        }
        return unsubscribeInvocation;
    }

    @Override
    @CheckForNull
    public Object invoke(@Nonnull Method method, Object[] args) throws ApplicationException {
        logger.trace("calling proxy.{}({}) on domain: {} and interface {}, proxy participant ID: {}",
                     method.getName(),
                     args,
                     domains,
                     interfaceName,
                     proxyParticipantId);
        Class<?> methodInterfaceClass = method.getDeclaringClass();
        try {
            if (JoynrSubscriptionInterface.class.isAssignableFrom(methodInterfaceClass)
                    || JoynrBroadcastSubscriptionInterface.class.isAssignableFrom(methodInterfaceClass)) {
                return executeSubscriptionMethod(method, args);
            } else if (methodInterfaceClass.getAnnotation(FireAndForget.class) != null) {
                return executeOneWayMethod(method, args);
            } else if (methodInterfaceClass.getAnnotation(Sync.class) != null) {
                return executeSyncMethod(method, args);
            } else if (methodInterfaceClass.getAnnotation(Async.class) != null) {
                return executeAsyncMethod(method, args);
            } else {
                throw new JoynrIllegalStateException("Method is not part of sync, async or subscription interface");
            }
        } catch (JoynrRuntimeException | ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new JoynrRuntimeException(e);
        }
    }

    @Override
    public void abort(JoynrRuntimeException exception) {
        setThrowableForInvoke(exception);

        for (Iterator<MethodInvocation<?>> iterator = queuedRpcList.iterator(); iterator.hasNext();) {
            MethodInvocation<?> invocation = iterator.next();
            try {
                MethodMetaInformation metaInfo = new MethodMetaInformation(invocation.getMethod());
                int callbackIndex = metaInfo.getCallbackIndex();
                if (callbackIndex > -1) {
                    ICallback callback = (ICallback) invocation.getArgs()[callbackIndex];
                    callback.onFailure(exception);
                }
            } catch (Exception metaInfoException) {
                logger.error("aborting call to method: " + invocation.getMethod().getName()
                                     + " but unable to call onError callback because of: "
                                     + metaInfoException.getMessage(),
                             metaInfoException);
            }
            invocation.getFuture().onFailure(exception);
        }

        for (Iterator<UnsubscribeInvocation> iterator = queuedUnsubscripeInvocationList.iterator(); iterator.hasNext();) {
            Invocation<String> invocation = iterator.next();
            invocation.getFuture().onFailure(exception);
        }
        for (SubscriptionAction subscriptionAction : queuedSubscriptionInvocationList) {
            subscriptionAction.fail(exception);
        }
    }
}
