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

import io.joynr.dispatcher.rpc.JoynrSubscriptionInterface;

import io.joynr.dispatcher.rpc.annotation.JoynrRpcSubscription;
import io.joynr.proxy.Future;
import io.joynr.pubsub.subscription.AttributeSubscriptionListener;
import io.joynr.pubsub.SubscriptionQos;


public interface RoutingSubscriptionInterface extends JoynrSubscriptionInterface, Routing {


	@JoynrRpcSubscription(attributeName = "globalAddress", attributeType = String.class)
	public Future<String> subscribeToGlobalAddress(AttributeSubscriptionListener<String> listener, SubscriptionQos subscriptionQos);

	@JoynrRpcSubscription(attributeName = "globalAddress", attributeType = String.class)
	public Future<String> subscribeToGlobalAddress(String subscriptionId, AttributeSubscriptionListener<String> listener, SubscriptionQos subscriptionQos);

	public void unsubscribeFromGlobalAddress(String subscriptionId);

	@JoynrRpcSubscription(attributeName = "replyToAddress", attributeType = String.class)
	public Future<String> subscribeToReplyToAddress(AttributeSubscriptionListener<String> listener, SubscriptionQos subscriptionQos);

	@JoynrRpcSubscription(attributeName = "replyToAddress", attributeType = String.class)
	public Future<String> subscribeToReplyToAddress(String subscriptionId, AttributeSubscriptionListener<String> listener, SubscriptionQos subscriptionQos);

	public void unsubscribeFromReplyToAddress(String subscriptionId);
}
