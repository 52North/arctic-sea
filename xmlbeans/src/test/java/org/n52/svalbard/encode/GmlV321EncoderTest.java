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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;

import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

/**
 * @since 1.0.0
 *
 */
public class GmlV321EncoderTest {

    private GmlEncoderv321 encoder;

    @Before
    public void setup() {
        EncoderRepository encoderRepository = new EncoderRepository();
        encoder = new GmlEncoderv321();
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setEncoderRepository(encoderRepository);

        encoderRepository.setEncoders(Arrays.asList(encoder));
        encoderRepository.init();
    }

    @Test(expected = EncodingException.class)
    public void throwIAEForEncodeNullTest() throws EncodingException {
        encoder.encode(null);
    }

    @Test(expected = UnsupportedEncoderInputException.class)
    public void isNullForNotSupportedObjectTest() throws OwsExceptionReport, EncodingException {
        encoder.encode(5);
    }

    @Test(expected = EncodingException.class)
    public void throwsIllegalArgumentExceptionWhenConstructorValueNullTest() throws EncodingException {
        QuantityValue quantity = new QuantityValue(null);
        encoder.encode(quantity);
    }

    @Test
    public void isMeasureTypeValidWithoutUnitTest() throws OwsExceptionReport, EncodingException {
        QuantityValue quantity = new QuantityValue(2.2);
        XmlObject encode = encoder.encode(quantity);
        assertTrue("Encoded Object is NOT valid", encode.validate());
    }

    @Test
    public void isMeasureTypeValidAllSetTest() throws OwsExceptionReport, EncodingException {
        QuantityValue quantity = new QuantityValue(2.2);
        quantity.setUnit("cm");
        XmlObject encode = encoder.encode(quantity);
        assertTrue("Encoded Object is NOT valid", encode.validate());
    }

}
