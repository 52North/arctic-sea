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
package org.n52.iceland.ogc.swes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.service.AbstractServiceCommunicationObject;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.util.CollectionHelper;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.Producers;
import org.n52.iceland.util.activation.Activatables;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;
import org.n52.iceland.util.activation.ActivationSource;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Repository for {@link OfferingExtensionProvider} implementations
 *
 * @since 4.1.0
 *
 */
public class OfferingExtensionRepository extends AbstractComponentRepository<OfferingExtensionKey, OfferingExtensionProvider, OfferingExtensionProviderFactory>
        implements ActivationManager<OfferingExtensionKey>,
                   ActivationSource<OfferingExtensionKey>,
                   Constructable {
    @Deprecated
    private static OfferingExtensionRepository instance;
    private final Map<OfferingExtensionKey, Producer<OfferingExtensionProvider>> offeringExtensionProviders = new HashMap<>(0);
    private final ActivationListeners<OfferingExtensionKey> activation = new ActivationListeners<>(true);

    @Autowired(required = false)
    private Collection<OfferingExtensionProvider> components;
    @Autowired(required = false)
    private Collection<OfferingExtensionProviderFactory> componentFactories;

    @Override
    public void init() {
        OfferingExtensionRepository.instance = this;
        Map<OfferingExtensionKey, Producer<OfferingExtensionProvider>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.offeringExtensionProviders.clear();
        this.offeringExtensionProviders.putAll(implementations);
    }

    @Override
    public void registerListener(ActivationListener<OfferingExtensionKey> listener) {
        this.activation.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<OfferingExtensionKey> listener) {
        this.activation.deregisterListener(listener);
    }

    @Override
    public boolean isActive(OfferingExtensionKey key) {
        return this.activation.isActive(key);
    }

    @Override
    public void activate(OfferingExtensionKey key) {
        this.activation.activate(key);
    }

    @Override
    public void deactivate(OfferingExtensionKey key) {
        this.activation.deactivate(key);
    }

    @Override
    public Set<OfferingExtensionKey> getKeys() {
        return Collections.unmodifiableSet(this.offeringExtensionProviders.keySet());
    }

    /**
     * For singleton use
     *
     * @return The single instance
     */
    @Deprecated
    public static OfferingExtensionRepository getInstance() {
        return OfferingExtensionRepository.instance;
    }

    /**
     * Get map of all, active and inactive, {@link OfferingExtensionProvider}s
     *
     * @return the map with all {@link OfferingExtensionProvider}s
     */
    public Map<OfferingExtensionKey, OfferingExtensionProvider> getAllOfferingExtensionProviders() {
        return Producers.produce(this.offeringExtensionProviders);
    }

    /**
     * Get map of all active {@link OfferingExtensionProvider}s
     *
     * @return the map with all active {@link OfferingExtensionProvider}s
     */
    public Map<OfferingExtensionKey, OfferingExtensionProvider> getOfferingExtensionProviders() {
        return Producers.produce(Activatables.activatedMap(offeringExtensionProviders, this.activation));
    }

    /**
     * Get the loaded {@link OfferingExtensionProvider} implementation for the
     * specific service and version
     *
     * @param message
     *            The {@link AbstractServiceCommunicationObject} with service
     *            and version
     * @return loaded {@link OfferingExtensionProvider} implementation
     */
    public Set<OfferingExtensionProvider> getOfferingExtensionProvider(
            AbstractServiceCommunicationObject message) {
        Set<OfferingExtensionProvider> providers = Sets.newHashSet();
        for (String name : getDomains()) {
            OfferingExtensionKey key = new OfferingExtensionKey(message.getService(), message.getVersion(), name);
            OfferingExtensionProvider provider = getOfferingExtensionProvider(key);
            if (provider != null) {
                providers.add(provider);
            }
        }
        return providers;
    }

    /**
     * Get the loaded {@link OfferingExtensionProvider} implementation for the
     * specific {@link OfferingExtensionKey}
     *
     * @param key
     *            The related {@link OfferingExtensionKey}
     * @return loaded {@link OfferingExtensionProvider} implementation
     */
    public OfferingExtensionProvider getOfferingExtensionProvider(OfferingExtensionKey key) {
        return getOfferingExtensionProviders().get(key);
    }

    /**
     * Check if a {@link OfferingExtensionProvider} implementation is loaded for
     * the specific {@link OfferingExtensionKey}
     *
     * @param key
     *            The related {@link OfferingExtensionKey} to check for
     * @return <code>true</code>, if a {@link OfferingExtensionProvider}
     *         implementation is loaded for the specific service
     */
    public boolean hasOfferingExtensionProviderFor(OfferingExtensionKey key) {
        return getOfferingExtensionProviders().containsKey(key);
    }

    /**
     * Check if a provider is available for the requested service and version
     *
     * @param message
     *            request object with service and version
     * @return <code>true</code>, if a {@link OfferingExtensionProvider} is
     *         available
     */
    public boolean hasOfferingExtensionProviderFor(AbstractServiceCommunicationObject message) {
        boolean hasProvider;
        for (String name : getDomains()) {
            OfferingExtensionKey key
                    = new OfferingExtensionKey(message.getService(), message.getVersion(), name);
            hasProvider = hasOfferingExtensionProviderFor(key);
            if (hasProvider) {
                return activation.isActive(key);
            }
        }
        return false;
    }

    /**
     * Change the status of the {@link OfferingExtensionProvider} which relates
     * to the requested {@link OfferingExtensionKey}
     *
     * @param oekt
     *            the {@link OfferingExtensionKey} to change the status for
     * @param active
     *            the new status
     */
    @Override
    public void setActive(final OfferingExtensionKey oekt, final boolean active) {
        this.activation.isActive(oekt);
    }

    /**
     * Get map with {@link ServiceOperatorKey} and linked domain values
     *
     * @return the map with {@link ServiceOperatorKey} and linked domain values
     */
    public Map<ServiceOperatorKey, Collection<String>> getAllDomains() {
        Map<ServiceOperatorKey, Collection<String>> domains = Maps.newHashMap();
        Set<OfferingExtensionKey> keys = Activatables.activatedKeys(this.offeringExtensionProviders, this.activation);
        for (OfferingExtensionKey key : keys) {
            CollectionHelper.addToCollectionMap(key.getServiceOperatorKey(), key.getDomain(), domains);
        }
        return domains;
    }

    /**
     * Get all domain values from {@link OfferingExtensionKey}
     *
     * @return the domain values
     */
    private Set<String> getDomains() {
        Set<OfferingExtensionKey> keys = Activatables.activatedKeys(this.offeringExtensionProviders, this.activation);
        Set<String> domains = Sets.newHashSet();
        for (OfferingExtensionKey key : keys) {
            domains.add(key.getDomain());
        }
        return domains;
    }
}
