/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.n52.iceland.coding.DocumentBuilderProvider;
import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.coding.decode.DecodingException;
import org.n52.iceland.coding.decode.OwsDecodingException;
import org.n52.iceland.coding.decode.XmlNamespaceOperationDecoderKey;
import org.n52.iceland.coding.decode.XmlStringOperationDecoderKey;
import org.n52.iceland.exception.CodedException;
import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.MissingParameterValueException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.ogc.ows.OWSConstants.RequestParams;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.request.Request;
import org.n52.iceland.util.StringHelper;
import org.n52.iceland.util.http.HttpUtils;
import org.n52.iceland.w3c.W3CConstants;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

/**
 * Abstract binding class for XML encoded requests
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractXmlBinding extends SimpleBinding {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractXmlBinding.class);


    private DocumentBuilderProvider documentFactory;

    @Autowired
    public void setDocumentFactory(DocumentBuilderProvider documentFactory) {
        this.documentFactory = documentFactory;
    }

    protected Request decode(HttpServletRequest request) throws OwsExceptionReport {
        String characterEncoding = getCharacterEncoding(request);
        String xmlString = xmlToString(request, characterEncoding);
        LOGGER.debug("XML-REQUEST: {}", xmlString);
        DecoderKey key = getDecoderKey(xmlString, characterEncoding);
        LOGGER.trace("Found decoder key: {}", key);
        Decoder<AbstractServiceRequest<?>, String> decoder = getDecoder(key);
        if (decoder == null) {
            // if this a GetCapabilities request, then the service is not supported
            String opOrType = null;
            Optional<String> service = Optional.empty();
            if (key instanceof XmlNamespaceOperationDecoderKey) {
                XmlNamespaceOperationDecoderKey xmlNamespaceKey = (XmlNamespaceOperationDecoderKey) key;
                opOrType = xmlNamespaceKey.getType();
            } else if (key instanceof XmlStringOperationDecoderKey) {
                XmlStringOperationDecoderKey xmlStringKey = (XmlStringOperationDecoderKey) key;
                opOrType = xmlStringKey.getOperation();
                service = Optional.of(xmlStringKey.getService());
            }
            if (OWSConstants.Operations.GetCapabilities.toString().equalsIgnoreCase(opOrType)) {
                if (service.isPresent()) {
                    throw new InvalidParameterValueException(OWSConstants.GetCapabilitiesParams.service, service.get())
                            .withMessage("The service '%s' is not supported.", service);
                } else {
                    throw new MissingParameterValueException(OWSConstants.GetCapabilitiesParams.service)
                            .withMessage("The parameter '%s' is missing.", OWSConstants.GetCapabilitiesParams.service);
                }
            } else {
                throw new InvalidParameterValueException().withMessage(
                        "No decoder found for incoming message based on derived decoder key: %s\nMessage: %s", key, xmlString);
            }
        } else {
            LOGGER.trace("Using decoder: {}", decoder);
        }
        try {
            return decoder.decode(xmlString);
        } catch (OwsDecodingException ex) {
            throw ex.getCause();
        } catch (DecodingException ex) {
            throw new NoApplicableCodeException().withMessage(ex.getMessage()).causedBy(ex);
        }
    }

    @VisibleForTesting
    protected DecoderKey getDecoderKey(String xmlContent, String characterEncoding) throws CodedException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(xmlContent.getBytes(characterEncoding))) {
            Document document = documentFactory.newDocumentBuilder().parse(stream);
            Element element = document.getDocumentElement();
            element.normalize();
            if (element.hasAttributes() && element.hasAttribute(OWSConstants.RequestParams.service.name())) {
                OperationKey operationKey = getOperationKey(element);
                XmlStringOperationDecoderKey decoderKey = new XmlStringOperationDecoderKey(operationKey, getDefaultContentType());
                return decoderKey;
            } else {
                return getNamespaceOperationDecoderKey(element);
            }

        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new NoApplicableCodeException().causedBy(e).withMessage(
                    "An error occured when parsing the request! Message: %s", e.getMessage());
        }
    }

    private XmlNamespaceOperationDecoderKey getNamespaceOperationDecoderKey(Element element) {
        String nodeName = element.getNodeName();
        if (Strings.isNullOrEmpty(element.getNamespaceURI())) {
            String[] splittedNodeName = nodeName.split(":");
            String elementName;
            String namespace;
            String name;
            String prefix = null;
            if (splittedNodeName.length == 2) {
                prefix = splittedNodeName[0];
                elementName = splittedNodeName[1];
            } else {
                elementName = splittedNodeName[0];
            }
            // TODO get namespace for prefix
            if (Strings.isNullOrEmpty(prefix)) {
                name = W3CConstants.AN_XMLNS;
            } else {
                name = W3CConstants.PREFIX_XMLNS + prefix;
            }
            namespace = element.getAttribute(name);
            return new XmlNamespaceOperationDecoderKey(namespace, elementName);
        } else {
            return new XmlNamespaceOperationDecoderKey(element.getNamespaceURI(), nodeName.substring(nodeName.indexOf(":") + 1));
        }
    }

    protected OperationKey getOperationKey(Element element) {
        String service = null;
        String version = null;
        String operation = null;
        if (element.hasAttributes()) {
            service = Strings.emptyToNull(element.getAttribute(OWSConstants.RequestParams.service.name()));
            version = Strings.emptyToNull(element.getAttribute(OWSConstants.RequestParams.version.name()));
            if (!Strings.isNullOrEmpty(service)) {
                String nodeName = element.getNodeName();
                operation = nodeName.substring(nodeName.indexOf(":") + 1);
            }
        }
        return new OperationKey(service, version, operation);
    }

    protected String xmlToString(HttpServletRequest request, String characterEncoding) throws OwsExceptionReport {
        try {
            if (request.getParameterMap().isEmpty()) {
                return StringHelper.convertStreamToString(HttpUtils.getInputStream(request), characterEncoding);
            } else {
                return parseHttpPostBodyWithParameter(request.getParameterNames(), request.getParameterMap());
            }
        } catch (final IOException ioe) {
            throw new NoApplicableCodeException().causedBy(ioe).withMessage(
                    "Error while reading request! Message: %s", ioe.getMessage());
        }
    }

    private String getCharacterEncoding(HttpServletRequest request) {
        return !Strings.isNullOrEmpty(request.getCharacterEncoding()) ? request.getCharacterEncoding() : "UTF-8";
    }

    /**
     * Parses the HTTP-Post body with a parameter
     *
     * @param paramNames
     *            Parameter names
     * @param parameterMap
     *            Parameter map
     * @return Value of the parameter
     *
     * @throws OwsExceptionReport
     *             * If the parameter is not supported by this service.
     */
    private String parseHttpPostBodyWithParameter(final Enumeration<?> paramNames, final Map<?, ?> parameterMap)
            throws OwsExceptionReport {
        while (paramNames.hasMoreElements()) {
            final String paramName = (String) paramNames.nextElement();
            if (RequestParams.request.name().equalsIgnoreCase(paramName)) {
                final String[] paramValues = (String[]) parameterMap.get(paramName);
                if (paramValues.length == 1) {
                    return paramValues[0];
                } else {
                    throw new NoApplicableCodeException()
                            .withMessage(
                                    "The parameter '%s' has more than one value or is empty for HTTP-Post requests to this service!",
                                    paramName);
                }
            } else {
                throw new NoApplicableCodeException().withMessage(
                        "The parameter '%s' is not supported for HTTP-Post requests to this service!", paramName);
            }
        }
        // FIXME: valid exception
        throw new NoApplicableCodeException();
    }

}
