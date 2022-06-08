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
package org.n52.shetland.w3c.wsdl.soap;

import java.util.Objects;

import org.n52.janmayen.Comparables;
import org.n52.shetland.w3c.wsdl.ExtensibilityElement;
import org.n52.shetland.w3c.wsdl.WSDLConstants;

public class SoapBody extends ExtensibilityElement {

    private String use;

    public SoapBody(String use) {
        super(WSDLConstants.QN_SOAP_12_BODY);
        this.setUse(use);
    }

    /**
     * @return the use
     */
    public String getUse() {
        return use;
    }

    /**
     * @param use
     *            the use to set
     */
    public void setUse(String use) {
        this.use = use;
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
        final SoapBody other = (SoapBody) obj;
        return getUse() != null && other.getUse() != null && getUse().equals(other.getUse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUse());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        if (o instanceof SoapBody) {
            return Comparables.compare(getUse(), ((SoapBody) o).getUse());
        }
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName().getNamespaceURI());
    }

}
