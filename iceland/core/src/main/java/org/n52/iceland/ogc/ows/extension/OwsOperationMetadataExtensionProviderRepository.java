/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

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

/**
 * Repository for {@link OwsOperationMetadataExtension}. Loads all implemented
 * {@link OwsOperationMetadataExtensionProvider} and adds to this repository.
 *
 * @since 1.0.0
 *
 */
public class OwsOperationMetadataExtensionProviderRepository
        extends AbstractComponentRepository<OwsOperationMetadataExtensionProviderKey,
                                            OwsOperationMetadataExtensionProvider,
                                            OwsOperationMetadataExtensionProviderFactory>
        implements ActivationManager<OwsOperationMetadataExtensionProviderKey>,
                   ActivationSource<OwsOperationMetadataExtensionProviderKey>,
                   Constructable {

    private final Map<OwsOperationMetadataExtensionProviderKey, Producer<OwsOperationMetadataExtensionProvider>>
            extendedCapabilitiesProvider = new HashMap<>();

    @Inject
    private Optional<Collection<OwsOperationMetadataExtensionProvider>> components =
            Optional.of(Collections.emptyList());

    @Inject
    private Optional<Collection<OwsOperationMetadataExtensionProviderFactory>> componentFactories =
            Optional.of(Collections.emptyList());

    private final ActivationListeners<OwsOperationMetadataExtensionProviderKey> activations
            = new ActivationListeners<>(true);

    @Override
    public void init() {
        Map<OwsOperationMetadataExtensionProviderKey, Producer<OwsOperationMetadataExtensionProvider>> implemtations
                = getUniqueProviders(this.components, this.componentFactories);
        this.extendedCapabilitiesProvider.clear();
        this.extendedCapabilitiesProvider.putAll(implemtations);
    }

    @Override
    public Set<OwsOperationMetadataExtensionProviderKey> getKeys() {
        return Collections.unmodifiableSet(this.extendedCapabilitiesProvider.keySet());
    }

    @Override
    public void registerListener(ActivationListener<OwsOperationMetadataExtensionProviderKey> listener) {
        this.activations.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<OwsOperationMetadataExtensionProviderKey> listener) {
        this.activations.deregisterListener(listener);
    }

    @Override
    public boolean isActive(OwsOperationMetadataExtensionProviderKey key) {
        return this.activations.isActive(key);
    }

    @Override
    public void activate(OwsOperationMetadataExtensionProviderKey key) {
        this.activations.activate(key);
    }

    @Override
    public void deactivate(OwsOperationMetadataExtensionProviderKey key) {
        this.activations.deactivate(key);
    }

    /**
     * Get map of all, active and inactive, {@link OwsOperationMetadataExtensionProvider}s
     *
     * @return the map with all {@link OwsOperationMetadataExtensionProvider}s
     */
    public Map<OwsOperationMetadataExtensionProviderKey, OwsOperationMetadataExtensionProvider>
            getAllExtendedCapabilitiesProviders() {
        return Producers.produce(this.extendedCapabilitiesProvider);
    }

    /**
     * Get map of all active {@link OwsOperationMetadataExtensionProvider}s
     *
     * @return the map with all active {@link OwsOperationMetadataExtensionProvider}s
     */
    public Map<OwsOperationMetadataExtensionProviderKey, OwsOperationMetadataExtensionProvider>
            getExtendedCapabilitiesProviders() {
        return Producers.produce(Activatables.activatedMap(this.extendedCapabilitiesProvider, this.activations));
    }

    /**
     * Get the loaded {@link OwsOperationMetadataExtensionProvider} implementation for the specific service and version
     *
     * @param serviceCommunicationObject The {@link OwsServiceCommunicationObject} with service and version
     *
     * @return loaded {@link OwsOperationMetadataExtensionProvider} implementation
     */
    public OwsOperationMetadataExtensionProvider getExtendedCapabilitiesProvider(
            OwsServiceCommunicationObject serviceCommunicationObject) {
        String service = serviceCommunicationObject.getService();
        String version = serviceCommunicationObject.getVersion();
        return getExtendedCapabilitiesProvider(service, version);
    }

    /**
     * Get the loaded {@link OwsOperationMetadataExtensionProvider} implementation for the specific service and version
     *
     * @param service the service
     * @param version the version
     *
     * @return loaded {@link OwsOperationMetadataExtensionProvider} implementation
     */
    public OwsOperationMetadataExtensionProvider getExtendedCapabilitiesProvider(String service, String version) {
        return getDomains().stream()
                .map(domain -> new OwsOperationMetadataExtensionProviderKey(service, version, domain))
                .map(this::getExtendedCapabilitiesProvider)
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    /**
     * Get the loaded {@link OwsOperationMetadataExtensionProvider} implementation for the specific
     * {@link OwsOperationMetadataExtensionProviderKey}
     *
     * @param key The related {@link OwsOperationMetadataExtensionProviderKey}
     *
     * @return loaded {@link OwsOperationMetadataExtensionProvider} implementation
     */
    public OwsOperationMetadataExtensionProvider getExtendedCapabilitiesProvider(
            OwsOperationMetadataExtensionProviderKey key) {
        return getExtendedCapabilitiesProviders().get(key);
    }

    /**
     * Check if a {@link OwsOperationMetadataExtensionProvider} implementation is loaded for the specific
     * {@link OwsServiceCommunicationObject}
     *
     * @param serviceCommunicationObject The {@link OwsServiceCommunicationObject} with service and version
     *
     * @return <code>true</code>, if a {@link OwsOperationMetadataExtensionProvider} implementation is loaded for the
     *         specific {@link OwsServiceCommunicationObject}
     */
    public boolean hasExtendedCapabilitiesProvider(OwsServiceCommunicationObject serviceCommunicationObject) {
        String service = serviceCommunicationObject.getService();
        String version = serviceCommunicationObject.getVersion();
        return hasExtendedCapabilitiesProvider(service, version);
    }

    /**
     * Check if a {@link OwsOperationMetadataExtensionProvider} implementation is loaded for the specific
     * {@code service} and {@code version}.
     *
     * @param service the service
     * @param version the version
     *
     * @return {@code true}, if a {@link OwsOperationMetadataExtensionProvider} implementation is loaded for the
     *         specific {@code service} and {@code version}
     */
    public boolean hasExtendedCapabilitiesProvider(String service, String version) {
        return getDomains().stream()
                .map(domain -> new OwsOperationMetadataExtensionProviderKey(service, version, domain))
                .anyMatch(this::hasExtendedCapabilitiesProvider);
    }

    /**
     * Check if a {@link OwsOperationMetadataExtensionProvider} implementation is loaded for the specific
     * {@link OwsOperationMetadataExtensionProviderKey}
     *
     * @param key The related {@link OwsOperationMetadataExtensionProviderKey} to check for
     *
     * @return <code>true</code>, if a {@link OwsOperationMetadataExtensionProvider} implementation is loaded for the
     *         specific {@link OwsOperationMetadataExtensionProviderKey}
     */
    public boolean hasExtendedCapabilitiesProvider(OwsOperationMetadataExtensionProviderKey key) {
        return getExtendedCapabilitiesProviders().containsKey(key);
    }

    /**
     * Change the status of the {@link OwsOperationMetadataExtensionProvider} which relates to the requested
     * {@link OwsOperationMetadataExtensionProviderKey}
     *
     * @param oeckt  the {@link OwsOperationMetadataExtensionProviderKey} to change the status for
     * @param active the new status
     */
    @Override
    public void setActive(OwsOperationMetadataExtensionProviderKey oeckt, boolean active) {
        if (this.extendedCapabilitiesProvider.containsKey(oeckt)) {
            if (!active) {
                this.activations.deactivate(oeckt);
            } else {
                this.extendedCapabilitiesProvider.keySet().stream()
                        .filter(key -> key.getService().equals(oeckt.getService()))
                        .filter(key -> key.getVersion().equals(oeckt.getVersion()))
                        .forEach(this.activations::deactivate);
                this.activations.activate(oeckt);
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
                .collect(groupingBy(OwsOperationMetadataExtensionProviderKey::getServiceOperatorKey,
                                    mapping(OwsOperationMetadataExtensionProviderKey::getDomain,
                                            toCollection(LinkedList::new))));
    }

    /**
     * Get all domain values from {@link OwsOperationMetadataExtensionProviderKey}
     *
     * @return the domain values
     */
    private Set<String> getDomains() {
        return Activatables.activatedKeys(extendedCapabilitiesProvider, activations)
                .stream()
                .map(OwsOperationMetadataExtensionProviderKey::getDomain)
                .collect(toSet());
    }

}
