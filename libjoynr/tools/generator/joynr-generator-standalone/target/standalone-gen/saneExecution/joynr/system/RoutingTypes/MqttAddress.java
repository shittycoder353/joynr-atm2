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
 * Mqtt Address
 */
@SuppressWarnings("serial")
public class MqttAddress extends Address implements Serializable, JoynrType {
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	@JsonProperty("brokerUri")
	private String brokerUri;
	@JsonProperty("topic")
	private String topic;

	/**
	 * Default Constructor
	 */
	public MqttAddress() {
		this.brokerUri = "";
		this.topic = "";
	}

	/**
	 * Copy constructor
	 *
	 * @param mqttAddressObj reference to the object to be copied
	 */
	public MqttAddress(MqttAddress mqttAddressObj) {
		super(mqttAddressObj);
		this.brokerUri = mqttAddressObj.brokerUri;
		this.topic = mqttAddressObj.topic;
	}

	/**
	 * Parameterized constructor
	 *
	 * @param brokerUri Mqtt broker uri
	 * @param topic MQTT topic identification
	 */
	public MqttAddress(
		String brokerUri,
		String topic
		) {
		super(
		);
		this.brokerUri = brokerUri;
		this.topic = topic;
	}

	/**
	 * Gets BrokerUri
	 *
	 * @return Mqtt broker uri
	 */
	@JsonIgnore
	public String getBrokerUri() {
		return this.brokerUri;
	}

	/**
	 * Sets BrokerUri
	 *
	 * @param brokerUri Mqtt broker uri
	 */
	@JsonIgnore
	public void setBrokerUri(String brokerUri) {
		this.brokerUri = brokerUri;
	}

	/**
	 * Gets Topic
	 *
	 * @return MQTT topic identification
	 */
	@JsonIgnore
	public String getTopic() {
		return this.topic;
	}

	/**
	 * Sets Topic
	 *
	 * @param topic MQTT topic identification
	 */
	@JsonIgnore
	public void setTopic(String topic) {
		this.topic = topic;
	}


	/**
	 * Stringifies the class
	 *
	 * @return stringified class content
	 */
	@Override
	public String toString() {
		return "MqttAddress ["
		+ super.toString() + ", "
		+ "brokerUri=" + this.brokerUri + ", "
		+ "topic=" + this.topic
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
		MqttAddress other = (MqttAddress) obj;
		if (this.brokerUri == null) {
			if (other.brokerUri != null) {
				return false;
			}
		} else if (!this.brokerUri.equals(other.brokerUri)){
			return false;
		}
		if (this.topic == null) {
			if (other.topic != null) {
				return false;
			}
		} else if (!this.topic.equals(other.topic)){
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
		result = prime * result + ((this.brokerUri == null) ? 0 : this.brokerUri.hashCode());
		result = prime * result + ((this.topic == null) ? 0 : this.topic.hashCode());
		return result;
	}
}

	
