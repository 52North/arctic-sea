/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import net.opengis.swe.x20.BooleanPropertyType;
import net.opengis.swe.x20.BooleanType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 * @since 3.2.0
 */
public class SwesExtensionEncoderv20Test {

    private SwesExtensionEncoderv20 encoder;

    @BeforeEach
    public void setup() {
        encoder = new SwesExtensionEncoderv20();
        encoder.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv20 sweCommonEncoder = new SweCommonEncoderv20();
        sweCommonEncoder.setXmlOptions(XmlOptions::new);

        EncoderRepository repo = new EncoderRepository();
        repo.setEncoders(CollectionHelper.list(encoder, sweCommonEncoder));
        repo.init();

        repo.getEncoders().forEach(e -> ((AbstractDelegatingEncoder<?, ?>)e).setEncoderRepository(repo));
    }

    @Test
    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
        UnsupportedEncoderInputException assertThrows = assertThrows(UnsupportedEncoderInputException.class, () -> {
            encoder.encode(null);
        });
        assertEquals("Encoder " +
                SwesExtensionEncoderv20.class.getSimpleName() +
                " can not encode 'null'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmptyExtensionReceived() throws EncodingException {
        UnsupportedEncoderInputException assertThrows = assertThrows(UnsupportedEncoderInputException.class, () -> {
            encoder.encode(new SwesExtension<>());
        });
        assertEquals("Encoder " +
                SwesExtensionEncoderv20.class.getSimpleName() +
                " can not encode 'null extension content'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenExtensioneWithoutSweTypeReceived() throws EncodingException {
        UnsupportedEncoderInputException assertThrows = assertThrows(UnsupportedEncoderInputException.class, () -> {
            SwesExtension<Boolean> extension = new SwesExtension<>();
            extension.setValue(Boolean.TRUE);
            encoder.encode(extension);
        });
        assertEquals("Encoder " +
                SwesExtensionEncoderv20.class.getSimpleName() +
                " can not encode 'java.lang.Boolean'", assertThrows.getMessage());
    }

    @Test
    public void shouldEncodeSweTypeBoolean() throws EncodingException {
        String identifier = "test-identifier";
        String definition = "test-definition";
        Boolean value = Boolean.TRUE;

        SweBoolean sweBoolean = new SweBoolean();
        sweBoolean.setDefinition(definition);
        sweBoolean.setIdentifier(identifier);
        sweBoolean.setValue(value);

        SwesExtension<SweBoolean> extension = new SwesExtension<>();
        extension.setValue(sweBoolean);

        XmlObject encodedObject = encoder.encode(extension);

        MatcherAssert.assertThat(encodedObject, Matchers.instanceOf(BooleanPropertyType.class));
        BooleanType xbBoolean = ((BooleanPropertyType) encodedObject).getBoolean();
        MatcherAssert.assertThat(xbBoolean.getDefinition(), Is.is(definition));
        MatcherAssert.assertThat(xbBoolean.getIdentifier(), Is.is(identifier));
        MatcherAssert.assertThat(xbBoolean.getValue(), Is.is(value));
    }

}
