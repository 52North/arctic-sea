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

import net.opengis.ows.x11.CapabilitiesBaseType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public class CapabilitiesBaseTypeDecoder
        extends AbstractCapabilitiesBaseTypeDecoder<CapabilitiesBaseType, OwsCapabilities> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CapabilitiesBaseTypeDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(OWSConstants.NS_OWS, CapabilitiesBaseType.class);

    public CapabilitiesBaseTypeDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public OwsCapabilities decode(CapabilitiesBaseType cbt) throws DecodingException {
        if (cbt != null) {
            return parseCapabilitiesBaseType(null, cbt);
        }
        return null;
    }

}
