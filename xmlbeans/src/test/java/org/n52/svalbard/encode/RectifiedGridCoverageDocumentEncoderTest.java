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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import net.opengis.gml.x32.RectifiedGridCoverageDocument;

import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;

import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.QuantityRangeValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.RectifiedGridCoverage;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.XmlHelper;

public class RectifiedGridCoverageDocumentEncoderTest {

    private RectifiedGridCoverageDocumentEncoder encoder;

    @Before
    public void setup() {
        EncoderRepository encoderRepository = new EncoderRepository();
        encoder = new RectifiedGridCoverageDocumentEncoder();
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setEncoderRepository(encoderRepository);

        encoderRepository.setEncoders(Arrays.asList(encoder));
        encoderRepository.init();
    }

    @Test
    public void test_quantity_encoding() throws DecodingException, EncodingException {
        RectifiedGridCoverageDocument encoded = encoder.encode(getRectifiedGridCoverage());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
    }

    @Test
    public void test_category_encoding() throws DecodingException, EncodingException {
        RectifiedGridCoverageDocument encoded = encoder.encode(getCategoryRectifiedGridCoverage());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
    }

    @Test
    public void test_text_encoding() throws DecodingException, EncodingException {
        RectifiedGridCoverageDocument encoded = encoder.encode(getTextRectifiedGridCoverage());
        assertThat(XmlHelper.validateDocument(encoded), is(TRUE));
    }

    private RectifiedGridCoverage getRectifiedGridCoverage() {
        RectifiedGridCoverage rgc = new RectifiedGridCoverage("quantity");
        rgc.addValue(new QuantityValue(2.5, "m"), new QuantityValue(10.0));
        rgc.addValue(new QuantityValue(5.0, "m"), new QuantityValue(8.0));
        rgc.addValue(new QuantityValue(10.0, "m"), new QuantityValue(3.0));
        rgc.setUnit("C");
        return rgc;
    }

    private RectifiedGridCoverage getCategoryRectifiedGridCoverage() {
        RectifiedGridCoverage rgc = new RectifiedGridCoverage("category");
        rgc.setUnit("d");
        rgc.setRangeParameters("category_param");
        rgc.addValue(new QuantityRangeValue(0.0, 5.0, "m"), new CategoryValue("test category"));
        rgc.addValue(new QuantityRangeValue(5.0, 10.0, "m"), new CategoryValue("test category 2"));
        rgc.addValue(new QuantityRangeValue(10.0, 15.0, "m"), new CategoryValue("test category 2 test"));
        return rgc;
    }

    private RectifiedGridCoverage getTextRectifiedGridCoverage() {
        RectifiedGridCoverage rgc = new RectifiedGridCoverage("text");
        rgc.setUnit("d");
        rgc.setRangeParameters("text_param");
        rgc.addValue(new QuantityRangeValue(0.0, 5.0, "m"), new TextValue("test text"));
        rgc.addValue(new QuantityRangeValue(5.0, 10.0, "m"), new TextValue("test text 2"));
        rgc.addValue(new QuantityRangeValue(10.0, 15.0, "m"), new TextValue("test text 2 test"));
        return rgc;
    }
}
