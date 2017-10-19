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
#include <cassert>
#include <functional>
#include <memory>
#include <tuple>

#include "joynr/serializer/Serializer.h"

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonInProcessConnector.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonRequestCaller.h"


#include "joynr/InProcessAddress.h"
#include "joynr/ISubscriptionManager.h"
#include "joynr/PublicationManager.h"
#include "joynr/UnicastSubscriptionCallback.h"
#include "joynr/MulticastSubscriptionCallback.h"
#include "joynr/Util.h"
#include "joynr/Future.h"
#include "joynr/SubscriptionUtil.h"
#include "joynr/CallContext.h"
#include "joynr/IPlatformSecurityManager.h"
#include "joynr/exceptions/JoynrException.h"
#include "joynr/SubscriptionRequest.h"

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 

INIT_LOGGER(VehicleDataCommonInProcessConnector);

VehicleDataCommonInProcessConnector::VehicleDataCommonInProcessConnector(
			joynr::ISubscriptionManager* subscriptionManager,
			joynr::PublicationManager* publicationManager,
			joynr::InProcessPublicationSender* inProcessPublicationSender,
			std::shared_ptr<joynr::IPlatformSecurityManager> securityManager,
			const std::string& proxyParticipantId,
			const std::string& providerParticipantId,
			std::shared_ptr<joynr::InProcessAddress> address
) :
	proxyParticipantId(proxyParticipantId),
	providerParticipantId(providerParticipantId),
	address(address),
	subscriptionManager(subscriptionManager),
	publicationManager(publicationManager),
	inProcessPublicationSender(inProcessPublicationSender),
	securityManager(securityManager)
{
}

void VehicleDataCommonInProcessConnector::getVin(std::string& vin)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<std::string>>();

	std::function<void(const std::string& vin)> onSuccess =
			[future] (const std::string& vin) {
				future->onSuccess(vin);
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onError =
			[future] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
			};

	//see header for more information
	vehicleDataCommonCaller->getVin(std::move(onSuccess), std::move(onError));
	future->get(vin);
}

