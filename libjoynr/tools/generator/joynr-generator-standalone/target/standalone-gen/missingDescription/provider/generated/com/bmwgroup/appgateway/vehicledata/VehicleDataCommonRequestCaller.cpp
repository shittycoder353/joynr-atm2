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

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonRequestCaller.h"
#include <string>
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonProvider.h"

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
VehicleDataCommonRequestCaller::VehicleDataCommonRequestCaller(std::shared_ptr<joynr::com::bmwgroup::appgateway::vehicledata::VehicleDataCommonProvider> provider)
	: joynr::RequestCaller(provider->getInterfaceName()),
	  provider(std::move(provider))
{
}

INIT_LOGGER(VehicleDataCommonRequestCaller);

// attributes
void VehicleDataCommonRequestCaller::getVin(
		std::function<void(
				const std::string& vin
		)>&& onSuccess,
		std::function<void(
				const std::shared_ptr<exceptions::ProviderRuntimeException>&
		)> onError
) {
	std::function<void(const exceptions::ProviderRuntimeException&)> onErrorWrapper =
	[onError] (const exceptions::ProviderRuntimeException& error) {
		onError(std::make_shared<exceptions::ProviderRuntimeException>(error));
	};
	try {
		provider->getVin(std::move(onSuccess), std::move(onErrorWrapper));
	} catch (const exceptions::ProviderRuntimeException& e) {
		onError(std::make_shared<exceptions::ProviderRuntimeException>(e));
	} catch (const exceptions::JoynrException& e) {
		std::string message = "Could not perform VehicleDataCommonRequestCaller::getVin, caught exception: " +
							e.getTypeName() + ":" + e.getMessage();
		onError(std::make_shared<exceptions::ProviderRuntimeException>(message));
	}
}

void VehicleDataCommonRequestCaller::getActualSpeedKmh(
		std::function<void(
				const double& actualSpeedKmh
		)>&& onSuccess,
		std::function<void(
				const std::shared_ptr<exceptions::ProviderRuntimeException>&
		)> onError
) {
	std::function<void(const exceptions::ProviderRuntimeException&)> onErrorWrapper =
	[onError] (const exceptions::ProviderRuntimeException& error) {
		onError(std::make_shared<exceptions::ProviderRuntimeException>(error));
	};
	try {
		provider->getActualSpeedKmh(std::move(onSuccess), std::move(onErrorWrapper));
	} catch (const exceptions::ProviderRuntimeException& e) {
		onError(std::make_shared<exceptions::ProviderRuntimeException>(e));
	} catch (const exceptions::JoynrException& e) {
		std::string message = "Could not perform VehicleDataCommonRequestCaller::getActualSpeedKmh, caught exception: " +
							e.getTypeName() + ":" + e.getMessage();
		onError(std::make_shared<exceptions::ProviderRuntimeException>(message));
	}
}

void VehicleDataCommonRequestCaller::getLanguage(
		std::function<void(
				const std::string& language
		)>&& onSuccess,
		std::function<void(
				const std::shared_ptr<exceptions::ProviderRuntimeException>&
		)> onError
) {
	std::function<void(const exceptions::ProviderRuntimeException&)> onErrorWrapper =
	[onError] (const exceptions::ProviderRuntimeException& error) {
		onError(std::make_shared<exceptions::ProviderRuntimeException>(error));
	};
	try {
		provider->getLanguage(std::move(onSuccess), std::move(onErrorWrapper));
	} catch (const exceptions::ProviderRuntimeException& e) {
		onError(std::make_shared<exceptions::ProviderRuntimeException>(e));
	} catch (const exceptions::JoynrException& e) {
		std::string message = "Could not perform VehicleDataCommonRequestCaller::getLanguage, caught exception: " +
							e.getTypeName() + ":" + e.getMessage();
		onError(std::make_shared<exceptions::ProviderRuntimeException>(message));
	}
}
void VehicleDataCommonRequestCaller::setLanguage(
		const std::string& language,
		std::function<void()>&& onSuccess,
		std::function<void(
				const std::shared_ptr<exceptions::ProviderRuntimeException>&
		)> onError
) {
	std::function<void(const exceptions::ProviderRuntimeException&)> onErrorWrapper =
	[onError] (const exceptions::ProviderRuntimeException& error) {
		onError(std::make_shared<exceptions::ProviderRuntimeException>(error));
	};
	try {
		provider->setLanguage(language, std::move(onSuccess), std::move(onErrorWrapper));
	} catch (const exceptions::ProviderRuntimeException& e) {
		std::string message = "Could not perform VehicleDataCommonRequestCaller::setLanguage, caught exception: " +
							e.getTypeName() + ":" + e.getMessage();
		onError(std::make_shared<exceptions::ProviderRuntimeException>(e));
	} catch (const exceptions::JoynrException& e) {
		std::string message = "Could not perform VehicleDataCommonRequestCaller::setLanguage, caught exception: " +
							e.getTypeName() + ":" + e.getMessage();
		onError(std::make_shared<exceptions::ProviderRuntimeException>(message));
	}
}


joynr::types::Version VehicleDataCommonRequestCaller::getProviderVersion()
{
	return joynr::types::Version(provider->MAJOR_VERSION, provider->MINOR_VERSION);
}

std::shared_ptr<IJoynrProvider> VehicleDataCommonRequestCaller::getProvider()
{
	return provider;
}


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
