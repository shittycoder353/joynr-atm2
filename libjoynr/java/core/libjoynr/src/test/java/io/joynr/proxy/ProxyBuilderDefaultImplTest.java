package io.joynr.proxy;

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

import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.joynr.JoynrVersion;
import io.joynr.arbitration.ArbitrationCallback;
import io.joynr.arbitration.Arbitrator;
import io.joynr.arbitration.DiscoveryQos;
import io.joynr.discovery.LocalDiscoveryAggregator;
import io.joynr.exceptions.JoynrRuntimeException;
import io.joynr.exceptions.MultiDomainNoCompatibleProviderFoundException;
import io.joynr.exceptions.NoCompatibleProviderFoundException;
import io.joynr.messaging.MessagingQos;
import io.joynr.proxy.ProxyBuilder.ProxyCreatedCallback;
import joynr.types.Version;

/**
 * Unit tests for the {@link ProxyBuilderDefaultImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProxyBuilderDefaultImplTest {

    private static final long MAX_MESSAGE_TTL = 1024L;
    private static final long DISCOVERY_TIMEOUT_MS = 30000L;
    private static final long RETRY_INTERVAL_MS = 2000L;

    @JoynrVersion(major = 1, minor = 1)
    private static interface TestInterface {
        String INTERFACE_NAME = "test/interface";
    }

    @Mock
    private Arbitrator arbitrator;

    @Mock
    private LocalDiscoveryAggregator localDiscoveryAggregator;

    @Mock
    private ProxyInvocationHandlerFactory proxyInvocationHandlerFactory;

    @Mock
    private ProxyInvocationHandler proxyInvocationHandler;

    @Captor
    private ArgumentCaptor<ArbitrationCallback> arbitrationCallbackCaptor;

    @Captor
    private ArgumentCaptor<JoynrRuntimeException> exceptionCaptor;

    private ProxyBuilderDefaultImpl<TestInterface> subject;

    @Mock
    private ProxyCreatedCallback<TestInterface> proxyCreatedCallback;

    public void setup(Set<String> domains) throws Exception {
        subject = new ProxyBuilderDefaultImpl<TestInterface>(localDiscoveryAggregator,
                                                             domains,
                                                             TestInterface.class,
                                                             proxyInvocationHandlerFactory,
                                                             MAX_MESSAGE_TTL,
                                                             DISCOVERY_TIMEOUT_MS,
                                                             RETRY_INTERVAL_MS);
        Field arbitratorField = ProxyBuilderDefaultImpl.class.getDeclaredField("arbitrator");
        arbitratorField.setAccessible(true);
        arbitratorField.set(subject, arbitrator);
        when(proxyInvocationHandlerFactory.create(Mockito.<Set<String>> any(),
                                                  eq(TestInterface.INTERFACE_NAME),
                                                  Mockito.<String> any(),
                                                  Mockito.<DiscoveryQos> any(),
                                                  Mockito.<MessagingQos> any())).thenReturn(proxyInvocationHandler);
    }

    @Test
    public void testNoCompatibleProviderPassedToOnError() throws Exception {
        final String domain = "domain1";
        final Set<String> domains = Sets.newHashSet(domain);
        setup(domains);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                executor.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        Thread.sleep(10L);
                        verify(arbitrator).setArbitrationListener(arbitrationCallbackCaptor.capture());
                        ArbitrationCallback callback = arbitrationCallbackCaptor.getValue();
                        Set<Version> discoveredVersions = Sets.newHashSet(new Version(100, 100));
                        callback.onError(new NoCompatibleProviderFoundException(TestInterface.INTERFACE_NAME,
                                                                                new Version(1, 1),
                                                                                domain,
                                                                                discoveredVersions));
                        return null;
                    }
                });
                return null;
            }
        }).when(arbitrator).startArbitration();
        subject.build(proxyCreatedCallback);
        executor.shutdown();
        executor.awaitTermination(100L, TimeUnit.MILLISECONDS);
        verify(proxyCreatedCallback).onProxyCreationError(exceptionCaptor.capture());
        JoynrRuntimeException capturedException = exceptionCaptor.getValue();
        assertTrue(capturedException instanceof NoCompatibleProviderFoundException);
    }

    @Test
    public void testMultiDomainNoCompatibleProviderFoundSetOnInvocationHandler() throws Exception {
        final Set<String> domains = Sets.newHashSet("domain-1", "domain-2");
        setup(domains);
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                executor.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        Thread.sleep(10L);
                        verify(arbitrator).setArbitrationListener(arbitrationCallbackCaptor.capture());
                        ArbitrationCallback callback = arbitrationCallbackCaptor.getValue();
                        Map<String, Set<Version>> versionsByDomain = new HashMap<>();
                        HashSet<Version> discoveredVersions = Sets.newHashSet(new Version(100, 100));
                        Map<String, NoCompatibleProviderFoundException> exceptionsByDomain = Maps.newHashMap();
                        for (String domain : domains) {
                            versionsByDomain.put(domain, discoveredVersions);
                            exceptionsByDomain.put(domain, new NoCompatibleProviderFoundException("interfaceName",
                                                                                                  new Version(1, 1),
                                                                                                  domain,
                                                                                                  discoveredVersions));
                        }
                        callback.onError(new MultiDomainNoCompatibleProviderFoundException(exceptionsByDomain));
                        return null;
                    }
                });
                return null;
            }
        }).when(arbitrator).startArbitration();
        subject.build(proxyCreatedCallback);
        executor.shutdown();
        executor.awaitTermination(100L, TimeUnit.MILLISECONDS);
        verify(proxyCreatedCallback).onProxyCreationError(exceptionCaptor.capture());
        JoynrRuntimeException capturedException = exceptionCaptor.getValue();
        assertTrue(capturedException instanceof MultiDomainNoCompatibleProviderFoundException);
    }
}
