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
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonProvider.h"
#include "joynr/InterfaceRegistrar.h"

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonRequestInterpreter.h"
#include <string>

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
VehicleDataCommonProvider::VehicleDataCommonProvider()
{
	// Register a request interpreter to interpret requests to this interface
	joynr::InterfaceRegistrar::instance().registerRequestInterpreter<VehicleDataCommonRequestInterpreter>(INTERFACE_NAME());
}

VehicleDataCommonProvider::~VehicleDataCommonProvider()
{
	// Unregister the request interpreter
	joynr::InterfaceRegistrar::instance().unregisterRequestInterpreter(
			INTERFACE_NAME()
	);
}

const std::string& VehicleDataCommonProvider::INTERFACE_NAME()
{
	static const std::string INTERFACE_NAME("com/bmwgroup/appgateway/vehicledata/VehicleDataCommon");
	return INTERFACE_NAME;
}

const std::uint32_t VehicleDataCommonProvider::MAJOR_VERSION = 0;
const std::uint32_t VehicleDataCommonProvider::MINOR_VERSION = 1;


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
