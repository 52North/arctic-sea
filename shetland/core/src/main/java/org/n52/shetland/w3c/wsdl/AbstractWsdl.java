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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.n52.janmayen.Comparables;

public abstract class AbstractWsdl implements Comparable<AbstractWsdl> {

    private String name;

    private Map<QName, Object> extensionAttributes = new LinkedHashMap<>();

    private QName qName;

    private Collection<ExtensibilityElement> extensibilityElements = new TreeSet<>();

    public AbstractWsdl() {
        this(null, null);
    }

    public AbstractWsdl(String name) {
        this(name, null);
    }

    public AbstractWsdl(QName qName) {
        this(null, qName);
    }

    public AbstractWsdl(String name, QName qName) {
        this.name = name;
        this.qName = qName;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSetName() {
        return getName() != null && !getName().isEmpty();
    }

    /**
     * @return the qName
     */
    public QName getQName() {
        return qName;
    }

    /**
     * @param qName the qName to set
     */
    public void setQName(QName qName) {
        this.qName = qName;
    }

    public boolean isSetQName() {
        return getQName() != null;
    }

    public AbstractWsdl addExtensionAttribute(QName key, Object value) {
        if (key != null && value != null) {
            this.extensionAttributes.put(key, value);
        }
        return this;
    }

    public AbstractWsdl setExtensionAttribute(QName key, Object value) {
        this.extensionAttributes.clear();
        return addExtensionAttribute(key, value);
    }

    public Map<QName, Object> getExtensionAttributes() {
        return extensionAttributes;
    }

    public boolean isSetExtensionAttributes() {
        return !getExtensionAttributes().isEmpty();
    }

    public AbstractWsdl addExtensibilityElement(ExtensibilityElement extensibilityElement) {
        if (extensibilityElement != null) {
            this.extensibilityElements.add(extensibilityElement);
        }
        return this;
    }

    public AbstractWsdl addExtensibilityElements(Collection<ExtensibilityElement> extensibilityElements) {
        if (extensibilityElements != null) {
            extensibilityElements.forEach(p -> {
                addExtensibilityElement(p);
            });
        }
        return this;
    }

    public AbstractWsdl setExtensibilityElements(Collection<ExtensibilityElement> extensibilityElements) {
        this.extensibilityElements.clear();
        return addExtensibilityElements(extensibilityElements);
    }

    public Collection<ExtensibilityElement> getExtensibilityElements() {
        return extensibilityElements;
    }

    public boolean isSetExtensibilityElements() {
        return getExtensibilityElements() != null && !getExtensibilityElements().isEmpty();
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
        final AbstractWsdl other = (AbstractWsdl) obj;
        return getName() != null && other.getName() != null && getName().equals(other.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public int compareTo(AbstractWsdl o) {
        Objects.requireNonNull(o);
        return Comparables.compare(getName(), o.getName());
    }
}
