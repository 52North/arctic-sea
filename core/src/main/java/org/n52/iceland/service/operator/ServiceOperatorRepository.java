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
package org.n52.iceland.service.operator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.CollectionHelper;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.collections.MultiMaps;
import org.n52.iceland.util.collections.SetMultiMap;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ServiceOperatorRepository extends AbstractComponentRepository<ServiceOperatorKey, ServiceOperator, ServiceOperatorFactory> implements Constructable{
    @Deprecated
    private static ServiceOperatorRepository instance;
    /**
     * Implemented ServiceOperator
     */
    private final Map<ServiceOperatorKey, Producer<ServiceOperator>> serviceOperators = Maps.newHashMap();

    /** supported SOS versions */
    private final SetMultiMap<String, String> supportedVersions = MultiMaps.newSetMultiMap();


    @Autowired(required = false)
    private Collection<ServiceOperator> components;
    @Autowired(required = false)
    private Collection<ServiceOperatorFactory> componentFactories;

    @Override
    public void init() {
        ServiceOperatorRepository.instance = this;
        Map<ServiceOperatorKey, Producer<ServiceOperator>> implementations
                = getUniqueProviders(this.components, this.componentFactories);

        this.serviceOperators.clear();
        this.supportedVersions.clear();

        for (Entry<ServiceOperatorKey, Producer<ServiceOperator>> entry: implementations.entrySet()) {
            ServiceOperatorKey key = entry.getKey();
            Producer<ServiceOperator> producer = entry.getValue();
            this.serviceOperators.put(key, producer);
            this.supportedVersions.add(key.getService(), key.getVersion());
        }
    }

    /**
     * @return the implemented request listener
     */
    public Map<ServiceOperatorKey, ServiceOperator> getServiceOperators() {
        Map<ServiceOperatorKey, ServiceOperator> result = Maps.newHashMap();
        for (Entry<ServiceOperatorKey, Producer<ServiceOperator>> entrySet : this.serviceOperators.entrySet()) {
            ServiceOperatorKey key = entrySet.getKey();
            Producer<ServiceOperator> value = entrySet.getValue();
            result.put(key, value.get());
        }
        return result;
    }

    @Deprecated
    public Set<ServiceOperatorKey> getServiceOperatorKeyTypes() {
        return getServiceOperatorKeys();
    }

    public Set<ServiceOperatorKey> getServiceOperatorKeys() {
        return getServiceOperators().keySet();
    }

    public ServiceOperator getServiceOperator(ServiceOperatorKey sok) {
        Producer<ServiceOperator> producer = serviceOperators.get(sok);
        return producer == null ? null : producer.get();
    }

    /**
     * @param service
     *            the service
     * @param version
     *            the version
     * @return the implemented request listener
     *
     *
     * @throws OwsExceptionReport
     */
    public ServiceOperator getServiceOperator(final String service, final String version) throws OwsExceptionReport {
        return getServiceOperator(new ServiceOperatorKey(service, version));
    }

    public Set<String> getAllSupportedVersions() {
        return this.supportedVersions.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * @param service
     *            the service
     * @return the supportedVersions
     *
     */
    public Set<String> getSupportedVersions(String service) {
        return Collections.unmodifiableSet(this.supportedVersions.getOrDefault(service, Collections.emptySet()));
    }

    /**
     * @param service
     *            the service
     * @param version
     *            the version
     * @return the supportedVersions
     *
     */
    public boolean isVersionSupported(String service, String version) {
        return getSupportedVersions(service).contains(version);
    }

    /**
     * @return the supportedVersions
     */
    public Set<String> getSupportedServices() {
        return Collections.unmodifiableSet(this.supportedVersions.keySet());
    }

    public boolean isServiceSupported(String service) {
        return this.supportedVersions.containsKey(service);
    }

    @Deprecated
    public static ServiceOperatorRepository getInstance() {
        return ServiceOperatorRepository.instance;
    }

}
