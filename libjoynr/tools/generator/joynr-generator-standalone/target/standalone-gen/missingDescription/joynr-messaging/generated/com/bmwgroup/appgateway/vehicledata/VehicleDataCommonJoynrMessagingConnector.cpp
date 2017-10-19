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

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonJoynrMessagingConnector.h"
#include "joynr/serializer/Serializer.h"
#include "joynr/ReplyCaller.h"
#include "joynr/IMessageSender.h"
#include "joynr/UnicastSubscriptionCallback.h"
#include "joynr/MulticastSubscriptionCallback.h"
#include "joynr/Util.h"
#include "joynr/SubscriptionStop.h"
#include "joynr/Future.h"
#include <cstdint>
#include "joynr/SubscriptionUtil.h"
#include "joynr/exceptions/JoynrException.h"
#include "joynr/Request.h"
#include "joynr/OneWayRequest.h"
#include "joynr/SubscriptionRequest.h"




namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
VehicleDataCommonJoynrMessagingConnector::VehicleDataCommonJoynrMessagingConnector(
		std::shared_ptr<joynr::IMessageSender> messageSender,
		std::shared_ptr<joynr::ISubscriptionManager> subscriptionManager,
		const std::string& domain,
		const std::string& proxyParticipantId,
		const joynr::MessagingQos &qosSettings,
		const joynr::types::DiscoveryEntryWithMetaInfo& providerDiscoveryEntry)
	: joynr::AbstractJoynrMessagingConnector(messageSender, subscriptionManager, domain, INTERFACE_NAME(), proxyParticipantId, qosSettings, providerDiscoveryEntry)
{
}

void VehicleDataCommonJoynrMessagingConnector::getVin(std::string& vin)
{
	auto future = getVinAsync();
	future->get(vin);
}

