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
#include "joynr/com/bmwgroup/appgateway/vehicledata/DefaultVehicleDataCommonProvider.h"

#include <chrono>
#include <cstdint>
#include <tuple>


namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 

INIT_LOGGER(DefaultVehicleDataCommonProvider);

DefaultVehicleDataCommonProvider::DefaultVehicleDataCommonProvider() :
		VehicleDataCommonAbstractProvider()
		,
		vin(),
		actualSpeedKmh(),
		language()
{
	// default uses a priority that is the current time,
	// causing arbitration to the last started instance if highest priority arbitrator is used
	std::chrono::milliseconds millisSinceEpoch = std::chrono::duration_cast<std::chrono::milliseconds>(
			std::chrono::system_clock::now().time_since_epoch()
	);
}

DefaultVehicleDataCommonProvider::~DefaultVehicleDataCommonProvider()
{
}

// attributes
void DefaultVehicleDataCommonProvider::getVin(
		std::function<void(
				const std::string&
		)> onSuccess,
		std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
) {
	std::ignore = onError;
	onSuccess(vin);
}

void DefaultVehicleDataCommonProvider::getActualSpeedKmh(
		std::function<void(
				const double&
		)> onSuccess,
		std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
) {
	std::ignore = onError;
	onSuccess(actualSpeedKmh);
}

void DefaultVehicleDataCommonProvider::getLanguage(
		std::function<void(
				const std::string&
		)> onSuccess,
		std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
) {
	std::ignore = onError;
	onSuccess(language);
}

void DefaultVehicleDataCommonProvider::setLanguage(
		const std::string& language,
		std::function<void()> onSuccess,
		std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
) {
	std::ignore = onError;
	this->language = language;
	languageChanged(language);
	onSuccess();
}


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
