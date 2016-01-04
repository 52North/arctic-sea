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
package org.n52.iceland.request.operator;

import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.util.activation.DefaultActive;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

/**
 * Key to identify a {@link RequestOperator}. The {@link RequestOperatorKey}
 * consists of service, version and operation name.
 *
 * @since 1.0.0
 */
public class RequestOperatorKey implements Comparable<RequestOperatorKey>, DefaultActive {
    private final ServiceOperatorKey sok;

    private final String operationName;

    private final boolean defaultActive;

    /**
     * Constructor
     *
     * @param sok
     * @param operationName
     */
    public RequestOperatorKey(ServiceOperatorKey sok, String operationName) {
        this(sok, operationName, true);
    }

    /**
     * Constructor
     *
     * @param sok
     * @param operationName
     * @param defaultActive
     */
    public RequestOperatorKey(ServiceOperatorKey sok, String operationName, boolean defaultActive) {
        this.sok = sok;
        this.operationName = operationName;
        this.defaultActive = defaultActive;
    }

    /**
     * Constructor
     *
     * @param service
     * @param version
     * @param operationName
     */
    public RequestOperatorKey(String service, String version, String operationName) {
        this(new ServiceOperatorKey(service, version), operationName, true);
    }

    /**
     * Constructor
     *
     * @param service
     * @param version
     * @param operationName
     * @param defaultActive
     */
    public RequestOperatorKey(String service, String version, String operationName, boolean defaultActive) {
        this(new ServiceOperatorKey(service, version), operationName, defaultActive);
    }

    /**
     * Constructor
     *
     * @param sok
     * @param operationName
     */
    public RequestOperatorKey(ServiceOperatorKey sok, Enum<?> operationName) {
        this(sok, operationName.name());
    }

    /**
     * Constructor
     *
     * @param sok
     * @param operationName
     * @param defaultActive
     */
    public RequestOperatorKey(ServiceOperatorKey sok, Enum<?> operationName, boolean defaultActive) {
        this(sok, operationName.name(), defaultActive);
    }

    /**
     * Constructor
     *
     * @param service
     * @param version
     * @param operationName
     */
    public RequestOperatorKey(String service, String version, Enum<?> operationName) {
        this(service, version, operationName.name());
    }

    /**
     * Constructor
     *
     * @param service
     * @param version
     * @param operationName
     * @param defaultActive
     */
    public RequestOperatorKey(String service, String version, Enum<?> operationName, boolean defaultActive) {
        this(service, version, operationName.name(), defaultActive);
    }

    /**
     * @return the {@link ServiceOperatorKey}
     */
    public ServiceOperatorKey getServiceOperatorKey() {
        return sok;
    }

    /**
     * @return The service name
     */
    public String getService() {
        return sok == null ? null : sok.getService();
    }

    /**
     * @return The service version
     */
    public String getVersion() {
        return sok == null ? null : sok.getVersion();
    }

    /**
     * @return The operation name
     */
    public String getOperationName() {
        return operationName;
    }

    @Override
    public boolean isDefaultActive() {
        return defaultActive;
    }

    @Override
    public int compareTo(RequestOperatorKey o) {
        Preconditions.checkNotNull(o);
        return ComparisonChain.start().compare(getServiceOperatorKey(), o.getServiceOperatorKey())
                .compare(getOperationName(), o.getOperationName()).result();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            RequestOperatorKey o = (RequestOperatorKey) obj;
            return Objects.equal(getServiceOperatorKey(), o.getServiceOperatorKey())
                    && Objects.equal(getOperationName(), o.getOperationName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getServiceOperatorKey(), getOperationName());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("serviceOperatorKey", getServiceOperatorKey())
                .add("operationName", getOperationName())
                .add("defaultActive", isDefaultActive())
                .toString();
    }
}
