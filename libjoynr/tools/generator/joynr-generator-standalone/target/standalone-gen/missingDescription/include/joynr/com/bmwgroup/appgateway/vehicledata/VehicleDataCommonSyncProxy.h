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

#ifndef GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONSYNCPROXY_H
#define GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONSYNCPROXY_H

#include "joynr/PrivateCopyAssign.h"
#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonProxyBase.h"

#include <string>

#include <memory>

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
/**
 * @brief Synchronous proxy for interface VehicleDataCommon
 *
 * @version 0.1
 */
class  VehicleDataCommonSyncProxy :
		virtual public VehicleDataCommonProxyBase,
		virtual public IVehicleDataCommonSync
{
public:
	/**
	 * @brief Parameterized constructor
	 * @param connectorFactory The connector factory
	 * @param domain The provider domain
	 * @param qosSettings The quality of service settings
	 */
	VehicleDataCommonSyncProxy(
			std::weak_ptr<joynr::JoynrRuntime> runtime,
			joynr::ConnectorFactory* connectorFactory,
			const std::string& domain,
			const joynr::MessagingQos& qosSettings
	);

	
	/**
	* @brief Synchronous getter for the vin attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	void getVin(std::string& vin)
	override;
	
	
	/**
	* @brief Synchronous getter for the actualSpeedKmh attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	void getActualSpeedKmh(double& actualSpeedKmh)
	override;
	
	
	/**
	* @brief Synchronous getter for the language attribute.
	*
	* @param result The result that will be returned to the caller.
	* @throws JoynrException if the request is not successful
	*/
	void getLanguage(std::string& language)
	override;
	
	
	/**
	* @brief Synchronous setter for the language attribute.
	*
	* @param language The value to set.
	* @throws JoynrException if the request is not successful
	*/
	void setLanguage(const std::string& language)
	override;

	friend class VehicleDataCommonProxy;

private:
	DISALLOW_COPY_AND_ASSIGN(VehicleDataCommonSyncProxy);
};

} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
#endif // GENERATED_INTERFACE_JOYNR_COM_BMWGROUP_APPGATEWAY_VEHICLEDATA_VEHICLEDATACOMMONSYNCPROXY_H
