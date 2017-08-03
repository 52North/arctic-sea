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
package org.n52.svalbard.coding;

import static com.github.fge.jackson.JsonLoader.fromResource;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.n52.svalbard.coding.json.matchers.ValidationMatchers.validObservation;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.n52.svalbard.coding.json.JSONConstants;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ObservationValidationTest {
    @Test
    public void testMeasurementGeometryInline()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-inline.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testMeasuremenetGeometryRef()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testMeasurementMissingUOM()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json.path(JSONConstants.RESULT)).remove(JSONConstants.UOM);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementMissingValue()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json.path(JSONConstants.RESULT)).remove(JSONConstants.VALUE);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementMissingProcedure()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json).remove(JSONConstants.PROCEDURE);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementMissingObservedProperty()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json).remove(JSONConstants.OBSERVED_PROPERTY);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementMissingPhenomenonTime()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json).remove(JSONConstants.PHENOMENON_TIME);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementMissingResultTime()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json).remove(JSONConstants.RESULT_TIME);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementTimePeriodResultTime()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ArrayNode resultTime = ((ObjectNode) json).putArray(JSONConstants.RESULT_TIME);
        resultTime.add("2013-01-01T00:00:00+02:00").add("2013-01-01T01:00:00+02:00");
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementMissingValidTime()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json).remove(JSONConstants.VALID_TIME);
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testMeasurementMissingFeatureOfInterest()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json).remove(JSONConstants.FEATURE_OF_INTEREST);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasurementMissingResult()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-geometry-ref.json");
        ((ObjectNode) json).remove(JSONConstants.RESULT);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testMeasuremenetPhenomenonTimePeriod()
            throws IOException {
        JsonNode json = fromResource("/examples/measurement-phenomenon-time-period.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testTruthObservation()
            throws IOException {
        JsonNode json = fromResource("/examples/truth-observation.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testCategoryObservation()
            throws IOException {
        JsonNode json = fromResource("/examples/category-observation.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testCountObservation()
            throws IOException {
        JsonNode json = fromResource("/examples/count-observation.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testCountObservationWithFloatingPointNumber()
            throws IOException {
        JsonNode json = fromResource("/examples/count-observation.json");
        ((ObjectNode) json).put(JSONConstants.RESULT, Math.PI);
        Assert.assertThat(json, is(not(validObservation())));
    }

    @Test
    public void testGeometryObservation()
            throws IOException {
        JsonNode json = fromResource("/examples/geometry-observation.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testTextObservation()
            throws IOException {
        JsonNode json = fromResource("/examples/text-observation.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testComplexObservation()
            throws IOException {
        JsonNode json = fromResource("/examples/complex-observation.json");
        Assert.assertThat(json, is(validObservation()));
    }

    @Test
    public void testSWEArrayObservation()
            throws IOException {
        JsonNode json = fromResource("/examples/swearray-observation.json");
        Assert.assertThat(json, is(validObservation()));
    }
}
