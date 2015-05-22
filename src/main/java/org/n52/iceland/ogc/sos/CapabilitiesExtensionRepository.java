/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.sos;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.ogc.ows.OwsExceptionReport;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.iceland.util.Producer;
import org.n52.iceland.component.AbstractComponentRepository;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.SetMultimap;

/**
 * Repository for {@link CapabilitiesExtension} implementations
 *
 * @since 4.0.0
 *
 */
public class CapabilitiesExtensionRepository extends
        AbstractComponentRepository<CapabilitiesExtensionKey, CapabilitiesExtensionProvider, CapabilitiesExtensionProviderFactory> {
    @Deprecated
    private static CapabilitiesExtensionRepository instance;
    @Inject
    private RequestOperatorRepository requestOperatorRepository;

    /**
     * Implemented {@link CapabilitiesExtensionProvider}
     */
    private final ListMultimap<CapabilitiesExtensionKey, Producer<CapabilitiesExtensionProvider>> providers
            = LinkedListMultimap.create();

    @Override
    protected void processImplementations(
            SetMultimap<CapabilitiesExtensionKey, Producer<CapabilitiesExtensionProvider>> implementations) {
        this.providers.clear();
        for (Entry<CapabilitiesExtensionKey, Producer<CapabilitiesExtensionProvider>> entry
             : implementations.entries()) {
            CapabilitiesExtensionKey key = entry.getKey();
            Producer<CapabilitiesExtensionProvider> value = entry.getValue();
            CapabilitiesExtensionProvider provider = value.get();
            if (!provider.hasRelatedOperation() ||
                checkIfRelatedOperationIsActivated(key, provider
                                                   .getRelatedOperation())) {
                this.providers.put(key, value);
            }
        }
    }

    @Deprecated
    public static CapabilitiesExtensionRepository getInstance() {
        return CapabilitiesExtensionRepository.instance;
    }

    /**
     * Load implemented Capabilities extension provider
     *
     * @throws ConfigurationException
     *                                If no Capabilities extension provider is implemented
     */
    private CapabilitiesExtensionRepository()
            throws ConfigurationException {
        super(CapabilitiesExtensionProvider.class, CapabilitiesExtensionProviderFactory.class);
    }

    /**
     * Load the implemented Capabilities extension provider and add them to a
     * map with operation name as key
     *
     * @param implementations
     *                        the loaded implementations
     */
    protected void processImplementations(
            Map<CapabilitiesExtensionKey, Producer<CapabilitiesExtensionProvider>> implementations) {
        providers.clear();

    }

    public List<CapabilitiesExtensionProvider> getCapabilitiesExtensionProvider(
            CapabilitiesExtensionKey key)
            throws OwsExceptionReport {
        return getAllValidCapabilitiesExtensionProvider(key, providers.get(key));
    }

    /**
     * Get the implemented {@link CapabilitiesExtensionProvider} for service and
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
    public List<CapabilitiesExtensionProvider> getCapabilitiesExtensionProvider(
            String service, String version)
            throws OwsExceptionReport {
        return getCapabilitiesExtensionProvider(new CapabilitiesExtensionKey(service, version));
    }

    /**
     * Get all valid {@link CapabilitiesExtensionProvider}
     *
     * @param key  the key
     * @param list
     *             Loaded CapabilitiesExtensionProvider
     *
     * @return Valid CapabilitiesExtensionProvider
     */
    private List<CapabilitiesExtensionProvider> getAllValidCapabilitiesExtensionProvider(
            CapabilitiesExtensionKey key, List<Producer<CapabilitiesExtensionProvider>> list) {
        List<CapabilitiesExtensionProvider> activated = Lists.newLinkedList();
        for (CapabilitiesExtensionProvider provider : produce(list)) {
            if (!provider.hasRelatedOperation() ||
                checkIfRelatedOperationIsActivated(key, provider.getRelatedOperation())) {
                activated.add(provider);
            }
        }
        return activated;
    }

    /**
     * Check if the related operation for the loaded
     * {@link CapabilitiesExtensionProvider} is active
     *
     * @param key              the key
     * @param relatedOperation the operation
     *
     * @return <code>true</code>, if related operation is active
     */
    private boolean checkIfRelatedOperationIsActivated(
            CapabilitiesExtensionKey key, String relatedOperation) {
        RequestOperatorKey rok = new RequestOperatorKey(key.getService(),
                                                        key.getVersion(),
                                                        relatedOperation);
        return this.requestOperatorRepository.isActive(rok);
    }

}
