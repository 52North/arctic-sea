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
package org.n52.shetland.inspire;

import org.n52.shetland.w3c.Nillable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class InspireID {

    private String namespace;
    private String localId;
    private Nillable<String> versionId = Nillable.absent();

    public String getNamespace() {
        return namespace;
    }

    public InspireID setNamespace(String namespace) {
        this.namespace = Preconditions.checkNotNull(namespace);
        return this;
    }

    public String getLocalId() {
        return localId;
    }

    public InspireID setLocalId(String localId) {
        this.localId = Preconditions.checkNotNull(localId);
        return this;
    }

    public Nillable<String> getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = Nillable.of(versionId);
    }

    public InspireID setVersionId(Nillable<String> versionId) {
        this.versionId = versionId;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNamespace(), getLocalId(), getVersionId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InspireID) {
            InspireID that = (InspireID) obj;
            return Objects.equal(this.getNamespace(), that.getNamespace()) &&
                   Objects.equal(this.getLocalId(), that.getLocalId()) &&
                   Objects.equal(this.getVersionId(), that.getVersionId());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("localId", getLocalId())
                .add("namespace", getNamespace())
                .add("versionId", getVersionId())
                .toString();
    }
}
