package io.joynr.generator.cpp.defaultProvider
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
import io.joynr.generator.templates.util.AttributeUtil
import io.joynr.generator.templates.util.InterfaceUtil
import io.joynr.generator.templates.util.MethodUtil
import io.joynr.generator.templates.util.NamingUtil
import org.franca.core.franca.FBasicTypeId

class DefaultInterfaceProviderCppTemplate extends InterfaceTemplate{

	@Inject private extension TemplateBase
	@Inject private extension CppStdTypeUtil
	@Inject private extension NamingUtil
	@Inject private extension MethodUtil
	@Inject private extension InterfaceUtil
	@Inject private extension AttributeUtil

	@Inject
	private extension JoynrCppGeneratorExtensions

	override generate()
'''
«val interfaceName = francaIntf.joynrName»
«warning()»
#include "«getPackagePathWithJoynrPrefix(francaIntf, "/")»/Default«interfaceName»Provider.h"

#include <chrono>
#include <cstdint>
#include <tuple>


«getNamespaceStarter(francaIntf)»

INIT_LOGGER(Default«interfaceName»Provider);

Default«interfaceName»Provider::Default«interfaceName»Provider() :
		«interfaceName»AbstractProvider()
		«IF !francaIntf.attributes.empty»,«ENDIF»
		«FOR attribute : francaIntf.attributes SEPARATOR ","»
			«attribute.joynrName»()
		«ENDFOR»
{
	// default uses a priority that is the current time,
	// causing arbitration to the last started instance if highest priority arbitrator is used
	std::chrono::milliseconds millisSinceEpoch = std::chrono::duration_cast<std::chrono::milliseconds>(
			std::chrono::system_clock::now().time_since_epoch()
	);
}

Default«interfaceName»Provider::~Default«interfaceName»Provider()
{
}

«IF !francaIntf.attributes.empty»
	// attributes
«ENDIF»
«FOR attribute : francaIntf.attributes»
	«var attributeName = attribute.joynrName»
	«IF attribute.readable»
		void Default«interfaceName»Provider::get«attributeName.toFirstUpper»(
				std::function<void(
						const «attribute.typeName»&
				)> onSuccess,
				std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
		) {
			std::ignore = onError;
			onSuccess(«attributeName»);
		}

	«ENDIF»
	«IF attribute.writable»
		void Default«interfaceName»Provider::set«attributeName.toFirstUpper»(
				const «attribute.typeName»& «attributeName»,
				std::function<void()> onSuccess,
				std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
		) {
			std::ignore = onError;
			this->«attributeName» = «attributeName»;
			«IF attribute.notifiable»
				«attributeName»Changed(«attributeName»);
			«ENDIF»
			onSuccess();
		}

	«ENDIF»
«ENDFOR»
«val methodToErrorEnumName = francaIntf.methodToErrorEnumName»
«IF !francaIntf.methods.empty»
	// methods
«ENDIF»
«FOR method : francaIntf.methods»
	«val outputTypedParamList = method.commaSeperatedTypedConstOutputParameterList»
	«val outputUntypedParamList = getCommaSeperatedUntypedOutputParameterList(method)»
	«val inputTypedParamList = getCommaSeperatedTypedConstInputParameterList(method)»
	«val methodName = method.joynrName»
	void Default«interfaceName»Provider::«method.joynrName»(
			«IF !method.inputParameters.empty»
				«inputTypedParamList»«IF !method.fireAndForget»,«ENDIF»
			«ENDIF»
			«IF !method.fireAndForget»
				«IF method.outputParameters.empty»
					std::function<void()> onSuccess,
				«ELSE»
					std::function<void(
							«outputTypedParamList»
					)> onSuccess,
				«ENDIF»
				«IF method.hasErrorEnum»
					«IF method.errors != null»
						«val packagePath = getPackagePathWithJoynrPrefix(method.errors, "::")»
						std::function<void (const «packagePath»::«methodToErrorEnumName.get(method)»::«nestedEnumName»& errorEnum)> onError
					«ELSE»
						std::function<void (const «method.errorEnum.typeName»& errorEnum)> onError
					«ENDIF»
				«ELSE»
					std::function<void (const joynr::exceptions::ProviderRuntimeException&)> onError
				«ENDIF»
			«ENDIF»
	) {
		«IF !method.fireAndForget»
			std::ignore = onError;
		«ENDIF»
		«FOR inputParameter: getInputParameters(method)»
			std::ignore = «inputParameter.joynrName»;
		«ENDFOR»
		«FOR argument : method.outputParameters»
			«val outputParamType = argument.typeName»
			«val argumentType = argument.type.resolveTypeDef»
			«IF !argument.isArray && argumentType.isPrimitive»
				«val type = argumentType.getPrimitive»
				«IF type==FBasicTypeId.STRING»
					«outputParamType» «argument.joynrName» = "Hello World";
				«ELSEIF type==FBasicTypeId.BOOLEAN»
					«outputParamType» «argument.joynrName» = false;
				«ELSEIF type==FBasicTypeId.INT8   ||
						type==FBasicTypeId.UINT8  ||
						type==FBasicTypeId.INT16  ||
						type==FBasicTypeId.UINT16 ||
						type==FBasicTypeId.INT32  ||
						type==FBasicTypeId.UINT32 ||
						type==FBasicTypeId.INT64  ||
						type==FBasicTypeId.UINT64»
					«outputParamType» «argument.joynrName» = 42;
				«ELSEIF type==FBasicTypeId.DOUBLE   ||
						type==FBasicTypeId.FLOAT»
					«outputParamType» «argument.joynrName» = 3.1415;
				«ELSE»
					«outputParamType» «argument.joynrName»;
				«ENDIF»
			«ELSE»
				«outputParamType» «argument.joynrName»;
			«ENDIF»
		«ENDFOR»
		JOYNR_LOG_WARN(logger, "**********************************************");
		JOYNR_LOG_WARN(logger, "* Default«interfaceName»Provider::«methodName» called");
		JOYNR_LOG_WARN(logger, "**********************************************");
		«IF !method.fireAndForget»
			onSuccess(
					«outputUntypedParamList»
			);
		«ENDIF»
	}

«ENDFOR»
«getNamespaceEnder(francaIntf)»
'''

	/**
	 * add to line 73
	 *
	 *	«ELSEIF isArray(getOutputParameter(method))»
	 *	result = QList<«getMappedDatatype(getOutputParameter(method))»>();
	 *
	 */
}
