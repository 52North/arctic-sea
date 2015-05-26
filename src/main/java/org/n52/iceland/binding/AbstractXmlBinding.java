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
package org.n52.iceland.binding;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.coding.decode.Decoder;
import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.coding.decode.OperationDecoderKey;
import org.n52.iceland.coding.decode.XmlNamespaceOperationDecoderKey;
import org.n52.iceland.exception.CodedException;
import org.n52.iceland.exception.ows.NoApplicableCodeException;
import org.n52.iceland.ogc.ows.OWSConstants.RequestParams;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.ogc.ows.OwsExceptionReport;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.request.Request;
import org.n52.iceland.util.Constants;
import org.n52.iceland.util.StringHelper;
import org.n52.iceland.util.http.HTTPUtils;
import org.n52.iceland.w3c.W3CConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;

/**
 * Abstract binding class for XML requests
 * 
 * @author Carsten Hollmann <c.hollmann@52north.org>
 * @since 1.0.0
 *
 */
public abstract class AbstractXmlBinding extends SimpleBinding {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractXmlBinding.class);

    protected Request decode(HttpServletRequest request) throws OwsExceptionReport {
        String xmlString = xmlToString(request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("XML-REQUEST: {}", xmlString);
        }
        Decoder<AbstractServiceRequest<?>, String> decoder = getDecoder(getDecoderKey(xmlString));
        return decoder.decode(xmlString);
    }

    @VisibleForTesting
    protected DecoderKey getDecoderKey(String xmlContent) throws CodedException {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(xmlContent.getBytes())) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            Element element = document.getDocumentElement();
            element.normalize();
            if (element.hasAttributes() && element.hasAttribute(OWSConstants.RequestParams.service.name())) {
                return new OperationDecoderKey(getOperationKey(element), getDefaultContentType());
            } else {
                return getNamespaceOperationDecoderKey(element);
            }
            
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new NoApplicableCodeException().causedBy(e).withMessage(
                    "An error occured when parsing the request decoder! Message: %s", e.getMessage());
        }
    }
    
    private XmlNamespaceOperationDecoderKey getNamespaceOperationDecoderKey(Element element) {
        String nodeName = element.getNodeName();
        if (Strings.isNullOrEmpty(element.getNamespaceURI())) {
            String[] splittedNodeName = nodeName.split(Constants.COLON_STRING);
            String elementName, namespace, name;
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
            return new XmlNamespaceOperationDecoderKey(element.getNamespaceURI(), nodeName.substring(nodeName.indexOf(Constants.COLON_STRING) + 1));
        }
    }

    protected OperationKey getOperationKey(Element element) {
        String service = null;
        String version = null;
        String operation = null;
        if (element.hasAttributes()) {
            service = element.getAttribute(OWSConstants.RequestParams.service.name());
            version = element.getAttribute(OWSConstants.RequestParams.version.name());
            if (!Strings.isNullOrEmpty(service)) {
                String nodeName = element.getNodeName();
                operation = nodeName.substring(nodeName.indexOf(Constants.COLON_STRING) + 1);
            }
        }
        return new OperationKey(service, version, operation);
    }

    protected String xmlToString(HttpServletRequest request) throws OwsExceptionReport {
        try {
            if (request.getParameterMap().isEmpty()) {
                return StringHelper.convertStreamToString(HTTPUtils.getInputStream(request),
                        request.getCharacterEncoding());
            } else {
                return parseHttpPostBodyWithParameter(request.getParameterNames(), request.getParameterMap());
            }
        } catch (final IOException ioe) {
            throw new NoApplicableCodeException().causedBy(ioe).withMessage(
                    "Error while reading request! Message: %s", ioe.getMessage());
        }
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
     *             * If the parameter is not supported by this SOS.
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
                                    "The parameter '%s' has more than one value or is empty for HTTP-Post requests by this SOS!",
                                    paramName);
                }
            } else {
                throw new NoApplicableCodeException().withMessage(
                        "The parameter '%s' is not supported for HTTP-Post requests by this SOS!", paramName);
            }
        }
        // FIXME: valid exception
        throw new NoApplicableCodeException();
    }

}
