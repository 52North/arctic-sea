package org.n52.shetland.w3c.soap;

import java.util.List;

import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;

public abstract class AbstractSoap<T extends OwsServiceCommunicationObject> {
    private String soapNamespace;
    private String soapVersion;
    private String soapAction;
    private List<SoapHeader> soapHeader;
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
        this.soapHeader = soapHeader;
        return this;
    }

    /**
     * @return the soapHeader
     */
    public List<SoapHeader> getSoapHeader() {
        return soapHeader;
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

    public T getSoapBodyContent() {
        return bodyContent;
    }

    public AbstractSoap<T> setSoapBodyContent(T bodyContent) {
        this.bodyContent = bodyContent;
        return this;
    }

    public AbstractSoap<T> setBodyContent(T response) {
        this.bodyContent = response;
        return this;
    }

    public T getBodyContent() {
        return bodyContent;
    }

    public boolean isSetXmlBodyContent() {
        return isSetBodyContent();
    }

    public boolean isSetBodyContent() {
        return getBodyContent() != null;
    }

    public AbstractSoap<T> setSoapFault(SoapFault soapFault) {
        this.soapFault = soapFault;
        return this;
    }

    public SoapFault getSoapFault() {
        return soapFault;
    }

    public boolean hasSoapFault() {
        return getSoapFault() != null;
    }

    public boolean isSetSoapFault() {
        return hasSoapFault();
    }
}
