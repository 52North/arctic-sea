/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.w3c.wsdl;

import java.util.Objects;

import javax.xml.namespace.QName;

import org.n52.janmayen.Comparables;

public class ExtensibilityElement implements Comparable<ExtensibilityElement> {

    private QName qName;

    public ExtensibilityElement(QName qName) {
        this.qName = qName;
    }

    public QName getQName() {
        return qName;
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
        final ExtensibilityElement other = (ExtensibilityElement) obj;
        return getQName() != null && other.getQName() != null && getQName().equals(other.getQName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getQName());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName().getNamespaceURI());
    }
}
