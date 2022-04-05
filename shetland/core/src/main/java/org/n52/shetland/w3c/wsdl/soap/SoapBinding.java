/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

public class SoapBinding extends ExtensibilityElement {

    private String style;
    private String transport;

    public SoapBinding(String style, String transport) {
        super(WSDLConstants.QN_SOAP_12_BINDING);
        this.setStyle(style);
        this.setTransport(transport);
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style
     *            the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the transport
     */
    public String getTransport() {
        return transport;
    }

    /**
     * @param transport
     *            the transport to set
     */
    public void setTransport(String transport) {
        this.transport = transport;
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
        final SoapBinding other = (SoapBinding) obj;
        return (getStyle() != null && other.getStyle() != null && getStyle().equals(other.getStyle()))
                && (getTransport() != null && other.getTransport() != null
                        && getTransport().equals(other.getTransport()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStyle(), getTransport());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        if (o instanceof SoapBinding) {
            return ComparisonChain.start()
                    .compare(this.getStyle(), ((SoapBinding) o).getStyle())
                    .compare(this.getTransport(), ((SoapBinding) o).getTransport())
                    .result();
        }
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName()
                .getNamespaceURI());
    }

}
