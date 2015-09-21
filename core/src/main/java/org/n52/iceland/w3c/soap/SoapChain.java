/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.w3c.soap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @since 1.0.0
 */
public class SoapChain {
    private final HttpServletRequest httpRequest;

    private final HttpServletResponse httpResponse;

    private AbstractServiceRequest<?> bodyRequest;

    private AbstractServiceResponse bodyResponse;

    private SoapRequest soapRequest;

    private SoapResponse soapResponse = new SoapResponse();

    public SoapChain(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public AbstractServiceRequest<?> getBodyRequest() {
        return bodyRequest;
    }

    public boolean hasBodyRequest() {
        return getBodyRequest() != null;
    }

    public void setBodyRequest(AbstractServiceRequest<?> bodyRequest) {
        this.bodyRequest = bodyRequest;
    }

    public AbstractServiceResponse getBodyResponse() {
        return bodyResponse;
    }

    public boolean hasBodyResponse() {
        return getBodyResponse() != null;
    }

    public void setBodyResponse(AbstractServiceResponse bodyResponse) {
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
