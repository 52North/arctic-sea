/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.w3c.soap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public class SoapChain {
    private final HttpServletRequest httpRequest;

    private final HttpServletResponse httpResponse;

    private OwsServiceRequest bodyRequest;

    private OwsServiceResponse bodyResponse;

    private SoapRequest soapRequest;

    private SoapResponse soapResponse = new SoapResponse();

    public SoapChain(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public OwsServiceRequest getBodyRequest() {
        return bodyRequest;
    }

    public boolean hasBodyRequest() {
        return getBodyRequest() != null;
    }

    public void setBodyRequest(OwsServiceRequest bodyRequest) {
        this.bodyRequest = bodyRequest;
    }

    public OwsServiceResponse getBodyResponse() {
        return bodyResponse;
    }

    public boolean hasBodyResponse() {
        return getBodyResponse() != null;
    }

    public void setBodyResponse(OwsServiceResponse bodyResponse) {
        this.bodyResponse = bodyResponse;
        if (hasSoapResponse()) {
            getSoapResponse().setBodyContent(bodyResponse);
        }
    }

    public SoapRequest getSoapRequest() {
        return soapRequest;
    }

    public boolean hasSoapRequest() {
        return getSoapRequest() != null;
    }

    public void setSoapRequest(SoapRequest soapRequest) {
        this.soapRequest = soapRequest;
    }

    public SoapResponse getSoapResponse() {
        return soapResponse;
    }

    public boolean hasSoapResponse() {
        return getSoapResponse() != null;
    }

    public void setSoapResponse(SoapResponse soapResponse) {
        this.soapResponse = soapResponse;
        if (hasBodyResponse() && !soapResponse.isSetBodyContent()) {
            soapResponse.setBodyContent(getBodyResponse());
        }
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public boolean hasHttpRequest() {
        return getHttpRequest() != null;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public boolean hasHttpResponse() {
        return getHttpResponse() != null;
    }
}
