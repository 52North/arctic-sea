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
package org.n52.shetland.w3c.soap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class AbstractSoap<
        T extends OwsServiceCommunicationObject> {
    private String soapNamespace;
    private String soapVersion;
    private String soapAction;
    private List<SoapHeader> soapHeader = new LinkedList<>();
    private T bodyContent;
    private SoapFault soapFault;

    public AbstractSoap() {
    }

    public AbstractSoap(String soapNamespace, String soapVersion) {
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
    public AbstractSoap<T> setSoapNamespace(String soapNamespace) {
        this.soapNamespace = soapNamespace;
        return this;
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
    public AbstractSoap<T> setSoapVersion(String soapVersion) {
        this.soapVersion = soapVersion;
        return this;
    }

    public boolean hasSoapVersion() {
        return getSoapVersion() != null && !getSoapVersion().isEmpty();
    }

    public AbstractSoap<T> setAction(String soapAction) {
        this.soapAction = soapAction;
        return this;
    }

    public AbstractSoap<T> setSoapHeader(List<SoapHeader> soapHeader) {
        this.soapHeader.clear();
        if (soapHeader != null) {
            this.soapHeader.addAll(soapHeader);
        }
        return this;
    }

    /**
     * @return the soapHeader
     */
    public List<SoapHeader> getSoapHeader() {
        return Collections.unmodifiableList(soapHeader);
    }

    public AbstractSoap<T> setHeader(List<SoapHeader> list) {
        setSoapHeader(list);
        return this;
    }

    public List<SoapHeader> getHeader() {
        return getSoapHeader();
    }

    /**
     * @return the soapAction
     */
    public String getSoapAction() {
        return soapAction;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public T getSoapBodyContent() {
        return bodyContent;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public AbstractSoap<T> setSoapBodyContent(T bodyContent) {
        this.bodyContent = bodyContent;
        return this;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public AbstractSoap<T> setBodyContent(T response) {
        this.bodyContent = response;
        return this;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public T getBodyContent() {
        return bodyContent;
    }

    public boolean isSetXmlBodyContent() {
        return isSetBodyContent();
    }

    public boolean isSetBodyContent() {
        return getBodyContent() != null;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public AbstractSoap<T> setSoapFault(SoapFault soapFault) {
        this.soapFault = soapFault;
        return this;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SoapFault getSoapFault() {
        return soapFault;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public boolean hasSoapFault() {
        return getSoapFault() != null;
    }

    public boolean isSetSoapFault() {
        return hasSoapFault();
    }
}
