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
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.HelperValues;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;

import net.opengis.waterml.x20.TimeValuePairType;

/**
 * {@link Encoder} implementation for {@link TimeLocationValueTriple} to
 * {@link TimeValuePairType}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.4.0
 *
 */
public class TimeLocationValueTripleTypeEncoder extends AbstractTimeLocationValueTripleTypeEncoder<TimeValuePairType> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(InspireOMSOConstants.NS_OMSO_30, TimeLocationValueTriple.class);

    @Override
    public Set<EncoderKey> getEncoderKeyType() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public TimeValuePairType encode(TimeLocationValueTriple timeLocationValueTriple)
            throws OwsExceptionReport, UnsupportedEncoderInputException {
        return encode(timeLocationValueTriple, new EnumMap<HelperValues, String>(HelperValues.class));
    }

    @Override
    public TimeValuePairType encode(TimeLocationValueTriple timeLocationValueTriple,
            Map<HelperValues, String> additionalValues) throws OwsExceptionReport, UnsupportedEncoderInputException {
        return encodeTimeLocationValueTriple(timeLocationValueTriple);
    }

}
