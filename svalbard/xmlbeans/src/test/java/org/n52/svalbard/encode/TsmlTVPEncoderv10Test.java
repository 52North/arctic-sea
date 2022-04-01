/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.series.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.MeasurementTimeseriesMetadata;
import org.n52.shetland.ogc.om.series.Metadata;
import org.n52.shetland.ogc.om.series.tsml.DefaultTVPMeasurementMetadata;
import org.n52.shetland.ogc.om.series.tsml.TimeseriesMLConstants;
import org.n52.shetland.ogc.om.series.tsml.TimeseriesMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.MultiValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import net.opengis.gml.x32.ReferenceType;
import net.opengis.tsml.x10.PointMetadataDocument;
import net.opengis.tsml.x10.TimeseriesMetadataType;
import net.opengis.tsml.x10.TimeseriesTVPDocument;
import net.opengis.tsml.x10.TimeseriesTVPType;

/**
 *
 */
public class TsmlTVPEncoderv10Test {

    private static final long UTC_TIMESTAMP = 43200l;

    private TsmlTVPEncoderv10 encoder;

    private ObservationValue<MultiValue<List<TimeValuePair>>> mv;

    private static final String PROCEDURE = "proceduretest";
    private static final String OFFERING = "offeringtest";
    private static final String CODE_SPACE = "codespacetest";

    private static final String TOKEN_SEPERATOR = "##";
    private static final String TUPLE_SEPERATOR = "@@";

    @BeforeEach
    public void initObjects() {
        encoder = new TsmlTVPEncoderv10();
        encoder.setXmlOptions(XmlOptions::new);

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
        encoderRepository.setEncoders(Arrays.asList(
                encoder,
                gmlEncoderv321,
                sensorMLEncoderv20,
                sweCommonEncoderv20,
                samsEncoderv20,
                sweEncoderv20));
        encoderRepository.init();


        encoderRepository.getEncoders().stream()
            .forEach(e -> ((AbstractDelegatingEncoder<?,?>)e).setEncoderRepository(encoderRepository));

        MultiValue<List<TimeValuePair>> value = new TVPValue();
        String unit = "test-unit";
        value.setUnit(unit);

        TimeValuePair tvp1 =
                new TimeValuePair(new TimeInstant(new Date(UTC_TIMESTAMP)), new QuantityValue(52.1234567890));
        TimeValuePair tvp2 =
                new TimeValuePair(new TimeInstant(new Date(UTC_TIMESTAMP + 10000000)), new QuantityValue(25.0987654321));
        List<TimeValuePair> valueList = CollectionHelper.list(tvp1, tvp2);
        value.setValue(valueList);

        mv = new MultiObservationValues<>();
        mv.setValue(value);
    }

    @Test
    public void shouldSetDefaultCumulativeProperty() throws EncodingException {
        XmlObject encodedElement = encoder.encode(mv);

        MatcherAssert.assertThat(encodedElement, CoreMatchers.instanceOf(TimeseriesTVPDocument.class));
        final TimeseriesTVPDocument timeseriesDocument =
                (TimeseriesTVPDocument) encodedElement;
        MatcherAssert.assertThat(timeseriesDocument.getTimeseriesTVP().isSetMetadata(), Is.is(true));
        MatcherAssert.assertThat(timeseriesDocument.getTimeseriesTVP().getMetadata().getTimeseriesMetadata(),
                CoreMatchers.instanceOf(TimeseriesMetadataType.class));
        final TimeseriesMetadataType measurementTimeseriesMetadataType =
                timeseriesDocument.getTimeseriesTVP().getMetadata()
                        .getTimeseriesMetadata();
        MatcherAssert.assertThat(measurementTimeseriesMetadataType.isSetCumulative(), Is.is(true));
        MatcherAssert.assertThat(measurementTimeseriesMetadataType.getCumulative(), Is.is(false));
    }

