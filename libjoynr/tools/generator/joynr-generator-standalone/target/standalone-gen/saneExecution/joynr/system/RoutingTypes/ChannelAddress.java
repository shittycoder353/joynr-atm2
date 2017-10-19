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
 * Channel Address
 */
@SuppressWarnings("serial")
public class ChannelAddress extends Address implements Serializable, JoynrType {
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	@JsonProperty("messagingEndpointUrl")
	private String messagingEndpointUrl;
	@JsonProperty("channelId")
	private String channelId;

	/**
	 * Default Constructor
	 */
	public ChannelAddress() {
		this.messagingEndpointUrl = "";
		this.channelId = "";
	}

	/**
	 * Copy constructor
	 *
	 * @param channelAddressObj reference to the object to be copied
	 */
	public ChannelAddress(ChannelAddress channelAddressObj) {
		super(channelAddressObj);
		this.messagingEndpointUrl = channelAddressObj.messagingEndpointUrl;
		this.channelId = channelAddressObj.channelId;
	}

	/**
	 * Parameterized constructor
	 *
	 * @param messagingEndpointUrl Url of HTTP-based message receiver
	 * @param channelId Channel identification
	 */
	public ChannelAddress(
		String messagingEndpointUrl,
		String channelId
		) {
		super(
		);
		this.messagingEndpointUrl = messagingEndpointUrl;
		this.channelId = channelId;
	}

	/**
	 * Gets MessagingEndpointUrl
	 *
	 * @return Url of HTTP-based message receiver
	 */
	@JsonIgnore
	public String getMessagingEndpointUrl() {
		return this.messagingEndpointUrl;
	}

	/**
	 * Sets MessagingEndpointUrl
	 *
	 * @param messagingEndpointUrl Url of HTTP-based message receiver
	 */
	@JsonIgnore
	public void setMessagingEndpointUrl(String messagingEndpointUrl) {
		this.messagingEndpointUrl = messagingEndpointUrl;
	}

	/**
	 * Gets ChannelId
	 *
	 * @return Channel identification
	 */
	@JsonIgnore
	public String getChannelId() {
		return this.channelId;
	}

	/**
	 * Sets ChannelId
	 *
	 * @param channelId Channel identification
	 */
	@JsonIgnore
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


	/**
	 * Stringifies the class
	 *
	 * @return stringified class content
	 */
	@Override
	public String toString() {
		return "ChannelAddress ["
		+ super.toString() + ", "
		+ "messagingEndpointUrl=" + this.messagingEndpointUrl + ", "
		+ "channelId=" + this.channelId
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
		ChannelAddress other = (ChannelAddress) obj;
		if (this.messagingEndpointUrl == null) {
			if (other.messagingEndpointUrl != null) {
				return false;
			}
		} else if (!this.messagingEndpointUrl.equals(other.messagingEndpointUrl)){
			return false;
		}
		if (this.channelId == null) {
			if (other.channelId != null) {
				return false;
			}
		} else if (!this.channelId.equals(other.channelId)){
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
		result = prime * result + ((this.messagingEndpointUrl == null) ? 0 : this.messagingEndpointUrl.hashCode());
		result = prime * result + ((this.channelId == null) ? 0 : this.channelId.hashCode());
		return result;
	}
}

	
