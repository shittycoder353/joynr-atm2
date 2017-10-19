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
package io.joynr.messaging.routing;

import joynr.ImmutableMessage;

import joynr.system.RoutingTypes.Address;

public interface MessageRouter {
    static final String ROUTER_GLOBAL_ADDRESS = "io.joynr.messaging.globalAddress";
    static final String SCHEDULEDTHREADPOOL = "io.joynr.messaging.scheduledthreadpool";

    public void route(ImmutableMessage message);

    public void addNextHop(String participantId, Address address, boolean isGloballyVisible);

    public void shutdown();

    public void removeNextHop(String participantId);

    public boolean resolveNextHop(String participantId);

    void addMulticastReceiver(String multicastId, String subscriberParticipantId, String providerParticipantId);

    void removeMulticastReceiver(String multicastId, String subscriberParticipantId, String providerParticipantId);

    void registerMessageProcessedListener(MessageProcessedListener messageProcessedListener);

    void unregisterMessageProcessedListener(MessageProcessedListener messageProcessedListener);
}
