package io.joynr.generator.cpp.communicationmodel
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
import io.joynr.generator.cpp.util.CppInterfaceUtil
import io.joynr.generator.cpp.util.CppStdTypeUtil
import io.joynr.generator.cpp.util.JoynrCppGeneratorExtensions
import io.joynr.generator.cpp.util.TemplateBase
import io.joynr.generator.templates.InterfaceTemplate
import io.joynr.generator.templates.util.AttributeUtil
import io.joynr.generator.templates.util.FMapTypeAsLastComparator
import io.joynr.generator.templates.util.InterfaceUtil.TypeSelector
import io.joynr.generator.templates.util.NamingUtil

class InterfaceHTemplate extends InterfaceTemplate{

	@Inject private extension TemplateBase

	@Inject private extension CppInterfaceUtil
	@Inject private extension AttributeUtil
	@Inject private extension NamingUtil
	@Inject private extension CppStdTypeUtil

	@Inject private extension JoynrCppGeneratorExtensions

	override generate() {
		var selector = TypeSelector::defaultTypeSelector
		selector.errorTypes(true)
		selector.typeDefs(true)
'''
«val interfaceName = francaIntf.joynrName»
«val headerGuard = ("GENERATED_INTERFACE_"+getPackagePathWithJoynrPrefix(francaIntf, "_")+"_I"+interfaceName+"_h").toUpperCase»
«warning()»

#ifndef «headerGuard»
#define «headerGuard»

«FOR datatype: IterableExtensions.sortWith(getAllComplexTypes(francaIntf, selector),new FMapTypeAsLastComparator())»
	«IF isCompound(datatype) || isMap(datatype)»
		«datatype.forwardDeclaration»
	«ELSE »
		#include «datatype.includeOf»
	«ENDIF»
«ENDFOR»

«FOR include: francaIntf.allPrimitiveTypes.includesFor.addElements(includeForArray, includeForString)»
	#include «include»
«ENDFOR»

«getDllExportIncludeStatement()»

#include <memory>
#include <functional>

namespace joynr
{
	template <class ... Ts> class Future;

namespace exceptions
{
	class JoynrException;
	class JoynrRuntimeException;
} // namespace exceptions

} // namespace joynr

«getNamespaceStarter(francaIntf)»

/**
 * @brief Base interface.
 *
 * @version «majorVersion».«minorVersion»
 */
class «getDllExportMacro()» I«interfaceName»Base {
public:
	I«interfaceName»Base() = default;
	virtual ~I«interfaceName»Base() = default;

	static const std::string& INTERFACE_NAME();
	/**
	 * @brief MAJOR_VERSION The major version of this provider interface as specified in the
	 * Franca model.
	 */
	static const std::uint32_t MAJOR_VERSION;
	/**
	 * @brief MINOR_VERSION The minor version of this provider interface as specified in the
	 * Franca model.
	 */
	static const std::uint32_t MINOR_VERSION;
};

«IF hasFireAndForgetMethods(francaIntf)»
/**
 * @brief This is the «interfaceName» fireAndForget interface.
 *
 * @version «majorVersion».«minorVersion»
 */
class «getDllExportMacro()» I«interfaceName»FireAndForget : virtual public I«interfaceName»Base {
public:
	~I«interfaceName»FireAndForget() override = default;
	«produceFireAndForgetMethodDeclarations(francaIntf,true)»
};
«ENDIF»

/**
 * @brief This is the «interfaceName» synchronous interface.
 *
 * @version «majorVersion».«minorVersion»
 */
class «getDllExportMacro()» I«interfaceName»Sync :
		virtual public I«interfaceName»Base«IF hasFireAndForgetMethods(francaIntf)»,
		virtual public I«interfaceName»FireAndForget«ENDIF»
{
public:
	~I«interfaceName»Sync() override = default;
	«produceSyncGetterDeclarations(francaIntf,true)»
	«produceSyncSetterDeclarations(francaIntf,true)»
	«produceSyncMethodDeclarations(francaIntf,true)»
};

/**
 * @brief This is the «interfaceName» asynchronous interface.
 *
 * @version «majorVersion».«minorVersion»
 */
class «getDllExportMacro()» I«interfaceName»Async :
		virtual public I«interfaceName»Base«IF hasFireAndForgetMethods(francaIntf)»,
		virtual public I«interfaceName»FireAndForget«ENDIF»
{
public:
	~I«interfaceName»Async() override = default;
	«produceAsyncGetterDeclarations(francaIntf,true)»
	«produceAsyncSetterDeclarations(francaIntf,true)»
	«produceAsyncMethodDeclarations(francaIntf,true, true)»
};

/**
 * @brief This is the «interfaceName» interface.
 *
 * @version «majorVersion».«minorVersion»
 */
class «getDllExportMacro()» I«interfaceName» : virtual public I«interfaceName»Sync, virtual public I«interfaceName»Async {
public:
	~I«interfaceName»() override = default;
	«FOR attribute: getAttributes(francaIntf)»
		«val attributeName = attribute.name.toFirstUpper»
		«IF attribute.readable»
			using I«interfaceName»Sync::get«attributeName»;
			using I«interfaceName»Async::get«attributeName»Async;
		«ENDIF»
		«IF attribute.writable»
			using I«interfaceName»Sync::set«attributeName»;
			using I«interfaceName»Async::set«attributeName»Async;
		«ENDIF»
	«ENDFOR»
	«FOR method: getUniqueMethodNames(getMethods(francaIntf).filter[!fireAndForget])»
		using I«interfaceName»Sync::«method»;
		using I«interfaceName»Async::«method»Async;
	«ENDFOR»
	«FOR method: getUniqueMethodNames(getMethods(francaIntf).filter[fireAndForget])»
		using I«interfaceName»FireAndForget::«method»;
	«ENDFOR»
};

«getNamespaceEnder(francaIntf)»
#endif // «headerGuard»
'''
}
}
