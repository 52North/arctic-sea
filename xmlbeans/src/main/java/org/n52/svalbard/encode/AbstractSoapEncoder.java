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
package org.n52.svalbard.encode;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.ExceptionCode;
import org.n52.shetland.ogc.ows.exception.OwsExceptionCode;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.SosSoapConstants;
import org.n52.shetland.ogc.sos.exception.SosExceptionCode;
import org.n52.shetland.ogc.swes.exception.SwesExceptionCode;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapHelper;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;
import org.n52.svalbard.util.N52XmlHelper;

import com.google.common.collect.ImmutableSet;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public abstract class AbstractSoapEncoder<T, S> extends AbstractXmlEncoder<T, S> {
    public static final String DEFAULT_FAULT_REASON = "A server exception was encountered.";

    public static final String MISSING_RESPONSE_DETAIL_TEXT = "Missing SOS response document!";

    public static final String MISSING_EXCEPTION_DETAIL_TEXT
            = "Error while creating SOAPFault element from OWSException! OWSException is missing!";

    private final Set<EncoderKey> encoderKey;

    public AbstractSoapEncoder(String namespace) {
        this.encoderKey = ImmutableSet.<EncoderKey>of(new XmlEncoderKey(namespace, SoapResponse.class));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(encoderKey);
    }

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_SOAP_XML;
    }

    @Override
    public T encode(S response) throws EncodingException {
        return encode(response, null);
    }

    /**
     * Creates a SOAPBody element from SOS response
     *
     * @param soapResponseMessage SOAPBody element
     * @param sosResponse         SOS response
     * @param actionURI           the action URI
     *
     * @return the action URI
     *
     * @throws SOAPException if an error occurs.
     */
    protected String createSOAPBody(SOAPMessage soapResponseMessage, XmlObject sosResponse, String actionURI)
            throws SOAPException {

        if (sosResponse != null) {
            addAndRemoveSchemaLocationForSOAP(sosResponse, soapResponseMessage);
            soapResponseMessage.getSOAPBody().addDocument((Document) sosResponse.getDomNode());
            return actionURI;
        } else {
            SoapFault fault = new SoapFault();
            fault.setFaultCode(SOAPConstants.SOAP_RECEIVER_FAULT);
            fault.setFaultSubcode(new QName(OWSConstants.NS_OWS, OwsExceptionCode.NoApplicableCode.name(),
                                            OWSConstants.NS_OWS_PREFIX));
            fault.setFaultReason(DEFAULT_FAULT_REASON);
            fault.setLocale(Locale.ENGLISH);
            fault.setDetailText(MISSING_RESPONSE_DETAIL_TEXT);
            createSOAPFault(soapResponseMessage.getSOAPBody().addFault(), fault);
        }
        return null;
    }

    /**
     * Create and add the SOAPBody content
     *
     * @param soapResponseMessage SOAPMessage to add the body
     * @param soapResponse        SOS internal SOAP response
     * @param actionURI           The ation URI
     *
     * @return action URI
     *
     * @throws SOAPException     If an error occurs when add content to {@link SOAPMessage}
     * @throws EncodingException If an error occurs while encoding the body content
     */
    protected String createSOAPBody(SOAPMessage soapResponseMessage, SoapResponse soapResponse, String actionURI)
            throws SOAPException, EncodingException {
        return createSOAPBody(soapResponseMessage, getBodyContent(soapResponse), actionURI);
    }

    /**
     * Get the content for the SOAPBody as {@link XmlObject}
     *
     * @param response SOAP response
     *
     * @return SOAPBody content as {@link XmlObject}
     *
     * @throws EncodingException If no encoder is available, the object to encode is not supported or an error occurs
     *                           during the encoding
     */
    protected XmlObject getBodyContent(SoapResponse response) throws EncodingException {
        OperationResponseEncoderKey key = new OperationResponseEncoderKey(
                new OwsOperationKey(response.getBodyContent()), MediaTypes.APPLICATION_XML);
        Encoder<Object, OwsServiceResponse> encoder = getEncoder(key);
        if (encoder == null) {
            throw new NoEncoderForKeyException(key);
        }
        return (XmlObject) encoder.encode(response.getBodyContent());
    }

    /**
     * Check SOS response for xsi:schemaLocation, remove attribute and add attribute to SOAP message
     *
     * @param xmlObject           the document
     * @param soapResponseMessage SOAP response message
     *
     * @throws SOAPException If an error occurs
     */
    private void addAndRemoveSchemaLocationForSOAP(XmlObject xmlObject, SOAPMessage soapResponseMessage)
            throws SOAPException {
        String value = null;
        Node nodeToRemove = null;
        NamedNodeMap attributeMap = xmlObject.getDomNode().getFirstChild().getAttributes();
        for (int i = 0; i < attributeMap.getLength(); i++) {
            Node node = attributeMap.item(i);
            if (node.getLocalName().equals(W3CConstants.AN_SCHEMA_LOCATION)) {
                value = node.getNodeValue();
                nodeToRemove = node;
            }
        }
        if (nodeToRemove != null) {
            attributeMap.removeNamedItem(nodeToRemove.getNodeName());
        }
        SOAPEnvelope envelope = soapResponseMessage.getSOAPPart().getEnvelope();
        StringBuilder string = new StringBuilder();
        string.append(envelope.getNamespaceURI());
        string.append(' ');
        string.append(envelope.getNamespaceURI());
        if (value != null && !value.isEmpty()) {
            string.append(' ');
            string.append(value);
        }
        envelope.addAttribute(N52XmlHelper.getSchemaLocationQNameWithPrefix(), string.toString());
    }

    /**
     * Creates a SOAPFault element from SOS internal fault
     *
     * @param fault     SOAPFault element
     * @param soapFault SOS internal fault
     *
     * @throws SOAPException if an error occurs.
     */
    protected void createSOAPFault(SOAPFault fault, SoapFault soapFault) throws SOAPException {
        fault.setFaultCode(soapFault.getFaultCode());
        fault.setFaultString(soapFault.getFaultReason(), soapFault.getLocale());
        if (soapFault.getDetailText() != null) {
            fault.addDetail().setTextContent(soapFault.getDetailText());
        }
    }

    /**
     * Creates a SOAPFault element from SOS exception
     *
     * @param soapFault          SOAPFault element
     * @param owsExceptionReport SOS exception
     *
     * @return SOAP action URI.
     *
     * @throws SOAPException if an error occurs.
     */
    protected String createSOAPFaultFromExceptionResponse(SOAPFault soapFault, OwsExceptionReport owsExceptionReport)
            throws SOAPException {
        // FIXME: check and fix support for ExceptionReport with multiple
        // exceptions!
        if (!owsExceptionReport.getExceptions().isEmpty()) {
            CodedException firstException = owsExceptionReport.getExceptions().iterator().next();
            if (soapFault.getNamespaceURI().equalsIgnoreCase(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE)) {
                QName qname = new QName(soapFault.getNamespaceURI(), "Client", soapFault.getPrefix());
                soapFault.setFaultCode(qname);
            } else {
                soapFault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
                if (firstException.getCode() != null) {
                    soapFault.appendFaultSubcode(new QName(OWSConstants.NS_OWS, firstException.getCode().toString(),
                                                           OWSConstants.NS_OWS_PREFIX));
                } else {
                    soapFault.appendFaultSubcode(OWSConstants.QN_NO_APPLICABLE_CODE);
                }
            }
            soapFault.addFaultReasonText(SoapHelper.getSoapFaultReasonText(firstException.getCode()), Locale.ENGLISH);
            Detail detail = soapFault.addDetail();
            for (CodedException exception : owsExceptionReport.getExceptions()) {
                createSOAPFaultDetail(detail, exception);
            }
            return getExceptionActionURI(firstException.getCode());
        } else {
            SoapFault fault = new SoapFault();
            fault.setFaultCode(SOAPConstants.SOAP_RECEIVER_FAULT);
            fault.setFaultSubcode(OWSConstants.QN_NO_APPLICABLE_CODE);
            fault.setFaultReason(DEFAULT_FAULT_REASON);
            fault.setLocale(Locale.ENGLISH);
            fault.setDetailText(MISSING_EXCEPTION_DETAIL_TEXT);
            createSOAPFault(soapFault, fault);
            return SosSoapConstants.RESP_ACTION_SOS;
        }
    }

    /**
     * Get SOAP action URI depending on Exception code
     *
     * @param exceptionCode Exception code
     *
     * @return SOAP action URI
     */
    protected String getExceptionActionURI(ExceptionCode exceptionCode) {
        if (exceptionCode instanceof OwsExceptionCode) {
            return SosSoapConstants.RESP_ACTION_OWS;
        } else if (exceptionCode instanceof SwesExceptionCode) {
            return SosSoapConstants.RESP_ACTION_SWES;
        } else if (exceptionCode instanceof SosExceptionCode) {
            return SosSoapConstants.RESP_ACTION_SOS;
        } else {
            return SosSoapConstants.RESP_ACTION_OWS;
        }
    }

    /**
     * Creates a SOAPDetail element from SOS exception document.
     *
     * @param detail    SOAPDetail
     * @param exception SOS Exception document
     *
     * @throws SOAPException if an error occurs.
     */
    private void createSOAPFaultDetail(Detail detail, CodedException exception) throws SOAPException {
        SOAPElement exRep = detail.addChildElement(OWSConstants.QN_EXCEPTION);
        exRep.addNamespaceDeclaration(OWSConstants.NS_OWS_PREFIX, OWSConstants.NS_OWS);
        String code = exception.getCode().toString();
        String locator = exception.getLocator();
        StringBuilder exceptionText = new StringBuilder();
        exceptionText.append(exception.getMessage());
        exceptionText.append('\n');
        if (exception.getCause() != null) {
            exceptionText.append('\n').append("[EXCEPTION]: ").append('\n');
            if (exception.getCause().getLocalizedMessage() != null &&
                !exception.getCause().getLocalizedMessage().isEmpty()) {
                exceptionText.append(exception.getCause().getLocalizedMessage());
                exceptionText.append('\n');
            }
            if (exception.getCause().getMessage() != null && !exception.getCause().getMessage().isEmpty()) {
                exceptionText.append(exception.getCause().getMessage());
                exceptionText.append('\n');
            }
        }
        exRep.addAttribute(new QName(OWSConstants.EN_EXCEPTION_CODE), code);
        if (locator != null && !locator.isEmpty()) {
            exRep.addAttribute(new QName(OWSConstants.EN_LOCATOR), locator);
        }
        if (exceptionText.length() != 0) {
            SOAPElement execText = exRep.addChildElement(OWSConstants.QN_EXCEPTION_TEXT);
            execText.setTextContent(exceptionText.toString());
        }
    }
}
