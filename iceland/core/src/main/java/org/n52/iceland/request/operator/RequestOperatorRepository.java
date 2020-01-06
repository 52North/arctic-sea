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
package org.n52.iceland.request.operator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.n52.iceland.util.activation.Activatables;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;
import org.n52.iceland.util.activation.ActivationSource;
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
public class RequestOperatorRepository
        extends AbstractComponentRepository<RequestOperatorKey, RequestOperator, RequestOperatorFactory>
        implements ActivationManager<RequestOperatorKey>,
                   ActivationSource<RequestOperatorKey>,
                   Constructable {

    private final Map<RequestOperatorKey, Producer<RequestOperator>> requestOperators = Maps.newHashMap();

    private final ActivationListeners<RequestOperatorKey> activation = new ActivationListeners<>(true);

    @Inject
    private Optional<Collection<RequestOperator>> components = Optional.of(Collections.emptyList());
    @Inject
    private Optional<Collection<RequestOperatorFactory>> componentFactories = Optional.of(Collections.emptyList());

    @Override
    public void init() {
        Map<RequestOperatorKey, Producer<RequestOperator>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.requestOperators.clear();
        for (Entry<RequestOperatorKey, Producer<RequestOperator>> entry : implementations.entrySet()) {
            if (entry.getValue().get().isSupported()) {
                this.requestOperators.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public RequestOperator getRequestOperator(RequestOperatorKey key) {
        if (isActive(key)) {
            Producer<RequestOperator> producer = this.requestOperators.get(key);
            return producer == null ? null : producer.get();
        } else {
            return null;
        }
    }

    public RequestOperator getRequestOperator(OwsServiceKey sok, String operationName) {
        return getRequestOperator(new RequestOperatorKey(sok, operationName));
    }

    public Set<RequestOperator> getRequestOperators() {
        return this.requestOperators.entrySet().stream()
                .filter(e -> this.activation.isActive(e.getKey()))
                .map(Entry::getValue)
                .map(Producer::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void setActive(RequestOperatorKey rokt, boolean active) {
        this.activation.setActive(rokt, active);
    }

    public Set<RequestOperator> getActiveRequestOperators(OwsServiceKey sok) {
        return activeRequestOperatorStream(sok)
                .map(Entry::getValue)
                .map(Producer::get)
                .collect(Collectors.toSet());
    }

    public Set<RequestOperatorKey> getActiveRequestOperatorKeys() {
        return Activatables.activatedKeys(this.requestOperators, this.activation);
    }

    public Set<RequestOperatorKey> getActiveRequestOperatorKeys(OwsServiceKey sok) {
        return activeRequestOperatorStream(sok)
                .map(Entry::getKey)
                .collect(Collectors.toSet());
    }

    private Stream<Entry<RequestOperatorKey, Producer<RequestOperator>>> activeRequestOperatorStream(
            OwsServiceKey sok) {
        return this.requestOperators.entrySet().stream()
                .filter(e -> activation.isActive(e.getKey()))
                .filter(e -> e.getKey().getServiceOperatorKey().equals(sok));
    }

    @Override
    public boolean isActive(RequestOperatorKey rok) {
        return this.activation.isActive(rok);
    }

    @Override
    public void registerListener(ActivationListener<RequestOperatorKey> listener) {
        this.activation.registerListener(listener);
    }

    @Override
    public void deregisterListener(ActivationListener<RequestOperatorKey> listener) {
        this.activation.deregisterListener(listener);
    }

    @Override
    public void activate(RequestOperatorKey key) {
        this.activation.activate(key);
    }

    @Override
    public void deactivate(RequestOperatorKey key) {
        this.activation.deactivate(key);
    }

    @Override
    public Set<RequestOperatorKey> getKeys() {
        return Collections.unmodifiableSet(this.requestOperators.keySet());
    }

}
