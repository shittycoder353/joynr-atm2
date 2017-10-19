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
#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONREQUESTCALLER_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONREQUESTCALLER_H

#include <functional>
#include <memory>

#include "joynr/PrivateCopyAssign.h"
#include "joynr/RequestCaller.h"
#include "joynr/exceptions/JoynrException.h"
#include "joynr/types/Version.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/IVehicleDataCommon.h"

#include <string>
#include "joynr/Logger.h"

namespace joynr
{
class UnicastBroadcastListener;
class SubscriptionAttributeListener;
} // namespace joynr

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 

class VehicleDataCommonProvider;

/** @brief RequestCaller for interface VehicleDataCommon */
class  VehicleDataCommonRequestCaller : public joynr::RequestCaller {
public:
	/**
	 * @brief parameterized constructor
	 * @param provider The provider instance
	 */
	explicit VehicleDataCommonRequestCaller(std::shared_ptr<VehicleDataCommonProvider> provider);

	/** @brief Destructor */
	~VehicleDataCommonRequestCaller() override = default;

	// attributes
	/**
	 * @brief Gets the value of the Franca attribute Vin
	 * @param onSuccess A callback function to be called once the asynchronous computation has
	 * finished with success. It must expect a request status object as well as the return value.
	 * @param onError A callback function to be called once the asynchronous computation fails. It must expect the exception.
	 */
	virtual void getVin(
			std::function<void(
					const std::string&
			)>&& onSuccess,
			std::function<void(
					const std::shared_ptr<exceptions::ProviderRuntimeException>&
			)> onError
	);

	/**
	 * @brief Gets the value of the Franca attribute ActualSpeedKmh
	 * @param onSuccess A callback function to be called once the asynchronous computation has
	 * finished with success. It must expect a request status object as well as the return value.
	 * @param onError A callback function to be called once the asynchronous computation fails. It must expect the exception.
	 */
	virtual void getActualSpeedKmh(
			std::function<void(
					const double&
			)>&& onSuccess,
			std::function<void(
					const std::shared_ptr<exceptions::ProviderRuntimeException>&
			)> onError
	);

	/**
	 * @brief Gets the value of the Franca attribute Language
	 * @param onSuccess A callback function to be called once the asynchronous computation has
	 * finished with success. It must expect a request status object as well as the return value.
	 * @param onError A callback function to be called once the asynchronous computation fails. It must expect the exception.
	 */
	virtual void getLanguage(
			std::function<void(
					const std::string&
			)>&& onSuccess,
			std::function<void(
					const std::shared_ptr<exceptions::ProviderRuntimeException>&
			)> onError
	);
	/**
	 * @brief Sets the value of the Franca attribute Language
	 * @param language The new value of the attribute
	 * @param onSuccess A callback function to be called once the asynchronous computation has
	 * finished with success. It must expect a request status object.
	 * @param onError A callback function to be called once the asynchronous computation fails. It must expect the exception.
	 */
	virtual void setLanguage(
			const std::string& language,
			std::function<void()>&& onSuccess,
			std::function<void(
					const std::shared_ptr<exceptions::ProviderRuntimeException>&
			)> onError
	);


	/**
	 * @brief Get the version of the provider instance
	 * @return the version of the provider instance
	 */
	joynr::types::Version getProviderVersion() override;

protected:
	std::shared_ptr<IJoynrProvider> getProvider() override;

private:
	DISALLOW_COPY_AND_ASSIGN(VehicleDataCommonRequestCaller);
	std::shared_ptr<joynr::com::bmwgroup::appgateway::vehicledata::VehicleDataCommonProvider> provider;
	ADD_LOGGER(VehicleDataCommonRequestCaller);
};


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONREQUESTCALLER_H
