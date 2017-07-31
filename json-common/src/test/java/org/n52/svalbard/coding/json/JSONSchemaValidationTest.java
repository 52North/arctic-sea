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
package org.n52.svalbard.coding.json;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.n52.svalbard.coding.json.matchers.ValidationMatchers.validSchema;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@RunWith(Parameterized.class)
public class JSONSchemaValidationTest {
    private static final String[] SCHEMATA = { "BaseObservation", "CategoryObservation", "CodeType",
            "ComplexObservation", "CountObservation", "Envelope", "ExceptionReport", "FeatureOfInterest", "Field",
            "FieldWithValue", "GenericObservation", "Geometry", "GeometryObservation", "Measurement", "Observation",
            "ObservationWithResult", "SWEArrayObservation", "SpatialFilter", "TemplateObservation", "TemporalFilter",
            "TextObservation", "TimeInstant", "TimePeriod", "TimePrimitive", "TruthObservation", "sos/request/Batch",
            "sos/request/GetObservation", "sos/request/GetObservationById", "sos/request/GetFeatureOfInterest",
            "sos/request/InsertObservation", "sos/request/InsertResultTemplate", "sos/request/InsertSensor",
            "sos/request/GetCapabilities", "sos/request/DeleteSensor", "sos/request/DescribeSensor",
            "sos/request/UpdateSensorDescription", "sos/request/InsertResult", "sos/request/GetResult",
            "sos/request/GetResultTemplate", "sos/request/Request", "sos/response/Response", "sos/response/Batch",
            "sos/response/InsertSensor", "sos/response/GetObservation", "sos/response/GetObservationById",
            "sos/response/InsertObservation", "sos/response/GetFeatureOfInterest", "sos/response/InsertResult",
            "sos/response/GetResult", "sos/response/GetResultTemplate", "sos/response/UpdateSensorDescription",
            "sos/response/DeleteSensor", "sos/response/DescribeSensor", "sos/response/GetCapabilities" };

    private String name;

    private JsonNode schema;

    public JSONSchemaValidationTest(String name) {
        this.name = name;
    }

    @Before
    public void setUp() throws IOException {
        schema = JsonLoader.fromResource("/schema/" + name + ".json");
    }

    @Test
    public void isValidSchema() throws IOException {
        assertThat(name + " is not valid", schema, is(validSchema()));
    }

    @Parameters(name = "{0}")
    public static List<String[]> schemata() {
        String[][] params = new String[SCHEMATA.length][];
        for (int i = 0; i < params.length; ++i) {
            params[i] = new String[] { SCHEMATA[i] };
        }
        return Arrays.asList(params);
    }
}
