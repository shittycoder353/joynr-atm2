package joynr.system.RoutingTypes;

/*
 * #%L
 * %%
 * Copyright (C) 2011 - 2017 BMW Car IT GmbH
 * %%
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
 * #L%
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

public class RoutingTypesUtilTest {
    String mqttAddressString = "{\"_typeName\":\"joynr.system.RoutingTypes.MqttAddress\",\"brokerUri\":\"tcp://host:1234\",\"topic\":\"topic\"}";

    static final String BROKERURI = "tcp://host:1234";
    private static final String MESSAGINGENDPOINTURL = "http://server:8080/bounceproxy/";
    static final String TOPIC = "topic";
    static final String CHANNELID = UUID.randomUUID().toString();

    @Test
    public void toMqttAddressStringTest() {
        MqttAddress mqttAddress = new MqttAddress(BROKERURI, TOPIC);
        String addressString = RoutingTypesUtil.toAddressString(mqttAddress);
        assertEquals(mqttAddressString, addressString);
    }

    @Test
    public void fromMqttAddressString() {
        Address address = RoutingTypesUtil.fromAddressString(mqttAddressString);
        assertTrue(address instanceof MqttAddress);
        MqttAddress mqttAddress = (MqttAddress) address;
        assertEquals(BROKERURI, mqttAddress.getBrokerUri());
        assertEquals(TOPIC, mqttAddress.getTopic());
    }

    @Test
    public void fromChannelAddressString() {
        ChannelAddress channelAddress = new ChannelAddress(MESSAGINGENDPOINTURL, CHANNELID);
        String addressString = RoutingTypesUtil.toAddressString(channelAddress);
        Address address = RoutingTypesUtil.fromAddressString(addressString);
        assertTrue(address instanceof ChannelAddress);
        assertEquals(MESSAGINGENDPOINTURL, channelAddress.getMessagingEndpointUrl());
        assertEquals(CHANNELID, channelAddress.getChannelId());
    }
}
