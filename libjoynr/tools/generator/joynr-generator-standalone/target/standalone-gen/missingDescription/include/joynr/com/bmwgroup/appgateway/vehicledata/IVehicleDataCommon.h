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

#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_IVEHICLEDATACOMMON_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_IVEHICLEDATACOMMON_H


#include <vector>
#include <string>


#include <memory>
#include <functional>

namespace joynr
{
	template <class ... Ts> class Future;

namespace exceptions
{
	class JoynrException;
	class JoynrRuntimeException;
} // namespace exceptions

} // namespace joynr

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 

/**
 * @brief Base interface.
 *
 * @version 0.1
 */
class  IVehicleDataCommonBase {
public:
	IVehicleDataCommonBase() = default;
	virtual ~IVehicleDataCommonBase() = default;

	static const std::string& INTERFACE_NAME();
	/**
	 * @brief MAJOR_VERSION The major version of this provider interface as specified in the
	 * Franca model.
	 */
	static const std::uint32_t MAJOR_VERSION;
	/**
	 * @brief MINOR_VERSION The minor version of this provider interface as specified in the
	 * Franca model.
	 */
	static const std::uint32_t MINOR_VERSION;
};


/**
 * @brief This is the VehicleDataCommon synchronous interface.
 *
 * @version 0.1
 */
class  IVehicleDataCommonSync :
		virtual public IVehicleDataCommonBase
{
public:
	~IVehicleDataCommonSync() override = default;
	
	/**
	* @brief Synchronous getter for the vin attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	virtual 
	void getVin(std::string& vin)
	= 0;
	
	
	/**
	* @brief Synchronous getter for the actualSpeedKmh attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	virtual 
	void getActualSpeedKmh(double& actualSpeedKmh)
	= 0;
	
	
	/**
	* @brief Synchronous getter for the language attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	virtual 
	void getLanguage(std::string& language)
	= 0;
	
	
	/**
	* @brief Synchronous setter for the language attribute.
	*
	* @param language The value to set.
	* @throws JoynrException if the request is not successful
	*/
	virtual 
	void setLanguage(const std::string& language)
	= 0;
};

/**
 * @brief This is the VehicleDataCommon asynchronous interface.
 *
 * @version 0.1
 */
class  IVehicleDataCommonAsync :
		virtual public IVehicleDataCommonBase
{
public:
	~IVehicleDataCommonAsync() override = default;
	
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
	virtual 
	std::shared_ptr<joynr::Future<std::string> > getVinAsync(
				std::function<void(const std::string& vin)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	= 0;
	
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
	virtual 
	std::shared_ptr<joynr::Future<double> > getActualSpeedKmhAsync(
				std::function<void(const double& actualSpeedKmh)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	= 0;
	
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
	virtual 
	std::shared_ptr<joynr::Future<std::string> > getLanguageAsync(
				std::function<void(const std::string& language)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	= 0;
	
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
	virtual 
	std::shared_ptr<joynr::Future<void> > setLanguageAsync(
				std::string language,
				std::function<void(void)> onSuccess = nullptr,
				std::function<void(const joynr::exceptions::JoynrRuntimeException& error)> onError = nullptr)
	= 0;
};

/**
 * @brief This is the VehicleDataCommon interface.
 *
 * @version 0.1
 */
class  IVehicleDataCommon : virtual public IVehicleDataCommonSync, virtual public IVehicleDataCommonAsync {
public:
	~IVehicleDataCommon() override = default;
	using IVehicleDataCommonSync::getVin;
	using IVehicleDataCommonAsync::getVinAsync;
	using IVehicleDataCommonSync::getActualSpeedKmh;
	using IVehicleDataCommonAsync::getActualSpeedKmhAsync;
	using IVehicleDataCommonSync::getLanguage;
	using IVehicleDataCommonAsync::getLanguageAsync;
	using IVehicleDataCommonSync::setLanguage;
	using IVehicleDataCommonAsync::setLanguageAsync;
};


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_IVEHICLEDATACOMMON_H
