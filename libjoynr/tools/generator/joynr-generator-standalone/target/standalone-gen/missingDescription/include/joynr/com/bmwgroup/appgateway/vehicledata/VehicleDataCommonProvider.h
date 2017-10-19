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
#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROVIDER_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROVIDER_H

#include <string>

#include "joynr/PrivateCopyAssign.h"

#include "joynr/IJoynrProvider.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/IVehicleDataCommon.h"
#include "joynr/RequestCallerFactory.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonRequestCaller.h"

#include <string>

#include <memory>

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 

/**
 * @brief Provider class for interface VehicleDataCommon
 *
 * @version 0.1
 */
class  VehicleDataCommonProvider : public virtual IJoynrProvider
{

public:
	/** @brief Default constructor */
	VehicleDataCommonProvider();

	//for each Attribute the provider needs setters, sync and async getters.
	//They have default implementation for pushing Providers and can be overwritten by pulling Providers.

	/** @brief Destructor */
	~VehicleDataCommonProvider() override;

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

	// attributes
	/**
	 * @brief Gets Vin
	 *
	 * This method is called by a joynr middleware thread. The provider implementation
	 * must not block this thread; it must be released immediately after the method is
	 * called. Computations or further blocking calls must be performed asynchronously.
	 * Return the result of these computations by calling the onSuccess or onError
	 * callbacks asynchronously.
	 *
	 * @param onSuccess A callback function to be called with the attribute value once the asynchronous computation has
	 * finished with success. It expects a request status object as parameter.
	 * @param onError A callback function to be called once the asynchronous computation fails. It expects an exception.
	 */
	virtual void getVin(
			std::function<void(
					const std::string&
			)> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) = 0;
	/**
	 * @brief vinChanged must be called by a concrete provider
	 * to signal attribute modifications. It is used to implement onchange
	 * subscriptions.
	 * @param vin the new attribute value
	 */
	virtual void vinChanged(
			const std::string& vin
	) = 0;

	/**
	 * @brief Gets ActualSpeedKmh
	 *
	 * This method is called by a joynr middleware thread. The provider implementation
	 * must not block this thread; it must be released immediately after the method is
	 * called. Computations or further blocking calls must be performed asynchronously.
	 * Return the result of these computations by calling the onSuccess or onError
	 * callbacks asynchronously.
	 *
	 * @param onSuccess A callback function to be called with the attribute value once the asynchronous computation has
	 * finished with success. It expects a request status object as parameter.
	 * @param onError A callback function to be called once the asynchronous computation fails. It expects an exception.
	 */
	virtual void getActualSpeedKmh(
			std::function<void(
					const double&
			)> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) = 0;
	/**
	 * @brief actualSpeedKmhChanged must be called by a concrete provider
	 * to signal attribute modifications. It is used to implement onchange
	 * subscriptions.
	 * @param actualSpeedKmh the new attribute value
	 */
	virtual void actualSpeedKmhChanged(
			const double& actualSpeedKmh
	) = 0;

	/**
	 * @brief Gets Language
	 *
	 * This method is called by a joynr middleware thread. The provider implementation
	 * must not block this thread; it must be released immediately after the method is
	 * called. Computations or further blocking calls must be performed asynchronously.
	 * Return the result of these computations by calling the onSuccess or onError
	 * callbacks asynchronously.
	 *
	 * @param onSuccess A callback function to be called with the attribute value once the asynchronous computation has
	 * finished with success. It expects a request status object as parameter.
	 * @param onError A callback function to be called once the asynchronous computation fails. It expects an exception.
	 */
	virtual void getLanguage(
			std::function<void(
					const std::string&
			)> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) = 0;
	/**
	 * @brief Sets Language
	 *
	 * This method is called by a joynr middleware thread. The provider implementation
	 * must not block this thread; it must be released immediately after the method is
	 * called. Computations or further blocking calls must be performed asynchronously.
	 * Return the result of these computations by calling the onSuccess or onError
	 * callbacks asynchronously.
	 *
	 * @param language the new value of the attribute
	 * @param onSuccess A callback function to be called  once the asynchronous computation has
	 * finished with success. It expects a request status object as parameter.
	 * @param onError A callback function to be called once the asynchronous computation fails. It expects an exception.
	 */
	virtual void setLanguage(
			const std::string& language,
			std::function<void()> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) = 0;
	/**
	 * @brief languageChanged must be called by a concrete provider
	 * to signal attribute modifications. It is used to implement onchange
	 * subscriptions.
	 * @param language the new attribute value
	 */
	virtual void languageChanged(
			const std::string& language
	) = 0;

private:
	DISALLOW_COPY_AND_ASSIGN(VehicleDataCommonProvider);
};

} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr


namespace joynr {

// specialization of traits class RequestCallerTraits
// this links VehicleDataCommonProvider with VehicleDataCommonRequestCaller
template <>
struct RequestCallerTraits<joynr::com::bmwgroup::appgateway::vehicledata::VehicleDataCommonProvider>
{
	using RequestCaller = joynr::com::bmwgroup::appgateway::vehicledata::VehicleDataCommonRequestCaller;
};

} // namespace joynr

#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONPROVIDER_H
