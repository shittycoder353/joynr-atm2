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

import static com.google.inject.util.Modules.override;
import static java.lang.String.format;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import io.joynr.ProvidedBy;
import io.joynr.accesscontrol.StaticDomainAccessControlProvisioning;
import io.joynr.accesscontrol.StaticDomainAccessControlProvisioningModule;
import io.joynr.capabilities.ParticipantIdKeyUtil;
import io.joynr.exceptions.JoynrIllegalStateException;
import io.joynr.jeeintegration.api.JeeIntegrationPropertyKeys;
import io.joynr.jeeintegration.api.JoynrLocalDomain;
import io.joynr.jeeintegration.api.JoynrMqttClientIdProvider;
import io.joynr.jeeintegration.api.JoynrProperties;
import io.joynr.jeeintegration.api.JoynrRawMessagingPreprocessor;
import io.joynr.messaging.JoynrMessageProcessor;
import io.joynr.messaging.MessagingPropertyKeys;
import io.joynr.messaging.NoOpRawMessagingPreprocessor;
import io.joynr.messaging.RawMessagingPreprocessor;
import io.joynr.messaging.mqtt.MqttClientIdProvider;
import io.joynr.provider.JoynrInterface;
import io.joynr.runtime.AbstractJoynrApplication;
import io.joynr.runtime.CCInProcessRuntimeModule;
import io.joynr.runtime.JoynrInjectorFactory;
import io.joynr.runtime.JoynrRuntime;
import joynr.infrastructure.DacTypes.MasterAccessControlEntry;
import joynr.infrastructure.DacTypes.Permission;
import joynr.infrastructure.DacTypes.TrustLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation for {@link JoynrRuntimeFactory}, which will use information produced by
 * {@link JoynrProperties} and {@link JoynrLocalDomain}, if available, to configure
 * the joynr runtime and application with.
 * <p>
 * <b>IMPORTANT</b>: This class requires the EE runtime to have been configured with a ManagedScheduledExecutorService
 * resource which has been given the name 'concurrent/joynrMessagingScheduledExecutor'.
 */
