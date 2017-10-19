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

import joynr.system.RoutingTypes.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

// NOTE: serialVersionUID is not defined since we don't support Franca versions right now.
//       The compiler will generate a serialVersionUID based on the class and its members
//       (cf. http://docs.oracle.com/javase/6/docs/platform/serialization/spec/class.html#4100),
//       which is probably more restrictive than what we want.

/**
 * CommonApiDbus address
 */
@SuppressWarnings("serial")
public class CommonApiDbusAddress extends Address implements Serializable, JoynrType {
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	@JsonProperty("domain")
	private String domain;
	@JsonProperty("serviceName")
	private String serviceName;
	@JsonProperty("participantId")
	private String participantId;

	/**
	 * Default Constructor
	 */
	public CommonApiDbusAddress() {
		this.domain = "";
		this.serviceName = "";
		this.participantId = "";
	}

	/**
	 * Copy constructor
	 *
	 * @param commonApiDbusAddressObj reference to the object to be copied
	 */
	public CommonApiDbusAddress(CommonApiDbusAddress commonApiDbusAddressObj) {
		super(commonApiDbusAddressObj);
		this.domain = commonApiDbusAddressObj.domain;
		this.serviceName = commonApiDbusAddressObj.serviceName;
		this.participantId = commonApiDbusAddressObj.participantId;
	}

	/**
	 * Parameterized constructor
	 *
	 * @param domain Domain name
	 * @param serviceName Name of service
	 * @param participantId the ID of the target participant
	 */
	public CommonApiDbusAddress(
		String domain,
		String serviceName,
		String participantId
		) {
		super(
		);
		this.domain = domain;
		this.serviceName = serviceName;
		this.participantId = participantId;
	}

	/**
	 * Gets Domain
	 *
	 * @return Domain name
	 */
	@JsonIgnore
	public String getDomain() {
		return this.domain;
	}

	/**
	 * Sets Domain
	 *
	 * @param domain Domain name
	 */
	@JsonIgnore
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets ServiceName
	 *
	 * @return Name of service
	 */
	@JsonIgnore
	public String getServiceName() {
		return this.serviceName;
	}

	/**
	 * Sets ServiceName
	 *
	 * @param serviceName Name of service
	 */
	@JsonIgnore
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets ParticipantId
	 *
	 * @return the ID of the target participant
	 */
	@JsonIgnore
	public String getParticipantId() {
		return this.participantId;
	}

	/**
	 * Sets ParticipantId
	 *
	 * @param participantId the ID of the target participant
	 */
	@JsonIgnore
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}


	/**
	 * Stringifies the class
	 *
	 * @return stringified class content
	 */
	@Override
	public String toString() {
		return "CommonApiDbusAddress ["
		+ super.toString() + ", "
		+ "domain=" + this.domain + ", "
		+ "serviceName=" + this.serviceName + ", "
		+ "participantId=" + this.participantId
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
		if (!super.equals(obj))
			return false;
		CommonApiDbusAddress other = (CommonApiDbusAddress) obj;
		if (this.domain == null) {
			if (other.domain != null) {
				return false;
			}
		} else if (!this.domain.equals(other.domain)){
			return false;
		}
		if (this.serviceName == null) {
			if (other.serviceName != null) {
				return false;
			}
		} else if (!this.serviceName.equals(other.serviceName)){
			return false;
		}
		if (this.participantId == null) {
			if (other.participantId != null) {
				return false;
			}
		} else if (!this.participantId.equals(other.participantId)){
			return false;
		}
		return true;
	}

	/**
	 * Calculate code for hashing based on member contents
	 *
	 * @return The calculated hash code
	 */
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((this.domain == null) ? 0 : this.domain.hashCode());
		result = prime * result + ((this.serviceName == null) ? 0 : this.serviceName.hashCode());
		result = prime * result + ((this.participantId == null) ? 0 : this.participantId.hashCode());
		return result;
	}
}

	
