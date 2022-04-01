/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.request.handler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.n52.janmayen.Producer;
import org.n52.janmayen.Producers;
import org.n52.janmayen.component.AbstractComponentRepository;
import org.n52.janmayen.lifecycle.Constructable;

/**
 * In 52N SOS version 4.x called OperationDAORepository
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class OperationHandlerRepository
        extends AbstractComponentRepository<OperationHandlerKey, OperationHandler, OperationHandlerFactory>
        implements Constructable {
    private final Map<OperationHandlerKey, Producer<OperationHandler>> operationHandlers = new HashMap<>();
    private Optional<Collection<OperationHandler>> components = Optional.of(Collections.emptyList());
    private Optional<Collection<OperationHandlerFactory>> componentFactories = Optional.of(Collections.emptyList());

    @Inject
    public void setComponents(Optional<Collection<OperationHandler>> components) {
        this.components = components;
    }

    @Inject
    public void setComponentFactories(Optional<Collection<OperationHandlerFactory>> componentFactories) {
        this.componentFactories = componentFactories;
    }

    @Override
    public void init() {
        Map<OperationHandlerKey, Producer<OperationHandler>> implementations
                = getUniqueProviders(this.components, this.componentFactories);
        this.operationHandlers.clear();
        this.operationHandlers.putAll(implementations);
    }

    public Map<OperationHandlerKey, OperationHandler> getOperationHandlers() {
        return Producers.produce(this.operationHandlers);
    }

    public OperationHandler getOperationHandler(String service, String operationName) {
        return getOperationHandler(new OperationHandlerKey(service, operationName));
    }

    public OperationHandler getOperationHandler(OperationHandlerKey key) {
        return Producers.produce(operationHandlers.get(key));
    }

}
