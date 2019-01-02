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
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 * @since 3.2.0
 *
 */
public class AbstractSwesRequestEncoderTest {

    private AbstractSwesRequestEncoder<OwsServiceRequest> encoder = new AbstractSwesRequestEncoderSeam();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                AbstractSwesRequestEncoderSeam.class.getSimpleName() +
                " can not encode 'null'"));

        encoder.validateInput(null);
    }

    @Test
    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                AbstractSwesRequestEncoderSeam.class.getSimpleName() +
                " can not encode 'missing service'"));

        encoder.validateInput(new OwsServiceRequest() {});
    }

    @Test
    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                AbstractSwesRequestEncoderSeam.class.getSimpleName() +
                " can not encode 'missing version'"));

        encoder.validateInput(new OwsServiceRequest("SOS", "") {});
    }

    @Test
    public void shouldThrowNoExceptionIfVersionAndServiceAreSet() throws EncodingException {
        encoder.validateInput(new OwsServiceRequest("SOS", "2.0.0") {});
    }

    @Test
    public void shouldReturnTheCorrectSchemaLocation() {
        Assert.assertThat(encoder.getConcreteSchemaLocations(), Matchers.hasSize(1));
        Assert.assertThat(encoder.getConcreteSchemaLocations(), Matchers.hasItem(SwesConstants.SWES_20_SCHEMA_LOCATION));
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
