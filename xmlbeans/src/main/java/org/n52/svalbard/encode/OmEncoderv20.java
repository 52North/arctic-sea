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
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import net.opengis.om.x20.OMObservationType;

import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.om.AbstractObservationValue;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.CvDiscretePointCoverage;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.HrefAttributeValue;
import org.n52.shetland.ogc.om.values.MultiPointCoverage;
import org.n52.shetland.ogc.om.values.NilTemplateValue;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.QuantityRangeValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.RectifiedGridCoverage;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.SweDataArrayValue;
import org.n52.shetland.ogc.om.values.TLVTValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.TimeRangeValue;
import org.n52.shetland.ogc.om.values.UnknownValue;
import org.n52.shetland.ogc.om.values.XmlValue;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.util.OMHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.ConformanceClasses;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.SweHelper;
import org.n52.svalbard.write.OmV20XmlStreamWriter;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * @since 1.0.0
 *
 */
public class OmEncoderv20 extends AbstractOmEncoderv20 {

    private static final Logger LOGGER = LoggerFactory.getLogger(OmEncoderv20.class);

    private static final Set<EncoderKey> ENCODER_KEYS = CodingHelper.encoderKeysForElements(OmConstants.NS_OM_2,
            OmObservation.class, NamedValue.class, SingleObservationValue.class, MultiObservationValues.class);

    // TODO: change to correct conformance class
    private static final Set<String> CONFORMANCE_CLASSES = new HashSet<>(Arrays
            .asList(ConformanceClasses.OM_V2_MEASUREMENT,
                    ConformanceClasses.OM_V2_CATEGORY_OBSERVATION,
                    ConformanceClasses.OM_V2_COUNT_OBSERVATION,
                    ConformanceClasses.OM_V2_TRUTH_OBSERVATION,
                    ConformanceClasses.OM_V2_GEOMETRY_OBSERVATION,
                    ConformanceClasses.OM_V2_TEXT_OBSERVATION,
                    ConformanceClasses.OM_V2_COMPLEX_OBSERVATION));

    private static final Set<SupportedType> SUPPORTED_TYPES = new HashSet<>(Arrays
            .asList(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION_TYPE,
                    OmConstants.OBS_TYPE_COMPLEX_OBSERVATION_TYPE,
                    OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION_TYPE,
                    OmConstants.OBS_TYPE_CATEGORY_OBSERVATION_TYPE,
                    OmConstants.OBS_TYPE_COUNT_OBSERVATION_TYPE,
                    OmConstants.OBS_TYPE_MEASUREMENT_TYPE,
                    OmConstants.OBS_TYPE_TEXT_OBSERVATION_TYPE,
                    OmConstants.OBS_TYPE_TRUTH_OBSERVATION_TYPE,
                    OmConstants.OBS_TYPE_REFERENCE_OBSERVATION_TYPE));

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS = Collections.singletonMap(
            SosConstants.SOS,
            Collections.singletonMap(Sos2Constants.SERVICEVERSION, Collections.singleton(OmConstants.NS_OM_2)));

    public OmEncoderv20() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.singletonMap(OmConstants.NS_OM_2, getSupportedTypes());
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return true;
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        if (SUPPORTED_RESPONSE_FORMATS.get(service) != null &&
            SUPPORTED_RESPONSE_FORMATS.get(service).get(version) != null) {
            return SUPPORTED_RESPONSE_FORMATS.get(service).get(version);
        }
        return Collections.emptySet();
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
    public String getDefaultFeatureEncodingNamespace() {
        return null;
    }

    @Override
    public MediaType getContentType() {
        return OmConstants.CONTENT_TYPE_OM_2;
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        return Sets.newHashSet(OmConstants.OM_20_SCHEMA_LOCATION);
    }

    @Override
    public XmlObject encode(Object element, EncodingContext additionalValues) throws EncodingException {
        if (element instanceof ObservationValue) {
            return encodeResult((ObservationValue<?>) element);
        }
        return super.encode(element, additionalValues);
    }

