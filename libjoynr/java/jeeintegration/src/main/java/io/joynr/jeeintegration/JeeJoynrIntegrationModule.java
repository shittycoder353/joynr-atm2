/**
 *
 */
package io.joynr.jeeintegration;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

import io.joynr.capabilities.LocalCapabilitiesDirectory;
import io.joynr.jeeintegration.httpbridge.HttpBridgeEndpointRegistryClientModule;
import io.joynr.jeeintegration.messaging.JeeHttpMessagingModule;
import io.joynr.jeeintegration.messaging.JeeMqttMessageSendingModule;
import io.joynr.messaging.AbstractMiddlewareMessagingStubFactory;
import io.joynr.messaging.IMessagingSkeleton;
import io.joynr.messaging.IMessagingStub;
import io.joynr.messaging.JoynrMessageProcessor;
import io.joynr.messaging.MessagingSkeletonFactory;
import io.joynr.messaging.routing.MessageRouter;
import io.joynr.messaging.routing.MessagingStubFactory;
import io.joynr.runtime.JoynrInjectionConstants;
import joynr.system.RoutingTypes.Address;

/**
 * Guice module for the JEE integration of joynr which is used to override certain bindings to provide JEE resources
 * where available (e.g. a managed execution service rather than the J2SE version).
 */
public class JeeJoynrIntegrationModule extends AbstractModule {

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * Constructor which is passed in the JEE resources which are to be exposed to the Guice injector in which this
     * module is installed.
     *
     * @param scheduledExecutorService
     *            a scheduled executor service which is managed by the JEE runtime.
     */
    public JeeJoynrIntegrationModule(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    protected void configure() {
        bind(ScheduledExecutorService.class).annotatedWith(Names.named(MessageRouter.SCHEDULEDTHREADPOOL))
                                            .toInstance(scheduledExecutorService);
        bind(ScheduledExecutorService.class).annotatedWith(Names.named(JoynrInjectionConstants.JOYNR_SCHEDULER_CLEANUP))
                                            .toInstance(scheduledExecutorService);
        bind(ScheduledExecutorService.class).annotatedWith(Names.named(LocalCapabilitiesDirectory.JOYNR_SCHEDULER_CAPABILITIES_FRESHNESS))
                                            .toInstance(scheduledExecutorService);
        bind(ExecutorService.class).toInstance(scheduledExecutorService);

        MapBinder<Class<? extends Address>, IMessagingSkeleton> messagingSkeletonFactory;
        messagingSkeletonFactory = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<? extends Address>>() {
        }, new TypeLiteral<IMessagingSkeleton>() {
        }, Names.named(MessagingSkeletonFactory.MIDDLEWARE_MESSAGING_SKELETONS));

        MapBinder<Class<? extends Address>, AbstractMiddlewareMessagingStubFactory<? extends IMessagingStub, ? extends Address>> messagingStubFactory;
        messagingStubFactory = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<? extends Address>>() {
        }, new TypeLiteral<AbstractMiddlewareMessagingStubFactory<? extends IMessagingStub, ? extends Address>>() {
        }, Names.named(MessagingStubFactory.MIDDLEWARE_MESSAGING_STUB_FACTORIES));

        Multibinder.newSetBinder(binder(), JoynrMessageProcessor.class);

        install(new JeeHttpMessagingModule(messagingSkeletonFactory, messagingStubFactory));
        install(new HttpBridgeEndpointRegistryClientModule());
        install(new JeeMqttMessageSendingModule(messagingSkeletonFactory,
                                                messagingStubFactory));
    }

}
