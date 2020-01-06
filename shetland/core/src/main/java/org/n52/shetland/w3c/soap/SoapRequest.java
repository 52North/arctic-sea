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
package org.n52.shetland.w3c.soap;

import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;

/**
 * Representation of a SOAP request
 *
 * @since 1.0.0
 *
 */
public class SoapRequest {
    private String soapNamespace;
    private String soapVersion;
    private SoapFault soapFault;
    private OwsServiceRequest soapBodyContent;
    private String soapAction;
    private List<SoapHeader> soapHeader;

    public SoapRequest(String soapNamespace, String soapVersion) {
        this.soapNamespace = soapNamespace;
        this.soapVersion = soapVersion;
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

    public void setSoapFault(SoapFault fault) {
        this.soapFault = fault;

    }

    public SoapFault getSoapFault() {
        return soapFault;
    }

    public boolean hasSoapFault() {
        return getSoapFault() != null;
    }

    public OwsServiceRequest getSoapBodyContent() {
        return soapBodyContent;
    }

    public void setSoapBodyContent(OwsServiceRequest soapBodyContent) {
        this.soapBodyContent = soapBodyContent;

    }

    public void setAction(String soapAction) {
        this.soapAction = soapAction;

    }

    public void setSoapHeader(List<SoapHeader> soapHeader) {
        this.soapHeader = soapHeader;
    }

    /**
     * @return the soapAction
     */
    public String getSoapAction() {
        return soapAction;
    }

    /**
     * @return the soapHeader
     */
    public List<SoapHeader> getSoapHeader() {
        return soapHeader;
    }

}
