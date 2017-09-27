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

import com.github.fge.jsonschema.SchemaVersion;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public interface SchemaConstants {
    String SCHEMA_URI = SchemaVersion.DRAFTV4.getLocation().toASCIIString();

    interface Request {
        String INSERT_OBSERVATION
                = "http://www.52north.org/schema/json/sos/request/InsertObservation#";

        String GET_OBSERVATION
                = "http://www.52north.org/schema/json/sos/request/GetObservation#";

        String GET_OBSERVATION_BY_ID
                = "http://www.52north.org/schema/json/sos/request/GetObservationById#";

        String BULK_REQUEST
                = "http://www.52north.org/schema/json/sos/request/Batch#";

        String INSERT_SENSOR
                = "http://www.52north.org/schema/json/sos/request/InsertSensor#";

        String INSERT_RESULT_TEMPLATE
                = "http://www.52north.org/schema/json/sos/request/InsertResultTemplate#";

        String GET_DATA_AVAILABILITY
                = "http://www.52north.org/schema/json/sos/request/GetDataAvailability#";

        String DELETE_OBSERVATION
                = "http://www.52north.org/schema/json/sos/request/DeleteObservation#";

        String UPDATE_SENSOR_DESCRIPTION
                = "http://www.52north.org/schema/json/sos/request/UpdateSensorDescription#";

        String GET_CAPABILITIES
                = "http://www.52north.org/schema/json/sos/request/GetCapabilities#";

        String DELETE_SENSOR
                = "http://www.52north.org/schema/json/sos/request/DeleteSensor#";

        String DESCRIBE_SENSOR
                = "http://www.52north.org/schema/json/sos/request/DescribeSensor#";

        String GET_FEATURE_OF_INTEREST
                = "http://www.52north.org/schema/json/sos/request/GetFeatureOfInterest#";

        String INSERT_RESULT
                = "http://www.52north.org/schema/json/sos/request/InsertResult#";

        String GET_RESULT
                = "http://www.52north.org/schema/json/sos/request/GetResult#";

        String GET_RESULT_TEMPLATE
                = "http://www.52north.org/schema/json/sos/request/GetResultTemplate#";
    }

    interface Response {
    }

    interface Observation {
        String OBSERVATION = "http://www.52north.org/schema/json/Observation#";

        String CATEGORY_OBSERVATION
                = "http://www.52north.org/schema/json/CategoryObservation#";

        String COUNT_OBSERVATION
                = "http://www.52north.org/schema/json/CountObservation#";

        String COMPLEX_OBSERVATION
                = "http://www.52north.org/schema/json/ComplexObservation#";

        String TRUTH_OBSERVATION
                = "http://www.52north.org/schema/json/TruthObservation#";

        String TEXT_OBSERVATION
                = "http://www.52north.org/schema/json/TextObservation#";

        String GEOMETRY_OBSERVATION
                = "http://www.52north.org/schema/json/GeometryObservation#";

        String MEASUREMENT = "http://www.52north.org/schema/json/Measurement#";

        String SWE_ARRAY_OBSERVATION = "http://www.52north.org/schema/json/SWEArrayObservation#";

        String REFERENCE_OBSERVATION = "http://www.52north.org/schema/json/ReferenceObservation#";

        String TEMPLATE_OBSERVATION
                = "http://www.52north.org/schema/json/TemplateObservation#";
    }

    interface Common {
        String GEOMETRY = "http://www.52north.org/schema/json/Geometry#";

        String FEATURE_OF_INTEREST
                = "http://www.52north.org/schema/json/FeatureOfInterest#";

        String EXCEPTION_REPORT
                = "http://www.52north.org/schema/json/ExceptionReport#";

        String FIELD = "http://www.52north.org/schema/json/Field#";

        String SPATIAL_FILTER
                = "http://www.52north.org/schema/json/SpatialFilter#";

        String TEMPORAL_FILTER
                = "http://www.52north.org/schema/json/TemporalFilter#";

        String FIELD_WITH_VALUE
                = "http://www.52north.org/schema/json/FieldWithValue#";

        String TIME_PRIMITIVE
                = "http://www.52north.org/schema/json/TimePrimitive#";

        String TIME_PERIOD = "http://www.52north.org/schema/json/TimePeriod#";

        String TIME_INSTANT = "http://www.52north.org/schema/json/TimeInstant#";

        String ENVELOPE = "http://www.52north.org/schema/json/Envelope#";

        String PARAMETER = "http://www.52north.org/schema/json/Parameter#";

    }
}
