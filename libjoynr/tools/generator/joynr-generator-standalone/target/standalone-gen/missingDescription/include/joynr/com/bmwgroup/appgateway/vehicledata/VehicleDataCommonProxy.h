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

#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROXY_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROXY_H

#include "joynr/PrivateCopyAssign.h"
#include <string>
#include <memory>

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonSyncProxy.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonAsyncProxy.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/IVehicleDataCommon.h"

#ifdef _MSC_VER
	// Visual C++ gives a warning which is caused by diamond inheritance, but this is
	// not relevant when using pure virtual methods:
	// http://msdn.microsoft.com/en-us/library/6b3sy7ae(v=vs.80).aspx
	#pragma warning( disable : 4250 )
#endif

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
/**
 * @brief Proxy class for interface VehicleDataCommon
 *
 * @version 0.1
 */
class  VehicleDataCommonProxy : virtual public IVehicleDataCommon, virtual public VehicleDataCommonSyncProxy, virtual public VehicleDataCommonAsyncProxy {
public:
	/**
	 * @brief Parameterized constructor
	 * @param connectorFactory The connector factory
	 * @param domain The provider domain
	 * @param qosSettings The quality of service settings
	 */
	VehicleDataCommonProxy(
			std::weak_ptr<joynr::JoynrRuntime> runtime,
			joynr::ConnectorFactory* connectorFactory,
			const std::string& domain,
			const joynr::MessagingQos& qosSettings
	);

	/**
	 * @brief unsubscribes from attribute Vin
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 */
	void unsubscribeFromVin(const std::string &subscriptionId) override {
		VehicleDataCommonProxyBase::unsubscribeFromVin(subscriptionId);
	}

	/**
	 * @brief creates a new subscription to attribute Vin
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 * completion, to get the subscription id or the request status object. The subscription id will be available
	 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToVin(
				std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
				std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
	 override {
		return VehicleDataCommonProxyBase::subscribeToVin(
					subscriptionListener,
					subscriptionQos);
	}

	/**
	 * @brief updates an existing subscription to attribute Vin
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 * completion, to get the subscription id or the request status object. The subscription id will be available
	 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToVin(
				std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
				std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
				const std::string& subscriptionId)
	 override{
		return VehicleDataCommonProxyBase::subscribeToVin(
					subscriptionListener,
					subscriptionQos,
					subscriptionId);
	}

	/**
	 * @brief unsubscribes from attribute ActualSpeedKmh
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 */
	void unsubscribeFromActualSpeedKmh(const std::string &subscriptionId) override {
		VehicleDataCommonProxyBase::unsubscribeFromActualSpeedKmh(subscriptionId);
	}

	/**
	 * @brief creates a new subscription to attribute ActualSpeedKmh
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 * completion, to get the subscription id or the request status object. The subscription id will be available
	 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToActualSpeedKmh(
				std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
				std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
	 override {
		return VehicleDataCommonProxyBase::subscribeToActualSpeedKmh(
					subscriptionListener,
					subscriptionQos);
	}

	/**
	 * @brief updates an existing subscription to attribute ActualSpeedKmh
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 * completion, to get the subscription id or the request status object. The subscription id will be available
	 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToActualSpeedKmh(
				std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
				std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
				const std::string& subscriptionId)
	 override{
		return VehicleDataCommonProxyBase::subscribeToActualSpeedKmh(
					subscriptionListener,
					subscriptionQos,
					subscriptionId);
	}

	/**
	 * @brief unsubscribes from attribute Language
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 */
	void unsubscribeFromLanguage(const std::string &subscriptionId) override {
		VehicleDataCommonProxyBase::unsubscribeFromLanguage(subscriptionId);
	}

	/**
	 * @brief creates a new subscription to attribute Language
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 * completion, to get the subscription id or the request status object. The subscription id will be available
	 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToLanguage(
				std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
				std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
	 override {
		return VehicleDataCommonProxyBase::subscribeToLanguage(
					subscriptionListener,
					subscriptionQos);
	}

	/**
	 * @brief updates an existing subscription to attribute Language
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 * completion, to get the subscription id or the request status object. The subscription id will be available
	 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToLanguage(
				std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
				std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
				const std::string& subscriptionId)
	 override{
		return VehicleDataCommonProxyBase::subscribeToLanguage(
					subscriptionListener,
					subscriptionQos,
					subscriptionId);
	}


	/** @brief Destructor */
	~VehicleDataCommonProxy() override = default;

	// attributes
	using VehicleDataCommonAsyncProxy::getVinAsync;
	using VehicleDataCommonSyncProxy::getVin;
	using VehicleDataCommonAsyncProxy::getActualSpeedKmhAsync;
	using VehicleDataCommonSyncProxy::getActualSpeedKmh;
	using VehicleDataCommonAsyncProxy::getLanguageAsync;
	using VehicleDataCommonSyncProxy::getLanguage;
	using VehicleDataCommonAsyncProxy::setLanguageAsync;
	using VehicleDataCommonSyncProxy::setLanguage;

private:
	DISALLOW_COPY_AND_ASSIGN(VehicleDataCommonProxy);
};


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr

#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROXY_H
