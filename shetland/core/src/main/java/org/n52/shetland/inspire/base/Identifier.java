/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire.base;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.w3c.Nillable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class Identifier
        extends CodeWithAuthority {

    private Nillable<String> versionId = Nillable.absent();

    /**
     * @param localId
     *            the localID
     * @param namespace
     *            the namespace
     */
    public Identifier(String localId, String namespace) {
        super(localId, namespace);
    }

    public Identifier(CodeWithAuthority codeWithAuthority) {
        super(codeWithAuthority.getValue(), codeWithAuthority.getCodeSpace());
    }

    public Identifier() {
    }

    /**
     * Get localId
     *
     * @return LocalId
     */
    public String getLocalId() {
        return getValue();
    }

    /**
     * Get namespace
     *
     * @return Code space
     */
    public String getNamespace() {
        return getCodeSpace();
    }

    /**
     * Set localId and return this Identifier object
     *
     * @param localId
     *            LocalId to set
     * @return This Identifier object
     */
    public Identifier setLocalId(String localId) {
        setValue(localId);
        return this;
    }

    /**
     * Set namespace and return this Identifier object
     *
     * @param namespace
     *            Code space to set
     * @return This Identifier object
     */
    public Identifier setNamespace(String namespace) {
        setCodeSpace(namespace);
        return this;
    }

    /**
     * Check whether localId is set
     *
     * @return <code>true</code> if localId is set
     */
    public boolean isSetLocalId() {
        return !Strings.isNullOrEmpty(getLocalId());
    }

    /**
     * Check whether namespace is set
     *
     * @return <code>true</code> if namespace is set
     */
    public boolean isSetNamespace() {
        return !Strings.isNullOrEmpty(getNamespace());
    }

    public Nillable<String> getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = Nillable.of(versionId);
    }

    public Identifier setVersionId(Nillable<String> versionId) {
        this.versionId = versionId;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNamespace(), getLocalId(), getVersionId());
    }

    @Override
    @SuppressFBWarnings("EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC")
    public boolean equals(Object obj) {
        if (obj instanceof Identifier) {
            Identifier that = (Identifier) obj;
            return Objects.equal(this.getNamespace(), that.getNamespace())
                    && Objects.equal(this.getLocalId(), that.getLocalId())
                    && Objects.equal(this.getVersionId(), that.getVersionId());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("localId", getLocalId()).add("namespace", getNamespace())
                .add("versionId", getVersionId()).toString();
    }

}
