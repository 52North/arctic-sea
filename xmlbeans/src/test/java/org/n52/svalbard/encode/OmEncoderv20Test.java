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

import static org.hamcrest.Matchers.containsString;

import java.util.Arrays;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.OmCompositePhenomenon;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.swe.SweConstants;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.w3c.W3CConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.w3c.dom.Node;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Iterators;

public class OmEncoderv20Test {

    private static final String PROCEDURE = "procedure";
    private static final String OFFERING = "offering";
    private static final String PARENT_OBSERVABLE_PROPERTY = "http://example.tld/phenomenon/parent";
    private static final String CHILD_OBSERVABLE_PROPERTY_5 = "http://example.tld/phenomenon/child/5";
    private static final String CHILD_OBSERVABLE_PROPERTY_4 = "http://example.tld/phenomenon/child/4";
    private static final String CHILD_OBSERVABLE_PROPERTY_3 = "http://example.tld/phenomenon/child/3";
    private static final String CHILD_OBSERVABLE_PROPERTY_2 = "http://example.tld/phenomenon/child/2";
    private static final String CHILD_OBSERVABLE_PROPERTY_1 = "http://example.tld/phenomenon/child/1";
    private static final String CODE_SPACE = "codespace";

    private static final String TOKEN_SEPERATOR = "##";
    private static final String TUPLE_SEPERATOR = "@@";

    protected static final String CHILD_OBSERVABLE_PROPERTY_1_NAME = "child1";
    protected static final String CHILD_OBSERVABLE_PROPERTY_2_NAME = "child2";
    protected static final String CHILD_OBSERVABLE_PROPERTY_3_NAME = "child3";
    protected static final String CHILD_OBSERVABLE_PROPERTY_4_NAME = "child4";
    protected static final String CHILD_OBSERVABLE_PROPERTY_5_NAME = "child5";

    @Rule
    public final ErrorCollector errors = new ErrorCollector();
    private OmEncoderv20 omEncoderv20;

    @Before
    public void setup() {
        EncoderRepository encoderRepository = new EncoderRepository();

        omEncoderv20 = new OmEncoderv20();
        omEncoderv20.setXmlOptions(XmlOptions::new);
        omEncoderv20.setEncoderRepository(encoderRepository);

        GmlEncoderv321 gmlEncoderv321 = new GmlEncoderv321();
        gmlEncoderv321.setEncoderRepository(encoderRepository);
        gmlEncoderv321.setXmlOptions(XmlOptions::new);

        SensorMLEncoderv20 sensorMLEncoderv20 = new SensorMLEncoderv20();
        sensorMLEncoderv20.setXmlOptions(XmlOptions::new);
        sensorMLEncoderv20.setEncoderRepository(encoderRepository);

        SweCommonEncoderv20 sweCommonEncoderv20 = new SweCommonEncoderv20();
        sweCommonEncoderv20.setEncoderRepository(encoderRepository);
        sweCommonEncoderv20.setXmlOptions(XmlOptions::new);

        encoderRepository.setEncoders(Arrays.asList(omEncoderv20, gmlEncoderv321, sensorMLEncoderv20, sweCommonEncoderv20));
        encoderRepository.init();

    }

    @Test
    public void testComplexObservation()
            throws EncodingException {
        OmObservation observation = createComplexObservation();
        XmlObject xb = omEncoderv20.encode(observation, EncodingContext.of(XmlBeansEncodingFlags.DOCUMENT));
        Node node = xb.getDomNode();
        Checker checker = new Checker(new NamespaceContextImpl());
        System.out.println(xb.xmlText());
        errors.checkThat(node, checker.hasXPath("/om:OM_Observation/om:observedProperty[@xlink:href='http://example.tld/phenomenon/parent']"));
        errors.checkThat(node, checker.hasXPath("/om:OM_Observation/om:result/@xsi:type", containsString("DataRecordPropertyType")));
        errors.checkThat(node, checker.hasXPath("/om:OM_Observation/om:result/swe:DataRecord/swe:field[@name='child1']/swe:Quantity[@definition='http://example.tld/phenomenon/child/1']"));
        errors.checkThat(node, checker.hasXPath("/om:OM_Observation/om:result/swe:DataRecord/swe:field[@name='child2']/swe:Boolean[@definition='http://example.tld/phenomenon/child/2']"));
        errors.checkThat(node, checker.hasXPath("/om:OM_Observation/om:result/swe:DataRecord/swe:field[@name='child3']/swe:Count[@definition='http://example.tld/phenomenon/child/3']"));
        errors.checkThat(node, checker.hasXPath("/om:OM_Observation/om:result/swe:DataRecord/swe:field[@name='child4']/swe:Text[@definition='http://example.tld/phenomenon/child/4']"));
        errors.checkThat(node, checker.hasXPath("/om:OM_Observation/om:result/swe:DataRecord/swe:field[@name='child5']/swe:Category[@definition='http://example.tld/phenomenon/child/5']"));
    }


