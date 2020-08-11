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
package org.n52.shetland.w3c.wsdl.soap;

import java.util.Objects;

import org.n52.janmayen.Comparables;
import org.n52.shetland.w3c.wsdl.ExtensibilityElement;
import org.n52.shetland.w3c.wsdl.WSDLConstants;

import com.google.common.collect.ComparisonChain;

public class SoapFault extends ExtensibilityElement {

    private String name;

    private String use;

    public SoapFault(String name, String use) {
        super(WSDLConstants.QN_SOAP_12_FAULT);
        this.setName(name);
        this.setUse(use);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
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
        final SoapFault other = (SoapFault) obj;
        return (getUse() != null && other.getUse() != null && getUse().equals(other.getUse()))
                &&  (getName() != null && other.getName() != null && getName().equals(other.getName()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUse(), getName());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        if (o instanceof SoapFault) {
            return ComparisonChain.start()
                    .compare(this.getUse(), ((SoapFault) o).getUse())
                    .compare(this.getName(), ((SoapFault) o).getName())
                    .result();
        }
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName()
                .getNamespaceURI());
    }

}
