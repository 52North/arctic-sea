/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import static java.lang.Boolean.TRUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.n52.shetland.inspire.ompr.Process;
import org.n52.shetland.inspire.omso.TrajectoryObservation;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.MultiObservationValues;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.TimeLocationValueTriple;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TLVTValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.shetland.util.JTSHelper;
import org.n52.svalbard.decode.AbtractProcessDecodingTest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import eu.europa.ec.inspire.schemas.omso.x30.TrajectoryObservationType;

public class TrajectoryObservationTypeEncoderTest extends AbtractProcessDecodingTest {

    private static final String PROCEDURE = "procedure";
    private static final String OFFERING = "offering";
    private static final String OBSERVABLE_PROPERTY  = "observableProperty";
    private static final String CODE_SPACE = "codespace";
    private TrajectoryObservationTypeEncoder encoder;

    @BeforeEach
    public void setup() {
        super.initDecoder();
        encoder = new TrajectoryObservationTypeEncoder();
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

        IdentifierTypeEncoder identifierTypeEncoder = new IdentifierTypeEncoder();
        identifierTypeEncoder.setXmlOptions(XmlOptions::new);

        IdentifierPropertyTypeEncoder identifierPropertyTypeEncoder = new IdentifierPropertyTypeEncoder();
        identifierPropertyTypeEncoder.setXmlOptions(XmlOptions::new);

        ProcessDocumentEncoder processDocumentEncoder = new ProcessDocumentEncoder();
        processDocumentEncoder.setXmlOptions(XmlOptions::new);

        ProcessPropertyTypeEncoder processPropertyTypeEncoder = new ProcessPropertyTypeEncoder();
        processPropertyTypeEncoder.setXmlOptions(XmlOptions::new);

        ProcessTypeEncoder processTypeEncoder = new ProcessTypeEncoder();
        processTypeEncoder.setXmlOptions(XmlOptions::new);

        TimeLocationValueTripleTypeEncoder timeLocationValueTripleTypeEncoder = new TimeLocationValueTripleTypeEncoder();
        timeLocationValueTripleTypeEncoder.setXmlOptions(XmlOptions::new);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(encoder,
                gmlEncoderv321,
                sensorMLEncoderv20,
                sweCommonEncoderv20,
                samsEncoderv20,
                sweEncoderv20,
                identifierTypeEncoder,
                identifierPropertyTypeEncoder,
                processDocumentEncoder,
                processPropertyTypeEncoder,
                processTypeEncoder,
                timeLocationValueTripleTypeEncoder));
        encoderRepository.init();

        encoderRepository.getEncoders().stream()
            .forEach(e -> ((AbstractDelegatingEncoder<?,?>)e).setEncoderRepository(encoderRepository));

    }

    @Test
    public void test_multi_quantity() throws EncodingException, ParseException, DecodingException, XmlException, IOException {
        XmlObject encoded = encoder.encode(getQuantityObservation());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
        //System.out.println(encoded.xmlText(encoder.getXmlOptions()));
        assertThat(encoded, instanceOf(TrajectoryObservationType.class));
    }

    @Test
    public void test_multi_count() throws EncodingException, ParseException, DecodingException, XmlException, IOException {
        XmlObject encoded = encoder.encode(getCountObservation());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
        //System.out.println(encoded.xmlText(encoder.getXmlOptions()));
        assertThat(encoded, instanceOf(TrajectoryObservationType.class));
    }

    @Test
    public void test_multi_categoy() throws EncodingException, ParseException, DecodingException, XmlException, IOException {
        XmlObject encoded = encoder.encode(getCategoricalObservation());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
        //System.out.println(encoded.xmlText(encoder.getXmlOptions()));
        assertThat(encoded, instanceOf(TrajectoryObservationType.class));
    }

    private OmObservation createObservation() throws DecodingException, XmlException, IOException {
        DateTime now = new DateTime(DateTimeZone.UTC);
        TimeInstant resultTime = new TimeInstant(now);
        TrajectoryObservation observation = new TrajectoryObservation();
        observation.setObservationID("123");
        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        observationConstellation
                .setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority("feature", CODE_SPACE)));
        OmObservableProperty observableProperty = new OmObservableProperty(OBSERVABLE_PROPERTY);
        observationConstellation.setObservableProperty(observableProperty);
        observationConstellation.addOffering(OFFERING);
        Process procedure = createProcessFromFile();
        observationConstellation.setProcedure(procedure);
        observation.setObservationConstellation(observationConstellation);
        observation.setResultTime(resultTime);
        return observation;
    }

    private OmObservation getQuantityObservation() throws EncodingException, ParseException, DecodingException, XmlException, IOException {
        MultiObservationValues<List<TimeLocationValueTriple>> multiObservationValues = new MultiObservationValues<List<TimeLocationValueTriple>>();
        TLVTValue tlvtValue = new TLVTValue();
        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(15.6, "C")));
        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(16.5, "C")));
        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(17.6, "C")));
        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(18.7, "C")));
        multiObservationValues.setValue(tlvtValue);
        OmObservation observation = createObservation();
        observation.setValue(multiObservationValues);
        return observation;
    }

    private OmObservation getCountObservation() throws EncodingException, ParseException, DecodingException, XmlException, IOException {
        MultiObservationValues<List<TimeLocationValueTriple>> multiObservationValues = new MultiObservationValues<List<TimeLocationValueTriple>>();
        TLVTValue tlvtValue = new TLVTValue();
        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(15)));
        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(16)));
        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(17)));
        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(18)));
        multiObservationValues.setValue(tlvtValue);
        OmObservation observation = createObservation();
        observation.setValue(multiObservationValues);
        return observation;
    }

    private OmObservation getCategoricalObservation() throws EncodingException, ParseException, DecodingException, XmlException, IOException {
        MultiObservationValues<List<TimeLocationValueTriple>> multiObservationValues = new MultiObservationValues<List<TimeLocationValueTriple>>();
        TLVTValue tlvtValue = new TLVTValue();
        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_1", "test_voc")));
        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_1", "test_voc")));
        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_3", "test_voc")));
        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_4", "test_voc")));
        multiObservationValues.setValue(tlvtValue);
        OmObservation observation = createObservation();
        observation.setValue(multiObservationValues);
        return observation;
    }

    private TimeLocationValueTriple getTimeLocationValueTriple(Value<?> value) throws EncodingException, ParseException {
        return new TimeLocationValueTriple(new TimeInstant(new DateTime()), value, getGeometry() );
    }

    private Geometry getGeometry() throws ParseException {
    final String wktString = "POINT (52.7 7.52)";
    return JTSHelper.createGeometryFromWKT(wktString, 4326);
}

}
