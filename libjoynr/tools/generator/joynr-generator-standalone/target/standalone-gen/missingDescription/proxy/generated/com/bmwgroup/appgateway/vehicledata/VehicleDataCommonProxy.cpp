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

#include "joynr/com/bmwgroup/appgateway/vehicledata/VehicleDataCommonProxy.h"

namespace joynr { namespace com { namespace bmwgroup { namespace appgateway { namespace vehicledata { 
VehicleDataCommonProxy::VehicleDataCommonProxy(
		std::weak_ptr<joynr::JoynrRuntime> runtime,
		joynr::ConnectorFactory* connectorFactory,
		const std::string &domain,
		const joynr::MessagingQos &qosSettings
) :
		joynr::ProxyBase(runtime, connectorFactory, domain, qosSettings),
		VehicleDataCommonProxyBase(runtime, connectorFactory, domain, qosSettings),
		VehicleDataCommonSyncProxy(runtime, connectorFactory, domain, qosSettings),
		VehicleDataCommonAsyncProxy(runtime, connectorFactory, domain, qosSettings)
{
}


} // namespace vehicledata
} // namespace appgateway
} // namespace bmwgroup
} // namespace com
} // namespace joynr
