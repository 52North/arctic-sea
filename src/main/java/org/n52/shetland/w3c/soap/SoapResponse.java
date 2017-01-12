/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import java.util.List;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

/**
 * Representation of a SOAP response
 *
 * @since 1.0.0
 *
 */
public class SoapResponse {

    private String soapNamespace;

    private String soapVersion;

    private String soapAction;

    private SoapFault soapFault;

    private OwsServiceResponse bodyContent;

    private List<SoapHeader> header;

    private OwsExceptionReport exception;

    public SoapResponse() {
    }

    /**
     * @return the soapNamespace
     */
    public String getSoapNamespace() {
        return soapNamespace;
    }

    /**
     * @param soapNamespace
     *            the soapNamespace to set
     */
    public void setSoapNamespace(String soapNamespace) {
        this.soapNamespace = soapNamespace;
    }

    public boolean hasSoapNamespace() {
        return getSoapNamespace() != null && !getSoapNamespace().isEmpty();
    }

    /**
     * @return the soapVersion
     */
    public String getSoapVersion() {
        return soapVersion;
    }

    /**
     * @param soapVersion
     *            the soapVersion to set
     */
    public void setSoapVersion(String soapVersion) {
        this.soapVersion = soapVersion;
    }

    public boolean hasSoapVersion() {
        return getSoapVersion() != null && !getSoapVersion().isEmpty();
    }

    public void setSoapFault(SoapFault soapFault) {
        this.soapFault = soapFault;
    }

    public SoapFault getSoapFault() {
        return soapFault;
    }

    public OwsServiceResponse getSoapBodyContent() {
        return bodyContent;
    }

    public void setSoapAction(String soapAction) {
        this.soapAction = soapAction;
    }

    public String getSoapAction() {
        return soapAction;
    }

    public void setSoapBodyContent(OwsServiceResponse bodyContent) {
        this.bodyContent = bodyContent;
    }

    public void setBodyContent(OwsServiceResponse response) {
        this.bodyContent = response;
    }

    public OwsServiceResponse getBodyContent() {
        return bodyContent;
    }

    public void setHeader(List<SoapHeader> list) {
        this.header = list;
    }

    public List<SoapHeader> getHeader() {
        return header;
    }

    public void setException(OwsExceptionReport owse) {
        this.exception = owse;
    }

    public OwsExceptionReport getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }

    public boolean isSetXmlBodyContent() {
        return getSoapBodyContent() != null;
    }

    public boolean isSetBodyContent() {
        return getBodyContent() != null;
    }

    public boolean isSetSoapFault() {
        return getSoapFault() != null;
    }

}
