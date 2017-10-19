package io.joynr.generator.cpp.provider
/*
 * !!!
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

import com.google.inject.Inject
import io.joynr.generator.cpp.util.CppStdTypeUtil
import io.joynr.generator.cpp.util.JoynrCppGeneratorExtensions
import io.joynr.generator.cpp.util.TemplateBase
import io.joynr.generator.templates.InterfaceTemplate
import io.joynr.generator.templates.util.InterfaceUtil.TypeSelector
import io.joynr.generator.templates.util.NamingUtil

class InterfaceProviderCppTemplate extends InterfaceTemplate {

	@Inject private extension TemplateBase
	@Inject private extension CppStdTypeUtil
	@Inject private extension JoynrCppGeneratorExtensions
	@Inject private extension NamingUtil

	override generate() {
		var selector = TypeSelector::defaultTypeSelector
		selector.transitiveTypes(true)
'''
«warning()»
«val interfaceName = francaIntf.joynrName»
#include "«getPackagePathWithJoynrPrefix(francaIntf, "/")»/«interfaceName»Provider.h"
#include "joynr/InterfaceRegistrar.h"

#include "«getPackagePathWithJoynrPrefix(francaIntf, "/")»/«interfaceName»RequestInterpreter.h"
«FOR parameterType: getDataTypeIncludesFor(francaIntf)»
	#include «parameterType»
«ENDFOR»

«getNamespaceStarter(francaIntf)»
«interfaceName»Provider::«interfaceName»Provider()
{
	// Register a request interpreter to interpret requests to this interface
	joynr::InterfaceRegistrar::instance().registerRequestInterpreter<«interfaceName»RequestInterpreter>(INTERFACE_NAME());
}

«interfaceName»Provider::~«interfaceName»Provider()
{
	// Unregister the request interpreter
	joynr::InterfaceRegistrar::instance().unregisterRequestInterpreter(
			INTERFACE_NAME()
	);
}

const std::string& «interfaceName»Provider::INTERFACE_NAME()
{
	static const std::string INTERFACE_NAME("«getPackagePathWithoutJoynrPrefix(francaIntf, "/")»/«interfaceName»");
	return INTERFACE_NAME;
}

const std::uint32_t «interfaceName»Provider::MAJOR_VERSION = «majorVersion»;
const std::uint32_t «interfaceName»Provider::MINOR_VERSION = «minorVersion»;

«getNamespaceEnder(francaIntf)»
'''
}
}
