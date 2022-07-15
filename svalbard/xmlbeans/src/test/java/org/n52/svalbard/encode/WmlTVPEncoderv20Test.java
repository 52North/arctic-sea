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
package org.n52.svalbard.encode;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmCompositePhenomenon;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.series.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.MeasurementTimeseriesMetadata;
import org.n52.shetland.ogc.om.series.Metadata;
import org.n52.shetland.ogc.om.series.wml.DefaultTVPMeasurementMetadata;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.om.values.MultiValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuality;
import org.n52.shetland.ogc.swe.simpleType.SweQualityHolder;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import net.opengis.gml.x32.ReferenceType;
import net.opengis.waterml.x20.DefaultTVPMeasurementMetadataDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesDocument;
import net.opengis.waterml.x20.MeasurementTimeseriesMetadataType;
import net.opengis.waterml.x20.TVPDefaultMetadataPropertyType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 1.0.0
 */
public class WmlTVPEncoderv20Test {

    private static final long UTC_TIMESTAMP = 43200l;
    private static final String PROCEDURE = "phttp://example.tld/rocedure";
    private static final String OFFERING = "http://example.tld/offering";
    private static final String FEATURE = "http://example.tld/feature";
    private static final String OBSERVABLE_PROPERTY = "http://example.tld/phenomenon";
    private static final String CODE_SPACE = "http://example.tld/codespace";
    private static final DateTime DATE_TIME = new DateTime(UTC_TIMESTAMP);

    private static final String TOKEN_SEPERATOR = "##";
    private static final String TUPLE_SEPERATOR = "@@";

    private static final String RECREATIONAL = "Recreational";

    private WmlTVPEncoderv20 encoder;

    private ObservationValue<MultiValue<List<TimeValuePair>>> mv;

    @BeforeEach
    public void initObjects() {
        encoder = new WmlTVPEncoderv20();
        encoder.setXmlOptions(XmlOptions::new);

        OmEncoderv20 omEncoderv20 = new OmEncoderv20();
        omEncoderv20.setXmlOptions(XmlOptions::new);

        GmlEncoderv321 gmlEncoderv321 = new GmlEncoderv321();
        gmlEncoderv321.setXmlOptions(XmlOptions::new);

        SensorMLEncoderv20 sensorMLEncoderv20 = new SensorMLEncoderv20();
        sensorMLEncoderv20.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv20 sweCommonEncoderv20 = new SweCommonEncoderv20();
        sweCommonEncoderv20.setXmlOptions(XmlOptions::new);

        SamplingEncoderv20 samsEncoderv20 = new SamplingEncoderv20();
        samsEncoderv20.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv20 sweEncoderv20 = new SweCommonEncoderv20();
        sweEncoderv20.setXmlOptions(XmlOptions::new);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(encoder, omEncoderv20, gmlEncoderv321, sensorMLEncoderv20,
                sweCommonEncoderv20, samsEncoderv20, sweEncoderv20));
        encoderRepository.init();

        encoderRepository.getEncoders().stream()
                .forEach(e -> ((AbstractDelegatingEncoder<?, ?>) e).setEncoderRepository(encoderRepository));

        MultiValue<List<TimeValuePair>> value = new TVPValue();
        String unit = "test-unit";
        value.setUnit(unit);
        TimeValuePair tvp1 =
                new TimeValuePair(new TimeInstant(new Date(UTC_TIMESTAMP)), new QuantityValue(52.1234567890));
        List<TimeValuePair> valueList = CollectionHelper.list(tvp1);
        value.setValue(valueList);

