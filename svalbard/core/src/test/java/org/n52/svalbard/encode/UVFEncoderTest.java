/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.TimeValuePair;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.series.DefaultPointMetadata;
import org.n52.shetland.ogc.om.series.wml.DefaultTVPMeasurementMetadata;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants.InterpolationType;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.MultiValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TVPValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.response.BinaryAttachmentResponse;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.ogc.sos.response.GlobalObservationResponseValues;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JTSHelper;
import org.n52.shetland.uvf.UVFConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 */
public class UVFEncoderTest {

    private static final long UTC_TIMESTAMP_1 = 43200000l;
    private static final long UTC_TIMESTAMP_0 = -UTC_TIMESTAMP_1;
    private UVFEncoder encoder;
    private GetObservationResponse responseToEncode;
    private String obsPropIdentifier = "test-obs-prop-identifier";
    private String foiIdentifier = "test-foi-identifier";
    final String foiName = "test-foi-name";
    private String unit = "test-unit";

    @BeforeEach
    public void initObjects() throws OwsExceptionReport, ParseException {
        encoder = new UVFEncoder();

        final OmObservation omObservation = new OmObservation();
        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        omObservation.setObservationID("1");

        // Observed Property
        String valueType = "test-obs-prop-value-type";
        String description = "test-obs-prop-description";
        AbstractPhenomenon observableProperty = new OmObservableProperty(
                obsPropIdentifier,
                description,
                unit,
                valueType);
        observationConstellation.setObservableProperty(observableProperty);

        // Feature Of Interest
        CodeWithAuthority featureIdentifier = new CodeWithAuthority(foiIdentifier);
        AbstractFeature featureOfInterest = new SamplingFeature(featureIdentifier);
        featureOfInterest.addName(new CodeType(foiName));
        int srid = 4326;
        String geomWKT = "POINT(51.9350382 7.6521225)";
        final Geometry point = JTSHelper.createGeometryFromWKT(geomWKT, srid);
        ((SamplingFeature) featureOfInterest).setGeometry(point);
        observationConstellation.setFeatureOfInterest(featureOfInterest);

        // value
        final String uomId = "test-uom";
        final double testValue = 52.0;
        Value<?> measuredValue = new QuantityValue(testValue, uomId);

        // timestamps
        Time phenomenonTime = new TimeInstant(new Date(UTC_TIMESTAMP_1));

        // observation value
        ObservationValue<?> value = new SingleObservationValue<>(phenomenonTime, measuredValue);
        value.setDefaultPointMetadata(new DefaultPointMetadata().setDefaultTVPMeasurementMetadata(
                new DefaultTVPMeasurementMetadata().setInterpolationtype(InterpolationType.Continuous)));
        omObservation.setValue(value);

        // observation type
        observationConstellation.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);

