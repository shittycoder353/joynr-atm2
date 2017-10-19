package io.joynr.dispatching.rpc;

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

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Provider;

import io.joynr.context.JoynrMessageScope;
import io.joynr.dispatching.RequestCaller;
import io.joynr.dispatching.RequestCallerFactory;
import io.joynr.messaging.JoynrMessageMetaInfo;
import io.joynr.messaging.JoynrMessageCreator;
import joynr.OneWayRequest;
import joynr.tests.DefaulttestProvider;

/**
 * Unit tests for {@link RequestInterpreter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class RequestInterpreterTest {

    @Mock
    private JoynrMessageScope joynrMessageScope;

    @Mock
    private Provider<JoynrMessageCreator> joynrMessageCreatorProvider;

    @Mock
    private Provider<JoynrMessageMetaInfo> joynrMessageContextProvider;

    @Mock
    private JoynrMessageCreator joynrMessageCreator;

    @Mock
    private JoynrMessageMetaInfo joynrMessageContext;

    private RequestInterpreter subject;

    private RequestCaller requestCaller;

    private OneWayRequest request;

    @Before
    public void setup() {
        RequestCallerFactory requestCallerFactory = new RequestCallerFactory();
        requestCaller = requestCallerFactory.create(new DefaulttestProvider());
        subject = new RequestInterpreter(joynrMessageScope, joynrMessageCreatorProvider, joynrMessageContextProvider);
        request = new OneWayRequest("getTestAttribute", new Object[0], new Class[0]);
        request.setCreatorUserId("creator");
        Mockito.when(joynrMessageCreatorProvider.get()).thenReturn(joynrMessageCreator);
        Mockito.when(joynrMessageContextProvider.get()).thenReturn(joynrMessageContext);
    }

    @Test
    public void testJoynrMessageScopeActivated() {
        subject.invokeMethod(requestCaller, request);
        verify(joynrMessageScope).activate();
        verify(joynrMessageScope).deactivate();
    }

    @Test
    public void testCreatorSetInMessageContext() {
        subject.invokeMethod(requestCaller, request);
        verify(joynrMessageCreatorProvider).get();
        verify(joynrMessageCreator).setMessageCreatorId("creator");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testContextSet() {
        subject.invokeMethod(requestCaller, request);
        verify(joynrMessageContextProvider).get();
        verify(joynrMessageContext).setMessageContext(Mockito.anyMap());
    }
}
