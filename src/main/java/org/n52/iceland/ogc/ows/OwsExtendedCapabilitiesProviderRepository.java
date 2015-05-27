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
package org.n52.iceland.ogc.ows;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.component.AbstractUniqueKeyComponentRepository;
import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.service.AbstractServiceCommunicationObject;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.util.CollectionHelper;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.activation.Activatable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Repository for {@link OwsExtendedCapabilities}. Loads all implemented
 * {@link OwsExtendedCapabilitiesProvider} and adds to this repository.
 *
 * @since 4.0.0
 *
 */
public class OwsExtendedCapabilitiesProviderRepository
        extends AbstractUniqueKeyComponentRepository<OwsExtendedCapabilitiesProviderKey, OwsExtendedCapabilitiesProvider, OwsExtendedCapabilitiesProviderFactory> {
    @Deprecated
    private static OwsExtendedCapabilitiesProviderRepository instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(OwsExtendedCapabilitiesProviderRepository.class);

    private final Map<OwsExtendedCapabilitiesProviderKey, Activatable<Producer<OwsExtendedCapabilitiesProvider>>> extendedCapabilitiesProvider = new HashMap<>();

    @Inject
    private SettingsManager settingsManager;

    /**
     * For singleton use
     *
     * @return The single instance
     */
    @Deprecated
    public static OwsExtendedCapabilitiesProviderRepository getInstance() {
        return OwsExtendedCapabilitiesProviderRepository.instance;
    }

    /**
     * Load implemented {@link OwsExtendedCapabilities}.
     */
    public OwsExtendedCapabilitiesProviderRepository() {
        super(OwsExtendedCapabilitiesProvider.class, OwsExtendedCapabilitiesProviderFactory.class);
        OwsExtendedCapabilitiesProviderRepository.instance = this;
    }

    @Override
    protected void processImplementations(Map<OwsExtendedCapabilitiesProviderKey, Producer<OwsExtendedCapabilitiesProvider>> extendedCapabilitiesProviders) {
        this.extendedCapabilitiesProvider.clear();
        Set<ServiceOperatorKey> activeSokts = Sets.newHashSet();
        for (Entry<OwsExtendedCapabilitiesProviderKey, Producer<OwsExtendedCapabilitiesProvider>> entry: extendedCapabilitiesProviders.entrySet()) {
            try {
                OwsExtendedCapabilitiesProviderKey key = entry.getKey();
                Producer<OwsExtendedCapabilitiesProvider> value = entry.getValue();
                LOGGER.info("Registered OwsExtendedCapabilitiesProvider for {}", key);
                boolean isActive = this.settingsManager.isOwsExtendedCapabilitiesProviderActive(key);
                if (isActive) {
                    if (activeSokts.contains(key.getServiceOperatorKey())) {
                        this.settingsManager.setActive(key, false, false);
                    } else {
                        activeSokts.add(key.getServiceOperatorKey());
                    }
                }
                this.extendedCapabilitiesProvider.put(key, Activatable.from(value, isActive));
            } catch (final ConnectionProviderException ex) {
                throw new ConfigurationException("Could not check status of OwsExtendedCapabilitiesProvider", ex);
            }
        }
    }


    /**
     * Get map of all, active and inactive,
     * {@link OwsExtendedCapabilitiesProvider}s
     *
     * @return the map with all {@link OwsExtendedCapabilitiesProvider}s
     */
    public Map<OwsExtendedCapabilitiesProviderKey, OwsExtendedCapabilitiesProvider> getAllExtendedCapabilitiesProviders() {
        return produce(Activatable.unfiltered(extendedCapabilitiesProvider));
    }

    /**
     * Get map of all active {@link OwsExtendedCapabilitiesProvider}s
     *
     * @return the map with all active {@link OwsExtendedCapabilitiesProvider}s
     */
    public Map<OwsExtendedCapabilitiesProviderKey, OwsExtendedCapabilitiesProvider> getExtendedCapabilitiesProviders() {
        return produce(Activatable.filter(extendedCapabilitiesProvider));
    }

    /**
     * Get the loaded {@link OwsExtendedCapabilitiesProvider} implementation for
     * the specific service and version
     *
     * @param serviceCommunicationObject
     *            The {@link AbstractServiceCommunicationObject} with service
     *            and version
     * @return loaded {@link OwsExtendedCapabilitiesProvider} implementation
     */
    public OwsExtendedCapabilitiesProvider getExtendedCapabilitiesProvider(
            AbstractServiceCommunicationObject serviceCommunicationObject) {
        for (String name : getDomains()) {
            OwsExtendedCapabilitiesProvider provider =
                    getExtendedCapabilitiesProvider(new OwsExtendedCapabilitiesProviderKey(
                            serviceCommunicationObject.getService(), serviceCommunicationObject.getVersion(), name));
            if (provider != null) {
                return provider;
            }
        }
        return null;
    }

    /**
     * Get the loaded {@link OwsExtendedCapabilitiesProvider} implementation for
     * the specific {@link OwsExtendedCapabilitiesProviderKey}
     *
     * @param key
     *            The related {@link OwsExtendedCapabilitiesProviderKey}
     * @return loaded {@link OwsExtendedCapabilitiesProvider} implementation
     */
    public OwsExtendedCapabilitiesProvider getExtendedCapabilitiesProvider(OwsExtendedCapabilitiesProviderKey key) {
        return getExtendedCapabilitiesProviders().get(key);
//      final Activatable<OwsExtendedCapabilitiesProvider> provider = extendedCapabilitiesProvider.get(key);
//      return provider == null ? null : provider.get();
    }

    /**
     * Check if a {@link OwsExtendedCapabilitiesProvider} implementation is
     * loaded for the specific {@link AbstractServiceCommunicationObject}
     *
     * @param serviceCommunicationObject
     *            The {@link AbstractServiceCommunicationObject} with service
     *            and version
     * @return <code>true</code>, if a {@link OwsExtendedCapabilitiesProvider}
     *         implementation is loaded for the specific
     *         {@link AbstractServiceCommunicationObject}
     */
    public boolean hasExtendedCapabilitiesProvider(AbstractServiceCommunicationObject serviceCommunicationObject) {
        boolean hasProvider;
        for (String name : getDomains()) {
            hasProvider = hasExtendedCapabilitiesProvider(new OwsExtendedCapabilitiesProviderKey(
                            serviceCommunicationObject.getService(), serviceCommunicationObject.getVersion(), name));
            if (hasProvider) {
                return hasProvider;
            }
        }
        return false;
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
    public void setActive(OwsExtendedCapabilitiesProviderKey oeckt, boolean active) {
        if (getAllExtendedCapabilitiesProviders().containsKey(oeckt)) {
            if (active) {
                for (OwsExtendedCapabilitiesProviderKey key : getAllExtendedCapabilitiesProviders().keySet()) {
                    if (key.getService().equals(oeckt.getService()) && key.getVersion().equals(oeckt.getVersion())) {
                        extendedCapabilitiesProvider.get(key).setActive(false);
                    }
                }
            }
            extendedCapabilitiesProvider.get(oeckt).setActive(active);
        }
    }

    /**
     * Get map with {@link ServiceOperatorKey} and linked domain values
     *
     * @return the map with {@link ServiceOperatorKey} and linked domain values
     */
    public Map<ServiceOperatorKey, Collection<String>> getAllDomains() {
        Map<ServiceOperatorKey, Collection<String>> domains = Maps.newHashMap();
        for (OwsExtendedCapabilitiesProviderKey key : getAllExtendedCapabilitiesProviders().keySet()) {
            CollectionHelper.addToCollectionMap(key.getServiceOperatorKey(), key.getDomain(), domains);
        }
        return domains;
    }

    /**
     * Get all domain values from {@link OwsExtendedCapabilitiesProviderKey}
     *
     * @return the domain values
     */
    private Set<String> getDomains() {
        Set<String> domains = Sets.newHashSet();
        for (OwsExtendedCapabilitiesProviderKey key : getExtendedCapabilitiesProviders().keySet()) {
            domains.add(key.getDomain());
        }
        return domains;
    }

}
