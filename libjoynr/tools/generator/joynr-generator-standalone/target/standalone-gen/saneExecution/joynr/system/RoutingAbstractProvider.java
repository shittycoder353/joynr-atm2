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

import io.joynr.provider.AbstractJoynrProvider;


public abstract class RoutingAbstractProvider extends AbstractJoynrProvider implements RoutingProvider {

	public RoutingAbstractProvider() {
		super();
	}


	protected RoutingSubscriptionPublisher routingSubscriptionPublisher;

	@Override
	public void setSubscriptionPublisher(RoutingSubscriptionPublisher routingSubscriptionPublisher) {
		this.routingSubscriptionPublisher = routingSubscriptionPublisher;
	}


	public void globalAddressChanged(String globalAddress) {
		if (routingSubscriptionPublisher != null) {
			routingSubscriptionPublisher.globalAddressChanged(globalAddress);
		}
	}
	public void replyToAddressChanged(String replyToAddress) {
		if (routingSubscriptionPublisher != null) {
			routingSubscriptionPublisher.replyToAddressChanged(replyToAddress);
		}
	}

}
