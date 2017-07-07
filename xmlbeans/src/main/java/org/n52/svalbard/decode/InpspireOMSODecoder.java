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

import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.om.ObservationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * Encoder for INSPIRE OM Specialised Observations
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InpspireOMSODecoder
        extends AbstractOmDecoderv20 {

    private static final Logger LOGGER = LoggerFactory.getLogger(InpspireOMSODecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = Sets.newHashSet();

    private static final Set<SupportedType> SUPPORTED_TYPES =
            ImmutableSet.of(new ObservationType(InspireOMSOConstants.OBS_TYPE_MULTI_POINT_OBSERVATION),
                    new ObservationType(InspireOMSOConstants.OBS_TYPE_POINT_OBSERVATION),
                    new ObservationType(InspireOMSOConstants.OBS_TYPE_POINT_TIME_SERIES_OBSERVATION),
                    new ObservationType(InspireOMSOConstants.OBS_TYPE_PROFILE_OBSERVATION),
                    new ObservationType(InspireOMSOConstants.OBS_TYPE_TRAJECTORY_OBSERVATION));

    public InpspireOMSODecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

}
