/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.convert.RequestResponseModifier;
import org.n52.iceland.convert.RequestResponseModifierRepository;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.RequestEvent;
import org.n52.iceland.event.events.ResponseEvent;
import org.n52.iceland.request.handler.GenericOperationHandler;
import org.n52.iceland.request.handler.OperationHandlerRepository;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.shetland.ogc.ows.OwsOperation;
import org.n52.shetland.ogc.ows.exception.OperationNotSupportedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

import com.google.common.base.MoreObjects;

public class GenericRequestOperator<
            Q extends OwsServiceRequest,
            A extends OwsServiceResponse>
        implements RequestOperator {

    private static final Logger LOG = LoggerFactory
            .getLogger(GenericRequestOperator.class);

    private ParameterValidator<Q> validator;
    private RequestOperatorKey requestOperatorKey;
    private Class<Q> requestType;
    private OperationHandlerRepository operationHandlerRepository;
    private RequestResponseModifierRepository modifierRepository;
    private ServiceOperatorRepository serviceOperatorRepository;
    private ServiceEventBus serviceEventBus;

    public GenericRequestOperator(String service,
                                 String version,
                                 String operation,
                                 Class<Q> requestType,
                                 ParameterValidator<Q> validator) {
        this(new OperationKey(service, version, operation), true, requestType, validator);
    }

    public GenericRequestOperator(OperationKey operation,
                                 Class<Q> requestType,
                                 ParameterValidator<Q> validator) {
        this(operation, true, requestType, validator);
    }

    public GenericRequestOperator(OperationKey operation,
                                   boolean defaultActive,
                                   Class<Q> requestType,
                                   ParameterValidator<Q> validator) {
        this.requestOperatorKey = new RequestOperatorKey(operation.getService(), operation.getVersion(), operation.getOperation(), defaultActive);
        this.requestType = Objects.requireNonNull(requestType, "requestType");
        this.validator = Objects.requireNonNull(validator, "checker");
        LOG.info("{} initialized successfully for {}!", getClass().getSimpleName(), this.requestOperatorKey);
    }

    @Inject
    public void setOperationHandlerRepository(OperationHandlerRepository repo) {
        this.operationHandlerRepository = repo;
    }

    public OperationHandlerRepository getOperationHandlerRepository() {
        return operationHandlerRepository;
    }

    @Inject
    public void setRequestResponseModifierRepository(
            RequestResponseModifierRepository repo) {
        this.modifierRepository = repo;
    }

    public RequestResponseModifierRepository getRequestResponseModifierRepository() {
        return modifierRepository;
    }

    @Inject
    public void setServiceOperatorRepository(ServiceOperatorRepository repo) {
        this.serviceOperatorRepository = repo;
    }

    public ServiceOperatorRepository getServiceOperatorRepository() {
        return serviceOperatorRepository;
    }

    @Inject
    public void setServiceEventBus(ServiceEventBus serviceEventBus) {
        this.serviceEventBus = serviceEventBus;
    }

    public ServiceEventBus getServiceEventBus() {
        return serviceEventBus;
    }

    private void checkForModifierAndProcess(OwsServiceRequest request)
            throws OwsExceptionReport {
        if (!this.modifierRepository.hasRequestResponseModifier(request)) {
            return;
        }
        List<RequestResponseModifier> splitter = new LinkedList<>();
        List<RequestResponseModifier> remover = new LinkedList<>();
        List<RequestResponseModifier> defaultModifier = new LinkedList<>();
        this.modifierRepository
                .getRequestResponseModifier(request).stream()
                .forEach(modifier -> {
                    if (modifier.getFacilitator().isSplitter()) {
                        splitter.add(modifier);
                    } else if (modifier.getFacilitator().isAdderRemover()) {
                        remover.add(modifier);
                    } else {
                        defaultModifier.add(modifier);
                    }
                });
        // execute adder/remover
        for (RequestResponseModifier modifier : remover) {
            modifier.modifyRequest(request);
        }
        // execute default
        for (RequestResponseModifier modifier : defaultModifier) {
            modifier.modifyRequest(request);
        }
        // execute splitter
        for (RequestResponseModifier modifier : splitter) {
            modifier.modifyRequest(request);
        }
    }

    private void checkForModifierAndProcess(
            OwsServiceRequest request,
            OwsServiceResponse response)
            throws OwsExceptionReport {
        if (!this.modifierRepository.hasRequestResponseModifier(request, response)) {
            return;
        }
        List<RequestResponseModifier> defaultModifier = new LinkedList<>();
        List<RequestResponseModifier> remover = new LinkedList<>();
        List<RequestResponseModifier> merger = new LinkedList<>();
        this.modifierRepository
                .getRequestResponseModifier(request, response).stream()
                .forEach((modifier) -> {
                    if (modifier.getFacilitator().isMerger()) {
                        merger.add(modifier);
                    } else if (modifier.getFacilitator().isAdderRemover()) {
                        remover.add(modifier);
                    } else {
                        defaultModifier.add(modifier);
                    }
                });

        // execute merger
        for (RequestResponseModifier modifier : merger) {
            modifier.modifyResponse(request, response);
        }
        // execute default
        for (RequestResponseModifier modifier : defaultModifier) {
            modifier.modifyResponse(request, response);
        }
        // execute adder/remover
        for (RequestResponseModifier modifier : remover) {
            modifier.modifyResponse(request, response);
        }
    }

    @Override
    public OwsServiceResponse receiveRequest(
            final OwsServiceRequest abstractRequest)
            throws OwsExceptionReport {
        this.serviceEventBus.submit(new RequestEvent(abstractRequest));
        if (requestType.isAssignableFrom(abstractRequest.getClass())) {
            Q request = requestType.cast(abstractRequest);
            checkForModifierAndProcess(request);
            this.validator.validate(request);
            A response = receive(request);
            this.serviceEventBus.submit(new ResponseEvent(response));
            checkForModifierAndProcess(request, response);
            return response;
        } else {
            throw new OperationNotSupportedException(abstractRequest
                    .getOperationName());
        }
    }

    protected GenericOperationHandler<Q, A> getOperationHandler() {
        return getOptionalOperationHandler().orElseThrow(()
                -> new NullPointerException(String
                        .format("OperationHandler for Operation %s has no implementation!",
                                requestOperatorKey.getOperationName())));
    }

    protected Optional<GenericOperationHandler<Q, A>> getOptionalOperationHandler() {
        String service = this.requestOperatorKey.getService();
        String operationName = this.requestOperatorKey.getOperationName();
        return getOptionalOperationHandler(service, operationName);
    }

    @SuppressWarnings("unchecked")
    protected Optional<GenericOperationHandler<Q, A>> getOptionalOperationHandler(
            String service, String operationName) {
        return Optional.ofNullable(this.operationHandlerRepository
                .getOperationHandler(service, operationName))
                .map(x -> (GenericOperationHandler<Q, A>) x);
    }

    @Override
    public OwsOperation getOperationMetadata(String service, String version)
            throws OwsExceptionReport {
        Optional<GenericOperationHandler<Q, A>> optionalOperationHandler
                = getOptionalOperationHandler();
        if (optionalOperationHandler.isPresent()) {
            return optionalOperationHandler.get()
                    .getOperationsMetadata(service, version);
        } else {
            return null;
        }
    }

    protected String getOperationName() {
        return this.requestOperatorKey.getOperationName();
    }

    @Override
    public Set<RequestOperatorKey> getKeys() {
        return Collections.singleton(requestOperatorKey);
    }

    protected A receive(Q request)
            throws OwsExceptionReport {
        return getOperationHandler().handle(request);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", this.requestOperatorKey)
                .toString();
    }

}
