/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

/**
 * String request {@link Decoder} for SOS 1.0.0 requests
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public class SosStringDecoderv100 extends AbstractStringRequestDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SosStringDecoderv100.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.xmlStringDecoderKeysForOperationAndMediaType(
            SosConstants.SOS, Sos1Constants.SERVICEVERSION, SosConstants.Operations.GetCapabilities,
            SosConstants.Operations.GetObservation, SosConstants.Operations.GetFeatureOfInterest,
            SosConstants.Operations.GetObservationById, SosConstants.Operations.DescribeSensor);

    public SosStringDecoderv100() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }
}
