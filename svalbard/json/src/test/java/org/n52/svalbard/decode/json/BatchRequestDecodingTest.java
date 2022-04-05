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

import static com.github.fge.jackson.JsonLoader.fromResource;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.n52.shetland.ogc.sos.BatchConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.BatchRequest;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.BatchRequestDecoder;
import org.n52.svalbard.decode.json.FieldDecoder;
import org.n52.svalbard.decode.json.InsertObservationRequestDecoder;
import org.n52.svalbard.decode.json.InsertResultRequestDecoder;
import org.n52.svalbard.decode.json.InsertResultTemplateRequestDecoder;
import org.n52.svalbard.decode.json.InsertSensorRequestDecoder;
import org.n52.svalbard.decode.json.ObservationDecoder;

import com.fasterxml.jackson.databind.JsonNode;

import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.GmlDecoderv311;
import org.n52.svalbard.decode.SensorMLDecoderV101;
import org.n52.svalbard.decode.SweCommonDecoderV101;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class BatchRequestDecodingTest {

    private static JsonNode json;

    private BatchRequestDecoder decoder;

    private BatchRequest request;


    @BeforeAll
    public static void beforeClass() {
        try {
            json = fromResource("/examples/sos/BatchRequest.json");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @BeforeEach
    public void before()
            throws DecodingException {
        DecoderRepository decoderRepository = new DecoderRepository();
        this.decoder = new BatchRequestDecoder();
        this.decoder.setDecoderRepository(decoderRepository);

        InsertSensorRequestDecoder insertSensorRequestDecoder = new InsertSensorRequestDecoder();
        insertSensorRequestDecoder.setDecoderRepository(decoderRepository);

        InsertObservationRequestDecoder insertObservationRequestDecoder = new InsertObservationRequestDecoder();
        insertObservationRequestDecoder.setDecoderRepository(decoderRepository);

        SensorMLDecoderV101 sensorMLDecoder = new SensorMLDecoderV101();
        sensorMLDecoder.setXmlOptions(XmlOptions::new);
        sensorMLDecoder.setDecoderRepository(decoderRepository);

        SweCommonDecoderV101 sweCommonDecoder = new SweCommonDecoderV101();
        sweCommonDecoder.setXmlOptions(XmlOptions::new);
        sweCommonDecoder.setDecoderRepository(decoderRepository);

        GmlDecoderv311 gmlDecoderv311 = new GmlDecoderv311();

        ObservationDecoder observationDecoder = new ObservationDecoder();
        observationDecoder.setDecoderRepository(decoderRepository);

        InsertResultTemplateRequestDecoder insertResultTemplateRequestDecoder = new InsertResultTemplateRequestDecoder();
        insertResultTemplateRequestDecoder.setDecoderRepository(decoderRepository);

        InsertResultRequestDecoder insertResultRequestDecoder = new InsertResultRequestDecoder();
        insertResultRequestDecoder.setDecoderRepository(decoderRepository);

        FieldDecoder fieldDecoder = new FieldDecoder();
        fieldDecoder.setDecoderRepository(decoderRepository);

        decoderRepository.setDecoders(Arrays.asList(decoder,
                                                    insertSensorRequestDecoder,
                                                    insertObservationRequestDecoder,
                                                    insertResultTemplateRequestDecoder,
                                                    insertResultRequestDecoder,
                                                    sensorMLDecoder,
                                                    sweCommonDecoder,
                                                    observationDecoder,
                                                    fieldDecoder,
                                                    gmlDecoderv311));
        decoderRepository.init();

        this.request = decoder.decodeJSON(json, true);
    }

    @Test
    public void testService() throws DecodingException {
        assertThat(request.getService(), is(SosConstants.SOS));
    }

    @Test
    public void testVersion() throws DecodingException {
        assertThat(request.getVersion(), is(Sos2Constants.SERVICEVERSION));
    }

    @Test
    public void testOperationName() throws DecodingException {
        assertThat(request.getOperationName(), is(BatchConstants.OPERATION_NAME));
    }

    @Test
    public void testParsedRequests() throws DecodingException {
        assertThat(request.getRequests(), is(notNullValue()));
        assertThat(request.getRequests(), hasSize(19));
        assertThat(request.getRequests().get(0), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(1), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(2), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(3), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(4), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(5), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(6), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(7), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(8), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(9), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(10), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(11), is(instanceOf(InsertResultTemplateRequest.class)));
        assertThat(request.getRequests().get(12), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(13), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(14), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(15), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(16), is(instanceOf(InsertObservationRequest.class)));
        assertThat(request.getRequests().get(17), is(instanceOf(InsertSensorRequest.class)));
        assertThat(request.getRequests().get(18), is(instanceOf(InsertObservationRequest.class)));
    }
}
