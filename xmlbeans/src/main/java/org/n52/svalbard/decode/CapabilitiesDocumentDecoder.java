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

import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import net.opengis.sos.x20.CapabilitiesDocument;

public class CapabilitiesDocumentDecoder extends AbstractXmlDecoder<CapabilitiesDocument, GetCapabilitiesResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetObservationResponseDocumentDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(Sos2Constants.NS_SOS_20, CapabilitiesDocument.class);

    public CapabilitiesDocumentDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public GetCapabilitiesResponse decode(CapabilitiesDocument cd) throws DecodingException {
        if (cd != null) {
            GetCapabilitiesResponse response = new GetCapabilitiesResponse();
            OwsCapabilities capabilities = (OwsCapabilities) decodeXmlObject(cd.getCapabilities());
            response.setCapabilities(capabilities);
            return response;
        }
        throw new UnsupportedDecoderInputException(this, cd);
    }

}
