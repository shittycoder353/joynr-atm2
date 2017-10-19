package io.joynr.arbitration;

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

import static java.lang.String.format;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import io.joynr.exceptions.DiscoveryException;
import io.joynr.exceptions.JoynrRuntimeException;
import io.joynr.exceptions.JoynrShutdownException;
import io.joynr.exceptions.MultiDomainNoCompatibleProviderFoundException;
import io.joynr.exceptions.NoCompatibleProviderFoundException;
import io.joynr.proxy.Callback;
import joynr.exceptions.ApplicationException;
import joynr.system.DiscoveryAsync;
import joynr.types.DiscoveryEntry;
import joynr.types.DiscoveryEntryWithMetaInfo;
import joynr.types.ProviderQos;
import joynr.types.Version;

/**
 *
 * The Arbitrator controls the discovery process:
 * <ul>
 * <li> search for matching {@link DiscoveryEntry} elements locally and/or globally depending on the DiscoveryQos
 * <li> call the {@link ArbitrationStrategyFunction} to select a discoveryEntry to be used for the proxy being created
 * </ul>
 */
public class Arbitrator {
    private static final Logger logger = LoggerFactory.getLogger(Arbitrator.class);
    private final long MINIMUM_ARBITRATION_RETRY_DELAY;
    protected DiscoveryQos discoveryQos;
    protected DiscoveryAsync localDiscoveryAggregator;
    protected ArbitrationResult arbitrationResult = new ArbitrationResult();
    protected ArbitrationStatus arbitrationStatus = ArbitrationStatus.ArbitrationNotStarted;
    protected ArbitrationCallback arbitrationListener;
    // Initialized with 0 to block until the listener is registered
    private Semaphore arbitrationListenerSemaphore = new Semaphore(0);
    private long arbitrationDeadline;
    private Set<String> domains;
    private String interfaceName;
    private Version interfaceVersion;
    private ArbitrationStrategyFunction arbitrationStrategyFunction;
    private DiscoveryEntryVersionFilter discoveryEntryVersionFilter;
    private final Map<String, Set<Version>> discoveredVersions = new HashMap<>();

    // CHECKSTYLE IGNORE ParameterNumber FOR NEXT 1 LINES
    public Arbitrator(final Set<String> domains,
                      final String interfaceName,
                      final Version interfaceVersion,
                      final DiscoveryQos discoveryQos,
                      DiscoveryAsync localDiscoveryAggregator,
                      long minimumArbitrationRetryDelay,
                      ArbitrationStrategyFunction arbitrationStrategyFunction,
                      DiscoveryEntryVersionFilter discoveryEntryVersionFilter) {
        // CHECKSTYLE:ON
        this.domains = domains;
        this.interfaceName = interfaceName;
        this.interfaceVersion = interfaceVersion;
        MINIMUM_ARBITRATION_RETRY_DELAY = minimumArbitrationRetryDelay;
        this.discoveryQos = discoveryQos;
        this.localDiscoveryAggregator = localDiscoveryAggregator;
        this.arbitrationStrategyFunction = arbitrationStrategyFunction;
        arbitrationDeadline = System.currentTimeMillis() + discoveryQos.getDiscoveryTimeoutMs();
        this.discoveryEntryVersionFilter = discoveryEntryVersionFilter;
    }

    protected void onError(Throwable exception) {
        if (exception instanceof JoynrShutdownException) {
            arbitrationFailed(exception);
        } else if (exception instanceof JoynrRuntimeException) {
            if (isArbitrationInTime()) {
                restartArbitration();
            } else {
                arbitrationFailed(exception);
            }
        } else if (exception instanceof ApplicationException) {
            arbitrationFailed(exception);
        } else {
            arbitrationFailed(new JoynrRuntimeException(exception));
        }
    }