std::shared_ptr<joynr::Future<std::string> > VehicleDataCommonJoynrMessagingConnector::getVinAsync(
			std::function<void(const std::string& vin)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	joynr::Request request;
	// explicitly set to no parameters
	request.setParams();
	request.setMethodName("getVin");
	auto future = std::make_shared<joynr::Future<std::string>>();

	std::function<void(const std::string& vin)> onSuccessWrapper = [
			future,
			onSuccess = std::move(onSuccess),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] (const std::string& vin) {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST returns successful: requestReplyId: {}, method: {}, response: {}",
				requestReplyId,
				methodName,
				joynr::serializer::serializeToJson(vin)
		);
		future->onSuccess(vin);
		if (onSuccess){
			onSuccess(vin);
		}
	};

	std::function<void(const std::shared_ptr<exceptions::JoynrException>& error)> onErrorWrapper = [
			future,
			onError = std::move(onError),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] (const std::shared_ptr<exceptions::JoynrException>& error) {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST returns error: requestReplyId: {}, method: {}, response: {}",
				requestReplyId,
				methodName,
				error->what()
		);
		future->onError(error);
		if (onError){
			onError(static_cast<const exceptions::JoynrRuntimeException&>(*error));
		}
	};

	try {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST call proxy: requestReplyId: {}, method: {}, proxy "
				"participantId: {}, provider participantId: [{}]",
				request.getRequestReplyId(),
				request.getMethodName(),
				proxyParticipantId,
				providerParticipantId);
		auto replyCaller = std::make_shared<joynr::ReplyCaller<std::string>>(std::move(onSuccessWrapper), std::move(onErrorWrapper));
		operationRequest(std::move(replyCaller), std::move(request));
	} catch (const std::invalid_argument& exception) {
		throw joynr::exceptions::MethodInvocationException(exception.what());
	}

	return future;
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
 {
	joynr::SubscriptionRequest subscriptionRequest;
	return subscribeToVin(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
 {

	joynr::SubscriptionRequest subscriptionRequest;
	subscriptionRequest.setSubscriptionId(subscriptionId);
	return subscribeToVin(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToVin(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			SubscriptionRequest& subscriptionRequest
) {
	JOYNR_LOG_DEBUG(logger, "Subscribing to vin.");
	std::string attributeName("vin");
	joynr::MessagingQos clonedMessagingQos(qosSettings);
	clonedMessagingQos.setTtl(ISubscriptionManager::convertExpiryDateIntoTtlMs(*subscriptionQos));

	auto future = std::make_shared<Future<std::string>>();
	auto subscriptionCallback = std::make_shared<joynr::UnicastSubscriptionCallback<std::string>
	>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager.get());
	subscriptionManager->registerSubscription(
				attributeName,
				subscriptionCallback,
				subscriptionListener,
				subscriptionQos,
				subscriptionRequest);
	JOYNR_LOG_DEBUG(logger,
			"SUBSCRIPTION call proxy: subscriptionId: {}, attribute: {}, qos: {}, proxy "
			"participantId: {}, provider participantId: [{}]",
			subscriptionRequest.getSubscriptionId(),
			attributeName,
			joynr::serializer::serializeToJson(*subscriptionQos),
			proxyParticipantId,
			providerParticipantId);
	messageSender->sendSubscriptionRequest(
				proxyParticipantId,
				providerParticipantId,
				clonedMessagingQos,
				subscriptionRequest,
				providerDiscoveryEntry.getIsLocal()
	);
	return future;
}

void VehicleDataCommonJoynrMessagingConnector::unsubscribeFromVin(const std::string& subscriptionId)
 {
	joynr::SubscriptionStop subscriptionStop;
	subscriptionStop.setSubscriptionId(subscriptionId);

	subscriptionManager->unregisterSubscription(subscriptionId);
	messageSender->sendSubscriptionStop(
				proxyParticipantId,
				providerParticipantId,
				qosSettings,
				subscriptionStop
	);
}

void VehicleDataCommonJoynrMessagingConnector::getActualSpeedKmh(double& actualSpeedKmh)
{
	auto future = getActualSpeedKmhAsync();
	future->get(actualSpeedKmh);
}

std::shared_ptr<joynr::Future<double> > VehicleDataCommonJoynrMessagingConnector::getActualSpeedKmhAsync(
			std::function<void(const double& actualSpeedKmh)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	joynr::Request request;
	// explicitly set to no parameters
	request.setParams();
	request.setMethodName("getActualSpeedKmh");
	auto future = std::make_shared<joynr::Future<double>>();

	std::function<void(const double& actualSpeedKmh)> onSuccessWrapper = [
			future,
			onSuccess = std::move(onSuccess),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] (const double& actualSpeedKmh) {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST returns successful: requestReplyId: {}, method: {}, response: {}",
				requestReplyId,
				methodName,
				joynr::serializer::serializeToJson(actualSpeedKmh)
		);
		future->onSuccess(actualSpeedKmh);
		if (onSuccess){
			onSuccess(actualSpeedKmh);
		}
	};

	std::function<void(const std::shared_ptr<exceptions::JoynrException>& error)> onErrorWrapper = [
			future,
			onError = std::move(onError),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] (const std::shared_ptr<exceptions::JoynrException>& error) {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST returns error: requestReplyId: {}, method: {}, response: {}",
				requestReplyId,
				methodName,
				error->what()
		);
		future->onError(error);
		if (onError){
			onError(static_cast<const exceptions::JoynrRuntimeException&>(*error));
		}
	};

	try {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST call proxy: requestReplyId: {}, method: {}, proxy "
				"participantId: {}, provider participantId: [{}]",
				request.getRequestReplyId(),
				request.getMethodName(),
				proxyParticipantId,
				providerParticipantId);
		auto replyCaller = std::make_shared<joynr::ReplyCaller<double>>(std::move(onSuccessWrapper), std::move(onErrorWrapper));
		operationRequest(std::move(replyCaller), std::move(request));
	} catch (const std::invalid_argument& exception) {
		throw joynr::exceptions::MethodInvocationException(exception.what());
	}

	return future;
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
 {
	joynr::SubscriptionRequest subscriptionRequest;
	return subscribeToActualSpeedKmh(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
 {

	joynr::SubscriptionRequest subscriptionRequest;
	subscriptionRequest.setSubscriptionId(subscriptionId);
	return subscribeToActualSpeedKmh(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToActualSpeedKmh(
			std::shared_ptr<joynr::ISubscriptionListener<double> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			SubscriptionRequest& subscriptionRequest
) {
	JOYNR_LOG_DEBUG(logger, "Subscribing to actualSpeedKmh.");
	std::string attributeName("actualSpeedKmh");
	joynr::MessagingQos clonedMessagingQos(qosSettings);
	clonedMessagingQos.setTtl(ISubscriptionManager::convertExpiryDateIntoTtlMs(*subscriptionQos));

	auto future = std::make_shared<Future<std::string>>();
	auto subscriptionCallback = std::make_shared<joynr::UnicastSubscriptionCallback<double>
	>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager.get());
	subscriptionManager->registerSubscription(
				attributeName,
				subscriptionCallback,
				subscriptionListener,
				subscriptionQos,
				subscriptionRequest);
	JOYNR_LOG_DEBUG(logger,
			"SUBSCRIPTION call proxy: subscriptionId: {}, attribute: {}, qos: {}, proxy "
			"participantId: {}, provider participantId: [{}]",
			subscriptionRequest.getSubscriptionId(),
			attributeName,
			joynr::serializer::serializeToJson(*subscriptionQos),
			proxyParticipantId,
			providerParticipantId);
	messageSender->sendSubscriptionRequest(
				proxyParticipantId,
				providerParticipantId,
				clonedMessagingQos,
				subscriptionRequest,
				providerDiscoveryEntry.getIsLocal()
	);
	return future;
}

void VehicleDataCommonJoynrMessagingConnector::unsubscribeFromActualSpeedKmh(const std::string& subscriptionId)
 {
	joynr::SubscriptionStop subscriptionStop;
	subscriptionStop.setSubscriptionId(subscriptionId);

	subscriptionManager->unregisterSubscription(subscriptionId);
	messageSender->sendSubscriptionStop(
				proxyParticipantId,
				providerParticipantId,
				qosSettings,
				subscriptionStop
	);
}

void VehicleDataCommonJoynrMessagingConnector::getLanguage(std::string& language)
{
	auto future = getLanguageAsync();
	future->get(language);
}

std::shared_ptr<joynr::Future<std::string> > VehicleDataCommonJoynrMessagingConnector::getLanguageAsync(
			std::function<void(const std::string& language)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	joynr::Request request;
	// explicitly set to no parameters
	request.setParams();
	request.setMethodName("getLanguage");
	auto future = std::make_shared<joynr::Future<std::string>>();

	std::function<void(const std::string& language)> onSuccessWrapper = [
			future,
			onSuccess = std::move(onSuccess),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] (const std::string& language) {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST returns successful: requestReplyId: {}, method: {}, response: {}",
				requestReplyId,
				methodName,
				joynr::serializer::serializeToJson(language)
		);
		future->onSuccess(language);
		if (onSuccess){
			onSuccess(language);
		}
	};

	std::function<void(const std::shared_ptr<exceptions::JoynrException>& error)> onErrorWrapper = [
			future,
			onError = std::move(onError),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] (const std::shared_ptr<exceptions::JoynrException>& error) {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST returns error: requestReplyId: {}, method: {}, response: {}",
				requestReplyId,
				methodName,
				error->what()
		);
		future->onError(error);
		if (onError){
			onError(static_cast<const exceptions::JoynrRuntimeException&>(*error));
		}
	};

	try {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST call proxy: requestReplyId: {}, method: {}, proxy "
				"participantId: {}, provider participantId: [{}]",
				request.getRequestReplyId(),
				request.getMethodName(),
				proxyParticipantId,
				providerParticipantId);
		auto replyCaller = std::make_shared<joynr::ReplyCaller<std::string>>(std::move(onSuccessWrapper), std::move(onErrorWrapper));
		operationRequest(std::move(replyCaller), std::move(request));
	} catch (const std::invalid_argument& exception) {
		throw joynr::exceptions::MethodInvocationException(exception.what());
	}

	return future;
}

std::shared_ptr<joynr::Future<void> > VehicleDataCommonJoynrMessagingConnector::setLanguageAsync(
			std::string language,
			std::function<void(void)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	joynr::Request request;
	request.setMethodName("setLanguage");
	request.setParamDatatypes({"String"});
	request.setParams(language);

	auto future = std::make_shared<joynr::Future<void>>();

	std::function<void()> onSuccessWrapper = [
			future,
			onSuccess = std::move(onSuccess),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] () {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST returns successful: requestReplyId: {}, method: {}",
				requestReplyId,
				methodName
		);
		future->onSuccess();
		if (onSuccess) {
			onSuccess();
		}
	};

	std::function<void(const std::shared_ptr<exceptions::JoynrException>& error)> onErrorWrapper = [
			future,
			onError = std::move(onError),
			requestReplyId = request.getRequestReplyId(),
			methodName = request.getMethodName()
	] (const std::shared_ptr<exceptions::JoynrException>& error) {
		JOYNR_LOG_DEBUG(logger,
			"REQUEST returns error: requestReplyId: {}, method: {}, response: {}",
			requestReplyId,
			methodName,
			error->what()
		);
		future->onError(error);
		if (onError) {
			onError(static_cast<const exceptions::JoynrRuntimeException&>(*error));
		}
	};

	try {
		JOYNR_LOG_DEBUG(logger,
				"REQUEST call proxy: requestReplyId: {}, method: {}, params: {}, proxy "
				"participantId: {}, provider participantId: [{}]",
				request.getRequestReplyId(),
				request.getMethodName(),
				joynr::serializer::serializeToJson(language),
				proxyParticipantId,
				providerParticipantId);
		auto replyCaller = std::make_shared<joynr::ReplyCaller<void>>(std::move(onSuccessWrapper), std::move(onErrorWrapper));
		operationRequest(std::move(replyCaller), std::move(request));
	} catch (const std::invalid_argument& exception) {
		throw joynr::exceptions::MethodInvocationException(exception.what());
	}
	return future;
}

