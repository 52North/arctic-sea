/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.ows.service;

import java.util.Comparator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class OwsOperationKey implements Comparable<OwsOperationKey> {
    private static final Comparator<OwsOperationKey> COMPARATOR = Comparator.comparing(OwsOperationKey::getService)
            .thenComparing(OwsOperationKey::getVersion).thenComparing(OwsOperationKey::getOperation);
    private final String service;
    // TODO should be optional because GetCapabilities does not need to have it
    private final String version;
    private final String operation;

    public OwsOperationKey(OwsServiceCommunicationObject asco) {
        this(asco.getService(), asco.getVersion(), asco.getOperationName());
    }

    public OwsOperationKey(String service, String version, String operation) {
        this.service = service;
        this.version = version;
        this.operation = operation;
    }

    public OwsOperationKey(String service, String version, Enum<?> operation) {
        this(service, version, operation.name());
    }

    public OwsOperationKey(OwsOperationKey key) {
        this(key.getService(), key.getVersion(), key.getOperation());
    }

    public String getService() {
        return service;
    }

    public String getVersion() {
        return version;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            final OwsOperationKey o = (OwsOperationKey) obj;
            return Objects.equal(getService(), o.getService()) && Objects.equal(getVersion(), o.getVersion())
                    && Objects.equal(getOperation(), o.getOperation());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).add("service", getService()).add("version", getVersion())
                .add("operation", getOperation()).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass().getName(), getService(), getVersion(), getOperation());
    }

    public int getSimilarity(OwsOperationKey key) {
        return this.equals(key) ? 0 : -1;
    }

    @Override
    public int compareTo(OwsOperationKey other) {
        return COMPARATOR.compare(this, other);
    }
}
