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
package org.n52.svalbard.encode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.soap.AbstractSoap;
import org.n52.shetland.w3c.soap.SoapConstants;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapHeader;
import org.n52.shetland.w3c.soap.SoapRequest;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.shetland.w3c.wsa.WsaActionHeader;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.shetland.w3c.wsa.WsaHeader;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.N52XmlHelper;
import org.n52.svalbard.write.Soap12XmlStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xmlsoap.schemas.soap.envelope.Body;
import org.xmlsoap.schemas.soap.envelope.Envelope;
import org.xmlsoap.schemas.soap.envelope.EnvelopeDocument;
import org.xmlsoap.schemas.soap.envelope.Fault;
import org.xmlsoap.schemas.soap.envelope.FaultDocument;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public class Soap11Encoder extends AbstractSoapEncoder<XmlObject, Object>
        implements StreamingEncoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Soap11Encoder.class);

    private static final Set<EncoderKey> ENCODER_KEY_TYPES = CodingHelper.encoderKeysForElements(
            SoapConstants.NS_SOAP_11, SoapResponse.class, SoapRequest.class, SoapFault.class, OwsExceptionReport.class);

    public Soap11Encoder() {
        super(SoapConstants.NS_SOAP_11);
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                     Joiner.on(", ").join(ENCODER_KEY_TYPES));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(CollectionHelper.union(ENCODER_KEY_TYPES, super.getKeys()));
    }

    @Override
    public XmlObject encode(final Object element, EncodingContext additionalValues) throws EncodingException {
        if (element instanceof SoapResponse) {
            return createEnvelope((SoapResponse) element, additionalValues);
        } else if (element instanceof SoapRequest) {
            return createEnvelope((SoapRequest) element, additionalValues);
        } else if (element instanceof SoapFault) {
            return createFault((SoapFault) element);
        } else if (element instanceof OwsExceptionReport) {
            return createFaultFromExceptionResponse((OwsExceptionReport) element);
        } else {
            throw new UnsupportedEncoderInputException(this, element);
        }
    }

    @Override
    public void encode(Object element, OutputStream outputStream, EncodingContext ctx)
            throws EncodingException {
        if (element instanceof SoapResponse) {
            try {
                EncodingContext context = ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                    .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions);
                new Soap12XmlStreamWriter(context, outputStream, (SoapResponse) element).write();
            } catch (XMLStreamException ex) {
                throw new EncodingException(ex);
            }
        } else {
            try {
                encode(element, ctx).save(outputStream, getXmlOptions());
            } catch (IOException ioe) {
                throw new EncodingException("Error while writing element to stream!", ioe);
            }
        }
    }

    private XmlObject createEnvelope(AbstractSoap<?> soap, EncodingContext additionalValues)
            throws EncodingException {
        String action = null;
        final EnvelopeDocument envelopeDoc = EnvelopeDocument.Factory.newInstance();
        final Envelope envelope = envelopeDoc.addNewEnvelope();
        final Body body = envelope.addNewBody();
        if (soap.getSoapFault() != null) {
            body.set(createFault(soap.getSoapFault()));
        } else {
            if (soap instanceof SoapResponse && ((SoapResponse) soap).hasException()) {
                SoapResponse response = (SoapResponse) soap;
                if (!response.getException().getExceptions().isEmpty()) {
                    final CodedException firstException = response.getException().getExceptions().get(0);
                    action = getExceptionActionURI(firstException.getCode());
                }
                body.set(createFaultFromExceptionResponse(response.getException()));
                N52XmlHelper.setSchemaLocationsToDocument(
                        envelopeDoc,
                        Sets.newHashSet(N52XmlHelper.getSchemaLocationForSOAP12(),
                                        N52XmlHelper.getSchemaLocationForOWS110Exception()));
            } else {
                action = soap.getSoapAction();

                final XmlObject bodyContent = getBodyContent(soap);
                String value = null;
                Node nodeToRemove = null;
                final NamedNodeMap attributeMap = bodyContent.getDomNode().getFirstChild().getAttributes();
                for (int i = 0; i < attributeMap.getLength(); i++) {
                    final Node node = attributeMap.item(i);
                    if (node.getLocalName().equals(W3CConstants.AN_SCHEMA_LOCATION)) {
                        value = node.getNodeValue();
                        nodeToRemove = node;
                    }
                }
                if (nodeToRemove != null) {
                    attributeMap.removeNamedItem(nodeToRemove.getNodeName());
                }
                final Set<SchemaLocation> schemaLocations = Sets.newHashSet();
                schemaLocations.add(N52XmlHelper.getSchemaLocationForSOAP12());
                if (value != null && !value.isEmpty()) {
                    String[] split = value.split(" ");
                    for (int i = 0; i <= split.length - 2; i += 2) {
                        schemaLocations.add(new SchemaLocation(split[i], split[i + 1]));
                    }
                }
                N52XmlHelper.setSchemaLocationsToDocument(envelopeDoc, schemaLocations);
                body.set(bodyContent);
            }
        }

        if (soap.getHeader() != null) {
            createHeader(envelope, soap.getHeader(), action);
        } else {
            envelope.addNewHeader();
        }

        // TODO for testing an validating
        // checkAndValidateSoapMessage(envelopeDoc);
        return envelopeDoc;
    }

    private void createHeader(Envelope envelope, List<SoapHeader> headers, String action)
            throws EncodingException {
        Node headerDomNode = envelope.addNewHeader().getDomNode();
        for (SoapHeader header : headers) {
            if (WsaConstants.NS_WSA.equals(header.getNamespace()) && header instanceof WsaActionHeader) {
                ((WsaHeader) header).setValue(action);
            }
            XmlObject xmObject = encodeObjectToXml(header.getNamespace(), header);
            if (xmObject != null) {
                Node ownerDoc = headerDomNode.getOwnerDocument()
                        .importNode(xmObject.getDomNode().getFirstChild(), true);
                headerDomNode.insertBefore(ownerDoc, null);
            }
        }
    }

    private XmlObject createFault(final SoapFault soapFault) {
        final FaultDocument faultDoc = FaultDocument.Factory.newInstance();
        final Fault fault = faultDoc.addNewFault();
        fault.setFaultcode(soapFault.getFaultCode());
        fault.setFaultstring(soapFault.getFaultReason());
        if (soapFault.getDetailText() != null) {
            final XmlString xmlString = XmlString.Factory.newInstance();
            xmlString.setStringValue(soapFault.getDetailText());
            fault.addNewDetail().set(xmlString);
        }
        return faultDoc;
    }

    // see
    // http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ300
    // for more details
    private XmlObject createFaultFromExceptionResponse(final OwsExceptionReport owsExceptionReport)
            throws EncodingException {
        final FaultDocument faultDoc = FaultDocument.Factory.newInstance();
        final Fault fault = faultDoc.addNewFault();
        fault.setFaultcode(SoapConstants.SENDER_FAULT);

        // we encode only the first exception because of OGC#09-001 Section
        // 19.2.3 SOAP 1.2 Fault Binding
        if (!owsExceptionReport.getExceptions().isEmpty()) {
            final CodedException firstException = owsExceptionReport.getExceptions().get(0);
            fault.addNewDetail().set(encodeObjectToXml(OWSConstants.NS_OWS, firstException,
                    EncodingContext.of(XmlBeansEncodingFlags.ENCODE_OWS_EXCEPTION_ONLY)));
        }
        return faultDoc;
    }

    // private void checkAndValidateSoapMessage(XmlObject response) {
    // try {
    // XmlHelper.validateDocument(response);
    // } catch (OwsExceptionReport e) {
    // LOGGER.info("Error while checking SOAP response", e);
    // }
    // }

