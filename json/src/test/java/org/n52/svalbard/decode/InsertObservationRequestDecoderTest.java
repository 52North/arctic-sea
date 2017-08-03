/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.svalbard.ConfiguredSettingsManager;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.InsertObservationRequestDecoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@Ignore
public class InsertObservationRequestDecoderTest {
//    @ClassRule
//    public static final ConfiguredSettingsManager csm = new ConfiguredSettingsManager();

    private InsertObservationRequestDecoder decoder;

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Before
    public void before() {
        this.decoder = new InsertObservationRequestDecoder();
    }

    @Test
    public void singleObservation()
            throws IOException, DecodingException {
        final JsonNode json =
                JsonLoader.fromResource("/examples/sos/InsertObservationRequest-single-observation.json");
        final InsertObservationRequest req = decoder.decodeJSON(json, true);
        errors.checkThat(req.getService(), is(equalTo("SOS")));
        errors.checkThat(req.getVersion(), is(equalTo("2.0.0")));
        errors.checkThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        errors.checkThat(req.getOfferings(), hasSize(1));
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
        errors.checkThat(req.getService(), is(equalTo("SOS")));
        errors.checkThat(req.getVersion(), is(equalTo("2.0.0")));
        errors.checkThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        errors.checkThat(req.getOfferings(), hasSize(2));
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
        errors.checkThat(req.getService(), is(equalTo("SOS")));
        errors.checkThat(req.getVersion(), is(equalTo("2.0.0")));
        errors.checkThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        errors.checkThat(req.getOfferings(), hasSize(1));
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
        errors.checkThat(req.getService(), is(equalTo("SOS")));
        errors.checkThat(req.getVersion(), is(equalTo("2.0.0")));
        errors.checkThat(req.getOperationName(), is(equalTo("InsertObservation")));
        assertThat(req.getOfferings(), is(notNullValue()));
        errors.checkThat(req.getOfferings(), hasSize(2));
        assertThat(req.getOfferings().get(0), is(equalTo("offering1")));
        assertThat(req.getOfferings().get(1), is(equalTo("offering2")));
        assertThat(req.getObservations(), is(notNullValue()));
        assertThat(req.getObservations(), hasSize(1));
        assertThat(req.getObservations().get(0), is(notNullValue()));
        assertThat(req.getObservations().get(0).getValue().getValue(), is(instanceOf(TextValue.class)));
    }
}
