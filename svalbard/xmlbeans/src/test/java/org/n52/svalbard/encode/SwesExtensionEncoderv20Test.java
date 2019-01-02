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

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
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
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                SwesExtensionEncoderv20.class.getSimpleName() +
                " can not encode 'null'"));

        encoder.encode(null);
    }

    @Test
    public void shouldThrowExceptionWhenEmptyExtensionReceived() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                SwesExtensionEncoderv20.class.getSimpleName() +
                " can not encode 'null extension content'"));

        encoder.encode(new SwesExtension<>());
    }

    @Test
    public void shouldThrowExceptionWhenExtensioneWithoutSweTypeReceived() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                SwesExtensionEncoderv20.class.getSimpleName() +
                " can not encode 'java.lang.Boolean'"));

        SwesExtension<Boolean> extension = new SwesExtension<>();
        extension.setValue(Boolean.TRUE);
        encoder.encode(extension);
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

        Assert.assertThat(encodedObject, Matchers.instanceOf(BooleanPropertyType.class));
        BooleanType xbBoolean = ((BooleanPropertyType) encodedObject).getBoolean();
        Assert.assertThat(xbBoolean.getDefinition(), Is.is(definition));
        Assert.assertThat(xbBoolean.getIdentifier(), Is.is(identifier));
        Assert.assertThat(xbBoolean.getValue(), Is.is(value));
    }

}
