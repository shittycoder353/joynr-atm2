package io.joynr.generator.cpp.joynrmessaging
/*
 * !!!
 *
 * Copyright (C) 2017 BMW Car IT GmbH
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
import io.joynr.generator.cpp.util.InterfaceSubscriptionUtil
import io.joynr.generator.cpp.util.JoynrCppGeneratorExtensions
import io.joynr.generator.cpp.util.TemplateBase
import io.joynr.generator.templates.InterfaceTemplate
import io.joynr.generator.templates.util.AttributeUtil
import io.joynr.generator.templates.util.MethodUtil
import io.joynr.generator.templates.util.NamingUtil
import java.io.File
import org.franca.core.franca.FMethod

class InterfaceJoynrMessagingConnectorCppTemplate extends InterfaceTemplate{

	@Inject private extension TemplateBase
	@Inject private extension CppStdTypeUtil
	@Inject private extension JoynrCppGeneratorExtensions
	@Inject private extension NamingUtil
	@Inject private extension AttributeUtil
	@Inject private extension MethodUtil
	@Inject private extension CppInterfaceUtil
	@Inject private extension InterfaceSubscriptionUtil

	def produceParameterSetters(FMethod method)
'''
«IF !method.fireAndForget»
joynr::Request request;
«ELSE»
joynr::OneWayRequest request;
«ENDIF»

request.setMethodName("«method.joynrName»");
request.setParamDatatypes({
	«FOR param : getInputParameters(method) SEPARATOR ','»
	"«param.joynrTypeName»"
	«ENDFOR»
	});
request.setParams(
	«FOR param : getInputParameters(method) SEPARATOR ','»
		«param.name»
	«ENDFOR»
);
'''

	def getParamsPlaceholders(int numberOfParams) {
		var placeholders = ""
		for (var i = 0; i < numberOfParams; i++) {
			if(i != 0) {
				placeholders += ", "
			}
			placeholders += "{}"
		}
		return placeholders;
	}

	def logMethodCall(FMethod method)
	'''
		JOYNR_LOG_DEBUG(logger,
				"REQUEST call proxy: requestReplyId: {}, method: {}, params: «getParamsPlaceholders(method.inputParameters.size)», proxy "
				"participantId: {}, provider participantId: [{}]",
				request.getRequestReplyId(),
				request.getMethodName(),
				«FOR inputParam : method.inputParameters»
					joynr::serializer::serializeToJson(«inputParam.joynrName»),
				«ENDFOR»
				proxyParticipantId,
				providerParticipantId);
	'''

	override generate()
'''
«val interfaceName = francaIntf.joynrName»
«val methodToErrorEnumName = francaIntf.methodToErrorEnumName()»
«warning()»

#include "«getPackagePathWithJoynrPrefix(francaIntf, "/")»/«interfaceName»JoynrMessagingConnector.h"
#include "joynr/serializer/Serializer.h"
#include "joynr/ReplyCaller.h"
#include "joynr/IMessageSender.h"
#include "joynr/UnicastSubscriptionCallback.h"
#include "joynr/MulticastSubscriptionCallback.h"
#include "joynr/Util.h"
#include "joynr/SubscriptionStop.h"
#include "joynr/Future.h"
#include <cstdint>
#include "joynr/SubscriptionUtil.h"
#include "joynr/exceptions/JoynrException.h"
#include "joynr/Request.h"
#include "joynr/OneWayRequest.h"
«IF !francaIntf.attributes.empty»
	#include "joynr/SubscriptionRequest.h"
«ENDIF»
«IF !francaIntf.broadcasts.filter[selective].empty»
	#include "joynr/BroadcastSubscriptionRequest.h"
«ENDIF»
«IF !francaIntf.broadcasts.filter[!selective].empty»
	#include "joynr/MulticastSubscriptionQos.h"
	#include "joynr/MulticastSubscriptionRequest.h"
«ENDIF»

«FOR method : getMethods(francaIntf)»
	«IF method.hasErrorEnum»
		«var enumType = method.errors»
		«IF enumType != null»
			«enumType.name = methodToErrorEnumName.get(method)»
		«ELSE»
			«{enumType = method.errorEnum; ""}»
		«ENDIF»
		#include "«enumType.getPackagePathWithJoynrPrefix(File::separator, true) + File::separator + enumType.joynrName».h"
	«ENDIF»
«ENDFOR»

«FOR datatype: getAllComplexTypes(francaIntf)»
	«IF isCompound(datatype) || isMap(datatype)»
		#include «getIncludeOf(datatype)»
	«ENDIF»
«ENDFOR»

«FOR broadcastFilterParameters: getBroadcastFilterParametersIncludes(francaIntf)»
	#include «broadcastFilterParameters»
«ENDFOR»

«getNamespaceStarter(francaIntf)»
«val className = interfaceName + "JoynrMessagingConnector"»
«className»::«className»(
		std::shared_ptr<joynr::IMessageSender> messageSender,
		std::shared_ptr<joynr::ISubscriptionManager> subscriptionManager,
		const std::string& domain,
		const std::string& proxyParticipantId,
		const joynr::MessagingQos &qosSettings,
		const joynr::types::DiscoveryEntryWithMetaInfo& providerDiscoveryEntry)
	: joynr::AbstractJoynrMessagingConnector(messageSender, subscriptionManager, domain, INTERFACE_NAME(), proxyParticipantId, qosSettings, providerDiscoveryEntry)
{
}

«FOR attribute: getAttributes(francaIntf)»
	«val returnType = getTypeName(attribute)»
	«val attributeName = attribute.joynrName»
	«IF attribute.readable»
		«produceSyncGetterSignature(attribute, className)»
		{
			auto future = get«attributeName.toFirstUpper»Async();
			future->get(«attributeName»);
		}

		«produceAsyncGetterSignature(attribute, className)»
		{
			joynr::Request request;
			// explicitly set to no parameters
			request.setParams();
			request.setMethodName("get«attributeName.toFirstUpper»");
			auto future = std::make_shared<joynr::Future<«returnType»>>();

			std::function<void(const «returnType»& «attributeName»)> onSuccessWrapper = [
					future,
					onSuccess = std::move(onSuccess),
					requestReplyId = request.getRequestReplyId(),
					methodName = request.getMethodName()
			] (const «returnType»& «attributeName») {
				JOYNR_LOG_DEBUG(logger,
						"REQUEST returns successful: requestReplyId: {}, method: {}, response: {}",
						requestReplyId,
						methodName,
						joynr::serializer::serializeToJson(«attributeName»)
				);
				future->onSuccess(«attributeName»);
				if (onSuccess){
					onSuccess(«attributeName»);
				}
			};

			std::function<void(const std::shared_ptr<exceptions::JoynrException>& error)> onErrorWrapper = [
					future,
					onError = std::move(onError),
					requestReplyId = request.getRequestReplyId(),
					methodName = request.getMethodName()
			] (const std::shared_ptr<exceptions::JoynrException>& error) {
				JOYNR_LOG_DEBUG(logger,
						"REQUEST returns error: requestReplyId: {}, method: {}, response: {}",
						requestReplyId,
						methodName,
						error->what()
				);
				future->onError(error);
				if (onError){
					onError(static_cast<const exceptions::JoynrRuntimeException&>(*error));
				}
			};

			try {
				JOYNR_LOG_DEBUG(logger,
						"REQUEST call proxy: requestReplyId: {}, method: {}, proxy "
						"participantId: {}, provider participantId: [{}]",
						request.getRequestReplyId(),
						request.getMethodName(),
						proxyParticipantId,
						providerParticipantId);
				auto replyCaller = std::make_shared<joynr::ReplyCaller<«returnType»>>(std::move(onSuccessWrapper), std::move(onErrorWrapper));
				operationRequest(std::move(replyCaller), std::move(request));
			} catch (const std::invalid_argument& exception) {
				throw joynr::exceptions::MethodInvocationException(exception.what());
			}

			return future;
		}

	«ENDIF»
	«IF attribute.writable»
		«produceAsyncSetterSignature(attribute, className)»
		{
			joynr::Request request;
			request.setMethodName("set«attributeName.toFirstUpper»");
			request.setParamDatatypes({"«attribute.joynrTypeName»"});
			request.setParams(«attributeName»);

			auto future = std::make_shared<joynr::Future<void>>();

			std::function<void()> onSuccessWrapper = [
					future,
					onSuccess = std::move(onSuccess),
					requestReplyId = request.getRequestReplyId(),
					methodName = request.getMethodName()
			] () {
				JOYNR_LOG_DEBUG(logger,
						"REQUEST returns successful: requestReplyId: {}, method: {}",
						requestReplyId,
						methodName
				);
				future->onSuccess();
				if (onSuccess) {
					onSuccess();
				}
			};

			std::function<void(const std::shared_ptr<exceptions::JoynrException>& error)> onErrorWrapper = [
					future,
					onError = std::move(onError),
					requestReplyId = request.getRequestReplyId(),
					methodName = request.getMethodName()
			] (const std::shared_ptr<exceptions::JoynrException>& error) {
				JOYNR_LOG_DEBUG(logger,
					"REQUEST returns error: requestReplyId: {}, method: {}, response: {}",
					requestReplyId,
					methodName,
					error->what()
				);
				future->onError(error);
				if (onError) {
					onError(static_cast<const exceptions::JoynrRuntimeException&>(*error));
				}
			};

			try {
				JOYNR_LOG_DEBUG(logger,
						"REQUEST call proxy: requestReplyId: {}, method: {}, params: {}, proxy "
						"participantId: {}, provider participantId: [{}]",
						request.getRequestReplyId(),
						request.getMethodName(),
						joynr::serializer::serializeToJson(«attributeName»),
						proxyParticipantId,
						providerParticipantId);
				auto replyCaller = std::make_shared<joynr::ReplyCaller<void>>(std::move(onSuccessWrapper), std::move(onErrorWrapper));
				operationRequest(std::move(replyCaller), std::move(request));
			} catch (const std::invalid_argument& exception) {
				throw joynr::exceptions::MethodInvocationException(exception.what());
			}
			return future;
		}

		«produceSyncSetterSignature(attribute, className)»
		{
			auto future = set«attributeName.toFirstUpper»Async(«attributeName»);
			future->get();
		}

	«ENDIF»
	«IF attribute.notifiable»
		«produceSubscribeToAttributeSignature(attribute, className)» {
			joynr::SubscriptionRequest subscriptionRequest;
			return subscribeTo«attributeName.toFirstUpper»(subscriptionListener, subscriptionQos, subscriptionRequest);
		}

		«produceUpdateAttributeSubscriptionSignature(attribute, className)» {

			joynr::SubscriptionRequest subscriptionRequest;
			subscriptionRequest.setSubscriptionId(subscriptionId);
			return subscribeTo«attributeName.toFirstUpper»(subscriptionListener, subscriptionQos, subscriptionRequest);
		}

		std::shared_ptr<joynr::Future<std::string>> «className»::subscribeTo«attributeName.toFirstUpper»(
					std::shared_ptr<joynr::ISubscriptionListener<«returnType»> > subscriptionListener,
					std::shared_ptr<joynr::SubscriptionQos> subscriptionQos,
					SubscriptionRequest& subscriptionRequest
		) {
			JOYNR_LOG_DEBUG(logger, "Subscribing to «attributeName».");
			std::string attributeName("«attributeName»");
			joynr::MessagingQos clonedMessagingQos(qosSettings);
			clonedMessagingQos.setTtl(ISubscriptionManager::convertExpiryDateIntoTtlMs(*subscriptionQos));

			auto future = std::make_shared<Future<std::string>>();
			auto subscriptionCallback = std::make_shared<joynr::UnicastSubscriptionCallback<«returnType»>
			>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager.get());
			subscriptionManager->registerSubscription(
						attributeName,
						subscriptionCallback,
						subscriptionListener,
						subscriptionQos,
						subscriptionRequest);
			JOYNR_LOG_DEBUG(logger,
					"SUBSCRIPTION call proxy: subscriptionId: {}, attribute: {}, qos: {}, proxy "
					"participantId: {}, provider participantId: [{}]",
					subscriptionRequest.getSubscriptionId(),
					attributeName,
					joynr::serializer::serializeToJson(*subscriptionQos),
					proxyParticipantId,
					providerParticipantId);
			messageSender->sendSubscriptionRequest(
						proxyParticipantId,
						providerParticipantId,
						clonedMessagingQos,
						subscriptionRequest,
						providerDiscoveryEntry.getIsLocal()
			);
			return future;
		}

		«produceUnsubscribeFromAttributeSignature(attribute, className)» {
			joynr::SubscriptionStop subscriptionStop;
			subscriptionStop.setSubscriptionId(subscriptionId);

			subscriptionManager->unregisterSubscription(subscriptionId);
			messageSender->sendSubscriptionStop(
						proxyParticipantId,
						providerParticipantId,
						qosSettings,
						subscriptionStop
			);
		}

	«ENDIF»
«ENDFOR»

«FOR method: getMethods(francaIntf)»
	«var outputTypedConstParamList = getCommaSeperatedTypedConstOutputParameterList(method)»
	«val outputParameters = getCommaSeparatedOutputParameterTypes(method)»
	«var outputUntypedParamList = getCommaSeperatedUntypedOutputParameterList(method)»

	«IF !method.fireAndForget»
		«produceSyncMethodSignature(method, className)»
		{
			auto future = «method.joynrName»Async(«method.commaSeperatedUntypedInputParameterList»);
			future->get(«method.commaSeperatedUntypedOutputParameterList»);
		}

		«produceAsyncMethodSignature(francaIntf, method, className)»
		{
			«produceParameterSetters(method)»

			auto future = std::make_shared<joynr::Future<«outputParameters»>>();

			std::function<void(«outputTypedConstParamList»)> onSuccessWrapper = [
					future,
					onSuccess = std::move(onSuccess),
					requestReplyId = request.getRequestReplyId(),
					methodName = request.getMethodName()
			] («outputTypedConstParamList»)
			{
				JOYNR_LOG_DEBUG(logger,
					"REQUEST returns successful: requestReplyId: {}, method: {}, response: «getParamsPlaceholders(method.outputParameters.size)»",
					requestReplyId,
					methodName«IF !method.outputParameters.empty»,«ENDIF»
					«FOR outParam : method.outputParameters SEPARATOR ", "»
						joynr::serializer::serializeToJson(«outParam.joynrName»)
					«ENDFOR»
				);
				future->onSuccess(«outputUntypedParamList»);
				if (onSuccess) {
					onSuccess(«outputUntypedParamList»);
				}
			};

			std::function<void(const std::shared_ptr<exceptions::JoynrException>& error)> onErrorWrapper = [
					future,
					onRuntimeError = std::move(onRuntimeError),
					«IF method.hasErrorEnum»onApplicationError = std::move(onApplicationError),«ENDIF»
					requestReplyId = request.getRequestReplyId(),
					methodName = request.getMethodName()
			] (const std::shared_ptr<exceptions::JoynrException>& error)
			{
				JOYNR_LOG_DEBUG(logger,
					"REQUEST returns error: requestReplyId: {}, method: {}, response: {}",
					requestReplyId,
					methodName,
					error->what()
				);
				future->onError(error);
					«produceApplicationRuntimeErrorSplitForOnErrorWrapper(francaIntf, method)»
				};

			try {
				«logMethodCall(method)»

				auto replyCaller = std::make_shared<joynr::ReplyCaller<«outputParameters»>>(std::move(onSuccessWrapper), std::move(onErrorWrapper));
				operationRequest(std::move(replyCaller), std::move(request));
			} catch (const std::invalid_argument& exception) {
				throw joynr::exceptions::MethodInvocationException(exception.what());
			}
			return future;
		}
	«ELSE»
		«produceFireAndForgetMethodSignature(method, className)»
			{
				«produceParameterSetters(method)»

				operationOneWayRequest(std::move(request));
			}
	«ENDIF»
«ENDFOR»

«FOR broadcast: francaIntf.broadcasts»
	«val returnTypes = getCommaSeparatedOutputParameterTypes(broadcast)»
	«val broadcastName = broadcast.joynrName»
	«produceSubscribeToBroadcastSignature(broadcast, francaIntf, className)» {
		«IF broadcast.selective»
			joynr::BroadcastSubscriptionRequest subscriptionRequest;
			subscriptionRequest.setFilterParameters(filterParameters);
		«ELSE»
			auto subscriptionRequest = std::make_shared<joynr::MulticastSubscriptionRequest>();
		«ENDIF»
		return subscribeTo«broadcastName.toFirstUpper»Broadcast(
					std::move(subscriptionListener),
					std::move(subscriptionQos),
					subscriptionRequest«
					»«IF !broadcast.selective»«
					»,
					partitions«
					»«ENDIF»«
					»);
	}

	«produceUpdateBroadcastSubscriptionSignature(broadcast, francaIntf, className)» {
		«IF broadcast.selective»
			joynr::BroadcastSubscriptionRequest subscriptionRequest;
			subscriptionRequest.setFilterParameters(filterParameters);
			subscriptionRequest.setSubscriptionId(subscriptionId);
		«ELSE»
			auto subscriptionRequest = std::make_shared<joynr::MulticastSubscriptionRequest>();
			subscriptionRequest->setSubscriptionId(subscriptionId);
		«ENDIF»
		return subscribeTo«broadcastName.toFirstUpper»Broadcast(
					std::move(subscriptionListener),
					std::move(subscriptionQos),
					subscriptionRequest«
					»«IF !broadcast.selective»«
					»,
					partitions«
					»«ENDIF»«
					»);
	}

	std::shared_ptr<joynr::Future<std::string>> «className»::subscribeTo«broadcastName.toFirstUpper»Broadcast(
				std::shared_ptr<joynr::ISubscriptionListener<«returnTypes» > > subscriptionListener,
				«IF broadcast.selective»
					std::shared_ptr<joynr::OnChangeSubscriptionQos> subscriptionQos,
					BroadcastSubscriptionRequest& subscriptionRequest
				«ELSE»
					std::shared_ptr<joynr::MulticastSubscriptionQos> subscriptionQos,
					std::shared_ptr<MulticastSubscriptionRequest> subscriptionRequest,
					const std::vector<std::string>& partitions
				«ENDIF»
	) {
		JOYNR_LOG_TRACE(logger, "Subscribing to «broadcastName» broadcast.");
		std::string broadcastName("«broadcastName»");
		joynr::MessagingQos clonedMessagingQos(qosSettings);
		clonedMessagingQos.setTtl(ISubscriptionManager::convertExpiryDateIntoTtlMs(*subscriptionQos));

		auto future = std::make_shared<Future<std::string>>();
		«IF broadcast.selective»
			auto subscriptionCallback = std::make_shared<joynr::UnicastSubscriptionCallback<«returnTypes»>
			>(subscriptionRequest.getSubscriptionId(), future, subscriptionManager.get());
			subscriptionManager->registerSubscription(
							broadcastName,
							subscriptionCallback,
							subscriptionListener,
							subscriptionQos,
							subscriptionRequest);
			messageSender->sendBroadcastSubscriptionRequest(
						proxyParticipantId,
						providerParticipantId,
						clonedMessagingQos,
						subscriptionRequest,
						providerDiscoveryEntry.getIsLocal()
			);
		«ELSE»
			auto subscriptionCallback = std::make_shared<joynr::MulticastSubscriptionCallback<«returnTypes»>
			>(subscriptionRequest->getSubscriptionId(), future, subscriptionManager.get());
			std::function<void()> onSuccess =
					[messageSender = joynr::util::as_weak_ptr(messageSender),
					proxyParticipantId = proxyParticipantId,
					providerParticipantId = providerParticipantId,
					clonedMessagingQos, subscriptionRequest,
					isLocalMessage = providerDiscoveryEntry.getIsLocal()] () {
						if (auto ptr = messageSender.lock())
						{
							ptr->sendMulticastSubscriptionRequest(
										proxyParticipantId,
										providerParticipantId,
										clonedMessagingQos,
										*subscriptionRequest,
										isLocalMessage
							);
						}
					};

			std::string subscriptionId = subscriptionRequest«IF broadcast.selective».«ELSE»->«ENDIF»getSubscriptionId();
			std::function<void(const exceptions::ProviderRuntimeException& error)> onError =
					[subscriptionListener,
					subscriptionManager = joynr::util::as_weak_ptr(subscriptionManager),
					subscriptionId] (const exceptions::ProviderRuntimeException& error) {
						std::string message = "Could not register subscription to «broadcastName». Error from subscription manager: "
									+ error.getMessage();
						JOYNR_LOG_ERROR(logger, message);
						exceptions::SubscriptionException subscriptionException(message, subscriptionId);
						subscriptionListener->onError(subscriptionException);
						if (auto ptr = subscriptionManager.lock())
						{
							ptr->unregisterSubscription(subscriptionId);
						}
				};
			subscriptionManager->registerSubscription(
							broadcastName,
							proxyParticipantId,
							providerParticipantId,
							partitions,
							subscriptionCallback,
							subscriptionListener,
							subscriptionQos,
							*subscriptionRequest,
							std::move(onSuccess),
							std::move(onError));
		«ENDIF»
		JOYNR_LOG_DEBUG(logger,
				"SUBSCRIPTION call proxy: subscriptionId: {}, attribute: {}, qos: {}, proxy "
				"participantId: {}, provider participantId: [{}]",
				«IF broadcast.selective»
					subscriptionRequest.getSubscriptionId(),
				«ELSE»
					subscriptionRequest->getSubscriptionId(),
				«ENDIF»
				broadcastName,
				joynr::serializer::serializeToJson(*subscriptionQos),
				proxyParticipantId,
				providerParticipantId);
		return future;
	}

	«produceUnsubscribeFromBroadcastSignature(broadcast, className)» {
		joynr::SubscriptionStop subscriptionStop;
		subscriptionStop.setSubscriptionId(subscriptionId);

		subscriptionManager->unregisterSubscription(subscriptionId);
		messageSender->sendSubscriptionStop(
					proxyParticipantId,
					providerParticipantId,
					qosSettings,
					subscriptionStop
		);
	}

«ENDFOR»
«getNamespaceEnder(francaIntf)»
'''
}

