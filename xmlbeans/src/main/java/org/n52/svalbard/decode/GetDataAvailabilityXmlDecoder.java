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

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.impl.values.XmlAnyTypeImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.ogc.swes.SwesExtensions;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.XPathConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.XmlDecodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Joiner;

/**
 * {@code Decoder} to handle {@link GetDataAvailabilityRequest}s.
 *
 * @author Christian Autermann
 *
 * @since 4.0.0
 */
public class GetDataAvailabilityXmlDecoder extends AbstractXmlDecoder<XmlObject, GetDataAvailabilityRequest> {

    private static final Logger LOG = LoggerFactory.getLogger(GetDataAvailabilityXmlDecoder.class);

    private static final String BASE_PATH_SOS = getBasePath(XPathConstants.XPATH_PREFIX_SOS_20,
            SosConstants.NS_SOS_PREFIX);

    private static final String BASE_PATH_GDA = getBasePath(GetDataAvailabilityConstants.XPATH_PREFIXES_GDA,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(CodingHelper.decoderKeysForElements(
            Sos2Constants.NS_SOS_20, XmlObject.class), CodingHelper.decoderKeysForElements(
            GetDataAvailabilityConstants.NS_GDA, XmlObject.class), CodingHelper.xmlDecoderKeysForOperation(
            SosConstants.SOS, Sos2Constants.SERVICEVERSION, GetDataAvailabilityConstants.OPERATION_NAME));

    /**
     * Constructs a new {@code GetDataAvailabilityDecoder}.
     */
    public GetDataAvailabilityXmlDecoder() {
        LOG.debug("Decoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public GetDataAvailabilityRequest decode(XmlObject xml) throws DecodingException {
        return parseGetDataAvailability(xml);
    }

    /**
     * Parses a {@code GetDataAvailabilityRequest}.
     *
     * @param xml
     *            the request
     *
     * @return the parsed request
     * @throws DecodingException
     */
    public GetDataAvailabilityRequest parseGetDataAvailability(XmlObject xml) throws DecodingException {
        XmlObject[] roots = xml.selectPath(BASE_PATH_SOS);
        if (roots != null && roots.length > 0) {
            return parseGetDataAvailability(xml, BASE_PATH_SOS, XPathConstants.XPATH_PREFIX_SOS_20,
                    SosConstants.NS_SOS_PREFIX, Sos2Constants.NS_SOS_20);
        } else {
            roots = xml.selectPath(BASE_PATH_GDA);
            if (roots != null && roots.length > 0) {
                return parseGetDataAvailability(xml, BASE_PATH_GDA, GetDataAvailabilityConstants.XPATH_PREFIXES_GDA,
                        GetDataAvailabilityConstants.NS_GDA_PREFIX, GetDataAvailabilityConstants.NS_GDA);
            }
        }
        return new GetDataAvailabilityRequest();
    }

    /**
     * Parse the GetDataAvailability XML request
     *
     * @param xml
     *            GetDataAvailability XML request
     * @param basePath
     *            XPath base path
     * @param xpathPrefix
     *            XPath prefix
     * @param prefix
     *            XML document namespace prefix
     * @param namespace
     *            XML document namespace
     * @return {@code GetDataAvailabilityRequest}
     * @throws DecodingException
     *             If the document could no be parsed
     */
    private GetDataAvailabilityRequest parseGetDataAvailability(XmlObject xml, String basePath, String xpathPrefix,
            String prefix, String namespace) throws DecodingException {
        GetDataAvailabilityRequest request = new GetDataAvailabilityRequest();
        request.setNamespace(namespace);
        XmlObject[] roots = xml.selectPath(basePath);
        if (roots != null && roots.length > 0) {
            XmlObject version = roots[0].selectAttribute(GetDataAvailabilityConstants.SOS_VERSION);
            if (version == null) {
                version = roots[0].selectAttribute(GetDataAvailabilityConstants.VERSION);
            }
            if (version != null) {
                request.setVersion(parseStringValue(version));
            }
            XmlObject service = roots[0].selectAttribute(GetDataAvailabilityConstants.SOS_SERVICE);
            if (service == null) {
                service = roots[0].selectAttribute(GetDataAvailabilityConstants.SERVICE);
            }
            if (service != null) {
            request.setService(parseStringValue(service));
            }
        }

        for (XmlObject x : xml.selectPath(getPath(xpathPrefix, prefix, "observedProperty"))) {
            request.addObservedProperty(parseStringValue(x));
        }
        for (XmlObject x : xml.selectPath(getPath(xpathPrefix, prefix, "procedure"))) {
            request.addProcedure(parseStringValue(x));
        }
        for (XmlObject x : xml.selectPath(getPath(xpathPrefix, prefix, "featureOfInterest"))) {
            request.addFeatureOfInterest(parseStringValue(x));
        }
        for (XmlObject x : xml.selectPath(getPath(xpathPrefix, prefix, "offering"))) {
            request.addOffering(parseStringValue(x));
        }
        request.setExtensions(parseExtensions(xml));
        return request;
    }

    private String parseStringValue(XmlObject xmlObject) {
        if (xmlObject instanceof XmlString) {
            return ((XmlString) xmlObject).getStringValue();
        } else if (xmlObject instanceof XmlAnyURI) {
            return ((XmlAnyURI)xmlObject).getStringValue();
        } else {
            return ((XmlAnyTypeImpl) xmlObject).getStringValue();
        }
    }

    /**
     * Parse swes:extensions
     *
     * @param xml
     *            swes:extension
     * @return parsed {@code SwesExtensions}
     * @throws DecodingException
     *             if the swes:extension could not be parsed
     */
    private SwesExtensions parseExtensions(XmlObject xml) throws DecodingException {
        SwesExtensions extensions = new SwesExtensions();
        for (XmlObject x : xml.selectPath(getPath(XPathConstants.XPATH_PREFIXES_SWES, SwesConstants.NS_SWES_PREFIX, "extension"))) {
            try {
                if (x.getDomNode().hasChildNodes()) {
                    Object obj = decodeXmlElement(XmlObject.Factory.parse(XmlHelper.getNodeFromNodeList(x.getDomNode().getChildNodes())));
                    SwesExtension<?> extension = null;
                    if (!(obj instanceof SwesExtension<?>)) {
                        extension = new SwesExtension<>().setValue(obj);
                        if (isSweAbstractDataComponent(obj)) {
                            extension.setDefinition(((SweAbstractDataComponent) obj).getDefinition());
                        }
                    } else {
                        extension = (SwesExtension<?>) obj;
                    }
                    extensions.addExtension(extension);
                }
            } catch (XmlException xmle) {
                throw new XmlDecodingException("extension", xmle);
            }
        }
        return extensions;
    }

    /**
     * Check if the object is of type {@code SweAbstractDataComponent}
     *
     * @param object
     *            Object to check
     * @return <code>true</code>, if the object is of type
     *         {@code SweAbstractDataComponent}
     */
    private boolean isSweAbstractDataComponent(final Object object) {
        return object instanceof SweAbstractDataComponent && ((SweAbstractDataComponent) object).isSetDefinition();
    }

    /**
     * Create path from values
     *
     * @param xpathPrefix
     *            XPath prefix
     * @param prefix
     *            Namespace prefix
     * @param element
     *            Element name
     * @return XPath path
     */
    private String getPath(String xpathPrefix, String prefix, String element) {
        StringBuilder builder = new StringBuilder();
        builder.append(xpathPrefix);
        builder.append(".//");
        builder.append(prefix);
        builder.append(":");
        builder.append(element);
        return builder.toString();
    }

    private static String getBasePath(String basePath, String prefix) {
        StringBuilder builder = new StringBuilder();
        builder.append(basePath);
        builder.append("/");
        builder.append(prefix);
        builder.append(":");
        builder.append("GetDataAvailability");
        return builder.toString();
    }
}
