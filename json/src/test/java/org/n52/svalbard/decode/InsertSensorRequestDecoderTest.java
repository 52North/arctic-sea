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

import static java.util.Collections.singleton;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.svalbard.ConfiguredSettingsManager;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.InsertSensorRequestDecoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@Ignore
public class InsertSensorRequestDecoderTest {
//    @ClassRule
//    public static final ConfiguredSettingsManager csm = new ConfiguredSettingsManager();

    private InsertSensorRequestDecoder decoder;

    private InsertSensorRequest req;

    @Before
    public void setUp()
            throws DecodingException, IOException {
        this.decoder = new InsertSensorRequestDecoder();
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

        assertThat(req.getRelatedFeatures(), is(nullValue()));
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
