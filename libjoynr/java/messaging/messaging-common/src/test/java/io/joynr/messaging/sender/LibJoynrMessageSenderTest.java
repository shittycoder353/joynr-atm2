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
package io.joynr.messaging.sender;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import joynr.ImmutableMessage;
import joynr.MutableMessage;

@RunWith(MockitoJUnitRunner.class)
public class LibJoynrMessageSenderTest extends MessageSenderTestBase {
    @Test
    public void testReplyToIsSet() {
        MutableMessage message = createTestRequestMessage();
        String expectedReplyTo = "expectedReplyTo";

        LibJoynrMessageSender subject = new LibJoynrMessageSender(messageRouterMock);
        subject.setReplyToAddress(expectedReplyTo);
        subject.sendMessage(message);

        ArgumentCaptor<ImmutableMessage> argCaptor = ArgumentCaptor.forClass(ImmutableMessage.class);
        verify(messageRouterMock).route(argCaptor.capture());
        assertEquals(expectedReplyTo, argCaptor.getValue().getReplyTo());
    }
}