    /**
     * Called by the proxy builder to start the arbitration process.
     */
    public void startArbitration() {
        logger.debug("DISCOVERY lookup for domain: {}, interface: {}", domains, interfaceName);
        localDiscoveryAggregator.lookup(new DiscoveryCallback(),
                                        domains.toArray(new String[domains.size()]),
                                        interfaceName,
                                        new joynr.types.DiscoveryQos(discoveryQos.getCacheMaxAgeMs(),
                                                                     discoveryQos.getDiscoveryTimeoutMs(),
                                                                     joynr.types.DiscoveryScope.valueOf(discoveryQos.getDiscoveryScope()
                                                                                                                    .name()),
                                                                     discoveryQos.getProviderMustSupportOnChange()));
    }

    /**
     * Called by the proxy builder to register the listener for arbitration results (DiscoveryAgent Instance).
     * If the arbitration is already running or even finished the current state and in case of a successful arbitration
     * the result is set at the newly registered listener.
     *
     * @param arbitrationListener the arbitration listener
     */
    public void setArbitrationListener(ArbitrationCallback arbitrationListener) {

        this.arbitrationListener = arbitrationListener;
        // listener is now ready to receive arbitration result/status
        arbitrationListenerSemaphore.release();
        if (arbitrationStatus == ArbitrationStatus.ArbitrationSuccesful) {
            arbitrationFinished(arbitrationStatus, arbitrationResult);
        }
    }

    /**
     * Sets the arbitration result at the arbitrationListener if the listener is already registered
     */
    protected void arbitrationFinished(ArbitrationStatus arbitrationStatus, ArbitrationResult arbitrationResult) {
        this.arbitrationStatus = arbitrationStatus;
        this.arbitrationResult = arbitrationResult;

        // wait for arbitration listener to be registered
        if (arbitrationListenerSemaphore.tryAcquire()) {
            arbitrationListener.onSuccess(arbitrationResult);
            arbitrationListenerSemaphore.release();
        }
    }

    public ArbitrationStatus getArbitrationStatus() {
        return arbitrationStatus;
    }

    protected boolean isArbitrationInTime() {
        return System.currentTimeMillis() < arbitrationDeadline;
    }

