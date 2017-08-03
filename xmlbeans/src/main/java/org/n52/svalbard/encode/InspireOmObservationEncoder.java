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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.PointTimeSeriesObservationXmlStreamWriter;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * {@link ObservationEncoder} implementation for INSPIRE OM
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireOmObservationEncoder
        extends AbstractXmlEncoder<XmlObject, Object>
        implements ObservationEncoder<XmlObject, Object>, StreamingEncoder<XmlObject, Object> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            CodingHelper.encoderKeysForElements(InspireOMSOConstants.NS_OMSO_30, OmObservation.class);

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS =
            Collections.singletonMap(SosConstants.SOS, Collections.singletonMap(Sos2Constants.SERVICEVERSION,
                    Collections.singleton(InspireOMSOConstants.NS_OMSO_30)));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext ec) throws EncodingException {
        if (element instanceof OmObservation) {
            return encodeInspireOmsoType((OmObservation) element);
        }
        throw new UnsupportedEncoderInputException(this, element);
    }

    @Override
    public void encode(Object objectToEncode, OutputStream outputStream) throws EncodingException {
        encode(objectToEncode, outputStream, EncodingContext.empty());
    }

    @Override
    public void encode(Object element, OutputStream outputStream, EncodingContext ctx) throws EncodingException {
        try {
            if (element instanceof OmObservation && InspireOMSOConstants.OBS_TYPE_POINT_TIME_SERIES_OBSERVATION
                    .equals(((OmObservation) element).getObservationConstellation().getObservationType())) {
                new PointTimeSeriesObservationXmlStreamWriter(
                        ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                                .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions),
                        outputStream, (OmObservation) element).write();
            } else {
                // writeIndent(encodingValues.getIndent(), outputStream);
                encode(element, ctx).save(outputStream, getXmlOptions());
            }
        } catch (IOException | XMLStreamException e) {
            throw new EncodingException("Error while writing element to stream!", e);
        }
    }

    protected XmlObject encodeInspireOmsoType(OmObservation o) throws EncodingException {
        return encodeObjectToXml(InspireOMSOConstants.NS_OMSO_30, o);
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return true;
    }

    @Override
    public boolean shouldObservationsWithSameXBeMerged() {
        return false;
    }

    @Override
    public boolean supportsResultStreamingForMergedValues() {
        return false;
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        if (SUPPORTED_RESPONSE_FORMATS.get(service) != null
                && SUPPORTED_RESPONSE_FORMATS.get(service).get(version) != null) {
            return SUPPORTED_RESPONSE_FORMATS.get(service).get(version);
        }
        return new HashSet<>(0);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Maps.newHashMap();
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(InspireOMSOConstants.NS_OMSO_30, InspireOMSOConstants.NS_OMSO_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(InspireOMSOConstants.OMSO_SCHEMA_LOCATION);
    }
}
