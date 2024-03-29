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
package org.n52.svalbard.decode.json;

import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.google.common.collect.Sets;

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
public class InsertSensorRequestDecoderTest {

    private InsertSensorRequestDecoder decoder;

    private InsertSensorRequest req;

    @BeforeEach
    public void setUp()
            throws DecodingException, IOException {
        DecoderRepository decoderRepository = new DecoderRepository();

        this.decoder = new InsertSensorRequestDecoder();
        this.decoder.setDecoderRepository(decoderRepository);

        SensorMLDecoderV101 sensorMLDecoder = new SensorMLDecoderV101();
        sensorMLDecoder.setXmlOptions(XmlOptions::new);
        sensorMLDecoder.setDecoderRepository(decoderRepository);

        SweCommonDecoderV101 sweCommonDecoder = new SweCommonDecoderV101();
        sweCommonDecoder.setXmlOptions(XmlOptions::new);
        sweCommonDecoder.setDecoderRepository(decoderRepository);

        GmlDecoderv311 gmlDecoderv311 = new GmlDecoderv311();

        decoderRepository.setDecoders(Arrays.asList(decoder,
                                                    sensorMLDecoder,
                                                    sweCommonDecoder,
                                                    gmlDecoderv311));

        decoderRepository.init();
        final JsonNode json = JsonLoader.fromResource("/examples/sos/InsertSensorRequest.json");
        this.req = decoder.decode(json);
        assertThat(req, is(notNullValue()));
    }

    @Test
    public void testOperationName()
            throws OwsExceptionReport, IOException {
        assertThat(req.getOperationName(), is("InsertSensor"));
    }

    @Test
    public void testVersion()
            throws OwsExceptionReport, IOException {
        assertThat(req.getVersion(), is("2.0.0"));
    }

    @Test
    public void testService()
            throws OwsExceptionReport, IOException {
        assertThat(req.getService(), is("SOS"));
    }

    @Test
    public void testProcedureIdentifier()
            throws OwsExceptionReport, IOException {
        /* set in operator */
        assertThat(req.getAssignedProcedureIdentifier(), is(nullValue()));
    }

    @Test
    public void testProcedureDescriptionFormat()
            throws OwsExceptionReport, IOException {
        assertThat(req.getProcedureDescriptionFormat(), is("http://www.opengis.net/sensorML/1.0.1"));
    }

    @Test
    public void testObservableProperties()
            throws OwsExceptionReport, IOException {
        assertThat(req.getObservableProperty(),
                   is(Arrays.asList("http://www.52north.org/test/observableProperty/9_1",
                                    "http://www.52north.org/test/observableProperty/9_2",
                                    "http://www.52north.org/test/observableProperty/9_3",
                                    "http://www.52north.org/test/observableProperty/9_4",
                                    "http://www.52north.org/test/observableProperty/9_5")));
    }

    @Test
    public void testRelatedFeatures()
            throws OwsExceptionReport, IOException {
        assertThat(req.getRelatedFeatures(), is(empty()));
    }

    @Test
    public void testFeatureOfInterstTypes()
            throws OwsExceptionReport, IOException {
        assertThat(req.getMetadata(), is(notNullValue()));
        assertThat(req.getMetadata().getFeatureOfInterestTypes(),
                   is(singleton("http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint")));
    }

    @Test
    public void testObservationTypes()
            throws OwsExceptionReport, IOException {
        assertThat(req.getMetadata(), is(notNullValue()));
        assertThat(req.getMetadata().getObservationTypes(),
                   is((Set<String>) Sets.newHashSet(
                           "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement",
                           "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation",
                           "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation",
                           "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation",
                           "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation")));
    }

    @Test
    public void testProcedureDescription()
            throws OwsExceptionReport, IOException {
        assertThat(req.getProcedureDescription(), is(notNullValue()));
        assertThat(req.getProcedureDescription().getIdentifier(), is("http://www.52north.org/test/procedure/9"));
    }
}
