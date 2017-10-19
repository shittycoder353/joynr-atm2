package io.joynr.messaging.mqtt;

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

public interface JoynrMqttClient {

    public void start();

    public void setMessageListener(IMqttMessagingSkeleton rawMessaging);

    public void shutdown();

    public void publishMessage(String topic, byte[] serializedMessage);

    public void publishMessage(String topic, byte[] serializedMessage, int qosLevel);

    public void subscribe(String topic);

    public void unsubscribe(String topic);

    public void sendMqttAck(int mqttId, int mqttQos);

}