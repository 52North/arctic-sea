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

import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.ProfileLevel;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.om.values.Value;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.collect.Lists;

import net.opengis.gwmlWell.x22.GWGeologyLogCoveragePropertyType;

public class GWGeologyLogCoveragePropertyEncoderTest {

    private GWGeologyLogCoveragePropertyTypeEncoder encoder;

    @BeforeEach
    public void setup() {
        EncoderRepository encoderRepository = new EncoderRepository();
        encoder = new GWGeologyLogCoveragePropertyTypeEncoder();
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setEncoderRepository(encoderRepository);

        LogValuePropertyTypeEncoder logValuePropertyTypeEncoder = new LogValuePropertyTypeEncoder();
        logValuePropertyTypeEncoder.setXmlOptions(XmlOptions::new);
        logValuePropertyTypeEncoder.setEncoderRepository(encoderRepository);

        SweCommonEncoderv20 sweCommonEncoderv20 = new SweCommonEncoderv20();
        sweCommonEncoderv20.setXmlOptions(XmlOptions::new);
        sweCommonEncoderv20.setEncoderRepository(encoderRepository);

        encoderRepository.setEncoders(Arrays.asList(encoder, logValuePropertyTypeEncoder, sweCommonEncoderv20));
        encoderRepository.init();
    }

    @Test
    public void should_encode_GWGeologyLogCoverage_only_value() throws EncodingException, DecodingException {
        ProfileValue coverage = createGWGeologyLogCoverage(false, false);
        XmlObject encodedObject = encoder.encode(coverage);
        assertThat(XmlHelper.validateDocument(encodedObject), is(TRUE));
        assertThat(encodedObject, instanceOf(GWGeologyLogCoveragePropertyType.class));
    }

    @Test
    public void should_encode_GWGeologyLogCoverage_full() throws EncodingException, DecodingException {
        ProfileValue coverage = createGWGeologyLogCoverage(true, true);
        XmlObject encodedObject = encoder.encode(coverage);
        assertThat(XmlHelper.validateDocument(encodedObject), is(TRUE));
        assertThat(encodedObject, instanceOf(GWGeologyLogCoveragePropertyType.class));
    }

    @Test
    public void should_encode_GWGeologyLogCoverage_fromDepth() throws EncodingException, DecodingException {
        ProfileValue coverage = createGWGeologyLogCoverage(true, false);
        XmlObject encodedObject = encoder.encode(coverage);
        assertThat(XmlHelper.validateDocument(encodedObject), is(TRUE));
        assertThat(encodedObject, instanceOf(GWGeologyLogCoveragePropertyType.class));
    }

    @Test
    public void should_encode_GWGeologyLogCoverage_toDepth() throws EncodingException, DecodingException {
        ProfileValue coverage = createGWGeologyLogCoverage(false, true);
        XmlObject encodedObject = encoder.encode(coverage);
        assertThat(XmlHelper.validateDocument(encodedObject), is(TRUE));
        assertThat(encodedObject, instanceOf(GWGeologyLogCoveragePropertyType.class));
    }

    private ProfileValue createGWGeologyLogCoverage(boolean fromDepth, boolean toDepth) {
        ProfileValue coverage = new ProfileValue("");
        coverage.addValue(createLogValue(fromDepth, toDepth, 0.0));
        coverage.addValue(createLogValue(fromDepth, toDepth, 10.0));
        return coverage;
    }

    private ProfileLevel createLogValue(boolean fromDepth, boolean toDepth, double start) {
        ProfileLevel profileLevel = new ProfileLevel();
        if (fromDepth) {
            profileLevel.setLevelStart(createQuantity("fromDepth", start, "m"));
        }
        if (toDepth) {
            profileLevel.setLevelEnd(createQuantity("toDepth", start + 10.0, "m"));
        }
        profileLevel.setValue(createProfileLevel());
        return profileLevel;
    }

    private QuantityValue createQuantity(String definition, double value, String unit) {
        QuantityValue quantity = new QuantityValue(value, unit);
        quantity.setValue(value).setUom(unit).setDefinition(definition);
        return quantity;
    }

    private List<Value<?>> createProfileLevel() {
        List<Value<?>> list = Lists.newArrayList();
        CategoryValue category = new CategoryValue("weathered grey brown basalt", "unknown");
        category.setDefinition("http://www.opengis.net/def/gwml/2.0/observedProperty/earthMaterial");
        category.addName(new CodeType("lithology"));
        list.add(category);
        TextValue text = new TextValue("weathered grey brown basalt");
        text.setDefinition("http://www.opengis.net/def/gwml/2.0/observedProperty/earthMaterial");
        text.addName(new CodeType("text"));
        list.add(text);
        return list;
    }

}
