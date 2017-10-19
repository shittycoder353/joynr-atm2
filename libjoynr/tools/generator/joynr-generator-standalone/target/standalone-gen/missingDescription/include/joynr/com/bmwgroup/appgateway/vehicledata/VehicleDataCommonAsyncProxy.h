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

#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONASYNCPROXY_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONASYNCPROXY_H

#include "joynr/PrivateCopyAssign.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonProxyBase.h"

namespace joynr
{
	template <class ... Ts> class Future;

namespace exceptions
{
	class JoynrException;
	class JoynrRuntimeException;
} // namespace exceptions
} // namespace joynr

#include <string>

#include <memory>

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
/** @brief proxy class for asynchronous calls of interface VehicleDataCommon
 *
 * @version 0.1
 */
class  VehicleDataCommonAsyncProxy :
		virtual public VehicleDataCommonProxyBase,
		virtual public IVehicleDataCommonAsync
{
public:

	/**
	 * @brief Parameterized constructor
	 * @param connectorFactory The connector factory
	 * @param domain The provider domain
	 * @param qosSettings The quality of service settings
	 */
	VehicleDataCommonAsyncProxy(
			std::weak_ptr<joynr::JoynrRuntime> runtime,
			joynr::ConnectorFactory* connectorFactory,
			const std::string& domain,
			const joynr::MessagingQos& qosSettings
	);

	
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

	friend class VehicleDataCommonProxy;

private:
	DISALLOW_COPY_AND_ASSIGN(VehicleDataCommonAsyncProxy);
};

} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONASYNCPROXY_H