    protected OmObservation createComplexObservation() {
        DateTime now = new DateTime(DateTimeZone.UTC);
        TimeInstant resultTime = new TimeInstant(now);
        TimeInstant phenomenonTime = new TimeInstant(now);
        TimePeriod validTime = new TimePeriod(now.minusMinutes(5), now.plusMinutes(5));
        OmObservation observation = new OmObservation();
        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        observationConstellation.setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority("feature", CODE_SPACE)));
        OmCompositePhenomenon observableProperty = new OmCompositePhenomenon(PARENT_OBSERVABLE_PROPERTY);
        observableProperty.addPhenomenonComponent(new OmObservableProperty(CHILD_OBSERVABLE_PROPERTY_1));
        observableProperty.addPhenomenonComponent(new OmObservableProperty(CHILD_OBSERVABLE_PROPERTY_2));
        observableProperty.addPhenomenonComponent(new OmObservableProperty(CHILD_OBSERVABLE_PROPERTY_3));
        observableProperty.addPhenomenonComponent(new OmObservableProperty(CHILD_OBSERVABLE_PROPERTY_4));
        observationConstellation.setObservableProperty(observableProperty);
        observationConstellation.setObservationType(OmConstants.OBS_TYPE_COMPLEX_OBSERVATION);
        observationConstellation.addOffering(OFFERING);
        AbstractFeature procedure = new SosProcedureDescriptionUnknownType(PROCEDURE);
//        procedure.setIdentifier(new CodeWithAuthority(PROCEDURE, CODE_SPACE));
        observationConstellation.setProcedure(procedure);
        observation.setObservationConstellation(observationConstellation);
        observation.setParameter(null);
        observation.setResultTime(resultTime);
        observation.setTokenSeparator(TOKEN_SEPERATOR);
        observation.setTupleSeparator(TUPLE_SEPERATOR);
        observation.setValidTime(validTime);
        ComplexValue complexValue = new ComplexValue();
        SweDataRecord sweDataRecord = new SweDataRecord();
        SweQuantity sweQuantity = new SweQuantity();
        sweQuantity.setDefinition(CHILD_OBSERVABLE_PROPERTY_1);
        sweQuantity.setUom("unit");
        sweQuantity.setValue(42.0);
        sweDataRecord.addField(new SweField(CHILD_OBSERVABLE_PROPERTY_1_NAME, sweQuantity));
        SweBoolean sweBoolean = new SweBoolean();
        sweBoolean.setValue(Boolean.TRUE);
        sweBoolean.setDefinition(CHILD_OBSERVABLE_PROPERTY_2);
        sweDataRecord.addField(new SweField(CHILD_OBSERVABLE_PROPERTY_2_NAME, sweBoolean));
        SweCount sweCount = new SweCount();
        sweCount.setDefinition(CHILD_OBSERVABLE_PROPERTY_3);
        sweCount.setValue(42);
        sweDataRecord.addField(new SweField(CHILD_OBSERVABLE_PROPERTY_3_NAME, sweCount));
        SweText sweText = new SweText();
        sweText.setDefinition(CHILD_OBSERVABLE_PROPERTY_4);
        sweText.setValue("42");
        sweDataRecord.addField(new SweField(CHILD_OBSERVABLE_PROPERTY_4_NAME, sweText));
        SweCategory sweCategory = new SweCategory();
        sweCategory.setDefinition(CHILD_OBSERVABLE_PROPERTY_5);
        sweCategory.setCodeSpace(CODE_SPACE);
        sweCategory.setValue("52");
        sweDataRecord.addField(new SweField(CHILD_OBSERVABLE_PROPERTY_5_NAME, sweCategory));
        complexValue.setValue(sweDataRecord);
        observation.setValue(new SingleObservationValue<>(phenomenonTime, complexValue));
        return observation;
    }

//    @Before
//    public void init() {
//        Configurator configurator = mock(Configurator.class);
//        when(configurator.getProfileHandler()).thenReturn(new DefaultProfileHandler());
//        Configurator.setInstance(configurator);
//    }

    private class Checker {
        private final NamespaceContext ctx;

        Checker(NamespaceContext ctx) {
            this.ctx = ctx;
        }
        public Matcher<Node> hasXPath(String path) {
            return Matchers.hasXPath(path, ctx);
        }
        public Matcher<Node> hasXPath(String path, Matcher<String> value) {
            return Matchers.hasXPath(path, ctx, value);
        }
    }

    private class NamespaceContextImpl implements NamespaceContext {
        private ImmutableBiMap<String, String> map = ImmutableBiMap
                .<String, String>builder()
                .put(SweConstants.NS_SWE_PREFIX, SweConstants.NS_SWE_20)
                .put(OmConstants.NS_OM_PREFIX, OmConstants.NS_OM_2)
                .put(W3CConstants.NS_XSI_PREFIX, W3CConstants.NS_XSI)
                .put(W3CConstants.NS_XLINK_PREFIX, W3CConstants.NS_XLINK)
                .put(GmlConstants.NS_GML_PREFIX, GmlConstants.NS_GML_32)
                .build();

        @Override
        public String getNamespaceURI(String prefix) {
            return map.get(prefix);
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return map.inverse().get(namespaceURI);
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            return Iterators.singletonIterator(getPrefix(namespaceURI));
        }
    }
}
