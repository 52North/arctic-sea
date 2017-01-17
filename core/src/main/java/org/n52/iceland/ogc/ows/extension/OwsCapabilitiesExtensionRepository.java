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

import static org.n52.janmayen.Producers.produce;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.janmayen.Producer;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;

/**
 * Repository for {@link OwsCapabilitiesExtension} implementations
 *
 * @since 1.0.0
 *
 */
public class OwsCapabilitiesExtensionRepository extends
        AbstractComponentRepository<OwsCapabilitiesExtensionKey, OwsCapabilitiesExtensionProvider, OwsCapabilitiesExtensionProviderFactory> implements Constructable{
    @Deprecated
    private static OwsCapabilitiesExtensionRepository instance;
    @Inject
    private RequestOperatorRepository requestOperatorRepository;

    /**
     * Implemented {@link OwsCapabilitiesExtensionProvider}
     */
    private final ListMultimap<OwsCapabilitiesExtensionKey, Producer<OwsCapabilitiesExtensionProvider>> providers
            = LinkedListMultimap.create();

    @Autowired(required = false)
    private Collection<OwsCapabilitiesExtensionProvider> components;
    @Autowired(required = false)
    private Collection<OwsCapabilitiesExtensionProviderFactory> componentFactories;

    @Override
    public void init() {
        OwsCapabilitiesExtensionRepository.instance = this;

        SetMultimap<OwsCapabilitiesExtensionKey, Producer<OwsCapabilitiesExtensionProvider>> implementations
                = getProviders(this.components, this.componentFactories);
        this.providers.clear();
        for (Entry<OwsCapabilitiesExtensionKey, Producer<OwsCapabilitiesExtensionProvider>> entry: implementations.entries()) {
            OwsCapabilitiesExtensionKey key = entry.getKey();
            Producer<OwsCapabilitiesExtensionProvider> value = entry.getValue();
            OwsCapabilitiesExtensionProvider provider = value.get();
            if (!provider.hasRelatedOperation() ||
                checkIfRelatedOperationIsActivated(key, provider
                                                   .getRelatedOperation())) {
                this.providers.put(key, value);
            }
        }
    }

    public List<OwsCapabilitiesExtensionProvider> getCapabilitiesExtensionProvider(OwsCapabilitiesExtensionKey key) throws OwsExceptionReport {
        return getAllValidCapabilitiesExtensionProvider(key, providers.get(key));
    }

    /**
     * Get the implemented {@link OwsCapabilitiesExtensionProvider} for service and
     * version
     *
     * @param service
     *                Specific service
     * @param version
     *                Specific version
     *
     * @return the implemented Capabilities extension provider
     *
     * @throws OwsExceptionReport
     */
    public List<OwsCapabilitiesExtensionProvider> getCapabilitiesExtensionProvider(
            String service, String version)
            throws OwsExceptionReport {
        return getCapabilitiesExtensionProvider(new OwsCapabilitiesExtensionKey(service, version));
    }

    /**
     * Get all valid {@link OwsCapabilitiesExtensionProvider}
     *
     * @param key  the key
     * @param list
     *             Loaded CapabilitiesExtensionProvider
     *
     * @return Valid CapabilitiesExtensionProvider
     */
    private List<OwsCapabilitiesExtensionProvider> getAllValidCapabilitiesExtensionProvider(
            OwsCapabilitiesExtensionKey key, List<Producer<OwsCapabilitiesExtensionProvider>> list) {
        List<OwsCapabilitiesExtensionProvider> activated = Lists.newLinkedList();
        for (OwsCapabilitiesExtensionProvider provider : produce(list)) {
            if (!provider.hasRelatedOperation() ||
                checkIfRelatedOperationIsActivated(key, provider.getRelatedOperation())) {
                activated.add(provider);
            }
        }
        return activated;
    }

    /**
     * Check if the related operation for the loaded
     * {@link OwsCapabilitiesExtensionProvider} is active
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

    @Deprecated
    public static OwsCapabilitiesExtensionRepository getInstance(
    ) {
        return OwsCapabilitiesExtensionRepository.instance;
    }

}
