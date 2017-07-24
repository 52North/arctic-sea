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

import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.inspire.omso.MultiPointObservation;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.values.MultiPointCoverage;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import eu.europa.ec.inspire.schemas.omso.x30.MultiPointObservationType;
import net.opengis.om.x20.OMObservationType;

/**
 * {@link Encoder} implementation for {@link MultiPointObservation} to
 * {@link MultiPointObservationType}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class MultiPointObservationTypeEncoder
        extends AbstractOmInspireEncoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiPointObservationTypeEncoder.class);
    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(InspireOMSOConstants.NS_OMSO_30, MultiPointObservation.class);

    public MultiPointObservationTypeEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    protected XmlObject createResult(OmObservation sosObservation) throws EncodingException {
        return encodeResult(sosObservation.getValue());
    }

    @Override
    protected XmlObject encodeResult(ObservationValue<?> observationValue) throws EncodingException {
        if (observationValue.getValue() instanceof MultiPointCoverage) {
            return encodeGML(observationValue.getValue());
        }
        return null;
    }

    @Override
    protected String getObservationType() {
        return InspireOMSOConstants.OBS_TYPE_MULTI_POINT_OBSERVATION;
    }

    @Override
    public XmlObject encode(Object element, EncodingContext ec) throws EncodingException {
        return super.encode(element, ec);
    }

    @Override
    public void encode(Object objectToEncode, OutputStream outputStream, EncodingContext context)
            throws EncodingException {
        super.encode(objectToEncode, outputStream, context);
    }

    protected OMObservationType createOmObservationType() {
        return MultiPointObservationType.Factory.newInstance(getXmlOptions());
    }

}
