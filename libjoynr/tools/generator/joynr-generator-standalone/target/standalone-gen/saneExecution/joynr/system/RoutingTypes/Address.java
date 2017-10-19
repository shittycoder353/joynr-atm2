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

package joynr.system.RoutingTypes;
import java.io.Serializable;

import io.joynr.subtypes.JoynrType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

// NOTE: serialVersionUID is not defined since we don't support Franca versions right now.
//       The compiler will generate a serialVersionUID based on the class and its members
//       (cf. http://docs.oracle.com/javase/6/docs/platform/serialization/spec/class.html#4100),
//       which is probably more restrictive than what we want.

/**
 * Generic Address
 */
@SuppressWarnings("serial")
public class Address implements Serializable, JoynrType {
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;

	/**
	 * Default Constructor
	 */
	public Address() {
	}

	/**
	 * Copy constructor
	 *
	 * @param addressObj reference to the object to be copied
	 */
	public Address(Address addressObj) {
	}



	/**
	 * Stringifies the class
	 *
	 * @return stringified class content
	 */
	@Override
	public String toString() {
		return "Address ["
		+ "]";
	}

	/**
	 * Check for equality
	 *
	 * @param obj Reference to the object to compare to
	 * @return true, if objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	/**
	 * Calculate code for hashing based on member contents
	 *
	 * @return The calculated hash code
	 */
	@Override
	public int hashCode() {
		int result = 1;
		return result;
	}
}

	
