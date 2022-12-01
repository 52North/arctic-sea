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
package org.n52.svalbard.decode;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.xmlbeans.XmlObject;
import org.junit.jupiter.api.Test;
import org.n52.svalbard.decode.exception.DecodingException;

public interface DeleteDecoderTest {

    Decoder<?, XmlObject> getDecoder();

    XmlObject getIncorrectXmlObject();

    XmlObject getCorrectXmlObject();

    @Test
    default void decodeNullThrowsOwsExceptionReport() throws DecodingException {
        assertThrows(DecodingException.class, () -> {
            getDecoder().decode(null);
        });
    }

    @Test
    default void decodingIncorrectXmlObjectThrowsOwsExceptionReport() throws DecodingException {
        assertThrows(DecodingException.class, () -> {
            getDecoder().decode(getIncorrectXmlObject());
        });
    }

    @Test
    default void should_throw_DecodingException_when_receving_invalid_DeleteObservationDocument()
            throws DecodingException {
        assertThrows(DecodingException.class, () -> {
            getDecoder().decode(getCorrectXmlObject());
        });
    }
}