        mv = new MultiObservationValues<>();
        mv.setValue(value);
    }

    @Test
    public void shouldSetDefaultCumulativeProperty() throws EncodingException {
        XmlObject encodedElement = encoder.encode(mv);

        assertThat(encodedElement, CoreMatchers.instanceOf(MeasurementTimeseriesDocument.class));
        final MeasurementTimeseriesDocument measurementTimeseriesDocument =
                (MeasurementTimeseriesDocument) encodedElement;
        assertThat(measurementTimeseriesDocument.getTimeseries().isSetMetadata(), Is.is(true));
        assertThat(measurementTimeseriesDocument.getTimeseries().getMetadata().getTimeseriesMetadata(),
                CoreMatchers.instanceOf(MeasurementTimeseriesMetadataType.class));
        final MeasurementTimeseriesMetadataType measurementTimeseriesMetadataType =
                (MeasurementTimeseriesMetadataType) measurementTimeseriesDocument.getTimeseries().getMetadata()
                        .getTimeseriesMetadata();
        assertThat(measurementTimeseriesMetadataType.isSetCumulative(), Is.is(true));
        assertThat(measurementTimeseriesMetadataType.getCumulative(), Is.is(false));
    }

    @Test
    public void shouldEncodeCumulativeProperty() throws EncodingException {
        mv.setMetadata(new Metadata().setTimeseriesmetadata(new MeasurementTimeseriesMetadata().setCumulative(true)));

        XmlObject encodedElement = encoder.encode(mv);

        assertThat(((MeasurementTimeseriesMetadataType) ((MeasurementTimeseriesDocument) encodedElement)
                .getTimeseries().getMetadata().getTimeseriesMetadata()).getCumulative(), Is.is(true));
    }

    @Test
    public void shouldEncodeInterpolationType() throws EncodingException, XmlException {
        final InterpolationType type = WaterMLConstants.InterpolationType.MinPrec;

        mv.setDefaultPointMetadata(new DefaultPointMetadata()
                .setDefaultTVPMeasurementMetadata(new DefaultTVPMeasurementMetadata().setInterpolationtype(type)));

        XmlObject encodedElement = encoder.encode(mv);

        TVPDefaultMetadataPropertyType defaultPointMetadata =
                ((MeasurementTimeseriesDocument) encodedElement).getTimeseries().getDefaultPointMetadataArray(0);
        DefaultTVPMeasurementMetadataDocument tvpMeasurementMetadataDocument =
                DefaultTVPMeasurementMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getDefaultTVPMeasurementMetadata().getInterpolationType();
        assertThat(interpolationType.getHref(), Is.is(WaterMLConstants.InterpolationType.MinPrec.getIdentifier()));
        assertThat(interpolationType.getTitle(), Is.is(WaterMLConstants.InterpolationType.MinPrec.getTitle()));
    }

    @Test
    public void shouldEncodeInterpolationTypeContinuousAsDefault() throws EncodingException, XmlException {
        XmlObject encodedElement = encoder.encode(mv);

        TVPDefaultMetadataPropertyType defaultPointMetadata =
                ((MeasurementTimeseriesDocument) encodedElement).getTimeseries().getDefaultPointMetadataArray(0);
        DefaultTVPMeasurementMetadataDocument tvpMeasurementMetadataDocument =
                DefaultTVPMeasurementMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getDefaultTVPMeasurementMetadata().getInterpolationType();
        assertThat(interpolationType.getHref(), Is.is(WaterMLConstants.InterpolationType.Continuous.getIdentifier()));
        assertThat(interpolationType.getTitle(), Is.is(WaterMLConstants.InterpolationType.Continuous.getTitle()));
    }

    @Test
    public void shouldEncodeQualifier() throws EncodingException, XmlException, DecodingException {
        XmlObject encodedElement = encoder.encode(createQualifierObservation());
        MatcherAssert.assertThat(XmlHelper.validateDocument(encodedElement), Is.is(true));
    }

    @Test
    public void shouldEncodeQualifierStream() throws EncodingException, XmlException, IOException, DecodingException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            encoder.encode(createQualifierObservation(), baos, EncodingContext.empty());
            XmlObject encodedElement = XmlObject.Factory.parse(baos.toString());
            MatcherAssert.assertThat(XmlHelper.validateDocument(encodedElement), Is.is(true));
        }
    }

    @Test
    public void shouldEncodeDetectionLimit() throws EncodingException, XmlException, DecodingException {
        XmlObject encodedElement = encoder.encode(createDetectionLimitObservation());
        MatcherAssert.assertThat(XmlHelper.validateDocument(encodedElement), Is.is(true));
    }

    @Test
    public void shouldEncodeDetectionLimitStream()
            throws EncodingException, XmlException, IOException, DecodingException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            encoder.encode(createDetectionLimitObservation(), baos, EncodingContext.empty());
            XmlObject encodedElement = XmlObject.Factory.parse(baos.toString());
            MatcherAssert.assertThat(XmlHelper.validateDocument(encodedElement), Is.is(true));
        }
    }

    private OmObservation createDetectionLimitObservation() {
        TimeInstant phenomenonTime = new TimeInstant(DATE_TIME);
        OmObservation observation = createObservation();
        QuantityValue value = new QuantityValue();
        value.setUom("unit");
        SweQualityHolder holder = new SweQualityHolder();
        holder.addReference(WaterMLConstants.EN_CENSORED_REASON, createCensoredReference());
        holder.addQuality(createDetectionLimit());
        value.setQuality(holder);
        observation.setValue(new SingleObservationValue<>(phenomenonTime, value));
        return observation;
    }

    private OmObservation createQualifierObservation() {
        TimeInstant phenomenonTime = new TimeInstant(DATE_TIME);
        OmObservation observation = createObservation();
        QuantityValue value = new QuantityValue();
        value.setUom("unit");
        value.setValue(42.0);
        SweQualityHolder holder = new SweQualityHolder();
        holder.addQuality(createQualifier());
        holder.addQuality(createGrade());
        value.setQuality(holder);
        observation.setValue(new SingleObservationValue<>(phenomenonTime, value));
        return observation;
    }

    private OmObservation createObservation() {
        TimeInstant resultTime = new TimeInstant(DATE_TIME);
        TimePeriod validTime = new TimePeriod(DATE_TIME.minusMinutes(5), DATE_TIME.plusMinutes(5));
        OmObservation observation = new OmObservation();
        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        observationConstellation.setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority(FEATURE, CODE_SPACE)));
        OmObservableProperty observableProperty = new OmObservableProperty(OBSERVABLE_PROPERTY);
        observationConstellation.setObservableProperty(observableProperty);
        observationConstellation.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
        observationConstellation.addOffering(OFFERING);
        AbstractFeature procedure = new SosProcedureDescriptionUnknownType(PROCEDURE);
        observationConstellation.setProcedure(procedure);
        observationConstellation.setDefaultPointMetadata(new DefaultPointMetadata());
        observationConstellation.getDefaultPointMetadata()
                .setDefaultTVPMeasurementMetadata(new DefaultTVPMeasurementMetadata());
        observation.setObservationConstellation(observationConstellation);
        observation.setParameter(null);
        observation.setResultTime(resultTime);
        observation.setTokenSeparator(TOKEN_SEPERATOR);
        observation.setTupleSeparator(TUPLE_SEPERATOR);
        observation.setValidTime(validTime);
        return observation;
    }

    private org.n52.shetland.ogc.gml.ReferenceType createCensoredReference() {
        return new org.n52.shetland.ogc.gml.ReferenceType(OGCConstants.BELOW_DETECTION_RANGE,
                "Below threshold of sensor");
    }

    private SweQuality createDetectionLimit() {
        SweQuantity quantity = new SweQuantity();
        quantity.setDefinition("http://www.example.com/sensors/lower_threshold");
        quantity.setDescription("Lower limit for sensor");
        quantity.setUom("m");
        quantity.setValue(new BigDecimal("1.0"));
        return quantity;
    }

    private SweQuality createQualifier() {
        return createText("No Quality or Non-Verifies (NEMS)", "NON-VERIFIED", "200");
    }

    private SweQuality createGrade() {
        return createText(RECREATIONAL, RECREATIONAL, RECREATIONAL);
    }

    private SweText createText(String description, String label, String value) {
        SweText text = new SweText();
        text.setLabel(label);
        text.setDescription(description);
        text.setValue(value);
        return text;
    }

    // TODO add tests für sosObservation or remove duplicate code in
    // WmlTVPEncoderv20

}
