/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;

import com.google.common.base.Objects;

public class VersionedOperationEncoderKey extends OperationEncoderKey implements EncoderKey {
    private final String operationVersion;

    public VersionedOperationEncoderKey(String service, String version, String operation, MediaType contentType,
            String operationVersion) {
        super(service, version, operation, contentType);
        this.operationVersion = operationVersion;
    }

    public VersionedOperationEncoderKey(String service, String version, Enum<?> operation, MediaType contentType,
            String operationVersion) {
        super(service, version, operation, contentType);
        this.operationVersion = operationVersion;
    }

    public VersionedOperationEncoderKey(OwsOperationKey key, MediaType contentType, String operationVersion) {
        super(key, contentType);
        this.operationVersion = operationVersion;
    }

    public VersionedOperationEncoderKey(VersionedOperationEncoderKey key, MediaType contentType) {
        super(key, contentType);
        this.operationVersion = key.getOperationVersion();
    }

    @Override
    public int getSimilarity(EncoderKey key) {
        return equals(key) ? 0 : -1;
    }

    public String getOperationVersion() {
        return operationVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), this.operationVersion);
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        if (obj != null && getClass() == obj.getClass()) {
            final VersionedOperationEncoderKey other = (VersionedOperationEncoderKey) obj;
            return super.equals(obj) && getOperationVersion() != null
                    && getOperationVersion().equals(other.getOperationVersion());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[service=%s, version=%s, operation=%s, contentType=%s, operationVersion=%s]",
                getClass().getSimpleName(), getService(), getVersion(), getOperation(), getContentType(),
                getOperationVersion());
    }

}
