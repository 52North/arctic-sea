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
package org.n52.svalbard.decode;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapHelper;
import org.n52.shetland.w3c.soap.SoapRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.SchemaRepository;
import org.n52.svalbard.util.XmlHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

/**
 * class encapsulates decoding methods for SOAP elements.
 *
 * @since 1.0.0
 */
public class Soap12Decoder extends AbstractSoapDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Soap12Decoder.class);

    private SchemaRepository schemaRepository;

    public Soap12Decoder() {
        super(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE);
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(getKeys()));
    }

    @Inject
    public void setSchemaRepository(SchemaRepository schemaRepository) {
        this.schemaRepository = schemaRepository;
    }

    /**
     * Parses SOAP 1.2 Envelope to a SOS internal SOAP request.
     *
     * @param doc
     *            request as xml representation
     *
     * @return SOS internal SOAP request
     *
     * @throws DecodingException
     *             if an error occurs.
     */
    @Override
    protected SoapRequest createEnvelope(XmlObject doc) throws DecodingException {
        SoapRequest soapRequest =
                new SoapRequest(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, SOAPConstants.SOAP_1_2_PROTOCOL);

        String soapAction = "";
        try {
            SOAPMessage message;
            try {
                message = SoapHelper.getSoapMessageForProtocol(SOAPConstants.SOAP_1_2_PROTOCOL, doc.newInputStream());
            } catch (IOException | SOAPException ioe) {
                throw new NoApplicableCodeException().causedBy(ioe)
                        .withMessage("Error while parsing SOAPMessage from request string!");
            }
            try {
                if (message.getSOAPHeader() != null) {
                    soapRequest.setSoapHeader(getSoapHeader(message.getSOAPHeader()));
                }
                soapRequest.setAction(checkSoapAction(soapAction, soapRequest.getSoapHeader()));
                soapRequest.setSoapBodyContent(getBodyContent((EnvelopeDocument) doc));
            } catch (SOAPException soape) {
                throw new NoApplicableCodeException().causedBy(soape).withMessage("Error while parsing SOAPMessage!");
            }
        } catch (OwsExceptionReport owse) {
            throw new DecodingException(owse);
        }
        return soapRequest;
    }

    @Override
    protected SoapRequest createFault(DecodingException de) {
        SoapFault fault = new SoapFault();
        fault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
        fault.setLocale(Locale.ENGLISH);
        fault.setFaultReason(de.getMessage());
        SoapRequest r = new SoapRequest(SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE, SOAPConstants.SOAP_1_2_PROTOCOL);
        r.setSoapFault(fault);
        return r;
    }

    private OwsServiceRequest getBodyContent(EnvelopeDocument doc) throws DecodingException {
        Body body = doc.getEnvelope().getBody();
        try {
            Node domNode = body.getDomNode();
            if (domNode.hasChildNodes()) {
                NodeList childNodes = domNode.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    Node node = childNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        XmlObject content = XmlObject.Factory.parse(node);
                        // fix problem with invalid prefix in xsi:type value for
                        // om:result, e.g. OM_SWEArrayObservation or
                        // gml:ReferenceType
                        Map<?, ?> namespaces = XmlHelper.getNamespaces(doc.getEnvelope());
                        fixNamespaceForXsiType(content, namespaces);
                        XmlHelper.fixNamespaceForXsiType(content, SweConstants.QN_DATA_ARRAY_PROPERTY_TYPE_SWE_200);
                        return decodeXmlElement(content);
                    }
                }
            }
            return decodeXmlElement(body);
        } catch (XmlException xmle) {
            throw new DecodingException("Error while parsing SOAP body element!", xmle);
        }
    }

    private void fixNamespaceForXsiType(XmlObject content, Map<?, ?> namespaces) {
        final XmlCursor cursor = content.newCursor();
        while (cursor.hasNextToken()) {
            if (cursor.toNextToken().isStart()) {
                final String xsiType = cursor.getAttributeText(W3CConstants.QN_XSI_TYPE);
                if (xsiType != null) {
                    final String[] toks = xsiType.split(":");
                    if (toks.length > 1) {
                        String prefix = toks[0];
                        String localName = toks[1];
                        String namespace = (String) namespaces.get(prefix);
                        if (Strings.isNullOrEmpty(namespace)) {
                            namespace = schemaRepository.getNamespaceFor(prefix);
                        }
                        if (!Strings.isNullOrEmpty(namespace)) {
                            cursor.setAttributeText(W3CConstants.QN_XSI_TYPE,
                                    Joiner.on(":").join(
                                            XmlHelper.getPrefixForNamespace(content, (String) namespaces.get(prefix)),
                                            localName));
                        }
                    }

                }
            }
        }
        cursor.dispose();

    }
}
