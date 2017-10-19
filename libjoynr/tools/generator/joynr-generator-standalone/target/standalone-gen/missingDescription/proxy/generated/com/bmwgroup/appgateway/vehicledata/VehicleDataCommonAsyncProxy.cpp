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

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonAsyncProxy.h"

#include <string>

#include "joynr/Future.h"
#include "joynr/exceptions/JoynrException.h"

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
VehicleDataCommonAsyncProxy::VehicleDataCommonAsyncProxy(
		std::weak_ptr<joynr::JoynrRuntime> runtime,
		joynr::ConnectorFactory* connectorFactory,
		const std::string &domain,
		const joynr::MessagingQos &qosSettings
) :
	joynr::ProxyBase(runtime, connectorFactory, domain, qosSettings),
	VehicleDataCommonProxyBase(runtime, connectorFactory, domain, qosSettings)
{
}

/*
 * getVin
 */

std::shared_ptr<joynr::Future<std::string> > VehicleDataCommonAsyncProxy::getVinAsync(
			std::function<void(const std::string& vin)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorText;
		if (!runtimeSharedPtr) {
			errorText = "proxy cannot invoke getVin because the required runtime has been already destroyed.";
		}
		else {
			errorText = "proxy cannot invoke getVin, because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorText);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorText);
		if (onError) {
			onError(*error);
		}
		auto future = std::make_shared<joynr::Future<std::string>>();
		future->onError(error);
		return future;
	}
	else{
		return connector->getVinAsync(std::move(onSuccess), std::move(onError));
	}
}

/*
 * getActualSpeedKmh
 */

std::shared_ptr<joynr::Future<double> > VehicleDataCommonAsyncProxy::getActualSpeedKmhAsync(
			std::function<void(const double& actualSpeedKmh)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorText;
		if (!runtimeSharedPtr) {
			errorText = "proxy cannot invoke getActualSpeedKmh because the required runtime has been already destroyed.";
		}
		else {
			errorText = "proxy cannot invoke getActualSpeedKmh, because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorText);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorText);
		if (onError) {
			onError(*error);
		}
		auto future = std::make_shared<joynr::Future<double>>();
		future->onError(error);
		return future;
	}
	else{
		return connector->getActualSpeedKmhAsync(std::move(onSuccess), std::move(onError));
	}
}

/*
 * getLanguage
 */

std::shared_ptr<joynr::Future<std::string> > VehicleDataCommonAsyncProxy::getLanguageAsync(
			std::function<void(const std::string& language)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorText;
		if (!runtimeSharedPtr) {
			errorText = "proxy cannot invoke getLanguage because the required runtime has been already destroyed.";
		}
		else {
			errorText = "proxy cannot invoke getLanguage, because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorText);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorText);
		if (onError) {
			onError(*error);
		}
		auto future = std::make_shared<joynr::Future<std::string>>();
		future->onError(error);
		return future;
	}
	else{
		return connector->getLanguageAsync(std::move(onSuccess), std::move(onError));
	}
}

/*
 * setLanguage
 */
std::shared_ptr<joynr::Future<void> > VehicleDataCommonAsyncProxy::setLanguageAsync(
			std::string language,
			std::function<void(void)> onSuccess,
			std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorText;
		if (!runtimeSharedPtr) {
			errorText = "proxy cannot invoke setLanguage because the required runtime has been already destroyed.";
		}
		else {
			errorText = "proxy cannot invoke setLanguage, because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorText);
		auto error = std::make_shared<exceptions::JoynrRuntimeException>(errorText);
		if (onError) {
			onError(*error);
		}
		auto future = std::make_shared<joynr::Future<void>>();
		future->onError(error);
		return future;
	}
	else {
		return connector->setLanguageAsync(language, std::move(onSuccess), std::move(onError));
	}
}


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
