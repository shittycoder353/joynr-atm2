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
#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONABSTRACTPROVIDER_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONABSTRACTPROVIDER_H

#include <string>
#include <vector>
#include <memory>

#include "joynr/PrivateCopyAssign.h"
#include "joynr/AbstractJoynrProvider.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonProvider.h"

#include <string>


namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 


/** @brief Abstract provider class for interface VehicleDataCommon */
class  VehicleDataCommonAbstractProvider :
		public joynr::com::bmwgroup::appgateway::vehicledata::VehicleDataCommonProvider,
		public joynr::AbstractJoynrProvider
{

public:
	/** @brief Default constructor */
	VehicleDataCommonAbstractProvider();

	/** @brief Destructor */
	~VehicleDataCommonAbstractProvider() override;

	/**
	 * @brief Get the interface name
	 * @return The name of the interface
	 */
	const std::string& getInterfaceName() const override;


protected:

	// attributes
	/**
	 * @brief vinChanged must be called by a concrete provider to signal attribute
	 * modifications. It is used to implement onchange subscriptions.
	 * @param vin the new attribute value
	 */
	void vinChanged(
			const std::string& vin
	) override;
	/**
	 * @brief actualSpeedKmhChanged must be called by a concrete provider to signal attribute
	 * modifications. It is used to implement onchange subscriptions.
	 * @param actualSpeedKmh the new attribute value
	 */
	void actualSpeedKmhChanged(
			const double& actualSpeedKmh
	) override;
	/**
	 * @brief languageChanged must be called by a concrete provider to signal attribute
	 * modifications. It is used to implement onchange subscriptions.
	 * @param language the new attribute value
	 */
	void languageChanged(
			const std::string& language
	) override;

private:
	DISALLOW_COPY_AND_ASSIGN(VehicleDataCommonAbstractProvider);

};

} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr

#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONABSTRACTPROVIDER_H
