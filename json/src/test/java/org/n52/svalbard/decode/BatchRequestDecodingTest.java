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

import static com.github.fge.jackson.JsonLoader.fromResource;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.shetland.ogc.sos.BatchConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.BatchRequest;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.BatchRequestDecoder;
import org.n52.svalbard.decode.json.InsertSensorRequestDecoder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@Ignore
public class BatchRequestDecodingTest {
//    @ClassRule
//    public static final ConfiguredSettingsManager csm = new ConfiguredSettingsManager();

    private static JsonNode json;

    private BatchRequestDecoder decoder;

    private BatchRequest request;

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @BeforeClass
    public static void beforeClass() {
        try {
            json = fromResource("/examples/sos/BatchRequest.json");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void before()
            throws DecodingException {
        DecoderRepository decoderRepository = new DecoderRepository();
        this.decoder = new BatchRequestDecoder();
        decoder.setDecoderRepository(decoderRepository);
        decoderRepository.setDecoders(Arrays.asList(decoder, new InsertSensorRequestDecoder()));
        decoderRepository.init();

        this.request = decoder.decodeJSON(json, true);
    }

    @Test
    public void testService() {
        assertThat(request.getService(), is(SosConstants.SOS));
    }

    @Test
    public void testVersion() {
        assertThat(request.getVersion(), is(Sos2Constants.SERVICEVERSION));
    }

    @Test
    public void testOperationName() {
        assertThat(request.getOperationName(), is(BatchConstants.OPERATION_NAME));
    }

    @Test
    public void testParsedRequests() {
        assertThat(request.getRequests(), is(notNullValue()));
        assertThat(request.getRequests(), hasSize(19));
        errors.checkThat(request.getRequests().get(0), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(1), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(2), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(3), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(4), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(5), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(6), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(7), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(8), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(9), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(10), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(11), is(instanceOf(InsertResultTemplateRequest.class)));
        errors.checkThat(request.getRequests().get(12), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(13), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(14), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(15), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(16), is(instanceOf(InsertObservationRequest.class)));
        errors.checkThat(request.getRequests().get(17), is(instanceOf(InsertSensorRequest.class)));
        errors.checkThat(request.getRequests().get(18), is(instanceOf(InsertObservationRequest.class)));
    }
}
