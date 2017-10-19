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
#include <functional>
#include <tuple>

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonRequestInterpreter.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonRequestCaller.h"
#include "joynr/Util.h"
#include "joynr/Request.h"
#include "joynr/OneWayRequest.h"
#include "joynr/BaseReply.h"
#include "joynr/exceptions/JoynrException.h"
#include "joynr/exceptions/MethodInvocationException.h"

#include <string>

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 

INIT_LOGGER(VehicleDataCommonRequestInterpreter);

void VehicleDataCommonRequestInterpreter::execute(
		std::shared_ptr<joynr::RequestCaller> requestCaller,
		Request& request,
		std::function<void (BaseReply&& reply)>&& onSuccess,
		std::function<void (const std::shared_ptr<exceptions::JoynrException>& exception)>&& onError
) {
	// cast generic RequestCaller to VehicleDataCommonRequestcaller
	std::shared_ptr<VehicleDataCommonRequestCaller> vehicleDataCommonRequestCallerVar =
			std::dynamic_pointer_cast<VehicleDataCommonRequestCaller>(requestCaller);

	const std::vector<std::string>& paramTypes = request.getParamDatatypes();
	const std::string& methodName = request.getMethodName();

	// execute operation
		if (methodName == "getVin" && paramTypes.size() == 0){
			auto requestCallerOnSuccess =
					[onSuccess = std::move(onSuccess)](std::string vin){
						BaseReply reply;
						reply.setResponse(std::move(vin));
						onSuccess(std::move(reply));
					};
			vehicleDataCommonRequestCallerVar->getVin(
				std::move(requestCallerOnSuccess),
				std::move(onError));
			return;
		}
		if (methodName == "getActualSpeedKmh" && paramTypes.size() == 0){
			auto requestCallerOnSuccess =
					[onSuccess = std::move(onSuccess)](double actualSpeedKmh){
						BaseReply reply;
						reply.setResponse(std::move(actualSpeedKmh));
						onSuccess(std::move(reply));
					};
			vehicleDataCommonRequestCallerVar->getActualSpeedKmh(
				std::move(requestCallerOnSuccess),
				std::move(onError));
			return;
		}
		if (methodName == "getLanguage" && paramTypes.size() == 0){
			auto requestCallerOnSuccess =
					[onSuccess = std::move(onSuccess)](std::string language){
						BaseReply reply;
						reply.setResponse(std::move(language));
						onSuccess(std::move(reply));
					};
			vehicleDataCommonRequestCallerVar->getLanguage(
				std::move(requestCallerOnSuccess),
				std::move(onError));
			return;
		}
	if (methodName == "setLanguage" && paramTypes.size() == 1){
		try {
			std::string typedInputLanguage;
			request.getParams(typedInputLanguage);
			auto requestCallerOnSuccess =
					[onSuccess = std::move(onSuccess)] () {
						BaseReply reply;
						reply.setResponse();
						onSuccess(std::move(reply));
					};
			vehicleDataCommonRequestCallerVar->setLanguage(
																typedInputLanguage,
																std::move(requestCallerOnSuccess),
																onError);
		} catch (const std::exception&) {
			onError(
				std::make_shared<exceptions::MethodInvocationException>(
					"Illegal argument for attribute setter setLanguage (String)",
					requestCaller->getProviderVersion()));
		}
		return;
	}

	JOYNR_LOG_WARN(logger, "unknown method name for interface VehicleDataCommon: {}", request.getMethodName());
	onError(
		std::make_shared<exceptions::MethodInvocationException>(
			"unknown method name for interface VehicleDataCommon: " + request.getMethodName(),
			requestCaller->getProviderVersion()));
}

void VehicleDataCommonRequestInterpreter::execute(
		std::shared_ptr<joynr::RequestCaller> requestCaller,
		OneWayRequest& request
) {
	std::ignore = requestCaller;

	JOYNR_LOG_WARN(logger, "unknown method name for interface VehicleDataCommon: {}", request.getMethodName());
}

} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
