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
package org.n52.shetland.w3c.wsdl.http;

import java.util.Objects;

import org.n52.janmayen.Comparables;
import org.n52.shetland.w3c.wsdl.ExtensibilityElement;
import org.n52.shetland.w3c.wsdl.WSDLConstants;

public class HttpBinding extends ExtensibilityElement {

    private String verb;

    public HttpBinding(String verb) {
        super(WSDLConstants.QN_HTTP_BINDING);
        this.setVerb(verb);
    }

    /**
     * @return the verb
     */
    public String getVerb() {
        return verb;
    }

    /**
     * @param verb the verb to set
     */
    public void setVerb(String verb) {
        this.verb = verb;
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
        final HttpBinding other = (HttpBinding) obj;
        return getVerb() != null && other.getVerb() != null && getVerb().equals(other.getVerb());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getVerb());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        if (o instanceof HttpBinding) {
            return Comparables.compare(getVerb(), ((HttpBinding) o).getVerb());
        }
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName().getNamespaceURI());
    }

}
