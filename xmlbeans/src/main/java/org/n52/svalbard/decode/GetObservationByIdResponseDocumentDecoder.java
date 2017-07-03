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

import net.opengis.sos.x20.GetObservationByIdResponseDocument;
import net.opengis.sos.x20.GetObservationByIdResponseType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.response.GetObservationByIdResponse;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class GetObservationByIdResponseDocumentDecoder
        extends AbstractXmlDecoder<GetObservationByIdResponseDocument, GetObservationByIdResponse>
        implements SosResponseDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetObservationByIdResponseDocumentDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS
            = CodingHelper.decoderKeysForElements(Sos2Constants.NS_SOS_20, GetObservationByIdResponseDocument.class);

    public GetObservationByIdResponseDocumentDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public GetObservationByIdResponse decode(GetObservationByIdResponseDocument doc) throws DecodingException {
        if (doc != null) {
            GetObservationByIdResponse response = new GetObservationByIdResponse();
            setService(response);
            setVersions(response);
            GetObservationByIdResponseType type = doc.getGetObservationByIdResponse();
            response.setExtensions(parseExtensibleResponse(type));
            response.setObservationCollection(ObservationStream.of(parseObservations(type)));
            return response;
        }
        throw new UnsupportedDecoderInputException(this, doc);
    }

    private List<OmObservation> parseObservations(GetObservationByIdResponseType type) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(type.getObservationArray())) {
            List<OmObservation> observations = Lists.newArrayList();
            for (GetObservationByIdResponseType.Observation observation : type.getObservationArray()) {
                observations.add(decodeXmlObject(observation.getOMObservation()));
            }
            return observations;
        }
        return Collections.emptyList();
    }

}
