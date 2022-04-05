/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.decode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.xmlbeans.XmlObject;
import org.junit.jupiter.api.Test;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;

public interface InsertDecoderTest {

    Decoder<?, XmlObject> getDecoder();

    XmlObject getDocument();

    @Test
    default void shouldThrowExceptionOnWrongInputType() throws DecodingException {
        UnsupportedDecoderInputException assertThrows = assertThrows(UnsupportedDecoderInputException.class, () -> {
            getDecoder().decode(XmlObject.Factory.newInstance());
        });
        assertEquals(
                "Decoder " + getDecoder().getClass().getSimpleName() + " can not decode 'org.apache.xmlbeans.impl.values.XmlAnyTypeImpl'",
                assertThrows.getMessage());
    }

    @Test
    default void shouldThrowExceptionOnNullInput() throws DecodingException {
        UnsupportedDecoderInputException assertThrows = assertThrows(UnsupportedDecoderInputException.class, () -> {
            getDecoder().decode(null);
        });
        assertEquals("Decoder " + getDecoder().getClass().getSimpleName() + " can not decode 'null'", assertThrows.getMessage());
    }

    @Test
    default void shouldThrowExceptionOnMissingTypeInDocument() throws DecodingException {
        DecodingException assertThrows = assertThrows(DecodingException.class, () -> {
            getDecoder().decode(getDocument());
        });
        assertEquals("Received XML document is not valid. Set log level to debug to get more details",
                assertThrows.getMessage());
    }

}
