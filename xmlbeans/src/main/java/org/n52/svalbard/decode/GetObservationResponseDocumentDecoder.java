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
import java.util.List;
import java.util.Set;

import net.opengis.sos.x20.GetObservationResponseDocument;
import net.opengis.sos.x20.GetObservationResponseType;
import net.opengis.sos.x20.GetObservationResponseType.ObservationData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import org.n52.shetland.ogc.om.ObservationStream;

public class GetObservationResponseDocumentDecoder extends
        AbstractXmlDecoder<GetObservationResponseDocument, GetObservationResponse> implements SosResponseDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetObservationResponseDocumentDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(Sos2Constants.NS_SOS_20, GetObservationResponseDocument.class);

    public GetObservationResponseDocumentDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public GetObservationResponse decode(GetObservationResponseDocument gord) throws DecodingException {
        if (gord != null) {
            GetObservationResponse response = new GetObservationResponse();
            setService(response);
            setVersions(response);
            GetObservationResponseType gort = gord.getGetObservationResponse();
            response.setExtensions(parseExtensibleResponse(gort));
            response.setObservationCollection(ObservationStream.of(parseObservtions(gort)));

            return response;
        }
        throw new UnsupportedDecoderInputException(this, gord);
    }

    private List<OmObservation> parseObservtions(GetObservationResponseType gort) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(gort.getObservationDataArray())) {
            List<OmObservation> observations = Lists.newArrayList();
            for (ObservationData od : gort.getObservationDataArray()) {
                observations.add((OmObservation) decodeXmlObject(od.getOMObservation()));
            }
            return observations;
        }
        return Collections.emptyList();
    }

}
