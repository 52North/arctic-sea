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
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.config.SettingsManager;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.ds.OperationHandlerRepository;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.util.activation.Activatable;
import org.n52.iceland.util.Producer;
import org.n52.iceland.component.AbstractUniqueKeyComponentRepository;

import com.google.common.collect.Maps;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 4.0.0
 */
public class RequestOperatorRepository extends AbstractUniqueKeyComponentRepository<RequestOperatorKey, RequestOperator, RequestOperatorFactory> {
    private static final Logger LOG = LoggerFactory.getLogger(RequestOperatorRepository.class);

    @Deprecated
    private static RequestOperatorRepository instance;

    private final Map<RequestOperatorKey, Activatable<Producer<RequestOperator>>> requestOperators = Maps.newHashMap();

    @Inject
    private SettingsManager settingsManager;

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
        for (Entry<RequestOperatorKey, Producer<RequestOperator>> entry
             : implementations.entrySet()) {
            try {
                RequestOperatorKey key = entry.getKey();
                boolean active = this.settingsManager.isRequestOperatorActive(key);
                this.requestOperators.put(key, new Activatable<>(entry.getValue(), active));
            } catch (final ConnectionProviderException cpe) {
                throw new ConfigurationException("Error while checking RequestOperator", cpe);
            }
        }
    }

    @Override
    public void update() throws ConfigurationException {
        this.operationHandlerRepository.update();
        super.update();
    }

    public RequestOperator getRequestOperator(final RequestOperatorKey key) {
        final Activatable<Producer<RequestOperator>> a = requestOperators.get(key);
        return a == null ? null : a.get().get();
    }

    public RequestOperator getRequestOperator(final ServiceOperatorKey sok, final String operationName) {
        return getRequestOperator(new RequestOperatorKey(sok, operationName));
    }

    public void setActive(final RequestOperatorKey rokt, final boolean active) {
        if (requestOperators.get(rokt) != null) {
            requestOperators.get(rokt).setActive(active);
        }
    }

    public Set<RequestOperatorKey> getActiveRequestOperatorKeys() {
        return Activatable.filter(requestOperators).keySet();
    }

    public Set<RequestOperatorKey> getAllRequestOperatorKeys() {
        return Collections.unmodifiableSet(requestOperators.keySet());
    }

    public boolean isActive(RequestOperatorKey rok) {
        Activatable<Producer<RequestOperator>> ro = this.requestOperators.get(rok);
        return ro != null && ro.isActive();
    }

    @Deprecated
    public static RequestOperatorRepository getInstance() {
        return RequestOperatorRepository.instance;
    }
}
