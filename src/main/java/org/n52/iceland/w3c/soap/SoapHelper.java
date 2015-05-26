/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.w3c.soap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.n52.iceland.ogc.ows.ExceptionCode;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.service.SoapHeader;
//import org.n52.sos.ogc.sos.SosSoapConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

/**
 * Utility class for SOAP requests.
 * 
 * @since 4.0.0
 */
public class SoapHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoapHelper.class);

    /**
     * Checks the HTTP-Header for action or SOAPAction elements.
     * 
     * @param request
     *            HTTP request
     * @return SOAP action element
     */
    public static String checkSoapHeader(HttpServletRequest request) {
        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerNameKey = (String) headerNames.nextElement();
            if (headerNameKey.equalsIgnoreCase("type")) {
                String type = request.getHeader(headerNameKey);
                String[] typeArray = type.split(";");
                for (String string : typeArray) {
                    if (string.startsWith("action")) {
                        String soapAction = string.replace("action=", "");
                        soapAction = soapAction.replace("\"", "");
                        soapAction = soapAction.trim();
                        return soapAction;
                    }
                }
            } else if (headerNameKey.equalsIgnoreCase("SOAPAction")) {
                return request.getHeader(headerNameKey);
            }
        }
        return null;
    }

    /**
     * Get text content from element by namespace.
     * 
     * @param soapHeader
     *            SOAPHeader element
     * @param namespaceURI
     *            Namespace URI
     * @param localName
     *            local name
     * @return Text content.
     */
    public static String getContentFromElement(SOAPHeader soapHeader, String namespaceURI, String localName) {
        String elementContent = null;
        NodeList nodes = soapHeader.getElementsByTagNameNS(namespaceURI, localName);
        for (int i = 0; i < nodes.getLength(); i++) {
            elementContent = nodes.item(i).getTextContent();
        }
        return elementContent;
    }

    /**
     * Creates a SOAP message for SOAP 1.2 or 1.1
     * 
     * @param soapVersion
     *            SOAP version
     * @return Version depending SOAP message
     * @throws SOAPException
     *             if an error occurs.
     */
    public static SOAPMessage getSoapMessageForProtocol(String soapVersion) throws SOAPException {
        return MessageFactory.newInstance(soapVersion).createMessage();
    }

    public static SOAPMessage getSoapMessageForProtocol(String soapVersion, InputStream inputStream)
            throws SOAPException, IOException {
        return MessageFactory.newInstance(soapVersion).createMessage(new MimeHeaders(), inputStream);
    }

    /**
     * Get the reason for a SOAP fault from Exception code
     * 
     * @param exceptionCode
     *            OWS exception code to get reason for.
     * @return Text for SOAP fault reason
     */
    public static String getSoapFaultReasonText(ExceptionCode exceptionCode) {
        if (exceptionCode != null && exceptionCode.getSoapFaultReason() != null) {
            return exceptionCode.getSoapFaultReason();
        } else {
            return OWSConstants.SOAP_REASON_UNKNOWN;
        }
    }

//    public static String checkActionURIWithBodyContent(String soapAction, String operationName)
//            throws OwsExceptionReport {
//        if (soapAction != null && !soapAction.isEmpty()) {
//            if (operationName.equals(Operations.GetCapabilities.name())
//                    && soapAction.equals(SosSoapConstants.REQ_ACTION_GET_CAPABILITIES)) {
//                LOGGER.debug("ActionURI and SOAPBody content are valid!");
//                return SosSoapConstants.RESP_ACTION_GET_CAPABILITIES;
//            } else if (operationName.equals(Operations.DescribeSensor.name())
//                    && soapAction.equals(SosSoapConstants.REQ_ACTION_DESCRIBE_SENSOR)) {
//                LOGGER.debug("ActionURI and SOAPBody content are valid!");
//                return SosSoapConstants.RESP_ACTION_DESCRIBE_SENSOR;
//            } else if (operationName.equals(Operations.GetObservation.name())
//                    && soapAction.equals(SosSoapConstants.REQ_ACTION_GET_OBSERVATION)) {
//                LOGGER.debug("ActionURI and SOAPBody content are valid!");
//                return SosSoapConstants.RESP_ACTION_GET_OBSERVATION;
//            } else {
//                throw new NoApplicableCodeException().withMessage(
//                        "Error while actionURI (%s) is not compatible with the SOAPBody content (%s request)!",
//                        soapAction, operationName);
//            }
//        }
//        return null;
//    }

    private SoapHelper() {
    }

    public static byte[] headerToXML(Map<String, SoapHeader> soapHeader) {
        // TODO Auto-generated method stub
        return null;
    }
}
