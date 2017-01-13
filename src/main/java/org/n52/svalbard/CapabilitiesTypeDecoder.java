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
package org.n52.svalbard;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.n52.shetland.ogc.filter.FilterCapabilities;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosObservationOffering;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import net.opengis.fes.x20.FilterCapabilitiesDocument;
import net.opengis.sos.x20.CapabilitiesType;
import net.opengis.sos.x20.CapabilitiesType.Contents;
import net.opengis.sos.x20.ContentsType;

public class CapabilitiesTypeDecoder extends AbstractCapabilitiesBaseTypeDecoder implements
        Decoder<SosCapabilities, CapabilitiesType> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CapabilitiesTypeDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper
            .decoderKeysForElements(Sos2Constants.NS_SOS_20, CapabilitiesType.class);

    public CapabilitiesTypeDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!", Joiner.on(", ")
                     .join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public SosCapabilities decode(CapabilitiesType ct) throws DecodingException {
        if (ct != null) {
            OwsCapabilities owsCapabilities = parseCapabilitiesBaseType(SosConstants.SOS, ct);
            FilterCapabilities filterCapabilities = parseFilterCapabilities(ct.getFilterCapabilities());
            Collection<SosObservationOffering> contents = parseContents(ct.getContents());
            return new SosCapabilities(owsCapabilities, filterCapabilities, contents);
        }
        throw new UnsupportedDecoderInputException(this, ct);
    }

    private Collection<SosObservationOffering> parseContents(Contents contents) {
        if (contents == null) {
            return null;
        }
        return parseContents(contents.getContents());
    }

    private Collection<SosObservationOffering> parseContents(ContentsType contents) {
        //TODO parse contents
        return null;
    }

    private FilterCapabilities parseFilterCapabilities(CapabilitiesType.FilterCapabilities filterCapabilities) {
        if (filterCapabilities == null) {
            return null;
        }
        return parseFilterCapabilities(filterCapabilities.getFilterCapabilities());
    }

    private FilterCapabilities parseFilterCapabilities(FilterCapabilitiesDocument.FilterCapabilities filterCapabilities) {
        // TOOD parse filter capabilities
        return null;
    }
}
