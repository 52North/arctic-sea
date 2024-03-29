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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @since 1.0.0
 *
 */
public class GmlV321EncoderTest {

    private GmlEncoderv321 encoder;

    @BeforeEach
    public void setup() {
        EncoderRepository encoderRepository = new EncoderRepository();
        encoder = new GmlEncoderv321();
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setEncoderRepository(encoderRepository);

        encoderRepository.setEncoders(Arrays.asList(encoder));
        encoderRepository.init();
    }

    @Test
    public void throwIAEForEncodeNullTest() throws EncodingException {
        assertThrows(EncodingException.class, () -> {
            encoder.encode(null);
        });
    }

    @Test
    public void isNullForNotSupportedObjectTest() throws OwsExceptionReport, EncodingException {
        assertThrows(EncodingException.class, () -> {
            encoder.encode(5);
        });
    }

    @Test
    public void throwsIllegalArgumentExceptionWhenConstructorValueNullTest() throws EncodingException {
        assertThrows(EncodingException.class, () -> {
            QuantityValue quantity = new QuantityValue();
            encoder.encode(quantity);
        });
    }

    @Test
    public void isMeasureTypeValidWithoutUnitTest() throws OwsExceptionReport, EncodingException {
        QuantityValue quantity = new QuantityValue(2.2);
        XmlObject encode = encoder.encode(quantity);
        assertTrue(encode.validate(), "Encoded Object is NOT valid");
    }

    @Test
    public void isMeasureTypeValidAllSetTest() throws OwsExceptionReport, EncodingException {
        QuantityValue quantity = new QuantityValue(2.2);
        quantity.setUnit("cm");
        XmlObject encode = encoder.encode(quantity);
        assertTrue(encode.validate(), "Encoded Object is NOT valid");
    }

}
