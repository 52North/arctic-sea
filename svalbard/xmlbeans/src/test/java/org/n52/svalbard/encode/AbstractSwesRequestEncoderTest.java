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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.xmlbeans.XmlObject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 * @since 3.2.0
 *
 */
public class AbstractSwesRequestEncoderTest {

    private AbstractSwesRequestEncoder<OwsServiceRequest> encoder = new AbstractSwesRequestEncoderSeam();


    @Test
    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.validateInput(null);
        });
        assertEquals("Encoder " +
                AbstractSwesRequestEncoderSeam.class.getSimpleName() +
                " can not encode 'null'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.validateInput(new OwsServiceRequest() {});
        });
        assertEquals("Encoder " +
                AbstractSwesRequestEncoderSeam.class.getSimpleName() +
                " can not encode 'missing service'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
        EncodingException assertThrows = assertThrows(EncodingException.class, () -> {
            encoder.validateInput(new OwsServiceRequest("SOS", "") {});
        });
        assertEquals("Encoder " +
                AbstractSwesRequestEncoderSeam.class.getSimpleName() +
                " can not encode 'missing version'", assertThrows.getMessage());
    }

    @Test
    public void shouldThrowNoExceptionIfVersionAndServiceAreSet() throws EncodingException {
        encoder.validateInput(new OwsServiceRequest("SOS", "2.0.0") {});
    }

    @Test
    public void shouldReturnTheCorrectSchemaLocation() {
        assertThat(encoder.getConcreteSchemaLocations(), Matchers.hasSize(1));
        assertThat(encoder.getConcreteSchemaLocations(), Matchers.hasItem(SwesConstants.SWES_20_SCHEMA_LOCATION));
    }

    private class AbstractSwesRequestEncoderSeam extends AbstractSwesRequestEncoder<OwsServiceRequest> {

        public AbstractSwesRequestEncoderSeam() {
            super("operation", OwsServiceRequest.class);
        }

        @Override
        protected XmlObject create(OwsServiceRequest response) throws EncodingException {
            return null;
        }

    }

}
