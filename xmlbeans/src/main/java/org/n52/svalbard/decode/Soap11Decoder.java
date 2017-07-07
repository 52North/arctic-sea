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

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapHelper;
import org.n52.shetland.w3c.soap.SoapRequest;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.base.Joiner;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public class Soap11Decoder extends AbstractSoapDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Soap11Decoder.class);
    private static final String SOAP_ACTION = "SOAPAction:";
    private static final QName QN_CLIENT = new QName(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, "Client");

    public Soap11Decoder() {
        super(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE);
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(getKeys()));
    }

    /**
     * Parses SOAP 1.1 Envelope to a SOS internal SOAP request.
     *
     * @param doc Request as xml representation
     *
     * @return SOS internal SOAP request
     *
     * @throws DecodingException if an error occurs.
     */
    @Override
    protected SoapRequest createEnvelope(XmlObject doc) throws DecodingException {
        SoapRequest soapRequest = new SoapRequest(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE,
                                                  SOAPConstants.SOAP_1_1_PROTOCOL);
        String soapAction = "";

        try {
            SOAPMessage soapMessageRequest;
            try {
                soapMessageRequest = SoapHelper.getSoapMessageForProtocol(SOAPConstants.SOAP_1_1_PROTOCOL,
                                                                          doc.newInputStream());
            } catch (IOException | SOAPException ioe) {
                throw new NoApplicableCodeException().causedBy(ioe)
                        .withMessage("Error while parsing SOAPMessage from request string!");
            }
            // FIXME well... soapAction is always "" at this point
            // if SOAPAction is not spec conform, create SOAPFault
            if (soapAction.isEmpty() || !soapAction.startsWith(SOAP_ACTION)) {
                SoapFault fault = new SoapFault();
                fault.setFaultCode(QN_CLIENT);
                fault.setFaultReason("The SOAPAction parameter in the HTTP-Header is missing or not valid!");
                fault.setLocale(Locale.ENGLISH);
                soapRequest.setSoapFault(fault);
                soapRequest.setSoapFault(fault);
            } else {
                // trim SOAPAction value
                soapAction = soapAction.replace("\"", "").replace(" ", "").replace(SOAP_ACTION, "").trim();
            }
            try {
                if (soapMessageRequest.getSOAPHeader() != null) {
                    soapRequest.setSoapHeader(getSoapHeader(soapMessageRequest.getSOAPHeader()));
                }
                soapRequest.setAction(checkSoapAction(soapAction, soapRequest.getSoapHeader()));
                soapRequest.setSoapBodyContent(getSOAPBodyContent(soapMessageRequest));
            } catch (SOAPException | DecodingException soape) {
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
        fault.setFaultCode(QN_CLIENT);
        fault.setLocale(Locale.ENGLISH);
        fault.setFaultReason(de.getMessage());
        SoapRequest r = new SoapRequest(SOAPConstants.URI_NS_SOAP_1_1_ENVELOPE, SOAPConstants.SOAP_1_1_PROTOCOL);
        r.setSoapFault(fault);
        return r;
    }
}
