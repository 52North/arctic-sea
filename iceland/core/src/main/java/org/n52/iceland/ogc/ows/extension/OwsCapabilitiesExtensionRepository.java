/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.janmayen.Producer;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.function.Suppliers;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;

/**
 * Repository for {@link OwsCapabilitiesExtension} implementations
 *
 * @since 1.0.0
 *
 */
public class OwsCapabilitiesExtensionRepository
        extends AbstractComponentRepository<OwsCapabilitiesExtensionKey,
                                            OwsCapabilitiesExtensionProvider,
                                            OwsCapabilitiesExtensionProviderFactory>
        implements Constructable {
    @Inject
    private RequestOperatorRepository requestOperatorRepository;

    /**
     * Implemented {@link OwsCapabilitiesExtensionProvider}
     */
    private final Map<OwsCapabilitiesExtensionKey, Set<Producer<OwsCapabilitiesExtensionProvider>>> providers
            = new HashMap<>();

    @Inject
    private Optional<Collection<OwsCapabilitiesExtensionProvider>> components = Optional.of(Collections.emptyList());
    @Inject
    private Optional<Collection<OwsCapabilitiesExtensionProviderFactory>> componentFactories =
            Optional.of(Collections.emptyList());

    @Override
    public void init() {
        Map<OwsCapabilitiesExtensionKey, Set<Producer<OwsCapabilitiesExtensionProvider>>> implementations
                = getProviders(this.components, this.componentFactories);
        this.providers.clear();
        implementations.forEach((key, values) -> {
            values.stream().filter(value -> {
                OwsCapabilitiesExtensionProvider provider = value.get();
                return !provider.hasRelatedOperation() ||
                       checkIfRelatedOperationIsActivated(key, provider.getRelatedOperation());
            }).forEach(value -> this.providers.computeIfAbsent(key, Suppliers.asFunction(HashSet::new)).add(value));

        });
    }

    public List<OwsCapabilitiesExtensionProvider> getCapabilitiesExtensionProvider(OwsCapabilitiesExtensionKey key) {
        return getAllValidCapabilitiesExtensionProvider(key, providers.get(key));
    }

    /**
     * Get the implemented {@link OwsCapabilitiesExtensionProvider} for service and version
     *
     * @param service Specific service
     * @param version Specific version
     *
     * @return the implemented Capabilities extension provider
     */
    public List<OwsCapabilitiesExtensionProvider> getCapabilitiesExtensionProvider(
            String service, String version) {
        return getCapabilitiesExtensionProvider(new OwsCapabilitiesExtensionKey(service, version));
    }

    /**
     * Get all valid {@link OwsCapabilitiesExtensionProvider}
     *
     * @param key  the key
     * @param list Loaded CapabilitiesExtensionProvider
     *
     * @return Valid CapabilitiesExtensionProvider
     */
    private List<OwsCapabilitiesExtensionProvider> getAllValidCapabilitiesExtensionProvider(
            OwsCapabilitiesExtensionKey key, Collection<Producer<OwsCapabilitiesExtensionProvider>> list) {
        return Optional.ofNullable(list).map(Collection::stream).orElseGet(Stream::empty)
                .map(Producer::get)
                .filter(provider -> !provider.hasRelatedOperation() ||
                                    checkIfRelatedOperationIsActivated(key, provider.getRelatedOperation()))
                .collect(toList());
    }

    /**
     * Check if the related operation for the loaded {@link OwsCapabilitiesExtensionProvider} is active
     *
     * @param key              the key
     * @param relatedOperation the operation
     *
     * @return <code>true</code>, if related operation is active
     */
    private boolean checkIfRelatedOperationIsActivated(
            OwsCapabilitiesExtensionKey key, String relatedOperation) {
        RequestOperatorKey rok = new RequestOperatorKey(key.getService(),
                                                        key.getVersion(),
                                                        relatedOperation);
        return this.requestOperatorRepository.isActive(rok);
    }

}
