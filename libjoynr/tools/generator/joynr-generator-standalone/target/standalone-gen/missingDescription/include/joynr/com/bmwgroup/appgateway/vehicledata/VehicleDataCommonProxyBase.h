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

#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROXYBASE_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROXYBASE_H

#include "joynr/PrivateCopyAssign.h"
#include <string>
#include <memory>

#include "joynr/ProxyBase.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/IVehicleDataCommonConnector.h"

namespace joynr
{
namespace types
{
	class DiscoveryEntryWithMetaInfo;
} // namespace types
} // namespace joynr

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
/**
 * @brief Proxy base class for interface VehicleDataCommon
 *
 * @version 0.1
 */
class  VehicleDataCommonProxyBase: virtual public joynr::ProxyBase, virtual public joynr::com::bmwgroup::appgateway::vehicledata::IVehicleDataCommonSubscription {
public:
	/**
	 * @brief Parameterized constructor
	 * @param connectorFactory The connector factory
	 * @param domain The provider domain
	 * @param qosSettings The quality of service settings
	 */
	VehicleDataCommonProxyBase(
			std::weak_ptr<joynr::JoynrRuntime> runtime,
			joynr::ConnectorFactory* connectorFactory,
			const std::string& domain,
			const joynr::MessagingQos& qosSettings
	);

	/**
	 * @brief Called when arbitration is finished
	 * @param participantId The id of the participant
	 * @param connection The kind of connection
	 */
	void handleArbitrationFinished(
			const joynr::types::DiscoveryEntryWithMetaInfo& providerDiscoveryEntry,
			bool useInProcessConnector
	) override;

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
	 override;
	
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
	 override;
	
	/**
	 * @brief unsubscribes from attribute Vin
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 */
	void unsubscribeFromVin(const std::string& subscriptionId)
	 override;
	
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
	 override;
	
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
	 override;
	
	/**
	 * @brief unsubscribes from attribute ActualSpeedKmh
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 */
	void unsubscribeFromActualSpeedKmh(const std::string& subscriptionId)
	 override;
	
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
	 override;
	
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
	 override;
	
	/**
	 * @brief unsubscribes from attribute Language
	 * @param subscriptionId The subscription id returned earlier on creation of the subscription
	 */
	void unsubscribeFromLanguage(const std::string& subscriptionId)
	 override;
	

protected:
	/** @brief The kind of connector */
	std::unique_ptr<IVehicleDataCommonConnector> connector;

private:
	DISALLOW_COPY_AND_ASSIGN(VehicleDataCommonProxyBase);
};

} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROXYBASE_H
