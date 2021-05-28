/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

public class HttpOperation extends ExtensibilityElement {

    private String location;

    public HttpOperation(String location) {
        super(WSDLConstants.QN_HTTP_OPERATION);
        this.setLocation(location);
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
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
        final HttpOperation other = (HttpOperation) obj;
        return getLocation() != null && other.getLocation() != null && getLocation().equals(other.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getLocation());
    }

    @Override
    public int compareTo(ExtensibilityElement o) {
        Objects.requireNonNull(o);
        if (o instanceof HttpOperation) {
            return Comparables.compare(getLocation(), ((HttpOperation) o).getLocation());
        }
        return Comparables.compare(getQName().getNamespaceURI(), o.getQName().getNamespaceURI());
    }

}
