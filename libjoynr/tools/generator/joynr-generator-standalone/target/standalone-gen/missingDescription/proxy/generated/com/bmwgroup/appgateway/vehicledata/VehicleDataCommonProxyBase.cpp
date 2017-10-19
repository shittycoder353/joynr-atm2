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

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonProxyBase.h"
#include "joynr/ConnectorFactory.h"
#include "joynr/ISubscriptionListener.h"
#include "joynr/types/DiscoveryEntryWithMetaInfo.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonInProcessConnector.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonJoynrMessagingConnector.h"

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
VehicleDataCommonProxyBase::VehicleDataCommonProxyBase(
		std::weak_ptr<joynr::JoynrRuntime> runtime,
		joynr::ConnectorFactory* connectorFactory,
		const std::string &domain,
		const joynr::MessagingQos &qosSettings
) :
		joynr::ProxyBase(std::move(runtime), connectorFactory, domain, qosSettings),
		connector()
{
}

void VehicleDataCommonProxyBase::handleArbitrationFinished(
		const joynr::types::DiscoveryEntryWithMetaInfo& providerDiscoveryEntry,
		bool useInProcessConnector
) {
	connector = connectorFactory->create<joynr::com::bmwgroup::appgateway::vehicledata::IVehicleDataCommonConnector>(
				domain,
				proxyParticipantId,
				qosSettings,
				useInProcessConnector,
				providerDiscoveryEntry
	);

	joynr::ProxyBase::handleArbitrationFinished(providerDiscoveryEntry, useInProcessConnector);
}

void VehicleDataCommonProxyBase::unsubscribeFromVin(const std::string& subscriptionId)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		if (!runtimeSharedPtr) {
			JOYNR_LOG_WARN(logger, "proxy cannot unsubscribe from VehicleDataCommonProxyBase.vin, "
					 "because the required runtime has been already destroyed.");
			return;
		} else {
			JOYNR_LOG_WARN(logger, "proxy cannot unsubscribe from VehicleDataCommonProxyBase.vin, "
					 "because the communication end partner is not (yet) known");
			return;
		}
	}
	connector->unsubscribeFromVin(subscriptionId);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonProxyBase::subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
 {
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.vin, "
					"because the required runtime has been already destroyed.";
		} else {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.vin, "
					"because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorMsg);
		auto future = std::make_shared<Future<std::string>>();
		future->onError(error);
		subscriptionListener->onError(*error);
		return future;
	}
	return connector->subscribeToVin(
				subscriptionListener,
				subscriptionQos,
				subscriptionId);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonProxyBase::subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
 {
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.vin, "
					 "because the required runtime has been already destroyed.";
		} else {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.vin, "
					 "because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorMsg);
		auto future = std::make_shared<Future<std::string>>();
		future->onError(error);
		subscriptionListener->onError(*error);
		return future;
	}
	return connector->subscribeToVin(
				subscriptionListener,
				subscriptionQos);
}

void VehicleDataCommonProxyBase::unsubscribeFromActualSpeedKmh(const std::string& subscriptionId)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		if (!runtimeSharedPtr) {
			JOYNR_LOG_WARN(logger, "proxy cannot unsubscribe from VehicleDataCommonProxyBase.actualSpeedKmh, "
					 "because the required runtime has been already destroyed.");
			return;
		} else {
			JOYNR_LOG_WARN(logger, "proxy cannot unsubscribe from VehicleDataCommonProxyBase.actualSpeedKmh, "
					 "because the communication end partner is not (yet) known");
			return;
		}
	}
	connector->unsubscribeFromActualSpeedKmh(subscriptionId);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonProxyBase::subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
 {
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.actualSpeedKmh, "
					"because the required runtime has been already destroyed.";
		} else {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.actualSpeedKmh, "
					"because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorMsg);
		auto future = std::make_shared<Future<std::string>>();
		future->onError(error);
		subscriptionListener->onError(*error);
		return future;
	}
	return connector->subscribeToActualSpeedKmh(
				subscriptionListener,
				subscriptionQos,
				subscriptionId);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonProxyBase::subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
 {
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.actualSpeedKmh, "
					 "because the required runtime has been already destroyed.";
		} else {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.actualSpeedKmh, "
					 "because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorMsg);
		auto future = std::make_shared<Future<std::string>>();
		future->onError(error);
		subscriptionListener->onError(*error);
		return future;
	}
	return connector->subscribeToActualSpeedKmh(
				subscriptionListener,
				subscriptionQos);
}

void VehicleDataCommonProxyBase::unsubscribeFromLanguage(const std::string& subscriptionId)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		if (!runtimeSharedPtr) {
			JOYNR_LOG_WARN(logger, "proxy cannot unsubscribe from VehicleDataCommonProxyBase.language, "
					 "because the required runtime has been already destroyed.");
			return;
		} else {
			JOYNR_LOG_WARN(logger, "proxy cannot unsubscribe from VehicleDataCommonProxyBase.language, "
					 "because the communication end partner is not (yet) known");
			return;
		}
	}
	connector->unsubscribeFromLanguage(subscriptionId);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonProxyBase::subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
 {
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.language, "
					"because the required runtime has been already destroyed.";
		} else {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.language, "
					"because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorMsg);
		auto future = std::make_shared<Future<std::string>>();
		future->onError(error);
		subscriptionListener->onError(*error);
		return future;
	}
	return connector->subscribeToLanguage(
				subscriptionListener,
				subscriptionQos,
				subscriptionId);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonProxyBase::subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
 {
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || !connector) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.language, "
					 "because the required runtime has been already destroyed.";
		} else {
			errorMsg = "proxy cannot subscribe to VehicleDataCommonProxyBase.language, "
					 "because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorMsg);
		auto future = std::make_shared<Future<std::string>>();
		future->onError(error);
		subscriptionListener->onError(*error);
		return future;
	}
	return connector->subscribeToLanguage(
				subscriptionListener,
				subscriptionQos);
}




} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
