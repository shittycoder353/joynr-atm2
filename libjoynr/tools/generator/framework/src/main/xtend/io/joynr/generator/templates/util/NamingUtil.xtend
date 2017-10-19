package io.joynr.generator.templates.util
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

import javax.inject.Singleton
import org.franca.core.franca.FArgument
import org.franca.core.franca.FAttribute
import org.franca.core.franca.FBasicTypeId
import org.franca.core.franca.FBroadcast
import org.franca.core.franca.FEnumerator
import org.franca.core.franca.FField
import org.franca.core.franca.FInterface
import org.franca.core.franca.FMethod
import org.franca.core.franca.FModel
import org.franca.core.franca.FModelElement
import org.franca.core.franca.FType
import org.franca.core.franca.FTypeRef
import org.franca.core.franca.FTypedElement

@Singleton
class NamingUtil {

	def joynrName(FTypedElement element){
		element.name
	}

	def joynrName(FBasicTypeId type){
		type.getName
	}

	def joynrName(FModel model){
		model.name
	}

	def joynrName(FModelElement element){
		element.name
	}

	def joynrName(FEnumerator enumElement){
		enumElement.name.toUpperCase
	}

	def joynrName(FType type){
		type.name
	}

	def joynrName(FField member) {
		member.name
	}

	def joynrName(FInterface iFace){
		iFace.name
	}

	def joynrName(FMethod method) {
		method.name
	}

	def joynrName(FAttribute attribute) {
		attribute.name
	}

	def joynrName(FBroadcast event) {
		event.name
	}

	def joynrName(FArgument argument){
		argument.name
	}

	def joynrName(FTypeRef typeRef) {
		if (typeRef.derived != null) {
			typeRef.derived.joynrName
		}
		else {
			typeRef.predefined.joynrName
		}
	}
	def joynrName(Object type) {
		if (type instanceof FType){
			type.joynrName
		}
		else if (type instanceof FBasicTypeId){
			type.joynrName
		}
		else{
			return null;
			// throw new IllegalStateException("The typename cannot be resolved" + (if (type == null) ", because the invoked parameter is null " else (" for type " + type)))
		}
	}
}