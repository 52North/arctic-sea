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

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.opengis.sos.x20.ObservationOfferingType;
import net.opengis.sosgda.x10.DataAvailabilityMemberType;
import net.opengis.sosgda.x10.GetDataAvailabilityResponseDocument;
import net.opengis.sosgda.x10.GetDataAvailabilityResponseType;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * XML {@link Decoder} for {@link GetDataAvailabilityResponse}
 *
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 * @since 5.0.0
 *
 */
public class GetDataAvailabilityResponseDecoder
        extends AbstractXmlDecoder<GetDataAvailabilityResponseDocument, GetDataAvailabilityResponse>
        implements SosResponseDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetDataAvailabilityResponseDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS
            = CodingHelper.decoderKeysForElements(GetDataAvailabilityConstants.NS_GDA, GetDataAvailabilityResponseDocument.class);

    public GetDataAvailabilityResponseDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public GetDataAvailabilityResponse decode(GetDataAvailabilityResponseDocument document) throws DecodingException {
        if (document != null) {
            GetDataAvailabilityResponse response = new GetDataAvailabilityResponse();
            setService(response);
            setVersions(response);
            response.setDataAvailabilities(parseDataAvalabilities(document.getGetDataAvailabilityResponse()));
            return response;
        }
        throw new UnsupportedDecoderInputException(this, document);
    }

    private Collection<? extends GetDataAvailabilityResponse.DataAvailability> parseDataAvalabilities(
            GetDataAvailabilityResponseType response) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(response.getDataAvailabilityMemberArray())) {
            List<GetDataAvailabilityResponse.DataAvailability> availabilities = Lists.newArrayList();

            Map<String, TimePeriod> periods = Maps.newHashMap();
            // iterate once to get the phenomenonTime entries
            for (DataAvailabilityMemberType damt : response.getDataAvailabilityMemberArray()) {
                if (damt.getPhenomenonTime().getAbstractTimeObject() != null) {
                    TimePeriod phenomenonTime = decodeXmlElement(damt.getPhenomenonTime().getAbstractTimeObject());
                    periods.put(phenomenonTime.getGmlId(), phenomenonTime);
                }
            }

            for (DataAvailabilityMemberType damt : response.getDataAvailabilityMemberArray()) {
                ReferenceType procedure = decodeXmlElement(damt.getProcedure());
                ReferenceType featureOfInterest = decodeXmlElement(damt.getFeatureOfInterest());
                ReferenceType offering = null;
                ReferenceType observedProperty = decodeXmlElement(damt.getObservedProperty());
                TimePeriod phenomenonTime;
                if (damt.getPhenomenonTime().getAbstractTimeObject() != null) {
                    phenomenonTime = decodeXmlElement(damt.getPhenomenonTime().getAbstractTimeObject());
                } else {
                    String href = damt.getPhenomenonTime().getHref();
                    if (href.startsWith("#")) {
                        href = href.substring(1);
                    }
                    phenomenonTime = periods.get(href);
                }
                availabilities.add(new GetDataAvailabilityResponse.DataAvailability(procedure, observedProperty, featureOfInterest, offering, phenomenonTime));
            }
            return availabilities;
        }
        return null;
    }

}
