package io.joynr.messaging.routing;

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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.joynr.provider.Deferred;
import io.joynr.provider.DeferredVoid;
import io.joynr.provider.Promise;
import io.joynr.runtime.GlobalAddressProvider;
import io.joynr.runtime.ReplyToAddressProvider;
import joynr.system.RoutingAbstractProvider;
import joynr.system.RoutingTypes.Address;
import joynr.system.RoutingTypes.BrowserAddress;
import joynr.system.RoutingTypes.ChannelAddress;
import joynr.system.RoutingTypes.CommonApiDbusAddress;
import joynr.system.RoutingTypes.MqttAddress;
import joynr.system.RoutingTypes.RoutingTypesUtil;
import joynr.system.RoutingTypes.WebSocketAddress;
import joynr.system.RoutingTypes.WebSocketClientAddress;

/**
 * Implements the RoutingProvider interface to receive routing information from connected libjoynrs
 *
 */
public class RoutingProviderImpl extends RoutingAbstractProvider {
    private MessageRouter messageRouter;
    private String globalAddressString;
    private String replyToAddressString;
    private List<Deferred<String>> unresolvedGlobalAddressDeferreds = new ArrayList<Deferred<String>>();
    private List<Deferred<String>> unresolvedReplyToAddressDeferreds = new ArrayList<Deferred<String>>();

    /**
     * @param messageRouter handles the logic for the RoutingProvider
     */
    @Inject
    public RoutingProviderImpl(final MessageRouter messageRouter,
                               GlobalAddressProvider globalAddressProvider,
                               ReplyToAddressProvider replyToAddressProvider) {
        this.messageRouter = messageRouter;

        globalAddressProvider.registerGlobalAddressesReadyListener(new TransportReadyListener() {
            @Override
            public void transportReady(Address address) {
                synchronized (unresolvedGlobalAddressDeferreds) {
                    globalAddressString = RoutingTypesUtil.toAddressString(address);
                    for (Deferred<String> globalAddressDeferred : unresolvedGlobalAddressDeferreds) {
                        globalAddressDeferred.resolve(globalAddressString);
                    }
                    unresolvedGlobalAddressDeferreds.clear();
                    globalAddressChanged(globalAddressString);
                }
            }
        });
        replyToAddressProvider.registerGlobalAddressesReadyListener(new TransportReadyListener() {
            @Override
            public void transportReady(Address address) {
                synchronized (unresolvedGlobalAddressDeferreds) {
                    replyToAddressString = RoutingTypesUtil.toAddressString(address);
                    for (Deferred<String> replyToAddressDeferred : unresolvedReplyToAddressDeferreds) {
                        replyToAddressDeferred.resolve(replyToAddressString);
                    }
                    unresolvedReplyToAddressDeferreds.clear();
                    replyToAddressChanged(replyToAddressString);
                }
            }
        });
    }

    private Promise<DeferredVoid> resolvedDeferred() {
        final DeferredVoid deferred = new DeferredVoid();
        deferred.resolve();
        return new Promise<>(deferred);
    }

    @Override
    public Promise<DeferredVoid> addNextHop(String participantId, ChannelAddress address, Boolean isGloballyVisible) {
        messageRouter.addNextHop(participantId, address, isGloballyVisible);
        return resolvedDeferred();
    }

    @Override
    public Promise<DeferredVoid> addNextHop(String participantId, MqttAddress address, Boolean isGloballyVisible) {
        messageRouter.addNextHop(participantId, address, isGloballyVisible);
        return resolvedDeferred();
    }

    @Override
    public Promise<DeferredVoid> addNextHop(String participantId,
                                            CommonApiDbusAddress address,
                                            Boolean isGloballyVisible) {
        messageRouter.addNextHop(participantId, address, isGloballyVisible);
        return resolvedDeferred();
    }

    @Override
    public Promise<DeferredVoid> addNextHop(String participantId, BrowserAddress address, Boolean isGloballyVisible) {
        messageRouter.addNextHop(participantId, address, isGloballyVisible);
        return resolvedDeferred();
    }

    @Override
    public Promise<DeferredVoid> addNextHop(String participantId, WebSocketAddress address, Boolean isGloballyVisible) {
        messageRouter.addNextHop(participantId, address, isGloballyVisible);
        return resolvedDeferred();
    }

    @Override
    public Promise<DeferredVoid> addNextHop(String participantId,
                                            WebSocketClientAddress address,
                                            Boolean isGloballyVisible) {
        messageRouter.addNextHop(participantId, address, isGloballyVisible);
        return resolvedDeferred();
    }

    @Override
    public Promise<DeferredVoid> removeNextHop(String participantId) {
        messageRouter.removeNextHop(participantId);
        return resolvedDeferred();
    }

    @Override
    public Promise<ResolveNextHopDeferred> resolveNextHop(String participantId) {
        boolean resolved = messageRouter.resolveNextHop(participantId);
        ResolveNextHopDeferred deferred = new ResolveNextHopDeferred();
        deferred.resolve(resolved);
        return new Promise<>(deferred);
    }

    @Override
    public Promise<DeferredVoid> addMulticastReceiver(String multicastId,
                                                      String subscriberParticipantId,
                                                      String providerParticipantId) {
        messageRouter.addMulticastReceiver(multicastId, subscriberParticipantId, providerParticipantId);
        return resolvedDeferred();
    }

    @Override
    public Promise<DeferredVoid> removeMulticastReceiver(String multicastId,
                                                         String subscriberParticipantId,
                                                         String providerParticipantId) {
        messageRouter.removeMulticastReceiver(multicastId, subscriberParticipantId, providerParticipantId);
        return resolvedDeferred();
    }

    @Override
    public Promise<Deferred<String>> getGlobalAddress() {
        Deferred<String> globalAddressDeferred = new Deferred<String>();
        synchronized (unresolvedGlobalAddressDeferreds) {
            if (globalAddressString != null) {
                globalAddressDeferred.resolve(globalAddressString);
            } else {
                unresolvedGlobalAddressDeferreds.add(globalAddressDeferred);
            }
        }
        return new Promise<Deferred<String>>(globalAddressDeferred);
    }

    @Override
    public Promise<Deferred<String>> getReplyToAddress() {
        Deferred<String> replyToAddressDeferred = new Deferred<String>();
        synchronized (unresolvedReplyToAddressDeferreds) {
            if (replyToAddressString != null) {
                replyToAddressDeferred.resolve(replyToAddressString);
            } else {
                unresolvedReplyToAddressDeferreds.add(replyToAddressDeferred);
            }
        }
        return new Promise<Deferred<String>>(replyToAddressDeferred);
    }
}
