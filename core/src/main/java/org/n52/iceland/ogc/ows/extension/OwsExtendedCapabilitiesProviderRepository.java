/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.ogc.ows.extension;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.n52.iceland.util.activation.Activatables;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;
import org.n52.iceland.util.activation.ActivationSource;
import org.n52.janmayen.Producer;
import org.n52.janmayen.Producers;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.shetland.ogc.ows.OwsOperationMetadataExtension;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.shetland.ogc.ows.service.OwsServiceKey;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Repository for {@link OwsOperationMetadataExtension}. Loads all implemented
 * {@link OwsExtendedCapabilitiesProvider} and adds to this repository.
 *
 * @since 1.0.0
 *
 */
public class OwsExtendedCapabilitiesProviderRepository
        extends AbstractComponentRepository<OwsExtendedCapabilitiesProviderKey, OwsExtendedCapabilitiesProvider, OwsExtendedCapabilitiesProviderFactory>
        implements ActivationManager<OwsExtendedCapabilitiesProviderKey>,
                   ActivationSource<OwsExtendedCapabilitiesProviderKey>,
                   Constructable {
    @Deprecated
    private static OwsExtendedCapabilitiesProviderRepository instance;

    private final Map<OwsExtendedCapabilitiesProviderKey, Producer<OwsExtendedCapabilitiesProvider>> extendedCapabilitiesProvider = new HashMap<>();

    @Autowired(required = false)
    private Collection<OwsExtendedCapabilitiesProvider> components;

    @Autowired(required = false)
    private Collection<OwsExtendedCapabilitiesProviderFactory> componentFactories;

    private final ActivationListeners<OwsExtendedCapabilitiesProviderKey> activations = new ActivationListeners<>(true);

    @Override
    public void init() {
        OwsExtendedCapabilitiesProviderRepository.instance = this;
        Map<OwsExtendedCapabilitiesProviderKey, Producer<OwsExtendedCapabilitiesProvider>> implemtations
                = getUniqueProviders(this.components, this.componentFactories);
        this.extendedCapabilitiesProvider.clear();
        this.extendedCapabilitiesProvider.putAll(implemtations);
    }

    @Override
    public Set<OwsExtendedCapabilitiesProviderKey> getKeys() {
        return Collections.unmodifiableSet(this.extendedCapabilitiesProvider.keySet());
    }

    @Override
    public void registerListener(ActivationListener<OwsExtendedCapabilitiesProviderKey> listener) {
        this.activations.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<OwsExtendedCapabilitiesProviderKey> listener) {
        this.activations.deregisterListener(listener);
    }

    @Override
    public boolean isActive(OwsExtendedCapabilitiesProviderKey key) {
        return this.activations.isActive(key);
    }

    @Override
    public void activate(OwsExtendedCapabilitiesProviderKey key) {
        this.activations.activate(key);
    }

    @Override
    public void deactivate(OwsExtendedCapabilitiesProviderKey key) {
        this.activations.deactivate(key);
    }

    /**
     * Get map of all, active and inactive,
     * {@link OwsExtendedCapabilitiesProvider}s
     *
     * @return the map with all {@link OwsExtendedCapabilitiesProvider}s
     */
    public Map<OwsExtendedCapabilitiesProviderKey, OwsExtendedCapabilitiesProvider> getAllExtendedCapabilitiesProviders() {
        return Producers.produce(this.extendedCapabilitiesProvider);
    }

    /**
     * Get map of all active {@link OwsExtendedCapabilitiesProvider}s
     *
     * @return the map with all active {@link OwsExtendedCapabilitiesProvider}s
     */
    public Map<OwsExtendedCapabilitiesProviderKey, OwsExtendedCapabilitiesProvider> getExtendedCapabilitiesProviders() {
        return Producers.produce(Activatables.activatedMap(this.extendedCapabilitiesProvider, this.activations));
    }

    /**
     * Get the loaded {@link OwsExtendedCapabilitiesProvider} implementation for
     * the specific service and version
     *
     * @param serviceCommunicationObject
     *            The {@link OwsServiceCommunicationObject} with service
     *            and version
     * @return loaded {@link OwsExtendedCapabilitiesProvider} implementation
     */
    public OwsExtendedCapabilitiesProvider getExtendedCapabilitiesProvider(OwsServiceCommunicationObject serviceCommunicationObject) {
        String service = serviceCommunicationObject.getService();
        String version = serviceCommunicationObject.getVersion();
        return getExtendedCapabilitiesProvider(service, version);
    }

    /**
     * Get the loaded {@link OwsExtendedCapabilitiesProvider} implementation for
     * the specific service and version
     *
     * @param service the service
     * @param version the version
     *
     * @return loaded {@link OwsExtendedCapabilitiesProvider} implementation
     */
    public OwsExtendedCapabilitiesProvider getExtendedCapabilitiesProvider(String service, String version) {
        return getDomains().stream()
                .map(domain -> new OwsExtendedCapabilitiesProviderKey(service, version, domain))
                .map(this::getExtendedCapabilitiesProvider)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    /**
     * Get the loaded {@link OwsExtendedCapabilitiesProvider} implementation for
     * the specific {@link OwsExtendedCapabilitiesProviderKey}
     *
     * @param key
     *            The related {@link OwsExtendedCapabilitiesProviderKey}
     * @return loaded {@link OwsExtendedCapabilitiesProvider} implementation
     */
    public OwsExtendedCapabilitiesProvider getExtendedCapabilitiesProvider(
            OwsExtendedCapabilitiesProviderKey key) {
        return getExtendedCapabilitiesProviders().get(key);
    }

    /**
     * Check if a {@link OwsExtendedCapabilitiesProvider} implementation is
     * loaded for the specific {@link OwsServiceCommunicationObject}
     *
     * @param serviceCommunicationObject
     *            The {@link OwsServiceCommunicationObject} with service
     *            and version
     * @return <code>true</code>, if a {@link OwsExtendedCapabilitiesProvider}
     *         implementation is loaded for the specific
     *         {@link OwsServiceCommunicationObject}
     */
    public boolean hasExtendedCapabilitiesProvider(OwsServiceCommunicationObject serviceCommunicationObject) {
        String service = serviceCommunicationObject.getService();
        String version = serviceCommunicationObject.getVersion();
        return hasExtendedCapabilitiesProvider(service, version);
    }

    /**
     * Check if a {@link OwsExtendedCapabilitiesProvider} implementation is
     * loaded for the specific {@code service} and {@code version}.
     *
     * @param service the service
     * @param version the version
     *
     * @return {@code true}, if a {@link OwsExtendedCapabilitiesProvider}
     *         implementation is loaded for the specific {@code service} and
     *         {@code version}
     */
    public boolean hasExtendedCapabilitiesProvider(String service, String version) {
        return getDomains().stream()
                .map(domain -> new OwsExtendedCapabilitiesProviderKey(service, version, domain))
                .anyMatch(this::hasExtendedCapabilitiesProvider);
    }

    /**
     * Check if a {@link OwsExtendedCapabilitiesProvider} implementation is
     * loaded for the specific {@link OwsExtendedCapabilitiesProviderKey}
     *
     * @param key
     *            The related {@link OwsExtendedCapabilitiesProviderKey} to check for
     * @return <code>true</code>, if a {@link OwsExtendedCapabilitiesProvider}
     *         implementation is loaded for the specific
     *         {@link OwsExtendedCapabilitiesProviderKey}
     */
    public boolean hasExtendedCapabilitiesProvider(OwsExtendedCapabilitiesProviderKey key) {
        return getExtendedCapabilitiesProviders().containsKey(key);
    }

    /**
     * Change the status of the {@link OwsExtendedCapabilitiesProvider} which
     * relates to the requested {@link OwsExtendedCapabilitiesProviderKey}
     *
     * @param oeckt
     *            the {@link OwsExtendedCapabilitiesProviderKey} to change the status
     *            for
     * @param active
     *            the new status
     */
    @Override
    public void setActive(OwsExtendedCapabilitiesProviderKey oeckt, boolean active) {
        if (this.extendedCapabilitiesProvider.containsKey(oeckt)) {
            this.activations.activate(oeckt);
            if (!active) {
                this.activations.deactivate(oeckt);
            } else {
                this.extendedCapabilitiesProvider.keySet().stream()
                        .filter(key -> key.getService().equals(oeckt.getService()))
                        .filter(key -> key.getVersion().equals(oeckt.getVersion()))
                        .forEach(this.activations::deactivate);
            }
        }
    }

    /**
     * Get map with {@link OwsServiceKey} and linked domain values
     *
     * @return the map with {@link OwsServiceKey} and linked domain values
     */
    public Map<OwsServiceKey, Collection<String>> getAllDomains() {
        return this.extendedCapabilitiesProvider.keySet().stream()
                .collect(groupingBy(OwsExtendedCapabilitiesProviderKey::getServiceOperatorKey,
                            mapping(OwsExtendedCapabilitiesProviderKey::getDomain, toCollection(LinkedList::new))));
    }

    /**
     * Get all domain values from {@link OwsExtendedCapabilitiesProviderKey}
     *
     * @return the domain values
     */
    private Set<String> getDomains() {
        return Activatables.activatedKeys(extendedCapabilitiesProvider, activations)
                .stream()
                .map(OwsExtendedCapabilitiesProviderKey::getDomain)
                .collect(toSet());
    }

    /**
     * For singleton use
     *
     * @return The single instance
     */
    @Deprecated
    public static OwsExtendedCapabilitiesProviderRepository getInstance() {
        return OwsExtendedCapabilitiesProviderRepository.instance;
    }

}
