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
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import net.opengis.sosgda.x20.GetDataAvailabilityDocument;
import net.opengis.sosgda.x20.GetDataAvailabilityType;

/**
 * {@code Decoder} to handle {@link GetDataAvailabilityRequest}s for version
 * 2.0.
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public class GetDataAvailabilityV20XmlDecoder
        extends AbstractGetDataAvailabilityXmlDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(GetDataAvailabilityV20XmlDecoder.class);

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
     *             If the document could no be parsed
     */
    @Override
    public GetDataAvailabilityRequest parseGetDataAvailability(XmlObject xml)
            throws DecodingException {
        if (xml instanceof GetDataAvailabilityDocument) {
            return parseGetDataAvailability((GetDataAvailabilityDocument) xml);
        }
        return new GetDataAvailabilityRequest().setNamespace(GetDataAvailabilityConstants.NS_GDA_20);
    }

    /**
     * Parse the GetDataAvailability XML request
     *
     * @param xml
     *            GetDataAvailability XML request
     * @return {@code GetDataAvailabilityRequest}
     * @throws OwsExceptionReport
     *             If the document could no be parsed
     */
    private GetDataAvailabilityRequest parseGetDataAvailability(GetDataAvailabilityDocument xml)
            throws DecodingException {
        GetDataAvailabilityRequest request = new GetDataAvailabilityRequest();
        GetDataAvailabilityType gdat = xml.getGetDataAvailability();
        request.setNamespace(XmlHelper.getNamespace(xml));
        request.setResponseFormat(XmlHelper.getNamespace(xml));
        request.setService(gdat.getService());
        request.setVersion(gdat.getVersion());

        if (CollectionHelper.isNotNullOrEmpty(gdat.getObservedPropertyArray())) {
            for (String s : gdat.getObservedPropertyArray()) {
                request.addObservedProperty(s);
            }
        }
        if (CollectionHelper.isNotNullOrEmpty(gdat.getProcedureArray())) {
            for (String s : gdat.getProcedureArray()) {
                request.addProcedure(s);
            }
        }
        if (CollectionHelper.isNotNullOrEmpty(gdat.getFeatureOfInterestArray())) {
            for (String s : gdat.getFeatureOfInterestArray()) {
                request.addFeatureOfInterest(s);
            }
        }
        if (CollectionHelper.isNotNullOrEmpty(gdat.getOfferingArray())) {
            for (String s : gdat.getOfferingArray()) {
                request.addOffering(s);
            }
        }
        if (CollectionHelper.isNotNullOrEmpty(gdat.getResponseFormatArray())) {
            for (String s : gdat.getResponseFormatArray()) {
                request.setResponseFormat(s);
            }
        }
        request.setExtensions(parseExtensibleRequest(gdat));
        return request;
    }

}
