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
import io.joynr.generator.cpp.util.JoynrCppGeneratorExtensions
import io.joynr.generator.templates.util.NamingUtil
import java.io.File
import org.eclipse.xtext.generator.IFileSystemAccess
import org.franca.core.franca.FModel
import io.joynr.generator.cpp.util.CppTemplateFactory

class ProviderGenerator {

	@Inject private extension JoynrCppGeneratorExtensions
	@Inject private extension NamingUtil
	@Inject CppTemplateFactory templateFactory;

	def doGenerate(
		FModel model,
		IFileSystemAccess sourceFileSystem,
		IFileSystemAccess headerFileSystem,
		String sourceContainerPath,
		String headerContainerPath
	){

		for(serviceInterface: model.interfaces){
			val sourcePath = sourceContainerPath + getPackageSourceDirectory(serviceInterface) + File::separator;
			val headerPath = headerContainerPath + getPackagePathWithJoynrPrefix(serviceInterface, File::separator) + File::separator;
			var serviceName = serviceInterface.joynrName

			var interfaceRequestInterpreterHTemplate = templateFactory.createInterfaceRequestInterpreterHTemplate(serviceInterface)
			generateFile(
				headerFileSystem,
				headerPath + serviceName + "RequestInterpreter.h",
				interfaceRequestInterpreterHTemplate
			);

			var interfaceRequestInterpreterCppTemplate = templateFactory.createInterfaceRequestInterpreterCppTemplate(serviceInterface)
			generateFile(
				sourceFileSystem,
				sourcePath + serviceName + "RequestInterpreter.cpp",
				interfaceRequestInterpreterCppTemplate
			);

			var interfaceRequestCallerHTemplate = templateFactory.createInterfaceRequestCallerHTemplate(serviceInterface)
			generateFile(
				headerFileSystem,
				headerPath + serviceName + "RequestCaller.h",
				interfaceRequestCallerHTemplate
			);

			var interfaceRequestCallerCppTemplate = templateFactory.createInterfaceRequestCallerCppTemplate(serviceInterface)
			generateFile(
				sourceFileSystem,
				sourcePath + serviceName + "RequestCaller.cpp",
				interfaceRequestCallerCppTemplate
			);

			var interfaceProviderCppTemplate = templateFactory.createInterfaceProviderCppTemplate(serviceInterface)
			generateFile(
				sourceFileSystem,
				sourcePath + serviceName + "Provider.cpp",
				interfaceProviderCppTemplate
			);

			var interfaceProviderHTemplate = templateFactory.createInterfaceProviderHTemplate(serviceInterface)
			generateFile(
				headerFileSystem,
				headerPath + serviceName + "Provider.h",
				interfaceProviderHTemplate
			);

			var interfaceAbstractProviderCppTemplate = templateFactory.createInterfaceAbstractProviderCppTemplate(serviceInterface)
			generateFile(
				sourceFileSystem,
				sourcePath + serviceName + "AbstractProvider.cpp",
				interfaceAbstractProviderCppTemplate
			);

			var interfaceAbstractProviderHTemplate = templateFactory.createInterfaceAbstractProviderHTemplate(serviceInterface)
			generateFile(
				headerFileSystem,
				headerPath + serviceName + "AbstractProvider.h",
				interfaceAbstractProviderHTemplate
			);
		}
	}
}