//    public Soap11Encoder() {
//        super(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
//        LOGGER.debug("Encoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(getKeys()));
//    }
//
//    @Override
//    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
//    public SOAPMessage encode(SoapResponse soapResponse, EncodingContext additionalValues) throws EncodingException {
//        if (soapResponse == null) {
//            throw new UnsupportedEncoderInputException(this, soapResponse);
//        }
//        String soapVersion = soapResponse.getSoapVersion();
//        SOAPMessage soapResponseMessage;
//        String action = null;
//        try {
//            soapResponseMessage = SoapHelper.getSoapMessageForProtocol(soapVersion);
//            if (soapResponse.getSoapFault() != null) {
//                createSOAPFault(soapResponseMessage.getSOAPBody().addFault(), soapResponse.getSoapFault());
//            } else {
//                if (soapResponse.getException() != null) {
//                    action = createSOAPFaultFromExceptionResponse(soapResponseMessage.getSOAPBody().addFault(),
//                            soapResponse.getException());
//                    addSchemaLocationForExceptionToSOAPMessage(soapResponseMessage);
//                } else {
//                    action = createSOAPBody(soapResponseMessage, soapResponse, soapResponse.getSoapAction());
//                }
//            }
//            if (soapResponse.getHeader() != null) {
//                List<SoapHeader> headers = soapResponse.getHeader();
//                for (SoapHeader header : headers) {
//                    if (WsaConstants.NS_WSA.equals(header.getNamespace()) && header instanceof WsaActionHeader) {
//                        ((WsaHeader) header).setValue(action);
//                    }
//                    Encoder<Map<QName, String>, SoapHeader> encoder =
//                            getEncoder(CodingHelper.getEncoderKey(header.getNamespace(), header));
//                    if (encoder != null) {
//                        Map<QName, String> headerElements = encoder.encode(header);
//                        for (Entry<QName, String> entry : headerElements.entrySet()) {
//                            QName qName = entry.getKey();
//                            soapResponseMessage.getSOAPHeader().addChildElement(qName)
//                                    .setTextContent(headerElements.get(qName));
//                        }
//                    }
//                }
//
//            } else {
//                soapResponseMessage.getSOAPHeader().detachNode();
//            }
//            soapResponseMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, String.valueOf(true));
//            return soapResponseMessage;
//        } catch (SOAPException soape) {
//            throw new EncodingException("Error while encoding SOAPMessage!", soape);
//        }
//    }
//
//    private void addSchemaLocationForExceptionToSOAPMessage(SOAPMessage soapResponseMessage) throws SOAPException {
//        SOAPEnvelope envelope = soapResponseMessage.getSOAPPart().getEnvelope();
//        envelope.addNamespaceDeclaration(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
//        StringBuilder schemaLocation = new StringBuilder();
//        schemaLocation.append(envelope.getNamespaceURI());
//        schemaLocation.append(" ");
//        schemaLocation.append(envelope.getNamespaceURI());
//        schemaLocation.append(" ");
//        schemaLocation.append(N52XmlHelper.getSchemaLocationForOWS110Exception());
//        envelope.addAttribute(N52XmlHelper.getSchemaLocationQNameWithPrefix(), schemaLocation.toString());
//    }
}
