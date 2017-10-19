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

import io.joynr.Sync;
import io.joynr.ProvidedBy;
import io.joynr.UsedBy;

import joynr.system.RoutingTypes.BrowserAddress;
import joynr.system.RoutingTypes.ChannelAddress;
import joynr.system.RoutingTypes.CommonApiDbusAddress;
import joynr.system.RoutingTypes.MqttAddress;
import joynr.system.RoutingTypes.WebSocketAddress;
import joynr.system.RoutingTypes.WebSocketClientAddress;

@Sync
@ProvidedBy(RoutingProvider.class)
@UsedBy(RoutingProxy.class)
public interface RoutingSync extends Routing {

	public String getGlobalAddress();

	public String getReplyToAddress();


	/*
	* addNextHop
	*/
	public void addNextHop(
			String participantId,
			ChannelAddress channelAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public void addNextHop(
			String participantId,
			MqttAddress mqttAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public void addNextHop(
			String participantId,
			CommonApiDbusAddress commonApiDbusAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public void addNextHop(
			String participantId,
			BrowserAddress browserAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public void addNextHop(
			String participantId,
			WebSocketAddress webSocketAddress,
			Boolean isGloballyVisible
	);

	/*
	* addNextHop
	*/
	public void addNextHop(
			String participantId,
			WebSocketClientAddress webSocketClientAddress,
			Boolean isGloballyVisible
	);

	/*
	* removeNextHop
	*/
	public void removeNextHop(
			String participantId
	);

	/*
	* resolveNextHop
	*/
	public Boolean resolveNextHop(
			String participantId
	);

	/*
	* addMulticastReceiver
	*/
	public void addMulticastReceiver(
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId
	);

	/*
	* removeMulticastReceiver
	*/
	public void removeMulticastReceiver(
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId
	);
}
