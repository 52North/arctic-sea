/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows.service;

import java.util.Comparator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class VersionedOperationKey
        extends OwsOperationKey
        implements Comparable<OwsOperationKey> {

    private static final Comparator<VersionedOperationKey> COMPARATOR =
            Comparator.comparing(VersionedOperationKey::getService).thenComparing(VersionedOperationKey::getVersion)
                    .thenComparing(VersionedOperationKey::getOperation)
                    .thenComparing(VersionedOperationKey::getOperationVersion);

    private final String operationVersion;

    public VersionedOperationKey(String service, String version, String operation, String operationVersion) {
        super(service, version, operation);
        this.operationVersion = operationVersion;
    }

    public VersionedOperationKey(String service, String version, Enum<?> operation, String operationVersion) {
        this(service, version, operation.name(), operationVersion);
    }

    public VersionedOperationKey(OwsOperationKey key, String operationVersion) {
        this(key.getService(), key.getVersion(), key.getOperation(), operationVersion);
    }

    public VersionedOperationKey(VersionedOperationKey key) {
        this(key.getService(), key.getVersion(), key.getOperation(), key.getOperationVersion());
    }

    public String getOperationVersion() {
        return operationVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            final VersionedOperationKey o = (VersionedOperationKey) obj;
            return Objects.equal(getService(), o.getService()) && Objects.equal(getVersion(), o.getVersion())
                    && Objects.equal(getOperation(), o.getOperation())
                    && Objects.equal(getOperationVersion(), o.getOperationVersion());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).add("service", getService()).add("version", getVersion())
                .add("operation", getOperation()).add("operationVersion", getOperationVersion()).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass().getName(), getService(), getVersion(), getOperation(),
                getOperationVersion());
    }

    public int getSimilarity(OwsOperationKey key) {
        return this.equals(key) ? 0 : -1;
    }

    @Override
    public int compareTo(OwsOperationKey other) {
        if (other instanceof VersionedOperationKey) {
            return COMPARATOR.compare(this, (VersionedOperationKey) other);
        }
        return 1;
    }
}
