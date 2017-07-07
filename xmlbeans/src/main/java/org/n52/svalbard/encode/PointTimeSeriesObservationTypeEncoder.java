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
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.inspire.omso.PointTimeSeriesObservation;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.om.ObservationType;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.PointTimeSeriesObservationXmlStreamWriter;

import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.omso.x30.PointTimeSeriesObservationType;
import net.opengis.om.x20.OMObservationType;

/**
 * {@link Encoder} implementation for {@link PointTimeSeriesObservation} to
 * {@link PointTimeSeriesObservationType}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class PointTimeSeriesObservationTypeEncoder
        extends AbstractOmInspireEncoder {

    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(InspireOMSOConstants.NS_OMSO_30, PointTimeSeriesObservation.class);

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.singletonMap(InspireOMSOConstants.NS_OMSO_30,
                Sets.newHashSet(new ObservationType(InspireOMSOConstants.OBS_TYPE_POINT_TIME_SERIES_OBSERVATION)));
    }

    @Override
    protected XmlObject createResult(OmObservation sosObservation) throws EncodingException {
        return encodeResult(sosObservation.getValue());
    }

    @Override
    protected XmlObject encodeResult(ObservationValue<?> observationValue) throws EncodingException {
        return encodeObjectToXml(WaterMLConstants.NS_WML_20, observationValue);
    }

    @Override
    protected String getObservationType() {
        return InspireOMSOConstants.OBS_TYPE_POINT_TIME_SERIES_OBSERVATION;
    }

    @Override
    public XmlObject encode(Object element, EncodingContext ec) throws EncodingException {
        return super.encode(element, ec);
    }

    @Override
    public void encode(Object objectToEncode, OutputStream outputStream, EncodingContext context)
            throws EncodingException {
        if (objectToEncode instanceof OmObservation) {
            try {
                new PointTimeSeriesObservationXmlStreamWriter(outputStream, context, getEncoderRepository(),
                        XmlOptions::new, (OmObservation) objectToEncode).write();
            } catch (XMLStreamException xmlse) {
                throw new EncodingException("Error while writing element to stream!", xmlse);
            }
        }
        super.encode(objectToEncode, outputStream, context);
    }

    protected OMObservationType createOmObservationType() {
        return PointTimeSeriesObservationType.Factory.newInstance(getXmlOptions());
    }

}
