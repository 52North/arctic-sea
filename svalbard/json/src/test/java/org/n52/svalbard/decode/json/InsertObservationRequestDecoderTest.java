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
package org.n52.svalbard.decode.json;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.InsertObservationRequestDecoder;
import org.n52.svalbard.decode.json.ObservationDecoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

import org.n52.svalbard.decode.DecoderRepository;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class InsertObservationRequestDecoderTest {

    private InsertObservationRequestDecoder decoder;

    @BeforeEach
    public void before() {
        DecoderRepository decoderRepository = new DecoderRepository();
        this.decoder = new InsertObservationRequestDecoder();
        this.decoder.setDecoderRepository(decoderRepository);
        ObservationDecoder observationDecoder = new ObservationDecoder();
        observationDecoder.setDecoderRepository(decoderRepository);
        decoderRepository.setDecoders(Arrays.asList(decoder, observationDecoder));
        decoderRepository.init();
    }

    @Test
    public void singleObservation()
            throws IOException, DecodingException {
        final JsonNode json =
                JsonLoader.fromResource("/examples/sos/InsertObservationRequest-single-observation.json");
        final InsertObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req.getService(), is(equalTo("SOS")));
        assertThat(req.getVersion(), is(equalTo("2.0.0")));
        assertThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        assertThat(req.getOfferings(), hasSize(1));
        assertThat(req.getOfferings().get(0), is(equalTo("offering2")));
        assertThat(req.getObservations(), is(notNullValue()));
        assertThat(req.getObservations(), hasSize(1));
        assertThat(req.getObservations().get(0), is(notNullValue()));
        assertThat(req.getObservations().get(0).getValue().getValue(), is(instanceOf(TextValue.class)));
    }

    @Test
    public void multipleObservation()
            throws IOException, DecodingException {
        final JsonNode json =
                JsonLoader.fromResource("/examples/sos/InsertObservationRequest-multiple-observations.json");
        final InsertObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req, is(notNullValue()));
        assertThat(req.getService(), is(equalTo("SOS")));
        assertThat(req.getVersion(), is(equalTo("2.0.0")));
        assertThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        assertThat(req.getOfferings(), hasSize(2));
        assertThat(req.getOfferings().get(0), is(equalTo("offering1")));
        assertThat(req.getOfferings().get(1), is(equalTo("offering2")));
        assertThat(req.getObservations(), is(notNullValue()));
        assertThat(req.getObservations(), hasSize(2));
        assertThat(req.getObservations().get(0), is(notNullValue()));
        assertThat(req.getObservations().get(0).getValue().getValue(), is(instanceOf(TextValue.class)));
        assertThat(req.getObservations().get(1), is(notNullValue()));
        assertThat(req.getObservations().get(1).getValue().getValue(), is(instanceOf(TextValue.class)));
    }

    @Test
    public void singleOffering()
            throws IOException, DecodingException {
        final JsonNode json = JsonLoader.fromResource("/examples/sos/InsertObservationRequest-single-offering.json");
        final InsertObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req.getService(), is(equalTo("SOS")));
        assertThat(req.getVersion(), is(equalTo("2.0.0")));
        assertThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        assertThat(req.getOfferings(), hasSize(1));
        assertThat(req.getOfferings().get(0), is(equalTo("offering2")));
        assertThat(req.getObservations(), is(notNullValue()));
        assertThat(req.getObservations(), hasSize(1));
        assertThat(req.getObservations().get(0), is(notNullValue()));
        assertThat(req.getObservations().get(0).getValue().getValue(), is(instanceOf(TextValue.class)));
    }

    @Test
    public void multipleOfferings()
            throws IOException, DecodingException {
        final JsonNode json =
                JsonLoader.fromResource("/examples/sos/InsertObservationRequest-multiple-offerings.json");
        final InsertObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req, is(notNullValue()));
        assertThat(req.getService(), is(equalTo("SOS")));
        assertThat(req.getVersion(), is(equalTo("2.0.0")));
        assertThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        assertThat(req.getOfferings(), hasSize(2));
        assertThat(req.getOfferings().get(0), is(equalTo("offering1")));
        assertThat(req.getOfferings().get(1), is(equalTo("offering2")));
        assertThat(req.getObservations(), is(notNullValue()));
        assertThat(req.getObservations(), hasSize(1));
        assertThat(req.getObservations().get(0), is(notNullValue()));
        assertThat(req.getObservations().get(0).getValue().getValue(), is(instanceOf(TextValue.class)));
    }

    public void complexObservation()
            throws IOException, DecodingException {
        final JsonNode json =
                JsonLoader.fromResource("/examples/sos/InsertObservationRequest-complex.json");
        final InsertObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req.getService(), is(equalTo("SOS")));
        assertThat(req.getVersion(), is(equalTo("2.0.0")));
        assertThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        assertThat(req.getOfferings(), hasSize(1));
        assertThat(req.getOfferings().get(0), is(equalTo("offering2")));
        assertThat(req.getObservations(), is(notNullValue()));
        assertThat(req.getObservations(), hasSize(1));
        assertThat(req.getObservations().get(0), is(notNullValue()));
        assertThat(req.getObservations().get(0).getValue().getValue(), is(instanceOf(ComplexValue.class)));
    }
}
