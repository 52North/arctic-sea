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
package org.n52.svalbard.encode;

import java.util.Collections;
import java.util.Set;

import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;

import net.opengis.waterml.x20.TimeValuePairType;

/**
 * {@link Encoder} implementation for {@link TimeLocationValueTriple} to
 * {@link TimeValuePairType}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class TimeLocationValueTripleTypeEncoder
        extends AbstractTimeLocationValueTripleTypeEncoder<TimeValuePairType> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(InspireOMSOConstants.NS_OMSO_30, TimeLocationValueTriple.class);

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public TimeValuePairType encode(TimeLocationValueTriple timeLocationValueTriple)
            throws EncodingException {
        return encode(timeLocationValueTriple, EncodingContext.empty());
    }

    @Override
    public TimeValuePairType encode(TimeLocationValueTriple timeLocationValueTriple, EncodingContext context)
            throws EncodingException {
        return encodeTimeLocationValueTriple(timeLocationValueTriple);
    }

}
