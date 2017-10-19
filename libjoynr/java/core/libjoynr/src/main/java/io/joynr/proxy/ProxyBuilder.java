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

import io.joynr.arbitration.DiscoveryQos;
import io.joynr.exceptions.DiscoveryException;
import io.joynr.exceptions.JoynrRuntimeException;
import io.joynr.messaging.MessagingQos;

/**
 * Builds a proxy instance for the given interface {@literal <T>}. Default proxy properties can be overwritten by the set...Qos
 * methods. After calling build the proxy can be used like a local instance of the provider. All invocations will be
 * queued until either the message TTL expires or the arbitration finishes successfully. Synchronous calls will block
 * until the arbitration is done.
 *
 * @param <T>
 *            Provided interface
 */
public interface ProxyBuilder<T> {

    /**
     * Callback for async proxy creation
     *
     * @param <T> Provided interface
     */
    public interface ProxyCreatedCallback<T> {

        /**
         * Called when the proxy is created and ready to use.
         * Discovery has completed successfully
         *
         * @param result result of proxy creation
         */
        public void onProxyCreationFinished(T result);

        /**
         * Called when an error occurred during proxy creation.
         *
         * @param error A JoynrRuntimeException that prevented proxy creation
         */
        public void onProxyCreationError(JoynrRuntimeException error);
    }

    /**
     * @return The proxies participantId
     */
    // TODO should this really be public ?
    public abstract String getParticipantId();

    public abstract void setParticipantId(String participantId);

    /**
     * Sets arbitration strategy, timeout and strategy specific parameters.
     *
     * @param discoveryQos discovery quality of service
     * @return Returns the ProxyBuilder
     * @throws DiscoveryException in case arbitration fails
     */
    public abstract ProxyBuilder<T> setDiscoveryQos(DiscoveryQos discoveryQos) throws DiscoveryException;

    /**
     * Sets the MessagingQos (e.g. request timeouts) which will be used by the created proxy.
     *
     * @param messagingQos messaging quality of service
     * @return Returns the ProxyBuilder
     */
    public abstract ProxyBuilder<T> setMessagingQos(MessagingQos messagingQos);

    /**
     * Final step to create a proxy object. Make sure all QoS parameters have been set before this method is called. Non
     * blocking.
     *
     * @return Returns a dynamic proxy object, implementing all methods of the interfaces passed in when the
     *         proxyBuilder was created.
     */
    public abstract T build();

    /**
     * Passes a callback to {@link build} that is called when the proxy is finished
     *
     * @param callback callback for asynchronous handling
     * @return Returns a dynamic proxy object, implementing all methods of the interfaces passed in when the
     *         proxyBuilder was created.
     */
    public abstract T build(ProxyCreatedCallback<T> callback);

}
