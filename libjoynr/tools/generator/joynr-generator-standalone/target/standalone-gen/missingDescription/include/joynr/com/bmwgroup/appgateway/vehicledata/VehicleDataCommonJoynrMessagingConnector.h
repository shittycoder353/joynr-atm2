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

#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONJOYNRMESSAGINGCONNECTOR_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONJOYNRMESSAGINGCONNECTOR_H

#include <string>

#include <memory>
#include <functional>
#include "joynr/com/bmwgroup/appgateway/vehicledata/IVehicleDataCommonConnector.h"
#include "joynr/AbstractJoynrMessagingConnector.h"
#include "joynr/JoynrMessagingConnectorFactory.h"
#include "joynr/SubscriptionQos.h"
#include "joynr/OnChangeSubscriptionQos.h"
#include "joynr/MulticastSubscriptionQos.h"

namespace joynr {
	class MessagingQos;
	class IMessageSender;
	class ISubscriptionManager;
	class SubscriptionRequest;
	template <class ... Ts> class Future;
	template <typename... Ts> class ISubscriptionListener;

namespace types
{
	class DiscoveryEntryWithMetaInfo;
} // namespace types

namespace exceptions
{
	class JoynrException;
	class JoynrRuntimeException;
} // namespace exceptions
}

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 


/** @brief JoynrMessagingConnector for interface VehicleDataCommon */
class  VehicleDataCommonJoynrMessagingConnector : public IVehicleDataCommonConnector, virtual public joynr::AbstractJoynrMessagingConnector {
private:
	/**
	 * @brief creates a new subscription or updates an existing subscription to attribute 
	 * Vin
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @param subscriptionRequest The subscription request
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 			 * completion, to get the subscription id or the request status object. The subscription id will be available
	 			 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			SubscriptionRequest& subscriptionRequest);
	/**
	 * @brief creates a new subscription or updates an existing subscription to attribute 
	 * ActualSpeedKmh
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @param subscriptionRequest The subscription request
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 			 * completion, to get the subscription id or the request status object. The subscription id will be available
	 			 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			SubscriptionRequest& subscriptionRequest);
	/**
	 * @brief creates a new subscription or updates an existing subscription to attribute 
	 * Language
	 * @param subscriptionListener The listener callback providing methods to call on publication and failure
	 * @param subscriptionQos The subscription quality of service settings
	 * @param subscriptionRequest The subscription request
	 * @return a future representing the result (subscription id) as string. It provides methods to wait for
	 			 * completion, to get the subscription id or the request status object. The subscription id will be available
	 			 * when the subscription is successfully registered at the provider.
	 */
	std::shared_ptr<joynr::Future<std::string>> subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			SubscriptionRequest& subscriptionRequest);
public:
	/**
	 * @brief Parameterized constructor
	 * @param messageSender The message sender
	 * @param subscriptionManager Pointer to subscription manager instance
	 * @param domain the provider domain
	 * @param proxyParticipantId The participant id of the proxy
	 * @param providerParticipantId The participant id of the provider
	 * @param qosSettings The quality of service settings
	 */
	VehicleDataCommonJoynrMessagingConnector(
		std::shared_ptr<joynr::IMessageSender> messageSender,
		std::shared_ptr<joynr::ISubscriptionManager> subscriptionManager,
		const std::string& domain,
		const std::string& proxyParticipantId,
		const joynr::MessagingQos &qosSettings,
		const joynr::types::DiscoveryEntryWithMetaInfo& providerDiscoveryInfo);

	/** @brief Destructor */
	~VehicleDataCommonJoynrMessagingConnector() override = default;

	
	/**
	* @brief Synchronous getter for the vin attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	void getVin(std::string& vin)
	override;
	
	
	/**
	* @brief Synchronous getter for the actualSpeedKmh attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	void getActualSpeedKmh(double& actualSpeedKmh)
	override;
	
	
	/**
	* @brief Synchronous getter for the language attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	void getLanguage(std::string& language)
	override;
	
	
	/**
	* @brief Asynchronous getter for the vin attribute.
	*
	* @param onSuccess A callback function to be called once the asynchronous computation has
	* finished successfully. It must expect the method out parameters.
	* @param onRuntimeError A callback function to be called once the asynchronous computation has
	* failed with an unexpected non-modeled exception. It must expect a JoynrRuntimeException object.
	* @returns A future representing the result of the asynchronous method call. It provides methods
	* to wait for completion, to get the result or the request status object.
	*/
	std::shared_ptr<joynr::Future<std::string> > getVinAsync(
				std::function<void(const std::string& vin)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	override;
	
	/**
	* @brief Asynchronous getter for the actualSpeedKmh attribute.
	*
	* @param onSuccess A callback function to be called once the asynchronous computation has
	* finished successfully. It must expect the method out parameters.
	* @param onRuntimeError A callback function to be called once the asynchronous computation has
	* failed with an unexpected non-modeled exception. It must expect a JoynrRuntimeException object.
	* @returns A future representing the result of the asynchronous method call. It provides methods
	* to wait for completion, to get the result or the request status object.
	*/
	std::shared_ptr<joynr::Future<double> > getActualSpeedKmhAsync(
				std::function<void(const double& actualSpeedKmh)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	override;
	
	/**
	* @brief Asynchronous getter for the language attribute.
	*
	* @param onSuccess A callback function to be called once the asynchronous computation has
	* finished successfully. It must expect the method out parameters.
	* @param onRuntimeError A callback function to be called once the asynchronous computation has
	* failed with an unexpected non-modeled exception. It must expect a JoynrRuntimeException object.
	* @returns A future representing the result of the asynchronous method call. It provides methods
	* to wait for completion, to get the result or the request status object.
	*/
	std::shared_ptr<joynr::Future<std::string> > getLanguageAsync(
				std::function<void(const std::string& language)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	override;
	
	/**
	* @brief Synchronous setter for the language attribute.
	*
	* @param language The value to set.
	* @throws JoynrException if the request is not successful
	*/
	void setLanguage(const std::string& language)
	override;
	
	/**
	* @brief Asynchronous setter for the language attribute.
	*
	* @param language The value to set.
	* @param onSuccess A callback function to be called once the asynchronous computation has
	* finished successfully. It must expect the method out parameters.
	* @param onRuntimeError A callback function to be called once the asynchronous computation has
	* failed with an unexpected non-modeled exception. It must expect a JoynrRuntimeException object.
	* @returns A future representing the result of the asynchronous method call. It provides methods
	* to wait for completion, to get the result or the request status object.
	*/
	std::shared_ptr<joynr::Future<void> > setLanguageAsync(
				std::string language,
				std::function<void(void)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	override;


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
	
};

} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr


namespace joynr {

// specialization of traits class JoynrMessagingTraits
// this links IVehicleDataCommonConnector with VehicleDataCommonJoynrMessagingConnector
template <>
struct JoynrMessagingTraits<joynr::com::bmwgroup::appgateway::vehicledata::IVehicleDataCommonConnector>
{
	using Connector = joynr::com::bmwgroup::appgateway::vehicledata::VehicleDataCommonJoynrMessagingConnector;
};

} // namespace joynr
#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONJOYNRMESSAGINGCONNECTOR_H
