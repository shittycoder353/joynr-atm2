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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Websocket protocol type
 */
public enum WebSocketProtocol {
	/**
	 * Websocket (non-secure)
	 */
	WS,
		/**
	 * Websocket (secure)
	 */
	WSS
	;

	public static final int MAJOR_VERSION = 0;
	public static final int MINOR_VERSION = 0;
	static final Map<Integer, WebSocketProtocol> ordinalToEnumValues = new HashMap<>();

	static{
		ordinalToEnumValues.put(0, WS);
		ordinalToEnumValues.put(1, WSS);
	}

	/**
	 * Get the matching enum for an ordinal number
	 * @param ordinal The ordinal number
	 * @return The matching enum for the given ordinal number
	 */
	public static WebSocketProtocol getEnumValue(Integer ordinal) {
		return ordinalToEnumValues.get(ordinal);
	}

	/**
	 * Get the matching ordinal number for this enum
	 * @return The ordinal number representing this enum
	 */
	public Integer getOrdinal() {
		// TODO should we use a bidirectional map from a third-party library?
		Integer ordinal = null;
		for(Entry<Integer, WebSocketProtocol> entry : ordinalToEnumValues.entrySet()) {
			if(this == entry.getValue()) {
				ordinal = entry.getKey();
				break;
			}
		}
		return ordinal;
	}
}
