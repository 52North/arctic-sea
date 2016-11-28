/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import org.n52.shetland.ogc.ows.exception.OperationNotSupportedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.request.operator.RequestOperator;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.iceland.response.AbstractServiceResponse;

import com.google.common.base.MoreObjects;

/**
 * Generic service operator implementation that just delegates to a matching
 * {@link RequestOperator}.
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public class GenericServiceOperator implements ServiceOperator {
    private RequestOperatorRepository requestOperatorRepository;
    private final ServiceOperatorKey key;
    private final String service;
    private final String version;

    public GenericServiceOperator(String service, String version) {
        this.service = Objects.requireNonNull(service);
        this.version = Objects.requireNonNull(version);
        this.key = new ServiceOperatorKey(service, version);
    }

    /**
     * Gets the {@code ServiceOperatorKey} for this service.
     *
     * @return the key
     */
    public ServiceOperatorKey getKey() {
        return this.key;
    }

    /**
     * Sets the {@code RequestOperatorRepository} to get matching
     * {@link RequestOperator}s.
     *
     * @param repo the repository
     */
    @Inject
    public void setRequestOperatorRepository(RequestOperatorRepository repo) {
        this.requestOperatorRepository = repo;
    }

    @Override
    public Set<ServiceOperatorKey> getKeys() {
        return Collections.singleton(this.key);
    }

    /**
     * {@inheritDoc}
     *
     * @throws OperationNotSupportedException if no matching
     *                                        {@link RequestOperator} could be
     *                                        found or if the operator returned
     *                                        a {@code null}-response.
     */
    @Override
    public AbstractServiceResponse receiveRequest(
            AbstractServiceRequest request)
            throws OwsExceptionReport {
        String operationName = request.getOperationName();
        RequestOperator operator = this.requestOperatorRepository
                .getRequestOperator(this.key, operationName);

        if (operator == null) {
            throw new OperationNotSupportedException(operationName);
        }

        AbstractServiceResponse response = operator.receiveRequest(request);

        if (response == null) {
            throw new OperationNotSupportedException(operationName);
        }

        return response;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GenericServiceOperator)) {
            return false;
        }
        GenericServiceOperator that = (GenericServiceOperator) obj;
        return Objects.equals(this.getKey(), that.getKey());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("service", this.service)
                .add("version", this.version)
                .toString();
    }

}
