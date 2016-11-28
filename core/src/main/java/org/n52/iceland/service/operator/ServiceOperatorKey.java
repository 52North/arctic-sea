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

import org.n52.janmayen.Comparables;

import com.google.common.base.Objects;

/**
 * This class defines a key for {@link ServiceOperator} which contains the
 * service name and the service version.
 *
 * @since 1.0.0
 *
 */
public class ServiceOperatorKey implements Comparable<ServiceOperatorKey> {
    private final String service;

    private final String version;

    /**
     * Constructor
     *
     * @param service
     *            Service name
     * @param version
     *            Service version
     */
    public ServiceOperatorKey(String service, String version) {
        this.service = service;
        this.version = version;
    }

    /**
     * Get the service name
     *
     * @return The service name
     */
    public String getService() {
        return service;
    }

    /**
     * Check if the service name is not null.
     *
     * @return <code>true</code>, if the service name is not null.
     */
    public boolean hasService() {
        return getService() != null;
    }

    /**
     * Get the service version
     *
     * @return The service version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Check if the service version is not null.
     *
     * @return <code>true</code>, if the service version is not null.
     */
    public boolean hasVersion() {
        return getVersion() != null;
    }

    @Override
    public int compareTo(ServiceOperatorKey other) {
        return Comparables.chain(other)
                .compare(getService(), other.getService())
                .compare(getVersion(), other.getVersion())
                .result();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == getClass()) {
            ServiceOperatorKey other = (ServiceOperatorKey) o;
            return Objects.equal(getService(), other.getService()) &&
                   Objects.equal(getVersion(), other.getVersion());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getService(), getVersion());
    }

    @Override
    public String toString() {
        return String.format("ServiceOperatorKey[service=%s, version=%s]", getService(), getVersion());
    }
}
