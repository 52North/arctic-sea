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
package org.n52.shetland.w3c.wsdl;

import java.util.Objects;

import org.n52.janmayen.Comparables;

import com.google.common.collect.ComparisonChain;

public class Schema extends ExtensibilityElement {

    private String elementFormDefault;
    private String targetNamespace;
    private Include include;

    public Schema(String targetNamespace, Include include) {
        this("qualified", targetNamespace, include);
    }

    public Schema(String elementFormDefault, String targetNamespace, Include include) {
        super(WSDLConstants.QN_XSD_SCHEMA);
        this.elementFormDefault = elementFormDefault;
        this.targetNamespace = targetNamespace;
        this.include = include;
    }

    public String getElementFormDefault() {
        return elementFormDefault;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public Include getInclude() {
        return include;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Schema other = (Schema) obj;
        return getTargetNamespace() != null && other.getTargetNamespace() != null
                && getTargetNamespace().equals(other.getTargetNamespace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTargetNamespace(), getElementFormDefault(), getInclude());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        if (o instanceof Schema) {
            return ComparisonChain.start()
                    .compare(this.getTargetNamespace(), ((Schema) o).getTargetNamespace())
                    .compare(this.getElementFormDefault(), ((Schema) o).getElementFormDefault())
                    .compare(this.getInclude(), ((Schema) o).getInclude())
                    .result();
        }
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName()
                .getNamespaceURI());
    }

}
