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
#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_DEFAULTVEHICLEDATACOMMONPROVIDER_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_DEFAULTVEHICLEDATACOMMONPROVIDER_H

#include <functional>

#include "joynr/com/bmwgroup/appgateway/vehicledata/IVehicleDataCommon.h"
#include "joynr/Logger.h"

#include <string>

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonAbstractProvider.h"

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 

class  DefaultVehicleDataCommonProvider : public joynr::com::bmwgroup::appgateway::vehicledata::VehicleDataCommonAbstractProvider {

public:
	DefaultVehicleDataCommonProvider();

	~DefaultVehicleDataCommonProvider() override;

	// attributes
	void getVin(
			std::function<void(
					const std::string&
			)> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) override;

	void getActualSpeedKmh(
			std::function<void(
					const double&
			)> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) override;

	void getLanguage(
			std::function<void(
					const std::string&
			)> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) override;
	void setLanguage(
			const std::string& language,
			std::function<void()> onSuccess,
			std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
	) override;

protected:
	std::string vin;
	double actualSpeedKmh;
	std::string language;

private:
	ADD_LOGGER(DefaultVehicleDataCommonProvider);

};


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr

#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_DEFAULTVEHICLEDATACOMMONPROVIDER_H
