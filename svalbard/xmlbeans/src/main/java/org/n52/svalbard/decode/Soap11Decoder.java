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
package org.n52.svalbard.decode;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.w3c.soap.AbstractSoap;
import org.n52.shetland.w3c.soap.SoapConstants;
import org.n52.shetland.w3c.soap.SoapFault;
import org.n52.shetland.w3c.soap.SoapHeader;
import org.n52.shetland.w3c.soap.SoapRequest;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlsoap.schemas.soap.envelope.Body;
import org.xmlsoap.schemas.soap.envelope.EnvelopeDocument;
import org.xmlsoap.schemas.soap.envelope.Header;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public class Soap11Decoder extends AbstractSoapDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(Soap11Decoder.class);
    private static final String SOAP_ACTION = "SOAPAction:";
    private static final QName QN_CLIENT = new QName(SoapConstants.NS_SOAP_11, "Client");

    public Soap11Decoder() {
        super(SoapConstants.NS_SOAP_11);
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
    protected AbstractSoap<?> createEnvelope(XmlObject doc) throws DecodingException {
        AbstractSoap<?> soap = null;
        String soapAction = "";
        if (doc instanceof EnvelopeDocument) {
            EnvelopeDocument envDoc = (EnvelopeDocument) doc;
            OwsServiceCommunicationObject bodyContent = getBodyContent(envDoc);
            soap = bodyContent instanceof OwsServiceRequest
                    ? new SoapRequest(SoapConstants.NS_SOAP_11, SoapConstants.SOAP_1_1_VERSION)
                            .setBodyContent((OwsServiceRequest) bodyContent)
                    : new SoapResponse(SoapConstants.NS_SOAP_11, SoapConstants.SOAP_1_1_VERSION)
                            .setBodyContent((OwsServiceResponse) bodyContent);

            // FIXME well... soapAction is always "" at this point
            // if SOAPAction is not spec conform, create SOAPFault
            if (soap instanceof SoapRequest && (soapAction.isEmpty() || !soapAction.startsWith(SOAP_ACTION))) {
                SoapFault fault = new SoapFault();
                fault.setFaultCode(QN_CLIENT);
                fault.setFaultReason("The SOAPAction parameter in the HTTP-Header is missing or not valid!");
                fault.setLocale(Locale.ENGLISH);
                ((SoapRequest) soap).setSoapFault(fault);
            } else {
                soap.setAction(checkSoapAction(soapAction, soap.getSoapHeader()));
            }
            if (envDoc.getEnvelope()
                    .isSetHeader()) {
                soap.setSoapHeader(getSoapHeader(envDoc.getEnvelope()
                        .getHeader()));
            }
            soap.setAction(checkSoapAction(soapAction, soap.getSoapHeader()));
        } else {
            throw new UnsupportedDecoderInputException(this, doc);
        }
        return soap;
    }

    @Override
    protected SoapRequest createFault(DecodingException de) {
        SoapFault fault = new SoapFault();
        fault.setFaultCode(QN_CLIENT);
        fault.setLocale(Locale.ENGLISH);
        fault.setFaultReason(getFaultReasons(de));
        SoapRequest r = new SoapRequest(SoapConstants.NS_SOAP_11, SoapConstants.SOAP_1_1_VERSION);
        r.setSoapFault(fault);
        return r;
    }

    private OwsServiceCommunicationObject getBodyContent(EnvelopeDocument doc) throws DecodingException {
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

    protected List<SoapHeader> getSoapHeader(Header header) {
        List<SoapHeader> soapHeaders = Lists.newArrayList();
        XmlCursor cursor = null;
        try {
            cursor = header.newCursor();
            if (cursor.toFirstChild()) {
                do {
                    Object object = decodeXmlElement(XmlObject.Factory.parse(cursor.xmlText(getXmlOptions())));
                    if (object instanceof SoapHeader) {
                        soapHeaders.add((SoapHeader) object);
                    } else if (object instanceof List<?>) {
                        for (Object o : (List<?>) object) {
                            if (o instanceof SoapHeader) {
                                soapHeaders.add((SoapHeader) o);
                            }
                        }
                    }
                } while (cursor.toNextSibling());
            }
        } catch (DecodingException | XmlException e) {
            LOGGER.debug("Requested SoapHeader element is not supported", e);
        } finally {
            if (cursor != null) {
                cursor.dispose();
            }
        }
        return soapHeaders;
    }
}
