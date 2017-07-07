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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import org.n52.janmayen.function.Functions;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.soap.SoapHeader;
import org.n52.shetland.w3c.soap.SoapRequest;
import org.n52.shetland.w3c.wsa.WsaActionHeader;
import org.n52.shetland.w3c.wsa.WsaConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.W3cHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public abstract class AbstractSoapDecoder extends AbstractXmlDecoder<XmlObject, SoapRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSoapDecoder.class);

    private final Set<DecoderKey> decoderKeys;

    public AbstractSoapDecoder(String namespace) {
        this.decoderKeys = Collections.<DecoderKey> singleton(new XmlNamespaceDecoderKey(namespace, XmlObject.class));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(decoderKeys);
    }

    @Override
    public SoapRequest decode(XmlObject xmlObject) throws DecodingException {
        try {
            return createEnvelope(xmlObject);
        } catch (DecodingException de) {
            return createFault(de);
        }
    }

    protected abstract SoapRequest createEnvelope(XmlObject xml) throws DecodingException;

    protected abstract SoapRequest createFault(DecodingException xml);

    /**
     * Parses the SOAPBody content to a text representation
     *
     * @param message
     *            SOAP message
     *
     * @return SOAPBody content as text
     *
     *
     * @throws DecodingException
     *             * if an error occurs.
     */
    protected OwsServiceRequest getSOAPBodyContent(SOAPMessage message) throws DecodingException {
        try {
            Document bodyRequestDoc = message.getSOAPBody().extractContentAsDocument();
            String xmlString = W3cHelper.nodeToXmlString(bodyRequestDoc.getDocumentElement());
            return decodeXmlElement(XmlObject.Factory.parse(xmlString, getXmlOptions()));
        } catch (SOAPException | XmlException | IOException e) {
            throw new DecodingException("Error while parsing SOAPMessage body content!", e);
        }
    }

    protected List<SoapHeader> getSoapHeader(SOAPHeader soapHeader) {
        Map<String, List<SOAPHeaderElement>> headersByNamespace = new HashMap<>();
        Iterator<?> headerElements = soapHeader.extractAllHeaderElements();
        while (headerElements.hasNext()) {
            SOAPHeaderElement element = (SOAPHeaderElement) headerElements.next();
            headersByNamespace.computeIfAbsent(element.getNamespaceURI(), Functions.forSupplier(LinkedList::new))
                    .add(element);
        }
        List<SoapHeader> soapHeaders = Lists.newArrayList();
        for (Entry<String, List<SOAPHeaderElement>> key : headersByNamespace.entrySet()) {
            String namespace = key.getKey();
            try {
                Decoder<?, List<SOAPHeaderElement>> decoder =
                        getDecoder(new XmlNamespaceDecoderKey(namespace, SOAPHeaderElement.class));
                if (decoder != null) {
                    Object object = decoder.decode(headersByNamespace.get(namespace));
                    if (object instanceof SoapHeader) {
                        soapHeaders.add((SoapHeader) object);
                    } else if (object instanceof List<?>) {
                        for (Object o : (List<?>) object) {
                            if (o instanceof SoapHeader) {
                                soapHeaders.add((SoapHeader) o);
                            }
                        }
                    }
                } else {
                    LOGGER.info("The SOAP-Header elements for namespace '{}' are not supported by this server!",
                            namespace);
                }
            } catch (DecodingException owse) {
                LOGGER.debug("Requested SOAPHeader element is not supported", owse);
            }
        }
        return soapHeaders;
    }

    protected String checkSoapAction(String soapAction, List<SoapHeader> soapHeaders) {
        if (soapAction != null && !soapAction.isEmpty()) {
            return soapAction;
        } else if (CollectionHelper.isNotEmpty(soapHeaders)) {
            for (SoapHeader soapHeader : soapHeaders) {
                if (WsaConstants.NS_WSA.equals(soapHeader.getNamespace()) && soapHeader instanceof WsaActionHeader) {
                    return ((WsaActionHeader) soapHeader).getValue();
                }
            }
        }
        return null;
    }
}
