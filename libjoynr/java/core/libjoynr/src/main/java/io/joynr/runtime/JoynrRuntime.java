package io.joynr.runtime;

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

import java.util.Set;

import io.joynr.proxy.Future;
import io.joynr.proxy.ProxyBuilder;
import joynr.types.ProviderQos;

/**
 * Central Joyn Api object, used to register / unregister providers and create proxy builders
 *
 */
public interface JoynrRuntime {
    /**
     * Registers a provider in the joynr framework
     *
     * @param domain
     *            The domain the provider should be registered for. Has to be identical at the client to be able to find
     *            the provider.
     * @param provider
     *            Instance of the provider implementation. It is assumed that the provided implementations offers
     *            the following annotations in its (inherited) class definition: {@link io.joynr.provider.JoynrInterface}
     *            and {@link io.joynr.JoynrVersion}.
     * @param providerQos
     *            The providers quality of service settings.
     * @return Returns a Future which can be used to check the registration status.
     */
    Future<Void> registerProvider(String domain, Object provider, ProviderQos providerQos);

    /**
     * Unregisters the provider from the joynr framework. It can no longer be used or discovered.
     *
     * @param domain
     *            The domain the provider was registered for.
     * @param provider
     *            The provider instance.
     */
    void unregisterProvider(String domain, Object provider);

    /**
     * Returns a proxy builder instance to build a proxy object for one or more
     * providers in the given domain. Generally, you will get a proxy for just
     * one provider, if one is found, but you can also provide custom
     * {@link ArbitrationStrategyFunction arbitration} in the
     * {@link ProxyBuilder#setDiscoveryQos(io.joynr.arbitration.DiscoveryQos) discovery QoS}
     * in order to match against multiple providers.
     * In this case, calling a method on the proxy will call that method on all
     * matched providers.
     * Note that this only works for fire-and-forget methods.
     * Fire-and-forget methods are RPC methods with no return value and are
     * marked with <code>fireAndForget</code> in their Franca IDL definition /
     * annotated with <code>@FireAndForget</code> in the Java interface.
     * An attempt to call a method which is not fire-and-forget when multiple
     * providers were matched will result in an exception being thrown.
     *
     * @param <T> interface of the provider you want to build a proxy for.
     * @param domain
     *            Domain of the provider.
     * @param interfaceClass
     *            Interface the provider offers.
     * @return After setting arbitration, proxy and messaging QoS parameters
     * the returned ProxyBuilder can be used to build the proxy instance.
     */
    <T> ProxyBuilder<T> getProxyBuilder(final String domain, final Class<T> interfaceClass);

    /**
     * Returns a proxy builder instance to build a proxy object for,
     * potentially, multiple providers in a given set of domains.
     * See {@link #getProxyBuilder(String, Class)} for a description of what
     * this method does, the only difference is that
     * the search for the providers is done with a set of domains rather than
     * just one. If more than one provider is matched, a call to the proxy's
     * methods will result in that method being called on each provider.
     * Note that this only works for fire-and-forget methods.
     * Fire-and-forget methods are RPC methods with no return value and are
     * marked with <code>fireAndForget</code> in their Franca IDL definition /
     * annotated with <code>@FireAndForget</code> in the Java interface.
     * An attempt to call a method which is not fire-and-forget when multiple
     * providers were matched will result in an exception being thrown.
     *
     * @param <T> interface of the provider you want to build a proxy for.
     * @param domains
     *            the set of domains of the providers.
     * @param interfaceClass
     *            Interface the provider offers.
     * @return After setting arbitration, proxy and messaging QoS parameters the returned ProxyBuilder can be used to
     *         build the proxy instance.
     */
    <T> ProxyBuilder<T> getProxyBuilder(final Set<String> domains, final Class<T> interfaceClass);

    /**
     * Shutdown the joynr instance:
     * <ul>
     * <li>Discards pending outgoing messages
     * <li>Does not wait for incoming messages
     * </ul>
     *
     * @param clear
     *            If true, the instance removes all artifacts it created:
     *            <ul>
     *            <li>all global capabilities are deregistered from the Global Capabilities Directory
     *            <li>The channel is deregistered from the Channel Url Directory
     *            <li>The channel is removed from the bounce proxy
     *            </ul>
     */
    void shutdown(boolean clear);
}