@Singleton
public class DefaultJoynrRuntimeFactory implements JoynrRuntimeFactory {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultJoynrRuntimeFactory.class);

    private static final String MQTT = "mqtt";

    private Properties joynrProperties;

    private final String joynrLocalDomain;

    private BeanManager beanManager;

    /**
     * The scheduled executor service to use for providing to the joynr runtime.
     */
    @Resource(name = JeeIntegrationPropertyKeys.JEE_MESSAGING_SCHEDULED_EXECUTOR_RESOURCE)
    private ScheduledExecutorService scheduledExecutorService;

    private Injector fInjector = null;

    private RawMessagingPreprocessor rawMessagePreprocessor;
    private MqttClientIdProvider mqttClientIdProvider;

    /**
     * Constructor in which the JEE runtime injects the managed resources and the JEE joynr integration specific
     * configuration data (see {@link JoynrProperties} and {@link JoynrLocalDomain}
     * ).
     * <p>
     * <b>Note</b> that if the EJB which contains the producer methods implements an interface, then the producer
     * methods also need to be declared in that interface, otherwise CDI won't recognise the method implementations as
     * producers.
     *
     * @param joynrProperties  the joynr properties, if present, by {@link #prepareJoynrProperties(Properties)} to prepare the properties with which the injector is created.
     * @param joynrLocalDomain the joynr local domain name to use for the application.
     * @param rawMessagePreprocessor can be optionally provided to intercept incoming messages and inspect or modify them
     */
    @Inject
    public DefaultJoynrRuntimeFactory(@JoynrProperties Instance<Properties> joynrProperties,
                                      @JoynrLocalDomain Instance<String> joynrLocalDomain,
                                      @JoynrRawMessagingPreprocessor Instance<RawMessagingPreprocessor> rawMessagePreprocessor,
                                      @JoynrMqttClientIdProvider Instance<MqttClientIdProvider> mqttClientIdProvider,
                                      BeanManager beanManager) {
        if (!joynrLocalDomain.isUnsatisfied() && !joynrLocalDomain.isAmbiguous()) {
            this.joynrLocalDomain = joynrLocalDomain.get();
        } else {
            String message = "No local domain name specified. Please provide a value for the local domain via @JoynrLocalDomain in your configuration EJB.";
            LOG.error(message);
            throw new JoynrIllegalStateException(message);
        }

        if (!rawMessagePreprocessor.isUnsatisfied()) {
            if (!rawMessagePreprocessor.isAmbiguous()) {
                this.rawMessagePreprocessor = rawMessagePreprocessor.get();
            } else {
                String message = "Only one RawMessagePreprocessor may be provided.";
                LOG.error(message);
                throw new JoynrIllegalStateException(message);
            }
        } else {
            this.rawMessagePreprocessor = new NoOpRawMessagingPreprocessor();
        }

        if (!mqttClientIdProvider.isUnsatisfied()) {
            if (!mqttClientIdProvider.isAmbiguous()) {
                this.mqttClientIdProvider = mqttClientIdProvider.get();
            } else {
                String message = "Only one MqttClientIdProvider may be provided";
                LOG.error(message);
                throw new JoynrIllegalStateException(message);
            }
        } else {
            this.mqttClientIdProvider = null;
        }

        Properties configuredProperties;
        if (!joynrProperties.isUnsatisfied() && !joynrProperties.isAmbiguous()) {
            configuredProperties = joynrProperties.get();
        } else {
            LOG.info("No custom joynr properties provided. Will use default properties.");
            configuredProperties = new Properties();
        }
        this.joynrProperties = prepareJoynrProperties(configuredProperties);
        this.beanManager = beanManager;
    }

    @Override
    public JoynrRuntime create(Set<Class<?>> providerInterfaceClasses) {
        LOG.info("Creating clusterable participant IDs for discovered providers.");
        createClusterableParticipantIds(providerInterfaceClasses);
        LOG.info("Provisioning access control for {}", providerInterfaceClasses);
        provisionAccessControl(joynrProperties, joynrLocalDomain, getProviderInterfaceNames(providerInterfaceClasses));
        LOG.info(format("Creating application with joynr properties:%n%s", joynrProperties));
        JoynrRuntime runtime = getInjector().getInstance(JoynrRuntime.class);
        LOG.info("Created runtime: {}", runtime);
        return runtime;
    }

    private void createClusterableParticipantIds(Set<Class<?>> providerInterfaceClasses) {
        for (Class<?> joynrProviderClass : providerInterfaceClasses) {
            String participantIdKey = ParticipantIdKeyUtil.getProviderParticipantIdKey(getLocalDomain(),
                                                                                       joynrProviderClass);
            if (!joynrProperties.containsKey(participantIdKey)) {
                joynrProperties.put(participantIdKey, createClusterableParticipantId(joynrProviderClass));
            }
        }
    }

    private String createClusterableParticipantId(Class<?> joynrProviderClass) {
        String key = getLocalDomain() + "." + joynrProperties.getProperty(MessagingPropertyKeys.CHANNELID) + "."
                + getInterfaceName(joynrProviderClass);
        return key.replace("/", ".");
    }

    @Override
    public Injector getInjector() {
        if (fInjector == null) {
            Module jeeModule = override(new CCInProcessRuntimeModule()).with(new JeeJoynrIntegrationModule(scheduledExecutorService));
            Module finalModule = override(jeeModule).with(new AbstractModule() {
                @Override
                protected void configure() {
                    bind(RawMessagingPreprocessor.class).toInstance(rawMessagePreprocessor);

                    if (mqttClientIdProvider != null) {
                        bind(MqttClientIdProvider.class).toInstance(mqttClientIdProvider);
                    }
                }
            });

            fInjector = new JoynrInjectorFactory(joynrProperties,
                                                 new StaticDomainAccessControlProvisioningModule(),
                                                 getMessageProcessorsModule(),
                                                 finalModule).getInjector();
        }
        return fInjector;
    }

    private AbstractModule getMessageProcessorsModule() {
        final Set<Bean<?>> joynrMessageProcessorBeans = beanManager.getBeans(JoynrMessageProcessor.class,
                                                                             new AnnotationLiteral<Any>() {
                                                                                 private static final long serialVersionUID = 1L;
                                                                             });
        return new AbstractModule() {
            @SuppressWarnings("unchecked")
            @Override
            protected void configure() {

                Multibinder<JoynrMessageProcessor> joynrMessageProcessorMultibinder = Multibinder.newSetBinder(binder(),
                                                                                                               new TypeLiteral<JoynrMessageProcessor>() {
                                                                                                               });
                for (Bean<?> bean : joynrMessageProcessorBeans) {
                    joynrMessageProcessorMultibinder.addBinding()
                                                    .toInstance((JoynrMessageProcessor) Proxy.newProxyInstance(getClass().getClassLoader(),
                                                                                                               new Class[]{
                                                                                                                       JoynrMessageProcessor.class },
                                                                                                               new BeanCallingProxy<JoynrMessageProcessor>((Bean<JoynrMessageProcessor>) bean,
                                                                                                                                                           beanManager)));
                }
            }
        };
    }

    private Properties prepareJoynrProperties(Properties configuredProperties) {
        Properties defaultJoynrProperties = new Properties();
        defaultJoynrProperties.setProperty(AbstractJoynrApplication.PROPERTY_JOYNR_DOMAIN_LOCAL, joynrLocalDomain);
        defaultJoynrProperties.setProperty(MessagingPropertyKeys.PROPERTY_MESSAGING_PRIMARYGLOBALTRANSPORT, MQTT);
        defaultJoynrProperties.putAll(configuredProperties);
        return defaultJoynrProperties;
    }

    private String[] getProviderInterfaceNames(Set<Class<?>> providerInterfaceClasses) {
        Set<String> providerInterfaceNames = new HashSet<>();
        for (Class<?> providerInterfaceClass : providerInterfaceClasses) {
            providerInterfaceNames.add(getInterfaceName(providerInterfaceClass));
        }
        return providerInterfaceNames.toArray(new String[providerInterfaceNames.size()]);
    }

    private String getInterfaceName(Class<?> providerInterfaceClass) {
        try {
            ProvidedBy providedBy = providerInterfaceClass.getAnnotation(ProvidedBy.class);
            JoynrInterface joynrInterface = providedBy.value().getAnnotation(JoynrInterface.class);
            return joynrInterface.name();
        } catch (SecurityException | IllegalArgumentException e) {
            LOG.debug("error getting interface details", e);
            return providerInterfaceClass.getSimpleName();
        }
    }

    private void provisionAccessControl(Properties properties, String domain, String[] interfaceNames) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTypingAsProperty(DefaultTyping.JAVA_LANG_OBJECT, "_typeName");
        List<MasterAccessControlEntry> allEntries = new ArrayList<>();
        for (String interfaceName : interfaceNames) {
            MasterAccessControlEntry newMasterAccessControlEntry = new MasterAccessControlEntry("*",
                                                                                                domain,
                                                                                                interfaceName,
                                                                                                TrustLevel.LOW,
                                                                                                new TrustLevel[]{
                                                                                                        TrustLevel.LOW },
                                                                                                TrustLevel.LOW,
                                                                                                new TrustLevel[]{
                                                                                                        TrustLevel.LOW },
                                                                                                "*",
                                                                                                Permission.YES,
                                                                                                new Permission[]{
                                                                                                        joynr.infrastructure.DacTypes.Permission.YES });
            allEntries.add(newMasterAccessControlEntry);
        }
        MasterAccessControlEntry[] provisionedAccessControlEntries = allEntries.toArray(new MasterAccessControlEntry[allEntries.size()]);
        String provisionedAccessControlEntriesAsJson;
        try {
            provisionedAccessControlEntriesAsJson = objectMapper.writeValueAsString(provisionedAccessControlEntries);
            properties.setProperty(StaticDomainAccessControlProvisioning.PROPERTY_PROVISIONED_MASTER_ACCESSCONTROLENTRIES,
                                   provisionedAccessControlEntriesAsJson);
        } catch (JsonProcessingException e) {
            LOG.error("Error parsing JSON.", e);
        }
    }

    @Override
    public String getLocalDomain() {
        return joynrLocalDomain;
    }

    public RawMessagingPreprocessor getRawMessagePreprocessor() {
        return rawMessagePreprocessor;
    }

    public MqttClientIdProvider getMqttClientIdProvider() {
        return mqttClientIdProvider;
    }
}
