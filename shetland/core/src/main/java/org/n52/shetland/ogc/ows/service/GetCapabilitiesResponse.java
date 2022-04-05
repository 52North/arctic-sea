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
package org.n52.shetland.ogc.ows.service;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.OwsCapabilities;

/**
 * Implementation of {@link OwsServiceResponse} for OWS GetCapabilities
 * operation
 *
 * @since 1.0.0
 *
 */
public class GetCapabilitiesResponse extends OwsServiceResponse {
    private OwsCapabilities capabilities;
    private String xmlString;

    public GetCapabilitiesResponse() {
        super(null, null, OWSConstants.Operations.GetCapabilities.name());
    }

    public GetCapabilitiesResponse(String service, String version) {
        super(service, version, OWSConstants.Operations.GetCapabilities.name());
    }

    public GetCapabilitiesResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public OwsCapabilities getCapabilities() {
        return capabilities;
    }

    /**
     * Set {@link OwsCapabilities}. Adds service and version from
     * {@link OwsCapabilities} to {@link GetCapabilitiesResponse} is missing.
     *
     * @param capabilities
     *                     {@link OwsCapabilities} to set
     *
     * @return this
     */
    public GetCapabilitiesResponse setCapabilities(OwsCapabilities capabilities) {
        this.capabilities = capabilities;
        capabilities.getService().ifPresent(this::setService);
        setVersion(capabilities.getVersion());
        return this;
    }

    public String getXmlString() {
        return xmlString;
    }

    public GetCapabilitiesResponse setXmlString(String xmlString) {
        this.xmlString = xmlString;
        return this;
    }

    public boolean isStatic() {
        return getXmlString() != null && !getXmlString().isEmpty();
    }
}