    @Override
    public void encode(Object objectToEncode, OutputStream outputStream, EncodingContext ctx)
            throws EncodingException {
        if (objectToEncode instanceof OmObservation) {
            try {
                new OmV20XmlStreamWriter(
                        ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                                .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions),
                        outputStream, (OmObservation) objectToEncode).write();
            } catch (XMLStreamException xmlse) {
                throw new EncodingException("Error while writing element to stream!", xmlse);
            }
        } else {
            super.encode(objectToEncode, ctx);
        }
    }

    @Override
    protected OMObservationType createOmObservationType() {
        return OMObservationType.Factory.newInstance(getXmlOptions());
    }

    @Override
    protected XmlObject createResult(OmObservation sosObservation) throws EncodingException {
        ObservationValue<?> value = sosObservation.getValue();
        // TODO if OM_SWEArrayObservation and get Reget(key)sultEncoding and
        // ResultStructure exists,
        if (value instanceof AbstractObservationValue) {
            AbstractObservationValue<?> abstractObservationValue = (AbstractObservationValue<?>) value;
            abstractObservationValue.setValuesForResultEncoding(sosObservation);
            return encodeResult(abstractObservationValue);
        }
        return null;
    }

    @Override
    protected XmlObject encodeResult(ObservationValue<?> observationValue) throws EncodingException {
        if (observationValue instanceof SingleObservationValue) {
            return createSingleObservationToResult((SingleObservationValue<?>) observationValue);
        } else if (observationValue instanceof MultiObservationValues) {
            return createMultiObservationValueToResult((MultiObservationValues<?>) observationValue);
        }
        return null;
    }

    @Override
    protected void addObservationType(OMObservationType xbObservation, String observationType) {
        if (!Strings.isNullOrEmpty(observationType)) {
            xbObservation.addNewType().setHref(observationType);
        }
    }

    // @Override
    // public String getDefaultFeatureEncodingNamespace() {
    // return SfConstants.NS_SAMS;
    // }
    @Override
    protected String getDefaultProcedureEncodingNamspace() {
        return SensorMLConstants.NS_SML;
    }

    @Override
    protected boolean convertEncodedProcedure() {
        return false;
    }

    @Override
    protected void addAddtitionalInformation(OMObservationType omot, OmObservation observation)
            throws EncodingException {
        // do nothing
    }

    private XmlObject createSingleObservationToResult(final SingleObservationValue<?> observationValue)
            throws EncodingException {
        final String observationType;
        if (observationValue.isSetObservationType()) {
            observationType = observationValue.getObservationType();
        } else {
            observationType = OMHelper.getObservationTypeFor(observationValue.getValue());
        }
        if (observationType.equals(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION)) {
            SweDataArray dataArray = new SweHelper().createSosSweDataArray(observationValue);
            return encodeSWE(dataArray, EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));
        }

        return observationValue.getValue()
                .accept(new ResultValueVisitor(observationType, observationValue.getObservationID()));
    }

    private XmlObject createMultiObservationValueToResult(MultiObservationValues<?> observationValue)
            throws EncodingException {
        // TODO create SosSweDataArray
        SweDataArray dataArray = new SweHelper().createSosSweDataArray(observationValue);

        return encodeObjectToXml(SweConstants.NS_SWE_20, dataArray,
                EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));
    }

    protected XmlString createXmlString() {
        return XmlString.Factory.newInstance(getXmlOptions());
    }

    protected XmlInteger createXmlInteger() {
        return XmlInteger.Factory.newInstance(getXmlOptions());
    }

    protected XmlBoolean createXmlBoolean() {
        return XmlBoolean.Factory.newInstance(getXmlOptions());
    }

    protected XmlObject encodeSWE(Object o) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o);
    }

    protected XmlObject encodeSWE(Object o, EncodingContext additionalValues) throws EncodingException {
        return encodeObjectToXml(SweConstants.NS_SWE_20, o, additionalValues);
    }

    private class ResultValueVisitor
            implements ValueVisitor<XmlObject, EncodingException> {
        private final String observationType;

        private final String observationId;

        ResultValueVisitor(String observationType, String observationId) {
            this.observationType = observationType;
            this.observationId = observationId;
        }

        @Override
        public XmlObject visit(BooleanValue value) throws EncodingException {
            if (observationType.equals(OmConstants.OBS_TYPE_TRUTH_OBSERVATION)) {
                XmlBoolean xbBoolean = createXmlBoolean();
                if (value.isSetValue()) {
                    xbBoolean.setBooleanValue(value.getValue());
                } else {
                    xbBoolean.setNil();
                }
                return xbBoolean;
            } else {
                return null;
            }
        }

        @Override
        public XmlObject visit(CategoryValue value) throws EncodingException {
            if (observationType.equals(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION)) {
                if (value.isSetValue() && !value.getValue().isEmpty()) {
                    return encodeGML(value, EncodingContext.of(XmlBeansEncodingFlags.GMLID,
                            SosConstants.OBS_ID_PREFIX + this.observationId));
                }
            }
            return null;
        }

        @Override
        public XmlObject visit(ComplexValue value) throws EncodingException {
            if (observationType.equals(OmConstants.OBS_TYPE_COMPLEX_OBSERVATION)) {
                if (value.isSetValue()) {
                    return encodeSWE(value.getValue(), EncodingContext.of(XmlBeansEncodingFlags.FOR_OBSERVATION));
                }
            }
            return null;
        }

        @Override
        public XmlObject visit(CountValue value) throws EncodingException {
            if (observationType.equals(OmConstants.OBS_TYPE_COUNT_OBSERVATION)) {
                XmlInteger xbInteger = createXmlInteger();
                if (value.isSetValue() && value.getValue() != Integer.MIN_VALUE) {
                    xbInteger.setBigIntegerValue(new BigInteger(value.getValue().toString()));
                } else {
                    xbInteger.setNil();
                }
                return xbInteger;
            } else {
                return null;
            }
        }

        @Override
        public XmlObject visit(GeometryValue value) throws EncodingException {
            if (observationType.equals(OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION)) {
                if (value.isSetValue()) {
                    return encodeGML(value.getValue(),
                            EncodingContext.empty()
                                    .with(XmlBeansEncodingFlags.GMLID, SosConstants.OBS_ID_PREFIX + this.observationId)
                                    .with(XmlBeansEncodingFlags.PROPERTY_TYPE));
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public XmlObject visit(HrefAttributeValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(NilTemplateValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(QuantityValue value) throws EncodingException {
            if (observationType.equals(OmConstants.OBS_TYPE_MEASUREMENT) ||
                     observationType.equals(WaterMLConstants.OBSERVATION_TYPE_MEASURMENT_TVP) ||
                     observationType.equals(WaterMLConstants.OBSERVATION_TYPE_MEASURMENT_TDR)) {
                if (value.isSetValue()) {
                    return encodeGML(value);
                }
            }
            return null;
        }

        @Override
        public XmlObject visit(QuantityRangeValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(ReferenceValue value) throws EncodingException {
            if (value.isSetValue()) {
                return encodeGML(value.getValue());
            }
            return null;
        }

        @Override
        public XmlObject visit(SweDataArrayValue value) throws EncodingException {
            return encodeSWE(value.getValue(), EncodingContext.of(XmlBeansEncodingFlags.FOR_OBSERVATION));
        }

        @Override
        public XmlObject visit(TVPValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(TextValue value) throws EncodingException {
            if (observationType.equals(OmConstants.OBS_TYPE_TEXT_OBSERVATION)) {
                XmlString xbString = createXmlString();
                if (value.isSetValue()) {
                    xbString.setStringValue(value.getValue());
                } else {
                    xbString.setNil();
                }
                return xbString;
            } else {
                return null;
            }
        }

        @Override
        public XmlObject visit(UnknownValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(TLVTValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(CvDiscretePointCoverage value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(MultiPointCoverage value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(RectifiedGridCoverage value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(ProfileValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(TimeRangeValue value) throws EncodingException {
            return null;
        }

        @Override
        public XmlObject visit(XmlValue<?> value) throws EncodingException {
            if (value.getValue() instanceof XmlObject) {
                return (XmlObject) value.getValue();
            }
            return null;
        }
    }
}
