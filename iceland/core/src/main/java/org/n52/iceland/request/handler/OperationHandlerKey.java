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

import java.util.Objects;

import com.google.common.collect.ComparisonChain;

/**
 * In 52N SOS version 4.x called OperationDAOKeyType
 *
 * @since 1.0.0
 *
 */
public class OperationHandlerKey implements Comparable<OperationHandlerKey> {

    private String operationName;

    private String service;

    public OperationHandlerKey() {
        this(null, (String) null);
    }

    public OperationHandlerKey(String service, String operationName) {
        this.service = service;
        this.operationName = operationName;
    }

    public OperationHandlerKey(String service, Enum<?> operationName) {
        this(service, operationName.name());
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Override
    public int compareTo(OperationHandlerKey o) {
        return ComparisonChain.start()
                .compare(getService(), o.getService())
                .compare(getOperationName(), o.getOperationName())
                .result();
    }

    @Override
    public boolean equals(Object paramObject) {
        if (service != null && operationName != null && paramObject instanceof OperationHandlerKey) {
            OperationHandlerKey toCheck = (OperationHandlerKey) paramObject;
            return Objects.equals(getService(), toCheck.getService()) &&
                   Objects.equals(getOperationName(), toCheck.getOperationName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getService(), getOperationName());
    }

}
