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
package org.n52.iceland.ds;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.n52.iceland.component.AbstractComponentRepository;
import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.util.Producer;

import com.google.common.collect.Maps;

/**
 * In 52N SOS version 4.x called OperationDAORepository
 *
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 1.0.0
 */
public class OperationHandlerRepository extends AbstractComponentRepository<OperationHandlerKey, OperationHandler, OperationHandlerFactory> implements Constructable {
    @Deprecated
    private static OperationHandlerRepository instance;
    private static String datasourceDaoIdentficator;
    private final Map<OperationHandlerKey, Producer<OperationHandler>> operationHandlers = new HashMap<>();

    @Inject
    private Collection<OperationHandler> components;

    @Inject
    private Collection<OperationHandlerFactory> componentFactories;

    @Override
    public void init() {
        OperationHandlerRepository.instance = this;
        Map<OperationHandlerKey, Producer<OperationHandler>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        operationHandlers.clear();
        for (Entry<OperationHandlerKey, Producer<OperationHandler>> entry : implementations.entrySet()) {
            if (checkDatasourceDaoIdentifications(entry.getValue().get())) {
                operationHandlers.put(entry.getKey(), entry.getValue());
            }
        }
    }

    protected boolean checkDatasourceDaoIdentifications(DatasourceDaoIdentifier datasourceDaoIdentifier) {
        String id = datasourceDaoIdentifier.getDatasourceDaoIdentifier();
        return datasourceDaoIdentficator.equalsIgnoreCase(id) ||
               DatasourceDaoIdentifier.IDEPENDET_IDENTIFIER.equals(id);
    }

    public Map<OperationHandlerKey, OperationHandler> getOperationHandlers() {
        Map<OperationHandlerKey, OperationHandler> handlers = Maps.newHashMapWithExpectedSize(this.operationHandlers.size());
        for (Entry<OperationHandlerKey, Producer<OperationHandler>> entry : this.operationHandlers.entrySet()) {
            OperationHandlerKey key = entry.getKey();
            Producer<OperationHandler> value = entry.getValue();
            handlers.put(key, value.get());

        }
        return handlers;
    }

    public OperationHandler getOperationHandler(String service, String operationName) {
        return getOperationHandler(new OperationHandlerKey(service, operationName));
    }

    public OperationHandler getOperationHandler(OperationHandlerKey key) {
        Producer<OperationHandler> handler = operationHandlers.get(key);
        return handler == null ? null : handler.get();
    }

    @Deprecated
    public OperationHandler getOperationDAO(OperationHandlerKey key) {
        return getOperationHandler(key);
    }

    @Deprecated
    public Map<OperationHandlerKey, OperationHandler> getOperationDAOs() {
        return getOperationHandlers();
    }

    @Deprecated
    public OperationHandler getOperationDAO(String service, String operationName) {
        return getOperationHandler(service, operationName);
    }

    @Deprecated
    public static OperationHandlerRepository getInstance() {
        return OperationHandlerRepository.instance;
    }
}
