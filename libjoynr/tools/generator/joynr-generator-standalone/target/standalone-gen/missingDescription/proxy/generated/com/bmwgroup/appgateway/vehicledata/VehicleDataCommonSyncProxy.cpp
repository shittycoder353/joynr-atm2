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

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonSyncProxy.h"

#include <string>

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
// The proxies will contain all arbitration checks
// the connectors will contain the JSON related code

VehicleDataCommonSyncProxy::VehicleDataCommonSyncProxy(
		std::weak_ptr<joynr::JoynrRuntime> runtime,
		joynr::ConnectorFactory* connectorFactory,
		const std::string &domain,
		const joynr::MessagingQos &qosSettings
) :
		joynr::ProxyBase(runtime, connectorFactory, domain, qosSettings),
		VehicleDataCommonProxyBase(runtime, connectorFactory, domain, qosSettings)
{
}

void VehicleDataCommonSyncProxy::getVin(std::string& vin)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot invoke getVin because the required runtime has been already destroyed.";
		}
		else {
			errorMsg = "proxy cannot invoke getVin because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		exceptions::JoynrRuntimeException error(errorMsg);
		throw error;
	}
	else{
		return connector->getVin(vin);
	}
}

void VehicleDataCommonSyncProxy::getActualSpeedKmh(double& actualSpeedKmh)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot invoke getActualSpeedKmh because the required runtime has been already destroyed.";
		}
		else {
			errorMsg = "proxy cannot invoke getActualSpeedKmh because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		exceptions::JoynrRuntimeException error(errorMsg);
		throw error;
	}
	else{
		return connector->getActualSpeedKmh(actualSpeedKmh);
	}
}

void VehicleDataCommonSyncProxy::getLanguage(std::string& language)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot invoke getLanguage because the required runtime has been already destroyed.";
		}
		else {
			errorMsg = "proxy cannot invoke getLanguage because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		exceptions::JoynrRuntimeException error(errorMsg);
		throw error;
	}
	else{
		return connector->getLanguage(language);
	}
}
void VehicleDataCommonSyncProxy::setLanguage(const std::string& language)
{
	auto runtimeSharedPtr = runtime.lock();
	if (!runtimeSharedPtr || (connector==nullptr)) {
		std::string errorMsg;
		if (!runtimeSharedPtr) {
			errorMsg = "proxy cannot invoke setLanguage because the required runtime has been already destroyed.";
		}
		else {
			errorMsg = "proxy cannot invoke setLanguage because the communication end partner is not (yet) known";
		}
		JOYNR_LOG_WARN(logger, errorMsg);
		exceptions::JoynrRuntimeException error(errorMsg);
		throw error;
	}
	else{
		return connector->setLanguage(language);
	}
}


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
