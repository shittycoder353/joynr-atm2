package io.joynr.dispatching.subscription;

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

import static io.joynr.runtime.JoynrInjectionConstants.JOYNR_SCHEDULER_CLEANUP;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.joynr.dispatching.Dispatcher;
import io.joynr.exceptions.JoynrRuntimeException;
import io.joynr.messaging.MessagingQos;
import io.joynr.messaging.util.MulticastWildcardRegexFactory;
import io.joynr.proxy.Future;
import io.joynr.proxy.invocation.AttributeSubscribeInvocation;
import io.joynr.proxy.invocation.BroadcastSubscribeInvocation;
import io.joynr.proxy.invocation.MulticastSubscribeInvocation;
import io.joynr.proxy.invocation.SubscriptionInvocation;
import io.joynr.pubsub.HeartbeatSubscriptionInformation;
import io.joynr.pubsub.SubscriptionQos;
import io.joynr.pubsub.subscription.AttributeSubscriptionListener;
import io.joynr.pubsub.subscription.BroadcastSubscriptionListener;
import joynr.BroadcastSubscriptionRequest;
import joynr.MulticastSubscriptionRequest;
import joynr.SubscriptionReply;
import joynr.SubscriptionRequest;
import joynr.SubscriptionStop;
import joynr.types.DiscoveryEntryWithMetaInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SubscriptionManagerImpl implements SubscriptionManager {

    private ConcurrentMap<String, AttributeSubscriptionListener<?>> subscriptionListenerDirectory;
    private ConcurrentMap<String, BroadcastSubscriptionListener> broadcastSubscriptionListenerDirectory;
    private ConcurrentMap<Pattern, Set<String>> multicastSubscribersDirectory;
    private ConcurrentMap<String, Future<String>> subscriptionFutureMap;
    private ConcurrentMap<String, Class<?>> subscriptionTypes;
    private ConcurrentMap<String, Class<?>[]> unicastBroadcastTypes;
    private ConcurrentMap<Pattern, Class<?>[]> multicastBroadcastTypes;
    private ConcurrentMap<String, PubSubState> subscriptionStates;
    private ConcurrentMap<String, MissedPublicationTimer> missedPublicationTimers;
    private ConcurrentMap<String, ScheduledFuture<?>> subscriptionEndFutures; // These futures will be needed if a
    // subscription
    // should be updated with a new end time

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionManagerImpl.class);
    private ScheduledExecutorService cleanupScheduler;
    private Dispatcher dispatcher;

    private final MulticastWildcardRegexFactory multicastWildcardRegexFactory;

    @Inject
    public SubscriptionManagerImpl(@Named(JOYNR_SCHEDULER_CLEANUP) ScheduledExecutorService cleanupScheduler,
                                   Dispatcher dispatcher,
                                   MulticastWildcardRegexFactory multicastWildcardRegexFactory) {
        this.cleanupScheduler = cleanupScheduler;
        this.dispatcher = dispatcher;
        this.subscriptionListenerDirectory = Maps.newConcurrentMap();
        this.broadcastSubscriptionListenerDirectory = Maps.newConcurrentMap();
        this.multicastSubscribersDirectory = Maps.newConcurrentMap();
        this.subscriptionStates = Maps.newConcurrentMap();
        this.missedPublicationTimers = Maps.newConcurrentMap();
        this.subscriptionEndFutures = Maps.newConcurrentMap();
        this.subscriptionTypes = Maps.newConcurrentMap();
        this.unicastBroadcastTypes = Maps.newConcurrentMap();
        this.multicastBroadcastTypes = Maps.newConcurrentMap();
        this.subscriptionFutureMap = Maps.newConcurrentMap();
        this.multicastWildcardRegexFactory = multicastWildcardRegexFactory;
    }

    // CHECKSTYLE IGNORE ParameterNumber FOR NEXT 1 LINES
    public SubscriptionManagerImpl(ConcurrentMap<String, AttributeSubscriptionListener<?>> attributeSubscriptionDirectory,
                                   ConcurrentMap<String, BroadcastSubscriptionListener> broadcastSubscriptionDirectory,
                                   ConcurrentMap<Pattern, Set<String>> multicastSubscribersDirectory,
                                   ConcurrentMap<String, PubSubState> subscriptionStates,
                                   ConcurrentMap<String, MissedPublicationTimer> missedPublicationTimers,
                                   ConcurrentMap<String, ScheduledFuture<?>> subscriptionEndFutures,
                                   ConcurrentMap<String, Class<?>> subscriptionAttributeTypes,
                                   ConcurrentMap<String, Class<?>[]> unicastBroadcastTypes,
                                   ConcurrentMap<Pattern, Class<?>[]> multicastBroadcastTypes,
                                   ConcurrentMap<String, Future<String>> subscriptionFutureMap,
                                   ScheduledExecutorService cleanupScheduler,
                                   Dispatcher dispatcher,
                                   MulticastWildcardRegexFactory multicastWildcardRegexFactory) {
        super();
        this.subscriptionListenerDirectory = attributeSubscriptionDirectory;
        this.broadcastSubscriptionListenerDirectory = broadcastSubscriptionDirectory;
        this.multicastSubscribersDirectory = multicastSubscribersDirectory;
        this.subscriptionStates = subscriptionStates;
        this.missedPublicationTimers = missedPublicationTimers;
        this.subscriptionEndFutures = subscriptionEndFutures;
        this.subscriptionTypes = subscriptionAttributeTypes;
        this.unicastBroadcastTypes = unicastBroadcastTypes;
        this.multicastBroadcastTypes = multicastBroadcastTypes;
        this.cleanupScheduler = cleanupScheduler;
        this.dispatcher = dispatcher;
        this.subscriptionFutureMap = subscriptionFutureMap;
        this.multicastWildcardRegexFactory = multicastWildcardRegexFactory;
    }

    private void cancelExistingSubscriptionEndRunnable(String subscriptionId) {
        ScheduledFuture<?> scheduledFuture = subscriptionEndFutures.get(subscriptionId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
    }

    private void registerSubscription(final SubscriptionQos qos, String subscriptionId) {

        cancelExistingSubscriptionEndRunnable(subscriptionId);

        PubSubState subState = new PubSubState();
        subState.updateTimeOfLastPublication();
        subscriptionStates.put(subscriptionId, subState);

        long expiryDate = qos.getExpiryDateMs();
        logger.trace("subscription: {} expiryDate: "
                             + (expiryDate == SubscriptionQos.NO_EXPIRY_DATE ? "never" : expiryDate
                                     - System.currentTimeMillis()),
                     subscriptionId);

        if (expiryDate != SubscriptionQos.NO_EXPIRY_DATE) {
            SubscriptionEndRunnable endRunnable = new SubscriptionEndRunnable(subscriptionId);
            ScheduledFuture<?> subscriptionEndFuture = cleanupScheduler.schedule(endRunnable,
                                                                                 expiryDate,
                                                                                 TimeUnit.MILLISECONDS);
            subscriptionEndFutures.put(subscriptionId, subscriptionEndFuture);
        }
    }

    @Override
    public void registerAttributeSubscription(String fromParticipantId,
                                              Set<DiscoveryEntryWithMetaInfo> toDiscoveryEntries,
                                              final AttributeSubscribeInvocation request) {
        registerSubscription(fromParticipantId,
                             toDiscoveryEntries,
                             request,
                             new RegisterDataAndCreateSubscriptionRequest() {
                                 @Override
                                 public SubscriptionRequest execute() {
                                     SubscriptionQos qos = request.getQos();
                                     logger.trace("Attribute subscription registered with Id: "
                                             + request.getSubscriptionId());
                                     subscriptionTypes.put(request.getSubscriptionId(),
                                                           request.getAttributeTypeReference());
                                     subscriptionListenerDirectory.put(request.getSubscriptionId(),
                                                                       request.getAttributeSubscriptionListener());
                                     subscriptionFutureMap.put(request.getSubscriptionId(), request.getFuture());

                                     if (qos instanceof HeartbeatSubscriptionInformation) {
                                         HeartbeatSubscriptionInformation heartbeat = (HeartbeatSubscriptionInformation) qos;

                                         // alerts only if alert after interval > 0
                                         if (heartbeat.getAlertAfterIntervalMs() > 0) {

                                             logger.trace("Will notify if updates are missed.");

                                             missedPublicationTimers.put(request.getSubscriptionId(),
                                                                         new MissedPublicationTimer(qos.getExpiryDateMs(),
                                                                                                    heartbeat.getPeriodMs(),
                                                                                                    heartbeat.getAlertAfterIntervalMs(),
                                                                                                    request.getAttributeSubscriptionListener(),
                                                                                                    subscriptionStates.get(request.getSubscriptionId()),
                                                                                                    request.getSubscriptionId()));
                                         }
                                     }

                                     return new SubscriptionRequest(request.getSubscriptionId(),
                                                                    request.getAttributeName(),
                                                                    request.getQos());
                                 }
                             });

    }

    @Override
    public void registerBroadcastSubscription(String fromParticipantId,
                                              Set<DiscoveryEntryWithMetaInfo> toDiscoveryEntries,
                                              final BroadcastSubscribeInvocation request) {
        registerSubscription(fromParticipantId,
                             toDiscoveryEntries,
                             request,
                             new RegisterDataAndCreateSubscriptionRequest() {
                                 @Override
                                 public SubscriptionRequest execute() {
                                     String subscriptionId = request.getSubscriptionId();
                                     logger.trace("Broadcast subscription registered with Id: " + subscriptionId);
                                     unicastBroadcastTypes.put(subscriptionId, request.getOutParameterTypes());
                                     broadcastSubscriptionListenerDirectory.put(subscriptionId,
                                                                                request.getBroadcastSubscriptionListener());
                                     return new BroadcastSubscriptionRequest(request.getSubscriptionId(),
                                                                             request.getBroadcastName(),
                                                                             request.getFilterParameters(),
                                                                             request.getQos());
                                 }
                             });
    }

    @Override
    public void registerMulticastSubscription(String fromParticipantId,
                                              Set<DiscoveryEntryWithMetaInfo> toDiscoveryEntries,
                                              final MulticastSubscribeInvocation multicastSubscribeInvocation) {
        for (DiscoveryEntryWithMetaInfo toDiscoveryEntry : toDiscoveryEntries) {
            final String multicastId = MulticastIdUtil.createMulticastId(toDiscoveryEntry.getParticipantId(),
                                                                         multicastSubscribeInvocation.getSubscriptionName(),
                                                                         multicastSubscribeInvocation.getPartitions());
            registerSubscription(fromParticipantId,
                                 toDiscoveryEntries,
                                 multicastSubscribeInvocation,
                                 new RegisterDataAndCreateSubscriptionRequest() {
                                     @Override
                                     public SubscriptionRequest execute() {
                                         String subscriptionId = multicastSubscribeInvocation.getSubscriptionId();
                                         logger.trace("Multicast subscription registered with Id: " + subscriptionId);
                                         Pattern multicastIdPattern = multicastWildcardRegexFactory.createIdPattern(multicastId);
                                         if (!multicastSubscribersDirectory.containsKey(multicastIdPattern)) {
                                             multicastSubscribersDirectory.putIfAbsent(multicastIdPattern,
                                                                                       Sets.<String> newHashSet());
                                         }
                                         multicastSubscribersDirectory.get(multicastIdPattern).add(subscriptionId);
                                         multicastBroadcastTypes.putIfAbsent(multicastIdPattern,
                                                                             multicastSubscribeInvocation.getOutParameterTypes());
                                         broadcastSubscriptionListenerDirectory.put(subscriptionId,
                                                                                    multicastSubscribeInvocation.getListener());
                                         return new MulticastSubscriptionRequest(multicastId,
                                                                                 multicastSubscribeInvocation.getSubscriptionId(),
                                                                                 multicastSubscribeInvocation.getSubscriptionName(),
                                                                                 multicastSubscribeInvocation.getQos());
                                     }
                                 });
            logger.debug("SUBSCRIPTION call proxy: subscriptionId: {}, multicastId: {}, broadcast: {}, qos: {}, proxy participantId: {}, provider: {}",
                         multicastSubscribeInvocation.getSubscriptionId(),
                         multicastId,
                         multicastSubscribeInvocation.getSubscriptionName(),
                         multicastSubscribeInvocation.getQos(),
                         fromParticipantId,
                         toDiscoveryEntry);
        }
    }

    private static interface RegisterDataAndCreateSubscriptionRequest {
        SubscriptionRequest execute();
    }

    private void registerSubscription(String fromParticipantId,
                                      Set<DiscoveryEntryWithMetaInfo> toDiscoveryEntries,
                                      SubscriptionInvocation subscriptionInvocation,
                                      RegisterDataAndCreateSubscriptionRequest registerDataAndCreateSubscriptionRequest) {
        if (!subscriptionInvocation.hasSubscriptionId()) {
            subscriptionInvocation.setSubscriptionId(UUID.randomUUID().toString());
        }
        String subscriptionId = subscriptionInvocation.getSubscriptionId();
        subscriptionFutureMap.put(subscriptionId, subscriptionInvocation.getFuture());
        registerSubscription(subscriptionInvocation.getQos(), subscriptionId);

        SubscriptionRequest subscriptionRequest = registerDataAndCreateSubscriptionRequest.execute();

        MessagingQos messagingQos = new MessagingQos();
        SubscriptionQos qos = subscriptionRequest.getQos();
        if (qos.getExpiryDateMs() == SubscriptionQos.NO_EXPIRY_DATE) {
            messagingQos.setTtl_ms(SubscriptionQos.INFINITE_SUBSCRIPTION);
        } else {
            messagingQos.setTtl_ms(qos.getExpiryDateMs() - System.currentTimeMillis());
        }

        dispatcher.sendSubscriptionRequest(fromParticipantId, toDiscoveryEntries, subscriptionRequest, messagingQos);
    }

    @Override
    public void unregisterSubscription(String fromParticipantId,
                                       Set<DiscoveryEntryWithMetaInfo> toDiscoveryEntries,
                                       String subscriptionId,
                                       MessagingQos qosSettings) {
        PubSubState subscriptionState = subscriptionStates.get(subscriptionId);
        if (subscriptionState != null) {
            logger.trace("Called unregister / unsubscribe on subscription id= " + subscriptionId);
            removeSubscription(subscriptionId);
        } else {
            logger.trace("Called unregister on a non/no longer existent subscription, used id= " + subscriptionId);
        }

        SubscriptionStop subscriptionStop = new SubscriptionStop(subscriptionId);

        dispatcher.sendSubscriptionStop(fromParticipantId,
                                        toDiscoveryEntries,
                                        subscriptionStop,
                                        new MessagingQos(qosSettings));
    }

    @Override
    public void handleBroadcastPublication(String subscriptionId, Object[] broadcastValues) {
        BroadcastSubscriptionListener broadcastSubscriptionListener = getBroadcastSubscriptionListener(subscriptionId);

        try {
            Class<?>[] broadcastTypes = getParameterTypesForBroadcastPublication(broadcastValues);
            Method receive = broadcastSubscriptionListener.getClass().getDeclaredMethod("onReceive", broadcastTypes);
            if (!receive.isAccessible()) {
                receive.setAccessible(true);
            }

            logger.debug("SUBSCRIPTION notify listener: subscriptionId: {}, broadcastValue: {}",
                         subscriptionId,
                         broadcastValues);
            receive.invoke(broadcastSubscriptionListener, broadcastValues);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            logger.error("Broadcast publication could not be processed", e);
        }
    }

    @Override
    public void handleMulticastPublication(String multicastId, Object[] publicizedValues) {
        for (Map.Entry<Pattern, Set<String>> entry : multicastSubscribersDirectory.entrySet()) {
            if (entry.getKey().matcher(multicastId).matches()) {
                for (String subscriptionId : entry.getValue()) {
                    logger.trace("SUBSCRIPTION notify listener: subscriptionId: {}, multicastId: {}, broadcastValue: {}",
                                 subscriptionId,
                                 multicastId,
                                 publicizedValues);
                    handleBroadcastPublication(subscriptionId, publicizedValues);
                }
            }
        }
    }

    @Override
    public <T> void handleAttributePublication(String subscriptionId, T attributeValue) {
        touchSubscriptionState(subscriptionId);
        AttributeSubscriptionListener<T> listener = getSubscriptionListener(subscriptionId);
        if (listener == null) {
            logger.error("No subscription listener found for incoming publication!");
        } else {
            logger.debug("SUBSCRIPTION notify listener: subscriptionId: {}, attributeValue: {}",
                         subscriptionId,
                         attributeValue);
            listener.onReceive(attributeValue);
        }
    }

    @Override
    public <T> void handleAttributePublicationError(String subscriptionId, JoynrRuntimeException error) {
        touchSubscriptionState(subscriptionId);
        AttributeSubscriptionListener<T> listener = getSubscriptionListener(subscriptionId);
        if (listener == null) {
            logger.error("No subscription listener found for incoming publication!");
        } else {
            logger.debug("SUBSCRIPTION notify listener: subscriptionId: {}, error: {}", subscriptionId, error);
            listener.onError(error);
        }
    }

    @Override
    public void handleSubscriptionReply(final SubscriptionReply subscriptionReply) {
        String subscriptionId = subscriptionReply.getSubscriptionId();

        if (subscriptionReply.getError() == null) {
            if (subscriptionFutureMap.containsKey(subscriptionId)) {
                subscriptionFutureMap.remove(subscriptionId).onSuccess(subscriptionId);
            }

            if (subscriptionListenerDirectory.containsKey(subscriptionId)) {
                subscriptionListenerDirectory.get(subscriptionId).onSubscribed(subscriptionId);
            } else if (broadcastSubscriptionListenerDirectory.containsKey(subscriptionId)) {
                broadcastSubscriptionListenerDirectory.get(subscriptionId).onSubscribed(subscriptionId);
            } else {
                logger.warn("No subscription listener found for incoming subscription reply for subscription ID {}!",
                            subscriptionId);
            }
        } else {
            logger.trace("Handling subscription reply with error: {}", subscriptionReply.getError());
            if (subscriptionFutureMap.containsKey(subscriptionId)) {
                subscriptionFutureMap.remove(subscriptionId).onFailure(subscriptionReply.getError());
            }

            if (subscriptionListenerDirectory.containsKey(subscriptionId)) {
                subscriptionListenerDirectory.remove(subscriptionId).onError(subscriptionReply.getError());
            } else if (broadcastSubscriptionListenerDirectory.containsKey(subscriptionId)) {
                broadcastSubscriptionListenerDirectory.remove(subscriptionId).onError(subscriptionReply.getError());
            } else {
                logger.warn("No subscription listener found for incoming subscription reply for subscription ID {}! Error message: {}",
                            subscriptionId,
                            subscriptionReply.getError().getMessage());
            }
            subscriptionTypes.remove(subscriptionId);
        }
    }

    @Override
    public void touchSubscriptionState(final String subscriptionId) {
        logger.trace("Touching subscription state for id=" + subscriptionId);
        if (!subscriptionStates.containsKey(subscriptionId)) {
            logger.trace("No subscription state found for id: " + subscriptionId);
            return;
        }
        PubSubState subscriptionState = subscriptionStates.get(subscriptionId);
        subscriptionState.updateTimeOfLastPublication();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> AttributeSubscriptionListener<T> getSubscriptionListener(final String subscriptionId) {
        if (!subscriptionStates.containsKey(subscriptionId)
                || !subscriptionListenerDirectory.containsKey(subscriptionId)) {
            logger.error("Received publication for not existing subscription callback with id=" + subscriptionId);
        }
        return (AttributeSubscriptionListener<T>) subscriptionListenerDirectory.get(subscriptionId);

    }

    @Override
    public BroadcastSubscriptionListener getBroadcastSubscriptionListener(String subscriptionId) {
        if (!subscriptionStates.containsKey(subscriptionId)
                || !broadcastSubscriptionListenerDirectory.containsKey(subscriptionId)) {
            logger.error("Received publication for not existing subscription callback with id=" + subscriptionId);
        }
        return broadcastSubscriptionListenerDirectory.get(subscriptionId);

    }

    @Override
    public boolean isBroadcast(String subscriptionId) {
        return broadcastSubscriptionListenerDirectory.containsKey(subscriptionId);
    }

    @Override
    public Class<?> getAttributeType(final String subscriptionId) {
        return subscriptionTypes.get(subscriptionId);
    }

    @Override
    public Class<?>[] getUnicastPublicationOutParameterTypes(String subscriptionId) {
        return unicastBroadcastTypes.get(subscriptionId);
    }

    @Override
    public Class<?>[] getMulticastPublicationOutParameterTypes(String multicastId) {
        Class<?>[] outParamterTypes = null;
        for (Map.Entry<Pattern, Class<?>[]> entry : multicastBroadcastTypes.entrySet()) {
            if (entry.getKey().matcher(multicastId).matches()) {
                outParamterTypes = entry.getValue();
                break;
            }
        }
        return outParamterTypes;
    }

    private void removeSubscription(String subscriptionId) {
        if (missedPublicationTimers.containsKey(subscriptionId)) {
            missedPublicationTimers.get(subscriptionId).cancel();
            missedPublicationTimers.remove(subscriptionId);
        }
        ScheduledFuture<?> future = subscriptionEndFutures.remove(subscriptionId);
        if (future != null) {
            future.cancel(true);
        }
        subscriptionStates.remove(subscriptionId);
        subscriptionListenerDirectory.remove(subscriptionId);
        unicastBroadcastTypes.remove(subscriptionId);
        broadcastSubscriptionListenerDirectory.remove(subscriptionId);
        for (Entry<Pattern, Set<String>> entry : multicastSubscribersDirectory.entrySet()) {
            Set<String> subscriptionIds = entry.getValue();
            subscriptionIds.remove(subscriptionId);
            if (subscriptionIds.isEmpty()) {
                multicastBroadcastTypes.remove(entry.getKey());
            }
        }
        subscriptionTypes.remove(subscriptionId);
    }

    private Class<?>[] getParameterTypesForBroadcastPublication(Object[] broadcastValues) {
        List<Class<?>> parameterTypes = new ArrayList<Class<?>>(broadcastValues.length);
        for (int i = 0; i < broadcastValues.length; i++) {
            parameterTypes.add(broadcastValues[i].getClass());
        }
        return parameterTypes.toArray(new Class<?>[parameterTypes.size()]);
    }

    class SubscriptionEndRunnable implements Runnable {
        private String subscriptionId;

        public SubscriptionEndRunnable(String subscriptionId) {
            super();
            this.subscriptionId = subscriptionId;
        }

        @Override
        public void run() {
            // subscription expired, stop the missed publication timer and remove it from the maps
            removeSubscription(subscriptionId);
        }

    }
}
