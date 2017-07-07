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

import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.n52.shetland.inspire.omso.InspireOMSOConstants;
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
import org.n52.shetland.ogc.sensorML.SensorML;
import org.n52.shetland.util.JTSHelper;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.n52.svalbard.util.XmlOptionsHelper;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

import eu.europa.ec.inspire.schemas.omso.x30.TrajectoryObservationType;

public class TrajectoryObservationTypeEncoderTest {

    private static final String PROCEDURE = "procedure";
    private static final String OFFERING = "offering";
    private static final String OBSERVABLE_PROPERTY  = "observableProperty";
    private static final String CODE_SPACE = "codespace";

//    @Before
//    public void init() {
//        Configurator configurator = mock(Configurator.class);
//        when(configurator.getProfileHandler()).thenReturn(new DefaultProfileHandler());
//        Configurator.setInstance(configurator);
//    }
//
//    @Test
//    public void test_multi_quantity() throws EncodingException {
//        XmlObject encoded = encodeObjectToXml(InspireOMSOConstants.NS_OMSO_30, getQuantityObservation());
//        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
//        System.out.println(encoded.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
//        assertThat(encoded, instanceOf(TrajectoryObservationType.class));
//    }
//
//    @Test
//    public void test_multi_count() throws EncodingException {
//        XmlObject encoded = encodeObjectToXml(InspireOMSOConstants.NS_OMSO_30, getCountObservation());
//        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
//        System.out.println(encoded.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
//        assertThat(encoded, instanceOf(TrajectoryObservationType.class));
//    }
//
//    @Test
//    public void test_multi_categoy() throws EncodingException {
//        XmlObject encoded = encodeObjectToXml(InspireOMSOConstants.NS_OMSO_30, getCategoricalObservation());
//        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
//        System.out.println(encoded.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
//        assertThat(encoded, instanceOf(TrajectoryObservationType.class));
//    }
//
//    private OmObservation createObservation() {
//        DateTime now = new DateTime(DateTimeZone.UTC);
//        TimeInstant resultTime = new TimeInstant(now);
//        TrajectoryObservation observation = new TrajectoryObservation();
//        observation.setObservationID("123");
//        OmObservationConstellation observationConstellation = new OmObservationConstellation();
//        observationConstellation
//                .setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority("feature", CODE_SPACE)));
//        OmObservableProperty observableProperty = new OmObservableProperty(OBSERVABLE_PROPERTY);
//        observationConstellation.setObservableProperty(observableProperty);
//        observationConstellation.addOffering(OFFERING);
//        SensorML procedure = new SensorML();
//        procedure.setIdentifier(new CodeWithAuthority(PROCEDURE, CODE_SPACE));
//        observationConstellation.setProcedure(procedure);
//        observation.setObservationConstellation(observationConstellation);
//        observation.setResultTime(resultTime);
//        return observation;
//    }
//
//    private OmObservation getQuantityObservation() throws EncodingException {
//        MultiObservationValues<List<TimeLocationValueTriple>> multiObservationValues = new MultiObservationValues<List<TimeLocationValueTriple>>();
//        TLVTValue tlvtValue = new TLVTValue();
//        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(15.6, "C")));
//        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(16.5, "C")));
//        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(17.6, "C")));
//        tlvtValue.addValue(getTimeLocationValueTriple(new QuantityValue(18.7, "C")));
//        multiObservationValues.setValue(tlvtValue);
//        OmObservation observation = createObservation();
//        observation.setValue(multiObservationValues);
//        return observation;
//    }
//
//    private OmObservation getCountObservation() throws EncodingException {
//        MultiObservationValues<List<TimeLocationValueTriple>> multiObservationValues = new MultiObservationValues<List<TimeLocationValueTriple>>();
//        TLVTValue tlvtValue = new TLVTValue();
//        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(15)));
//        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(16)));
//        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(17)));
//        tlvtValue.addValue(getTimeLocationValueTriple(new CountValue(18)));
//        multiObservationValues.setValue(tlvtValue);
//        OmObservation observation = createObservation();
//        observation.setValue(multiObservationValues);
//        return observation;
//    }
//
//    private OmObservation getCategoricalObservation() throws EncodingException {
//        MultiObservationValues<List<TimeLocationValueTriple>> multiObservationValues = new MultiObservationValues<List<TimeLocationValueTriple>>();
//        TLVTValue tlvtValue = new TLVTValue();
//        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_1", "test_voc")));
//        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_1", "test_voc")));
//        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_3", "test_voc")));
//        tlvtValue.addValue(getTimeLocationValueTriple(new CategoryValue("test_4", "test_voc")));
//        multiObservationValues.setValue(tlvtValue);
//        OmObservation observation = createObservation();
//        observation.setValue(multiObservationValues);
//        return observation;
//    }
//
//    private TimeLocationValueTriple getTimeLocationValueTriple(Value<?> value) throws EncodingException {
//        return new TimeLocationValueTriple(new TimeInstant(new DateTime()), value, getGeometry() );
//    }
//
//    private Geometry getGeometry() throws ParseException {
//    final String wktString = "POINT (52.7 7.52)";
//    return JTSHelper.createGeometryFromWKT(wktString, 4326);
//}

}
