/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.n52.janmayen.Producer;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.lifecycle.Constructable;
import org.n52.shetland.ogc.ows.service.OwsServiceKey;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ServiceOperatorRepository
        extends AbstractComponentRepository<OwsServiceKey, ServiceOperator, ServiceOperatorFactory>
        implements Constructable {

    private final Map<OwsServiceKey, Producer<ServiceOperator>> serviceOperators = Maps.newHashMap();
    private final Map<String, Set<String>> supportedVersions = Maps.newHashMap();

    @Inject
    private Optional<Collection<ServiceOperator>> components = Optional.of(Collections.emptyList());
    @Inject
    private Optional<Collection<ServiceOperatorFactory>> componentFactories = Optional.of(Collections.emptyList());

    @Override
    public void init() {
        Map<OwsServiceKey, Producer<ServiceOperator>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.serviceOperators.clear();
        this.supportedVersions.clear();
        this.serviceOperators.putAll(implementations);
        this.supportedVersions.putAll(implementations.keySet().stream()
                .collect(groupingBy(OwsServiceKey::getService, mapping(OwsServiceKey::getVersion, toSet()))));
    }

    /**
     * @return the implemented request listener
     */
    public Map<OwsServiceKey, ServiceOperator> getServiceOperators() {
        return this.serviceOperators.entrySet().stream().collect(toMap(Entry::getKey, e -> e.getValue().get()));
    }

    public Set<OwsServiceKey> getServiceOperatorKeys() {
        return getServiceOperators().keySet();
    }

    public ServiceOperator getServiceOperator(OwsServiceKey sok) {
        Producer<ServiceOperator> producer = serviceOperators.get(sok);
        return producer == null ? null : producer.get();
    }

    /**
     * @param service the service
     * @param version the version
     *
     * @return the implemented request listener
     *
     */
    public ServiceOperator getServiceOperator(final String service, final String version) {
        return getServiceOperator(new OwsServiceKey(service, version));
    }

    public Set<String> getAllSupportedVersions() {
        return this.supportedVersions.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

    /**
     * @param service the service
     *
     * @return the supportedVersions
     *
     */
    public Set<String> getSupportedVersions(String service) {
        return Collections.unmodifiableSet(this.supportedVersions.getOrDefault(service, Collections.emptySet()));
    }

    /**
     * @param service the service
     * @param version the version
     *
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

}
