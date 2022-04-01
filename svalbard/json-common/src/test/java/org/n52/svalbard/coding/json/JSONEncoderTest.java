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
package org.n52.svalbard.coding.json;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.n52.svalbard.coding.json.matchers.JSONMatchers.equalTo;

import org.junit.jupiter.api.Test;
import org.n52.janmayen.http.MediaTypes;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;
import org.n52.svalbard.encode.json.JSONEncoderKey;
import org.n52.svalbard.encode.json.JSONEncodingException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 */
public class JSONEncoderTest {


    private final JSONEncoder<String> encoder = new JSONEncoderForTesting(String.class);
    private final JSONEncoder<String> throwingEncoder = new JSONEncoderForExceptionTesting(String.class);

    @Test
    public void testEncoderKeyTypes() {
        assertThat(encoder.getKeys(), is(notNullValue()));
        assertThat(encoder.getKeys(), hasSize(1));
        assertThat(encoder.getKeys(), hasItem(new JSONEncoderKey(String.class)));
    }

    @Test
    public void testSupportedTypes() {
        assertThat(encoder.getSupportedTypes(), is(notNullValue()));
        assertThat(encoder.getSupportedTypes().size(), is(0));
    }

    @Test
    public void testContentType() {
        assertThat(encoder.getContentType(), is(MediaTypes.APPLICATION_JSON));
    }

    @Test
    public void testEncode() throws EncodingException {
        assertThat(encoder.encode("test"), equalTo("test"));
    }

    @Test
    public void testEncodeWithHelperValues() throws EncodingException {
        assertThat(encoder.encode("test", EncodingContext.empty()), equalTo("test"));
    }

    @Test
    public void testThrowingEncoder() throws EncodingException {
        JSONEncodingException assertThrows = assertThrows(JSONEncodingException.class, () -> {
            throwingEncoder.encode("test");
        });
        assertTrue(assertThrows.getMessage() != null);
        assertEquals("message", assertThrows.getMessage());
    }
}
