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
import joynr.system.RoutingTypes.WebSocketProtocol;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

// NOTE: serialVersionUID is not defined since we don't support Franca versions right now.
//       The compiler will generate a serialVersionUID based on the class and its members
//       (cf. http://docs.oracle.com/javase/6/docs/platform/serialization/spec/class.html#4100),
//       which is probably more restrictive than what we want.

/**
 * Websocket address
 */
@SuppressWarnings("serial")
public class WebSocketAddress extends Address implements Serializable, JoynrType {
	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	@JsonProperty("protocol")
	private WebSocketProtocol protocol;
	@JsonProperty("host")
	private String host;
	@JsonProperty("port")
	private Integer port;
	@JsonProperty("path")
	private String path;

	/**
	 * Default Constructor
	 */
	public WebSocketAddress() {
		this.protocol = WebSocketProtocol.WS;
		this.host = "";
		this.port = 0;
		this.path = "";
	}

	/**
	 * Copy constructor
	 *
	 * @param webSocketAddressObj reference to the object to be copied
	 */
	public WebSocketAddress(WebSocketAddress webSocketAddressObj) {
		super(webSocketAddressObj);
		this.protocol = webSocketAddressObj.protocol;
		this.host = webSocketAddressObj.host;
		this.port = webSocketAddressObj.port;
		this.path = webSocketAddressObj.path;
	}

	/**
	 * Parameterized constructor
	 *
	 * @param protocol Websocket protocol type
	 * @param host Websocket host
	 * @param port Websocket port
	 * @param path Websocket path
	 */
	public WebSocketAddress(
		WebSocketProtocol protocol,
		String host,
		Integer port,
		String path
		) {
		super(
		);
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.path = path;
	}

	/**
	 * Gets Protocol
	 *
	 * @return Websocket protocol type
	 */
	@JsonIgnore
	public WebSocketProtocol getProtocol() {
		return this.protocol;
	}

	/**
	 * Sets Protocol
	 *
	 * @param protocol Websocket protocol type
	 */
	@JsonIgnore
	public void setProtocol(WebSocketProtocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * Gets Host
	 *
	 * @return Websocket host
	 */
	@JsonIgnore
	public String getHost() {
		return this.host;
	}

	/**
	 * Sets Host
	 *
	 * @param host Websocket host
	 */
	@JsonIgnore
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Gets Port
	 *
	 * @return Websocket port
	 */
	@JsonIgnore
	public Integer getPort() {
		return this.port;
	}

	/**
	 * Sets Port
	 *
	 * @param port Websocket port
	 */
	@JsonIgnore
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * Gets Path
	 *
	 * @return Websocket path
	 */
	@JsonIgnore
	public String getPath() {
		return this.path;
	}

	/**
	 * Sets Path
	 *
	 * @param path Websocket path
	 */
	@JsonIgnore
	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * Stringifies the class
	 *
	 * @return stringified class content
	 */
	@Override
	public String toString() {
		return "WebSocketAddress ["
		+ super.toString() + ", "
		+ "protocol=" + this.protocol + ", "
		+ "host=" + this.host + ", "
		+ "port=" + this.port + ", "
		+ "path=" + this.path
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
		WebSocketAddress other = (WebSocketAddress) obj;
		if (this.protocol == null) {
			if (other.protocol != null) {
				return false;
			}
		} else if (!this.protocol.equals(other.protocol)){
			return false;
		}
		if (this.host == null) {
			if (other.host != null) {
				return false;
			}
		} else if (!this.host.equals(other.host)){
			return false;
		}
		if (this.port == null) {
			if (other.port != null) {
				return false;
			}
		} else if (!this.port.equals(other.port)){
			return false;
		}
		if (this.path == null) {
			if (other.path != null) {
				return false;
			}
		} else if (!this.path.equals(other.path)){
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
		result = prime * result + ((this.protocol == null) ? 0 : this.protocol.hashCode());
		result = prime * result + ((this.host == null) ? 0 : this.host.hashCode());
		result = prime * result + ((this.port == null) ? 0 : this.port.hashCode());
		result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
		return result;
	}
}

	