    protected void restartArbitration() {
        logger.trace("Restarting Arbitration");
        long backoff = Math.max(discoveryQos.getRetryIntervalMs(), MINIMUM_ARBITRATION_RETRY_DELAY);
        try {
            if (backoff > 0) {
                Thread.sleep(backoff);
            }
            startArbitration();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void arbitrationFailed() {
        arbitrationFailed(null);
    }

    protected void arbitrationFailed(Throwable exception) {
        arbitrationStatus = ArbitrationStatus.ArbitrationCanceledForever;
        Throwable reason;
        if (arbitrationListenerSemaphore.tryAcquire()) {
            if (exception != null) {
                reason = exception;
            } else if (discoveredVersions == null || discoveredVersions.isEmpty()) {
                reason = new DiscoveryException("Unable to find provider in time: interface: " + interfaceName
                        + " domains: " + domains);
            } else {
                reason = noCompatibleProviderFound(discoveredVersions);
            }
            arbitrationListener.onError(reason);
        }
    }

    private Throwable noCompatibleProviderFound(Map<String, Set<Version>> discoveredVersions) {
        Throwable reason = null;
        if (domains.size() == 1) {
            if (discoveredVersions.size() != 1) {
                reason = new IllegalStateException("Only looking for one domain, but got multi-domain result with discovered but incompatible versions.");
            } else {
                reason = new NoCompatibleProviderFoundException(interfaceName,
                                                                interfaceVersion,
                                                                discoveredVersions.keySet().iterator().next(),
                                                                discoveredVersions.values().iterator().next());
            }
        } else if (domains.size() > 1) {
            Map<String, NoCompatibleProviderFoundException> exceptionsByDomain = new HashMap<>();
            for (Map.Entry<String, Set<Version>> versionsByDomainEntry : discoveredVersions.entrySet()) {
                exceptionsByDomain.put(versionsByDomainEntry.getKey(),
                                       new NoCompatibleProviderFoundException(interfaceName,
                                                                              interfaceVersion,
                                                                              versionsByDomainEntry.getKey(),
                                                                              versionsByDomainEntry.getValue()));
            }
            reason = new MultiDomainNoCompatibleProviderFoundException(exceptionsByDomain);
        }
        return reason;
    }

    private class DiscoveryCallback extends Callback<DiscoveryEntryWithMetaInfo[]> {

        @Override
        public void onFailure(JoynrRuntimeException error) {
            Arbitrator.this.onError(error);
        }

        @Override
        public void onSuccess(DiscoveryEntryWithMetaInfo[] discoveryEntries) {
            assert discoveryEntries != null : "Discovery entries may not be null.";
            if (allDomainsDiscovered(discoveryEntries)) {
                logger.trace("Lookup succeeded. Got {}", Arrays.toString(discoveryEntries));
                Set<DiscoveryEntryWithMetaInfo> discoveryEntriesSet = filterDiscoveryEntries(discoveryEntries);

                Set<DiscoveryEntryWithMetaInfo> selectedCapabilities = arbitrationStrategyFunction.select(discoveryQos.getCustomParameters(),
                                                                                                          discoveryEntriesSet);

                logger.trace("Selected capabilities: {}", selectedCapabilities);
                if (selectedCapabilities != null && !selectedCapabilities.isEmpty()) {
                    arbitrationResult.setDiscoveryEntries(selectedCapabilities);
                    arbitrationFinished(ArbitrationStatus.ArbitrationSuccesful, arbitrationResult);
                } else {
                    arbitrationFailed();
                }
            } else {
                if (isArbitrationInTime()) {
                    restartArbitration();
                } else {
                    arbitrationFailed();
                }
            }
        }

        private boolean allDomainsDiscovered(DiscoveryEntry[] discoveryEntries) {
            Set<String> discoveredDomains = new HashSet<>();
            for (DiscoveryEntry foundDiscoveryEntry : discoveryEntries) {
                discoveredDomains.add(foundDiscoveryEntry.getDomain());
            }
            boolean allDomainsDiscovered = discoveredDomains.equals(domains);

            if (!allDomainsDiscovered) {
                Set<String> missingDomains = new HashSet<>(domains);
                missingDomains.removeAll(discoveredDomains);
                logger.trace("All domains must be found. Domains not found: {}", missingDomains);
            }

            return allDomainsDiscovered;
        }

        private Set<DiscoveryEntryWithMetaInfo> filterDiscoveryEntries(DiscoveryEntryWithMetaInfo[] discoveryEntries) {
            Set<DiscoveryEntryWithMetaInfo> discoveryEntriesSet;
            // If onChange subscriptions are required ignore
            // providers that do not support them
            if (discoveryQos.getProviderMustSupportOnChange()) {
                discoveryEntriesSet = new HashSet<>(discoveryEntries.length);
                for (DiscoveryEntryWithMetaInfo discoveryEntry : discoveryEntries) {
                    ProviderQos providerQos = discoveryEntry.getQos();
                    if (providerQos.getSupportsOnChangeSubscriptions()) {
                        discoveryEntriesSet.add(discoveryEntry);
                    }
                }
            } else {
                discoveryEntriesSet = Sets.newHashSet(discoveryEntries);
            }
            discoveryEntriesSet = discoveryEntryVersionFilter.filter(interfaceVersion,
                                                                     discoveryEntriesSet,
                                                                     discoveredVersions);
            if (discoveryEntriesSet.isEmpty()) {
                logger.debug(format("No discovery entries left after filtering while looking for %s in version %s.%nEntries found: %s",
                                    interfaceName,
                                    interfaceVersion,
                                    Arrays.toString(discoveryEntries)));
            }
            return discoveryEntriesSet;
        }
    }
}
