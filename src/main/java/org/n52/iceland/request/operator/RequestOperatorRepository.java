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
package org.n52.iceland.request.operator;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.component.AbstractUniqueKeyComponentRepository;
import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.ds.OperationHandlerRepository;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.util.Producer;
import org.n52.iceland.util.activation.Activatables;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationListeners;
import org.n52.iceland.util.activation.ActivationManager;
import org.n52.iceland.util.activation.ActivationSource;

import com.google.common.collect.Maps;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 4.0.0
 */
public class RequestOperatorRepository extends AbstractUniqueKeyComponentRepository<RequestOperatorKey, RequestOperator, RequestOperatorFactory>
        implements ActivationManager<RequestOperatorKey>,
                   ActivationSource<RequestOperatorKey>{
    @Deprecated
    private static RequestOperatorRepository instance;

    private final Map<RequestOperatorKey, Producer<RequestOperator>> requestOperators = Maps.newHashMap();

    private final ActivationListeners<RequestOperatorKey> activation = new ActivationListeners<>(true);

    @Inject
    private OperationHandlerRepository operationHandlerRepository;

    /**
     * private constructor for singleton
     *
     * @throws ConfigurationException
     */
    private RequestOperatorRepository() {
        super(RequestOperator.class, RequestOperatorFactory.class);
        RequestOperatorRepository.instance = this;
    }

    @Override
    protected void processImplementations(Map<RequestOperatorKey, Producer<RequestOperator>> implementations) {
        this.requestOperators.clear();
        this.requestOperators.putAll(implementations);
    }

    @Override
    public void update() throws ConfigurationException {
        this.operationHandlerRepository.update();
        super.update();
    }

    public RequestOperator getRequestOperator(RequestOperatorKey key) {
        if (isActive(key)) {
            Producer<RequestOperator> producer = this.requestOperators.get(key);
            return producer == null ? null : producer.get();
        } else {
            return null;
        }
    }

    public RequestOperator getRequestOperator(ServiceOperatorKey sok, String operationName) {
        return getRequestOperator(new RequestOperatorKey(sok, operationName));
    }

    @Override
    public void setActive(RequestOperatorKey rokt, boolean active) {
        this.activation.setActive(rokt, active);
    }

    public Set<RequestOperatorKey> getActiveRequestOperatorKeys() {
        return Activatables.activatedKeys(this.requestOperators, this.activation);
    }

    @Deprecated
    public Set<RequestOperatorKey> getAllRequestOperatorKeys() {
        return getKeys();
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

    @Deprecated
    public static RequestOperatorRepository getInstance() {
        return RequestOperatorRepository.instance;
    }
}
