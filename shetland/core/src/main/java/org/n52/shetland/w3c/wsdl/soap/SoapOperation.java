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

import java.net.URI;
import java.util.Objects;

import org.n52.janmayen.Comparables;
import org.n52.shetland.w3c.wsdl.ExtensibilityElement;
import org.n52.shetland.w3c.wsdl.WSDLConstants;

import com.google.common.collect.ComparisonChain;

public class SoapOperation extends ExtensibilityElement {

    private String style;
    private URI action;

    public SoapOperation(String style, URI action) {
        super(WSDLConstants.QN_SOAP_OPERATION);
        this.setStyle(style);
        this.setAction(action);
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the action
     */
    public URI getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(URI action) {
        this.action = action;
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
        final SoapOperation other = (SoapOperation) obj;
        return (getAction() != null && other.getAction() != null && getAction().equals(other.getAction()))
                &&  (getStyle() != null && other.getStyle() != null && getStyle().equals(other.getStyle()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getAction(), getStyle());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        if (o instanceof SoapOperation) {
            return ComparisonChain.start()
                    .compare(this.getAction(), ((SoapOperation) o).getAction())
                    .compare(this.getStyle(), ((SoapOperation) o).getStyle())
                    .result();
        }
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName()
                .getNamespaceURI());
    }

}