        // Final package
        omObservation.setObservationConstellation(observationConstellation);
        responseToEncode = new GetObservationResponse();
        responseToEncode.setObservationCollection(ObservationStream.of(omObservation));
    }

    @Test
    public void shouldThrowExceptionOnWrongInput() throws EncodingException {
        final Object objToEncode = new Object();

        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.encode(objToEncode);
        });
        assertEquals("Encoder " + encoder.getClass().getSimpleName() + " can not encode '"
                + objToEncode.getClass().getName() + "'", assertThrows.getMessage());
    }

    @Test
    public void shouldEncodeGetObservationResponse() throws EncodingException {
        BinaryAttachmentResponse encodedResponse = encoder.encode(responseToEncode);

        assertThat(encodedResponse, IsNot.not(CoreMatchers.nullValue()));
        final String[] split = new String(encodedResponse.getBytes()).split("\n");
        assertTrue(split.length >= 10, "Expected >= 10 elements in array, got " + split.length);
    }

    @Test
    public void shouldEncodeFunctionInterpretationLine() throws EncodingException {
        assertThat(getResponseString()[0],
                Is.is("$ib Funktion-Interpretation: Linie"));
    }

    @Test
    public void shouldEncodeIndexUnitTime() throws EncodingException {
        assertThat(getResponseString()[1],
                Is.is("$sb Index-Einheit: *** Zeit ***"));
    }

    @Test
    public void shouldEncodeMeasurementIdentifier() throws EncodingException {
        final String actual = getResponseString()[2];
        final String expected = "$sb Mess-Groesse: " + obsPropIdentifier
                .substring(obsPropIdentifier.length() - UVFConstants.MAX_IDENTIFIER_LENGTH, obsPropIdentifier.length());

        assertThat(actual, Is.is(expected));
        assertThat(actual.length(), Is.is(33));
    }

    @Test
    public void shouldEncodeUnitOfMeasurement() throws EncodingException {
        final String actual = getResponseString()[3];
        final String expected = "$sb Mess-Einheit: " + unit;

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeMeasurementLocationIdentifier()
            throws EncodingException {
        final String actual = getResponseString()[4];
        final String expected = "$sb Mess-Stellennummer: " + foiIdentifier
                .substring(foiIdentifier.length() - UVFConstants.MAX_IDENTIFIER_LENGTH, foiIdentifier.length());

        assertThat(actual, Is.is(expected));
        assertThat(actual.length(), Is.is(39));
    }

    @Test
    public void shouldEncodeTimeseriesTypeIdentifierTimebased()
            throws EncodingException {
        assertThat(getResponseString()[6], Is.is("*Z"));
    }

    @Test
    public void shouldEncodeTimeseriesIdentifierAndCenturies() throws EncodingException,
            OwsExceptionReport {
        final String actual = getResponseString()[7];
        final String expected = obsPropIdentifier.substring(
            obsPropIdentifier.length() - UVFConstants.MAX_IDENTIFIER_LENGTH,
            obsPropIdentifier.length()) +
            " " + unit + "     " +
            "1900 1900";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeTimeseriesIdentifierAndCenturiesFromStreamingValues() throws
            EncodingException {
        GlobalObservationResponseValues globalValues = new GlobalObservationResponseValues();
        DateTime end = new DateTime(0);
        DateTime start = new DateTime(0);
        Time phenomenonTime = new TimePeriod(start, end);
        globalValues.addPhenomenonTime(phenomenonTime);
        responseToEncode.setGlobalObservationValues(globalValues);
        final String actual = getResponseString()[7];
        final String expected = obsPropIdentifier.substring(
            obsPropIdentifier.length() - UVFConstants.MAX_IDENTIFIER_LENGTH,
            obsPropIdentifier.length()) +
            " " + unit + "     " +
            "1900 1900";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeMeasurementLocationName() throws EncodingException {
        final String actual = getResponseString()[5];
        final String expected = "$sb Mess-Stellenname: " + foiName;

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeMeasurementLocationIdAndCoordinates() throws EncodingException,
            OwsExceptionReport {
        final String actual = getResponseString()[8];
        final String expected = "1              7.6521225 51.9350382          ";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeTemporalBoundingBox() throws EncodingException {
        final String actual = getResponseString()[9];
        final String expected = "70010112007001011200Zeit    ";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeSingleObservationValueAndTimestamp() throws EncodingException,
            OwsExceptionReport {
        final String actual = getResponseString()[10];
        final String expected = "700101120052.0      ";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeShortenedSingleObservationValueAndTimestamp() throws EncodingException,
            OwsExceptionReport {
        OmObservation omObservation = responseToEncode.getObservationCollection().next();
        ((QuantityValue)omObservation.getValue().getValue()).
        setValue(52.1234567890);
        responseToEncode.setObservationCollection(ObservationStream.of(omObservation));
        final String actual = getResponseString()[10];
        final String expected = "700101120052.1234567";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeSingleObservationValueAndEndOfTimePeriodPhenomenonTime() throws
            EncodingException, NoSuchElementException, OwsExceptionReport {
        Time phenomenonTime = new TimePeriod(new Date(UTC_TIMESTAMP_0), new Date(UTC_TIMESTAMP_1));
        OmObservation omObservation = responseToEncode.getObservationCollection().next();
        omObservation.getValue().setPhenomenonTime(phenomenonTime);
        responseToEncode.setObservationCollection(ObservationStream.of(omObservation));
        final String actual = getResponseString()[10];
        final String expected = "700101120052.0      ";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldEncodeMultiObservationValueTimeValuePair() throws EncodingException,
            OwsExceptionReport {
        ObservationValue<MultiValue<List<TimeValuePair>>> mv = new MultiObservationValues<>();
        MultiValue<List<TimeValuePair>> value = new TVPValue();
        value.setUnit(unit);
        TimeValuePair tvp1 = new TimeValuePair(new TimeInstant(new Date(UTC_TIMESTAMP_0)),
                new QuantityValue(52.1234567890));
        TimeValuePair tvp2 = new TimeValuePair(new TimeInstant(new Date(UTC_TIMESTAMP_1)),
                new QuantityValue(52.1234567890));
        List<TimeValuePair> valueList = CollectionHelper.list(tvp1, tvp2);
        value.setValue(valueList);
        mv.setValue(value);
        OmObservation omObservation = responseToEncode.getObservationCollection().next();
        omObservation.setValue(mv);
        responseToEncode.setObservationCollection(ObservationStream.of(omObservation));

        final String[] encodedLines = getResponseString();

        assertThat(encodedLines[8], Is.is("69123112007001011200Zeit    "));
        assertThat(encodedLines[9], Is.is("691231120052.1234567"));
        assertThat(encodedLines[10], Is.is("700101120052.1234567"));
    }

    @Test
    public void shouldEncodeSingleObservationWithNoDataValue() throws
            EncodingException, NoSuchElementException, OwsExceptionReport {
        OmObservation omObservation = responseToEncode.getObservationCollection().next();
        omObservation.getValue().getValue().setValue(null);
        responseToEncode.setObservationCollection(ObservationStream.of(omObservation));
        final String actual = getResponseString()[9];
        final String expected = "7001011200-777      ";

        assertThat(actual, Is.is(expected));
    }

    @Test
    public void shouldNotEncodeUnitOfMeasurementForCountObservations() throws EncodingException, NoSuchElementException, OwsExceptionReport {
        OmObservation omObservation = responseToEncode.getObservationCollection().next();
        omObservation.getObservationConstellation().
                setObservationType(OmConstants.OBS_TYPE_COUNT_OBSERVATION);
        Time phenTime = new TimeInstant(new Date(UTC_TIMESTAMP_1));
        omObservation.setValue(new SingleObservationValue<>(phenTime,
                new CountValue(52)));
        ((OmObservableProperty)omObservation.getObservationConstellation()
                .getObservableProperty()).setUnit(null);
        responseToEncode.setObservationCollection(ObservationStream.of(omObservation));

        final String[] actual = getResponseString();
        final String expected = "$sb Mess-Einheit: " + unit;

        assertThat(Arrays.asList(actual), IsNot.not(CoreMatchers.hasItems(expected)));
    }

    @Test
    public void shouldEncodeTimeseriesIdentifierAndCenturiesWithoutUnitForCountObservations() throws
    EncodingException, NoSuchElementException, OwsExceptionReport {
        OmObservation omObservation = responseToEncode.getObservationCollection().next();
        omObservation.getObservationConstellation().
            setObservationType(OmConstants.OBS_TYPE_COUNT_OBSERVATION);
        Time phenTime = new TimeInstant(new Date(UTC_TIMESTAMP_1));
        omObservation.setValue(new SingleObservationValue<>(phenTime,
                new CountValue(52)));
        ((OmObservableProperty)omObservation.getObservationConstellation()
                .getObservableProperty()).setUnit(null);

        responseToEncode.setObservationCollection(ObservationStream.of(omObservation));
        final String[] actual = getResponseString();
        final String expected = obsPropIdentifier.substring(
            obsPropIdentifier.length() - UVFConstants.MAX_IDENTIFIER_LENGTH,
            obsPropIdentifier.length()) +
            " " + unit + "     " +
            "1970 1970";

        assertThat(Arrays.asList(actual), IsNot.not(CoreMatchers.hasItem(expected)));
    }

    @Test
    public void shouldReturnEmptyFileWhenObservationCollectionIsEmpty() throws EncodingException {
        responseToEncode.setObservationCollection(ObservationStream.empty());

        BinaryAttachmentResponse encodedResponse = encoder.encode(responseToEncode);
        assertThat(encodedResponse.getSize(), Is.is(-1));
    }

    private String[] getResponseString() throws EncodingException {
        return new String(encoder.encode(responseToEncode).getBytes()).split("\n");
    }
}