std::shared_ptr<joynr::Future<std::string> > VehicleDataCommonInProcessConnector::getVinAsync(
			std::function<void(const std::string& vin)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<std::string>>();

	std::function<void(const std::string& vin)> onSuccessWrapper =
			[future, onSuccess = std::move(onSuccess)] (const std::string& vin) {
				future->onSuccess(vin);
				if (onSuccess) {
					onSuccess(vin);
				}
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onErrorWrapper =
			[future, onError = std::move(onError)] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
				if (onError) {
					onError(*error);
				}
			};

	//see header for more information
	vehicleDataCommonCaller->getVin(std::move(onSuccessWrapper), std::move(onErrorWrapper));
	return future;
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
{
	joynr::SubscriptionRequest subscriptionRequest;
	return subscribeToVin(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
{
	joynr::SubscriptionRequest subscriptionRequest;
	subscriptionRequest.setSubscriptionId(subscriptionId);
	return subscribeToVin(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToVin(
		std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
		std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
		joynr::SubscriptionRequest& subscriptionRequest)
{
	JOYNR_LOG_TRACE(logger, "Subscribing to vin.");
	assert(subscriptionManager != nullptr);
	std::string attributeName("vin");
	auto future = std::make_shared<Future<std::string>>();
	auto subscriptionCallback = std::make_shared<
			joynr::UnicastSubscriptionCallback<std::string>
	>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager);
	subscriptionManager->registerSubscription(
			attributeName,
			subscriptionCallback,
			subscriptionListener,
			subscriptionQos,
			subscriptionRequest);
	JOYNR_LOG_TRACE(logger, "Registered subscription: {}", subscriptionRequest.toString());
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> requestCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);

	joynr::CallContext callContext;
	callContext.setPrincipal(securityManager->getCurrentProcessUserId());

	if(!requestCaller) {
		assert(publicationManager != nullptr);
		/**
		* Provider not registered yet
		* Dispatcher will call publicationManger->restore when a new provider is added to activate
		* subscriptions for that provider
		*/
		publicationManager->add(proxyParticipantId, providerParticipantId, subscriptionRequest);
	} else {
		publicationManager->add(proxyParticipantId, providerParticipantId, caller, subscriptionRequest, inProcessPublicationSender);
	}
	return future;
}

void VehicleDataCommonInProcessConnector::unsubscribeFromVin(const std::string& subscriptionId)
 {
	JOYNR_LOG_TRACE(logger, "Unsubscribing. Id={}", subscriptionId);
	assert(publicationManager != nullptr);
	JOYNR_LOG_TRACE(logger, "Stopping publications by publication manager.");
	publicationManager->stopPublication(subscriptionId);
	assert(subscriptionManager != nullptr);
	JOYNR_LOG_TRACE(logger, "Unregistering attribute subscription.");
	subscriptionManager->unregisterSubscription(subscriptionId);
}

void VehicleDataCommonInProcessConnector::getActualSpeedKmh(double& actualSpeedKmh)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<double>>();

	std::function<void(const double& actualSpeedKmh)> onSuccess =
			[future] (const double& actualSpeedKmh) {
				future->onSuccess(actualSpeedKmh);
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onError =
			[future] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
			};

	//see header for more information
	vehicleDataCommonCaller->getActualSpeedKmh(std::move(onSuccess), std::move(onError));
	future->get(actualSpeedKmh);
}

std::shared_ptr<joynr::Future<double> > VehicleDataCommonInProcessConnector::getActualSpeedKmhAsync(
			std::function<void(const double& actualSpeedKmh)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<double>>();

	std::function<void(const double& actualSpeedKmh)> onSuccessWrapper =
			[future, onSuccess = std::move(onSuccess)] (const double& actualSpeedKmh) {
				future->onSuccess(actualSpeedKmh);
				if (onSuccess) {
					onSuccess(actualSpeedKmh);
				}
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onErrorWrapper =
			[future, onError = std::move(onError)] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
				if (onError) {
					onError(*error);
				}
			};

	//see header for more information
	vehicleDataCommonCaller->getActualSpeedKmh(std::move(onSuccessWrapper), std::move(onErrorWrapper));
	return future;
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
{
	joynr::SubscriptionRequest subscriptionRequest;
	return subscribeToActualSpeedKmh(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
{
	joynr::SubscriptionRequest subscriptionRequest;
	subscriptionRequest.setSubscriptionId(subscriptionId);
	return subscribeToActualSpeedKmh(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToActualSpeedKmh(
		std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
		std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
		joynr::SubscriptionRequest& subscriptionRequest)
{
	JOYNR_LOG_TRACE(logger, "Subscribing to actualSpeedKmh.");
	assert(subscriptionManager != nullptr);
	std::string attributeName("actualSpeedKmh");
	auto future = std::make_shared<Future<std::string>>();
	auto subscriptionCallback = std::make_shared<
			joynr::UnicastSubscriptionCallback<double>
	>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager);
	subscriptionManager->registerSubscription(
			attributeName,
			subscriptionCallback,
			subscriptionListener,
			subscriptionQos,
			subscriptionRequest);
	JOYNR_LOG_TRACE(logger, "Registered subscription: {}", subscriptionRequest.toString());
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> requestCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);

	joynr::CallContext callContext;
	callContext.setPrincipal(securityManager->getCurrentProcessUserId());

	if(!requestCaller) {
		assert(publicationManager != nullptr);
		/**
		* Provider not registered yet
		* Dispatcher will call publicationManger->restore when a new provider is added to activate
		* subscriptions for that provider
		*/
		publicationManager->add(proxyParticipantId, providerParticipantId, subscriptionRequest);
	} else {
		publicationManager->add(proxyParticipantId, providerParticipantId, caller, subscriptionRequest, inProcessPublicationSender);
	}
	return future;
}

void VehicleDataCommonInProcessConnector::unsubscribeFromActualSpeedKmh(const std::string& subscriptionId)
 {
	JOYNR_LOG_TRACE(logger, "Unsubscribing. Id={}", subscriptionId);
	assert(publicationManager != nullptr);
	JOYNR_LOG_TRACE(logger, "Stopping publications by publication manager.");
	publicationManager->stopPublication(subscriptionId);
	assert(subscriptionManager != nullptr);
	JOYNR_LOG_TRACE(logger, "Unregistering attribute subscription.");
	subscriptionManager->unregisterSubscription(subscriptionId);
}

void VehicleDataCommonInProcessConnector::getLanguage(std::string& language)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<std::string>>();

	std::function<void(const std::string& language)> onSuccess =
			[future] (const std::string& language) {
				future->onSuccess(language);
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onError =
			[future] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
			};

	//see header for more information
	vehicleDataCommonCaller->getLanguage(std::move(onSuccess), std::move(onError));
	future->get(language);
}

std::shared_ptr<joynr::Future<std::string> > VehicleDataCommonInProcessConnector::getLanguageAsync(
			std::function<void(const std::string& language)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<std::string>>();

	std::function<void(const std::string& language)> onSuccessWrapper =
			[future, onSuccess = std::move(onSuccess)] (const std::string& language) {
				future->onSuccess(language);
				if (onSuccess) {
					onSuccess(language);
				}
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onErrorWrapper =
			[future, onError = std::move(onError)] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
				if (onError) {
					onError(*error);
				}
			};

	//see header for more information
	vehicleDataCommonCaller->getLanguage(std::move(onSuccessWrapper), std::move(onErrorWrapper));
	return future;
}

std::shared_ptr<joynr::Future<void> > VehicleDataCommonInProcessConnector::setLanguageAsync(
			std::string language,
			std::function<void(void)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<void>>();
	std::function<void()> onSuccessWrapper =
			[future, onSuccess = std::move(onSuccess)] () {
				future->onSuccess();
				if (onSuccess) {
					onSuccess();
				}
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onErrorWrapper =
			[future, onError = std::move(onError)] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
				if (onError) {
					onError(*error);
				}
			};

	//see header for more information
	JOYNR_LOG_ERROR(logger, "#### WARNING ##### VehicleDataCommonInProcessConnector::setLanguage(Future) is synchronous.");
	vehicleDataCommonCaller->setLanguage(language, std::move(onSuccessWrapper), std::move(onErrorWrapper));
	return future;
}

void VehicleDataCommonInProcessConnector::setLanguage(const std::string& language)
{
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);
	assert(vehicleDataCommonCaller);

	auto future = std::make_shared<joynr::Future<void>>();
	std::function<void()> onSuccess =
			[future] () {
				future->onSuccess();
			};

	std::function<void(const std::shared_ptr<exceptions::ProviderRuntimeException>&)> onError =
			[future] (const std::shared_ptr<exceptions::ProviderRuntimeException>& error) {
				future->onError(error);
			};

	//see header for more information
	vehicleDataCommonCaller->setLanguage(language, std::move(onSuccess), std::move(onError));
	return future->get();
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
{
	joynr::SubscriptionRequest subscriptionRequest;
	return subscribeToLanguage(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
{
	joynr::SubscriptionRequest subscriptionRequest;
	subscriptionRequest.setSubscriptionId(subscriptionId);
	return subscribeToLanguage(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonInProcessConnector::subscribeToLanguage(
		std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
		std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
		joynr::SubscriptionRequest& subscriptionRequest)
{
	JOYNR_LOG_TRACE(logger, "Subscribing to language.");
	assert(subscriptionManager != nullptr);
	std::string attributeName("language");
	auto future = std::make_shared<Future<std::string>>();
	auto subscriptionCallback = std::make_shared<
			joynr::UnicastSubscriptionCallback<std::string>
	>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager);
	subscriptionManager->registerSubscription(
			attributeName,
			subscriptionCallback,
			subscriptionListener,
			subscriptionQos,
			subscriptionRequest);
	JOYNR_LOG_TRACE(logger, "Registered subscription: {}", subscriptionRequest.toString());
	assert(address);
	std::shared_ptr<joynr::RequestCaller> caller = address->getRequestCaller();
	assert(caller);
	std::shared_ptr<VehicleDataCommonRequestCaller> requestCaller = std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(caller);

	joynr::CallContext callContext;
	callContext.setPrincipal(securityManager->getCurrentProcessUserId());

	if(!requestCaller) {
		assert(publicationManager != nullptr);
		/**
		* Provider not registered yet
		* Dispatcher will call publicationManger->restore when a new provider is added to activate
		* subscriptions for that provider
		*/
		publicationManager->add(proxyParticipantId, providerParticipantId, subscriptionRequest);
	} else {
		publicationManager->add(proxyParticipantId, providerParticipantId, caller, subscriptionRequest, inProcessPublicationSender);
	}
	return future;
}

void VehicleDataCommonInProcessConnector::unsubscribeFromLanguage(const std::string& subscriptionId)
 {
	JOYNR_LOG_TRACE(logger, "Unsubscribing. Id={}", subscriptionId);
	assert(publicationManager != nullptr);
	JOYNR_LOG_TRACE(logger, "Stopping publications by publication manager.");
	publicationManager->stopPublication(subscriptionId);
	assert(subscriptionManager != nullptr);
	JOYNR_LOG_TRACE(logger, "Unregistering attribute subscription.");
	subscriptionManager->unregisterSubscription(subscriptionId);
}




} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
