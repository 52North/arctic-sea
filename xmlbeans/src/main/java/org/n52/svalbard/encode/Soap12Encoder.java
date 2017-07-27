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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.CodedException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionCode;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.shetland.w3c.soap.SoapConstants;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapHeader;
import org.n52.shetland.w3c.soap.SoapHelper;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.shetland.w3c.wsa.WsaActionHeader;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.shetland.w3c.wsa.WsaHeader;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.N52XmlHelper;
import org.n52.svalbard.util.OwsHelper;
import org.n52.svalbard.write.Soap12XmlStreamWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.x2003.x05.soapEnvelope.Body;
import org.w3.x2003.x05.soapEnvelope.Envelope;
import org.w3.x2003.x05.soapEnvelope.EnvelopeDocument;
import org.w3.x2003.x05.soapEnvelope.Fault;
import org.w3.x2003.x05.soapEnvelope.FaultDocument;
import org.w3.x2003.x05.soapEnvelope.Faultcode;
import org.w3.x2003.x05.soapEnvelope.Reasontext;
import org.w3.x2003.x05.soapEnvelope.Subcode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

/**
 * Encoder implementation for SOAP 1.2
 *
 * @since 1.0.0
 *
 */
public class Soap12Encoder extends AbstractSoapEncoder<XmlObject, Object>
        implements StreamingEncoder<XmlObject, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Soap12Encoder.class);

    private static final Set<EncoderKey> ENCODER_KEY_TYPES = CodingHelper
            .encoderKeysForElements(SoapConstants.NS_SOAP_12, SoapFault.class, OwsExceptionReport.class);

    public Soap12Encoder() {
        super(SoapConstants.NS_SOAP_12);
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
            return createSOAP12Envelope((SoapResponse) element, additionalValues);
        } else if (element instanceof SoapFault) {
            return createSOAP12Fault((SoapFault) element);
        } else if (element instanceof OwsExceptionReport) {
            return createSOAP12FaultFromExceptionResponse((OwsExceptionReport) element);
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

    private XmlObject createSOAP12Envelope(SoapResponse response, EncodingContext additionalValues)
            throws EncodingException {
        String action = null;
        final EnvelopeDocument envelopeDoc = EnvelopeDocument.Factory.newInstance();
        final Envelope envelope = envelopeDoc.addNewEnvelope();
        final Body body = envelope.addNewBody();
        if (response.getSoapFault() != null) {
            body.set(createSOAP12Fault(response.getSoapFault()));
        } else {
            if (response.getException() != null) {
                if (!response.getException().getExceptions().isEmpty()) {
                    final CodedException firstException = response.getException().getExceptions().get(0);
                    action = getExceptionActionURI(firstException.getCode());
                }
                body.set(createSOAP12FaultFromExceptionResponse(response.getException()));
                N52XmlHelper.setSchemaLocationsToDocument(
                        envelopeDoc,
                        Sets.newHashSet(N52XmlHelper.getSchemaLocationForSOAP12(),
                                        N52XmlHelper.getSchemaLocationForOWS110Exception()));
            } else {
                action = response.getSoapAction();

                final XmlObject bodyContent = getBodyContent(response);
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
                    for (int i = 0; i < split.length; i += 2) {
                        schemaLocations.add(new SchemaLocation(split[i], split[i + 1]));
                    }
                }
                N52XmlHelper.setSchemaLocationsToDocument(envelopeDoc, schemaLocations);
                body.set(bodyContent);
            }
        }

        if (response.getHeader() != null) {
            createSOAP12Header(envelope, response.getHeader(), action);
        } else {
            envelope.addNewHeader();
        }

        // TODO for testing an validating
        // checkAndValidateSoapMessage(envelopeDoc);
        return envelopeDoc;
    }

    private void createSOAP12Header(Envelope envelope, List<SoapHeader> headers, String action)
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

    private XmlObject createSOAP12Fault(final SoapFault soapFault) {
        final FaultDocument faultDoc = FaultDocument.Factory.newInstance();
        final Fault fault = faultDoc.addNewFault();
        fault.addNewCode().setValue(soapFault.getFaultCode());
        final Reasontext addNewText = fault.addNewReason().addNewText();
        addNewText.setLang(soapFault.getLocale().getDisplayLanguage());
        addNewText.setStringValue(soapFault.getFaultReason());
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
    private XmlObject createSOAP12FaultFromExceptionResponse(final OwsExceptionReport owsExceptionReport)
            throws EncodingException {
        final FaultDocument faultDoc = FaultDocument.Factory.newInstance();
        final Fault fault = faultDoc.addNewFault();
        final Faultcode code = fault.addNewCode();
        code.setValue(SOAPConstants.SOAP_SENDER_FAULT);

        // we encode only the first exception because of OGC#09-001 Section
        // 19.2.3 SOAP 1.2 Fault Binding
        if (!owsExceptionReport.getExceptions().isEmpty()) {
            final CodedException firstException = owsExceptionReport.getExceptions().get(0);
            final Subcode subcode = code.addNewSubcode();
            QName qName;
            if (firstException.getCode() != null) {
                qName = OwsHelper.getQNameForLocalName(firstException.getCode().toString());
            } else {
                qName = OwsHelper.getQNameForLocalName(OwsExceptionCode.NoApplicableCode.name());
            }
            subcode.setValue(qName);
            final Reasontext addNewText = fault.addNewReason().addNewText();
            addNewText.setLang(Locale.ENGLISH.getLanguage());
            addNewText.setStringValue(SoapHelper.getSoapFaultReasonText(firstException.getCode()));

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
}
