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
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonAbstractProvider.h"
#include "joynr/InterfaceRegistrar.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonRequestInterpreter.h"

#include <string>


namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
VehicleDataCommonAbstractProvider::VehicleDataCommonAbstractProvider()
{
	// Register a request interpreter to interpret requests to this interface
	joynr::InterfaceRegistrar::instance().registerRequestInterpreter<VehicleDataCommonRequestInterpreter>(getInterfaceName());
}

VehicleDataCommonAbstractProvider::~VehicleDataCommonAbstractProvider()
{
	// Unregister the request interpreter
	joynr::InterfaceRegistrar::instance().unregisterRequestInterpreter(getInterfaceName());
}

const std::string& VehicleDataCommonAbstractProvider::getInterfaceName() const {
	return IVehicleDataCommonBase::INTERFACE_NAME();
}

void VehicleDataCommonAbstractProvider::vinChanged(
		const std::string& vin
) {
	onAttributeValueChanged("vin",vin);
}
void VehicleDataCommonAbstractProvider::actualSpeedKmhChanged(
		const double& actualSpeedKmh
) {
	onAttributeValueChanged("actualSpeedKmh",actualSpeedKmh);
}
void VehicleDataCommonAbstractProvider::languageChanged(
		const std::string& language
) {
	onAttributeValueChanged("language",language);
}


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
