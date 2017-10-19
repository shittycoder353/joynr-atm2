/*
 *
 * Copyright (C) 2011 - 2017 BMW Car IT GmbH
 *
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
 */

// #####################################################
//#######################################################
//###                                                 ###
//##    WARNING: This file is generated. DO NOT EDIT   ##
//##             All changes will be lost!             ##
//###                                                 ###
//#######################################################
// #####################################################
package joynr.system;

import io.joynr.provider.Promise;
import io.joynr.provider.Deferred;
import io.joynr.provider.AbstractDeferred;
import io.joynr.provider.DeferredVoid;

import io.joynr.provider.JoynrInterface;
import io.joynr.JoynrVersion;

import joynr.system.RoutingTypes.BrowserAddress;
import joynr.system.RoutingTypes.ChannelAddress;
import joynr.system.RoutingTypes.CommonApiDbusAddress;
import joynr.system.RoutingTypes.MqttAddress;
import joynr.system.RoutingTypes.WebSocketAddress;
import joynr.system.RoutingTypes.WebSocketClientAddress;

import io.joynr.provider.SubscriptionPublisherInjection;

interface RoutingSubscriptionPublisherInjection extends SubscriptionPublisherInjection<RoutingSubscriptionPublisher> {}

@JoynrInterface(provides = RoutingProvider.class, name = "system/Routing")
@JoynrVersion(major = 0, minor = 1)
public interface RoutingProvider extends RoutingSubscriptionPublisherInjection {


	Promise<Deferred<String>> getGlobalAddress();

	Promise<Deferred<String>> getReplyToAddress();

	/**
	 * addNextHop
	 * @param participantId the parameter participantId
	 * @param channelAddress the parameter channelAddress
	 * @param isGloballyVisible the parameter isGloballyVisible
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			ChannelAddress channelAddress,
			Boolean isGloballyVisible
	);

	/**
	 * addNextHop
	 * @param participantId the parameter participantId
	 * @param mqttAddress the parameter mqttAddress
	 * @param isGloballyVisible the parameter isGloballyVisible
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			MqttAddress mqttAddress,
			Boolean isGloballyVisible
	);

	/**
	 * addNextHop
	 * @param participantId the parameter participantId
	 * @param commonApiDbusAddress the parameter commonApiDbusAddress
	 * @param isGloballyVisible the parameter isGloballyVisible
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			CommonApiDbusAddress commonApiDbusAddress,
			Boolean isGloballyVisible
	);

	/**
	 * addNextHop
	 * @param participantId the parameter participantId
	 * @param browserAddress the parameter browserAddress
	 * @param isGloballyVisible the parameter isGloballyVisible
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			BrowserAddress browserAddress,
			Boolean isGloballyVisible
	);

	/**
	 * addNextHop
	 * @param participantId the parameter participantId
	 * @param webSocketAddress the parameter webSocketAddress
	 * @param isGloballyVisible the parameter isGloballyVisible
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			WebSocketAddress webSocketAddress,
			Boolean isGloballyVisible
	);

	/**
	 * addNextHop
	 * @param participantId the parameter participantId
	 * @param webSocketClientAddress the parameter webSocketClientAddress
	 * @param isGloballyVisible the parameter isGloballyVisible
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			WebSocketClientAddress webSocketClientAddress,
			Boolean isGloballyVisible
	);

	/**
	 * removeNextHop
	 * @param participantId the parameter participantId
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> removeNextHop(
			String participantId
	);

	/**
	 * resolveNextHop
	 * @param participantId the parameter participantId
	 * @return promise for asynchronous handling
	 */
	public Promise<ResolveNextHopDeferred> resolveNextHop(
			String participantId
	);

	/**
	 * addMulticastReceiver
	 * @param multicastId the parameter multicastId
	 * @param subscriberParticipantId the parameter subscriberParticipantId
	 * @param providerParticipantId the parameter providerParticipantId
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> addMulticastReceiver(
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId
	);

	/**
	 * removeMulticastReceiver
	 * @param multicastId the parameter multicastId
	 * @param subscriberParticipantId the parameter subscriberParticipantId
	 * @param providerParticipantId the parameter providerParticipantId
	 * @return promise for asynchronous handling
	 */
	public Promise<DeferredVoid> removeMulticastReceiver(
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId
	);

	public class ResolveNextHopDeferred extends AbstractDeferred {
		public synchronized boolean resolve(Boolean resolved) {
			return super.resolve(resolved);
		}
	}
}
