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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * {@code Decoder} to handle {@link GetDataAvailabilityRequest}s for version
 * 2.0.
 *
 * @author Christian Autermann
 *
 * @since 4.4.0
 */
public class GetDataAvailabilityV20XmlDecoder
        extends AbstractGetDataAvailabilityXmlDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(GetDataAvailabilityV20XmlDecoder.class);

    private static final String BASE_PATH_GDA = getBasePath(GetDataAvailabilityConstants.XPATH_PREFIXES_GDA_20,
            GetDataAvailabilityConstants.NS_GDA_PREFIX);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(GetDataAvailabilityConstants.NS_GDA_20, XmlObject.class);

    /**
     * Constructs a new {@code GetDataAvailabilityDecoder}.
     */
    public GetDataAvailabilityV20XmlDecoder() {
        LOG.debug("Decoder for the following keys initialized successfully: {}!", Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    /**
     * Parses a {@code GetDataAvailabilityRequest}.
     *
     * @param xml
     *            the request
     *
     * @return the parsed request
     * @throws OwsExceptionReport
     */
    @Override
    public GetDataAvailabilityRequest parseGetDataAvailability(XmlObject xml) throws DecodingException {
        XmlObject[] roots = xml.selectPath(BASE_PATH_GDA);
        if (roots != null && roots.length > 0) {
            return parseGetDataAvailability(xml, BASE_PATH_GDA, GetDataAvailabilityConstants.XPATH_PREFIXES_GDA_20,
                    GetDataAvailabilityConstants.NS_GDA_PREFIX, GetDataAvailabilityConstants.NS_GDA_20);
        }
        return new GetDataAvailabilityRequest().setNamespace(GetDataAvailabilityConstants.NS_GDA_20);
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
     * @throws OwsExceptionReport
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
        for (XmlObject x : xml.selectPath(getPath(xpathPrefix, prefix, "responseFormat"))) {
            request.setResponseFormat(parseStringValue(x));
        }
        request.setExtensions(parseExtensions(xml));
        return request;
    }

}
