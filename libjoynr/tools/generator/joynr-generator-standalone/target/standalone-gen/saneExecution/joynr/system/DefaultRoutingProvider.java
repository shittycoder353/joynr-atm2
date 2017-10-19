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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.joynr.provider.Promise;
import io.joynr.provider.Deferred;
import io.joynr.provider.DeferredVoid;

import joynr.system.RoutingTypes.BrowserAddress;
import joynr.system.RoutingTypes.ChannelAddress;
import joynr.system.RoutingTypes.CommonApiDbusAddress;
import joynr.system.RoutingTypes.MqttAddress;
import joynr.system.RoutingTypes.WebSocketAddress;
import joynr.system.RoutingTypes.WebSocketClientAddress;

public class DefaultRoutingProvider extends RoutingAbstractProvider {
	private static final Logger logger = LoggerFactory.getLogger(DefaultRoutingProvider.class);

	protected String globalAddress;
	protected String replyToAddress;

	public DefaultRoutingProvider() {
	}


	@Override
	public Promise<Deferred<String>> getGlobalAddress() {
		Deferred<String> deferred = new Deferred<>();
		deferred.resolve(globalAddress);
		return new Promise<>(deferred);
	}

	@Override
	public Promise<Deferred<String>> getReplyToAddress() {
		Deferred<String> deferred = new Deferred<>();
		deferred.resolve(replyToAddress);
		return new Promise<>(deferred);
	}


	/*
	* addNextHop
	*/
	@Override
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			ChannelAddress channelAddress,
			Boolean isGloballyVisible) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.addNextHop called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* addNextHop
	*/
	@Override
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			MqttAddress mqttAddress,
			Boolean isGloballyVisible) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.addNextHop called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* addNextHop
	*/
	@Override
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			CommonApiDbusAddress commonApiDbusAddress,
			Boolean isGloballyVisible) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.addNextHop called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* addNextHop
	*/
	@Override
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			BrowserAddress browserAddress,
			Boolean isGloballyVisible) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.addNextHop called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* addNextHop
	*/
	@Override
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			WebSocketAddress webSocketAddress,
			Boolean isGloballyVisible) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.addNextHop called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* addNextHop
	*/
	@Override
	public Promise<DeferredVoid> addNextHop(
			String participantId,
			WebSocketClientAddress webSocketClientAddress,
			Boolean isGloballyVisible) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.addNextHop called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* removeNextHop
	*/
	@Override
	public Promise<DeferredVoid> removeNextHop(
			String participantId) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.removeNextHop called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* resolveNextHop
	*/
	@Override
	public Promise<ResolveNextHopDeferred> resolveNextHop(
			String participantId) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.resolveNextHop called");
		logger.warn("**********************************************");
		ResolveNextHopDeferred deferred = new ResolveNextHopDeferred();
		Boolean resolved = false;
		deferred.resolve(resolved);
		return new Promise<>(deferred);
	}

	/*
	* addMulticastReceiver
	*/
	@Override
	public Promise<DeferredVoid> addMulticastReceiver(
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.addMulticastReceiver called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}

	/*
	* removeMulticastReceiver
	*/
	@Override
	public Promise<DeferredVoid> removeMulticastReceiver(
			String multicastId,
			String subscriberParticipantId,
			String providerParticipantId) {
		logger.warn("**********************************************");
		logger.warn("* DefaultRoutingProvider.removeMulticastReceiver called");
		logger.warn("**********************************************");
		DeferredVoid deferred = new DeferredVoid();
		deferred.resolve();
		return new Promise<>(deferred);
	}
}