    @Test
    public void shouldEncodeCumulativeProperty() throws EncodingException {
        mv.setMetadata(new Metadata().setTimeseriesmetadata(new MeasurementTimeseriesMetadata().setCumulative(true)));

        XmlObject encodedElement = encoder.encode(mv);

        MatcherAssert.assertThat(((TimeseriesTVPDocument) encodedElement).getTimeseriesTVP()
                .getMetadata().getTimeseriesMetadata().getCumulative(), Is.is(true));
    }

    @Test
    public void shouldEncodeInterpolationType() throws EncodingException, XmlException {
        final InterpolationType type = TimeseriesMLConstants.InterpolationType.MinPrec;

        mv.setDefaultPointMetadata(new DefaultPointMetadata()
                .setDefaultTVPMeasurementMetadata(new DefaultTVPMeasurementMetadata().setInterpolationtype(type)));

        XmlObject encodedElement = encoder.encode(mv);

        TimeseriesTVPType.DefaultPointMetadata defaultPointMetadata =
                ((TimeseriesTVPDocument) encodedElement).getTimeseriesTVP().getDefaultPointMetadata();
        PointMetadataDocument tvpMeasurementMetadataDocument =
                PointMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getPointMetadata().getInterpolationType();
        MatcherAssert.assertThat(interpolationType.getHref(),
                Is.is("http://www.opengis.net/def/timeseries/InterpolationCode/MinPrec"));
        MatcherAssert.assertThat(interpolationType.getTitle(), Is.is("MinPrec"));
    }

    @Test
    public void shouldEncodeInterpolationTypeContinuousAsDefault() throws EncodingException, XmlException {
        XmlObject encodedElement = encoder.encode(mv);

        TimeseriesTVPType.DefaultPointMetadata defaultPointMetadata =
                ((TimeseriesTVPDocument) encodedElement).getTimeseriesTVP().getDefaultPointMetadata();
        PointMetadataDocument tvpMeasurementMetadataDocument =
                PointMetadataDocument.Factory.parse(defaultPointMetadata.xmlText());
        ReferenceType interpolationType =
                tvpMeasurementMetadataDocument.getPointMetadata().getInterpolationType();
        MatcherAssert.assertThat(interpolationType.getHref(),
                Is.is("http://www.opengis.net/def/timeseries/InterpolationCode/Continuous"));
        MatcherAssert.assertThat(interpolationType.getTitle(), Is.is("Continuous"));
    }

   @Test
    public void shouldEncodeOMObservation() throws EncodingException, XmlException, DecodingException {
        XmlObject encodedElement = encoder.encode(createObservation(), EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));

        MatcherAssert.assertThat(XmlHelper.validateDocument(encodedElement), Is.is(true));
    }

    private OmObservation createObservation() {
        final InterpolationType type = TimeseriesMLConstants.InterpolationType.InstantTotal;
        DateTime now = new DateTime(DateTimeZone.UTC);
        TimeInstant resultTime = new TimeInstant(now);
        TimePeriod validTime = new TimePeriod(now.minusMinutes(5), now.plusMinutes(5));
        OmObservation observation = new OmObservation();
        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        observationConstellation.setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority("feature", CODE_SPACE)));
        observationConstellation.setObservableProperty(new OmObservableProperty("omobservableProperty"));
        observationConstellation.setDefaultPointMetadata(new DefaultPointMetadata()
                .setDefaultTVPMeasurementMetadata(new DefaultTVPMeasurementMetadata().setInterpolationtype(type)));
        observationConstellation.setObservationType(OmConstants.OBS_TYPE_OBSERVATION);
        observationConstellation.addOffering(OFFERING);
        AbstractFeature procedure = new SosProcedureDescriptionUnknownType(PROCEDURE);
        observationConstellation.setProcedure(procedure);
        observation.setObservationConstellation(observationConstellation);
        observation.setParameter(null);
        observation.setResultTime(resultTime);
        observation.setTokenSeparator(TOKEN_SEPERATOR);
        observation.setTupleSeparator(TUPLE_SEPERATOR);
        observation.setValidTime(validTime);
        observation.setValue(mv);
        return observation;
    }
}