void VehicleDataCommonJoynrMessagingConnector::setLanguage(const std::string& language)
{
	auto future = setLanguageAsync(language);
	future->get();
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos)
 {
	joynr::SubscriptionRequest subscriptionRequest;
	return subscribeToLanguage(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			const std::string& subscriptionId)
 {

	joynr::SubscriptionRequest subscriptionRequest;
	subscriptionRequest.setSubscriptionId(subscriptionId);
	return subscribeToLanguage(subscriptionListener, subscriptionQos, subscriptionRequest);
}

std::shared_ptr<joynr::Future<std::string>> VehicleDataCommonJoynrMessagingConnector::subscribeToLanguage(
			std::shared_ptr<joynr::ISubscriptionListener<std::string> > subscriptionListener,
			std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
			SubscriptionRequest& subscriptionRequest
) {
	JOYNR_LOG_DEBUG(logger, "Subscribing to language.");
	std::string attributeName("language");
	joynr::MessagingQos clonedMessagingQos(qosSettings);
	clonedMessagingQos.setTtl(ISubscriptionManager::convertExpiryDateIntoTtlMs(*subscriptionQos));

	auto future = std::make_shared<Future<std::string>>();
	auto subscriptionCallback = std::make_shared<joynr::UnicastSubscriptionCallback<std::string>
	>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager.get());
	subscriptionManager->registerSubscription(
				attributeName,
				subscriptionCallback,
				subscriptionListener,
				subscriptionQos,
				subscriptionRequest);
	JOYNR_LOG_DEBUG(logger,
			"SUBSCRIPTION call proxy: subscriptionId: {}, attribute: {}, qos: {}, proxy "
			"participantId: {}, provider participantId: [{}]",
			subscriptionRequest.getSubscriptionId(),
			attributeName,
			joynr::serializer::serializeToJson(*subscriptionQos),
			proxyParticipantId,
			providerParticipantId);
	messageSender->sendSubscriptionRequest(
				proxyParticipantId,
				providerParticipantId,
				clonedMessagingQos,
				subscriptionRequest,
				providerDiscoveryEntry.getIsLocal()
	);
	return future;
}

void VehicleDataCommonJoynrMessagingConnector::unsubscribeFromLanguage(const std::string& subscriptionId)
 {
	joynr::SubscriptionStop subscriptionStop;
	subscriptionStop.setSubscriptionId(subscriptionId);

	subscriptionManager->unregisterSubscription(subscriptionId);
	messageSender->sendSubscriptionStop(
				proxyParticipantId,
				providerParticipantId,
				qosSettings,
				subscriptionStop
	);
}




} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
