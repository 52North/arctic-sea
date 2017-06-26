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
package org.n52.shetland.inspire.base;


import org.n52.shetland.ogc.gml.CodeWithAuthority;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class Identifier extends CodeWithAuthority {

    private static final long serialVersionUID = -1604778811204130647L;
    private String versionId;

    /**
     * @param localId
     * @param namespace
     */
    public Identifier(String localId, String namespace) {
        super(localId, namespace);
    }

    public Identifier(CodeWithAuthority codeWithAuthority) {
        super(codeWithAuthority.getValue(), codeWithAuthority.getCodeSpace());
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

    /**
     * @return the versionId
     */
    public String getVersionId() {
        return versionId;
    }

    /**
     * @param versionId the versionId to set
     */
    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    /**
     * @return <code>true</code>, if versionId is set
     */
    public boolean isSetVersionId() {
        return !Strings.isNullOrEmpty(versionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNamespace(), getLocalId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!Identifier.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        Identifier other = (Identifier) obj;
        if (getNamespace() == null) {
            if (other.getNamespace() != null) {
                return false;
            }
        } else if (!getNamespace().equals(other.getNamespace())) {
            return false;
        }
        if (getLocalId() == null) {
            if (other.getLocalId() != null) {
                return false;
            }
        } else if (!getLocalId().equals(other.getLocalId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("Identifier [localId=%s, namespace=%s]", getLocalId(), getNamespace());
    }

}
