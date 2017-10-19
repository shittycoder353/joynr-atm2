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

import io.joynr.proxy.Callback;
import io.joynr.proxy.Future;
import io.joynr.dispatcher.rpc.annotation.JoynrRpcCallback;
import io.joynr.Async;
import io.joynr.ProvidedBy;
import io.joynr.UsedBy;

import joynr.system.RoutingTypes.BrowserAddress;
import joynr.system.RoutingTypes.ChannelAddress;
import joynr.system.RoutingTypes.CommonApiDbusAddress;
import joynr.system.RoutingTypes.MqttAddress;
import joynr.system.RoutingTypes.WebSocketAddress;
import joynr.system.RoutingTypes.WebSocketClientAddress;


@Async
@ProvidedBy(RoutingProvider.class)
@UsedBy(RoutingProxy.class)
public interface RoutingAsync extends Routing {

	public Future<String> getGlobalAddress(@JoynrRpcCallback(deserializationType = String.class) Callback<String> callback);
	public Future<String> getReplyToAddress(@JoynrRpcCallback(deserializationType = String.class) Callback<String> callback);




	/*
	* addNextHop
	*/
	public Future<Void> addNextHop(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String participantId,
			ChannelAddress channelAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public Future<Void> addNextHop(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String participantId,
			MqttAddress mqttAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public Future<Void> addNextHop(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String participantId,
			CommonApiDbusAddress commonApiDbusAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public Future<Void> addNextHop(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String participantId,
			BrowserAddress browserAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public Future<Void> addNextHop(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String participantId,
			WebSocketAddress webSocketAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public Future<Void> addNextHop(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String participantId,
			WebSocketClientAddress webSocketClientAddress,
			Boolean isGloballyVisible
	);

	/*
	* removeNextHop
	*/
	public Future<Void> removeNextHop(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String participantId
	);

	/*
	* resolveNextHop
	*/
	public Future<Boolean> resolveNextHop(
			@JoynrRpcCallback(deserializationType = Boolean.class) Callback<Boolean> callback,
			String participantId
	);

	/*
	* addMulticastReceiver
	*/
	public Future<Void> addMulticastReceiver(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId
	);

	/*
	* removeMulticastReceiver
	*/
	public Future<Void> removeMulticastReceiver(
			@JoynrRpcCallback(deserializationType = Void.class) Callback<Void> callback,
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId
	);
}
