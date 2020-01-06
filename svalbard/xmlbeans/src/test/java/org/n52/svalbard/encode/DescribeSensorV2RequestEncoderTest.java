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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.request.DescribeSensorRequest;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

public class DescribeSensorV2RequestEncoderTest {

    @Test
    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
        UnsupportedEncoderInputException assertThrows = assertThrows(UnsupportedEncoderInputException.class, () -> {
            new DescribeSensorV2RequestEncoder().create(new DescribeSensorRequest());
        });
        assertEquals("Encoder " +
                DescribeSensorV2RequestEncoder.class.getSimpleName() +
                " can not encode 'missing service'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
        UnsupportedEncoderInputException assertThrows = assertThrows(UnsupportedEncoderInputException.class, () -> {
            new DescribeSensorV2RequestEncoder().create(new DescribeSensorRequest("service", ""));
        });
        assertEquals("Encoder " +
                DescribeSensorV2RequestEncoder.class.getSimpleName() +
                " can not encode 'missing version'", assertThrows.getMessage());
    }

}
