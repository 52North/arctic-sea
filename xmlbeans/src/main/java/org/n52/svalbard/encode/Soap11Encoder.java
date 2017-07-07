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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.n52.shetland.w3c.W3CConstants;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public class Soap11Encoder extends AbstractSoapEncoder<SOAPMessage, SoapResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Soap11Encoder.class);

    public Soap11Encoder() {
        super(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(getKeys()));
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public SOAPMessage encode(SoapResponse soapResponse, EncodingContext additionalValues) throws EncodingException {
        if (soapResponse == null) {
            throw new UnsupportedEncoderInputException(this, soapResponse);
        }
        String soapVersion = soapResponse.getSoapVersion();
        SOAPMessage soapResponseMessage;
        String action = null;
        try {
            soapResponseMessage = SoapHelper.getSoapMessageForProtocol(soapVersion);
            if (soapResponse.getSoapFault() != null) {
                createSOAPFault(soapResponseMessage.getSOAPBody().addFault(), soapResponse.getSoapFault());
            } else {
                if (soapResponse.getException() != null) {
                    action = createSOAPFaultFromExceptionResponse(soapResponseMessage.getSOAPBody().addFault(),
                            soapResponse.getException());
                    addSchemaLocationForExceptionToSOAPMessage(soapResponseMessage);
                } else {
                    action = createSOAPBody(soapResponseMessage, soapResponse, soapResponse.getSoapAction());
                }
            }
            if (soapResponse.getHeader() != null) {
                List<SoapHeader> headers = soapResponse.getHeader();
                for (SoapHeader header : headers) {
                    if (WsaConstants.NS_WSA.equals(header.getNamespace()) && header instanceof WsaActionHeader) {
                        ((WsaHeader) header).setValue(action);
                    }
                    Encoder<Map<QName, String>, SoapHeader> encoder =
                            getEncoder(CodingHelper.getEncoderKey(header.getNamespace(), header));
                    if (encoder != null) {
                        Map<QName, String> headerElements = encoder.encode(header);
                        for (Entry<QName, String> entry : headerElements.entrySet()) {
                            QName qName = entry.getKey();
                            soapResponseMessage.getSOAPHeader().addChildElement(qName)
                                    .setTextContent(headerElements.get(qName));
                        }
                    }
                }

            } else {
                soapResponseMessage.getSOAPHeader().detachNode();
            }
            soapResponseMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, String.valueOf(true));
            return soapResponseMessage;
        } catch (SOAPException soape) {
            throw new EncodingException("Error while encoding SOAPMessage!", soape);
        }
    }

    private void addSchemaLocationForExceptionToSOAPMessage(SOAPMessage soapResponseMessage) throws SOAPException {
        SOAPEnvelope envelope = soapResponseMessage.getSOAPPart().getEnvelope();
        envelope.addNamespaceDeclaration(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI);
        StringBuilder schemaLocation = new StringBuilder();
        schemaLocation.append(envelope.getNamespaceURI());
        schemaLocation.append(" ");
        schemaLocation.append(envelope.getNamespaceURI());
        schemaLocation.append(" ");
        schemaLocation.append(N52XmlHelper.getSchemaLocationForOWS110Exception());
        envelope.addAttribute(N52XmlHelper.getSchemaLocationQNameWithPrefix(), schemaLocation.toString());
    }
}
