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
package org.n52.iceland.statistics.api.parameters;

import java.util.Objects;

import org.n52.iceland.statistics.api.parameters.Description.InformationOrigin;
import org.n52.iceland.statistics.api.parameters.Description.Operation;

public interface ObjectEsParameterFactory {
    String VALUE_REFERENCE = "value-reference";

    // ----------------- OBJECTS DETEILS -----------------//
    // ---------------- COUNTRY CODE ---------------------//
    SingleEsParameter GEOLOC_COUNTRY_CODE = new SingleEsParameter(
            "country-code",
            new Description(InformationOrigin.None, Operation.None,
                            "[ISO-3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) two letter country code"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter GEOLOC_CITY_NAME = new SingleEsParameter(
            "city-name",
            new Description(InformationOrigin.None, Operation.None,
                            "name of the nearest city based on the IP address"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter GEOLOC_GEO_POINT = new SingleEsParameter(
            "geopoint",
            new Description(InformationOrigin.None, Operation.None,
                            "latitude and longitude coordinates of the client"),
            ElasticsearchTypeRegistry.GEO_POINT_FIELD);

    // ---------------- BYTES WRITTEN --------------------//
    SingleEsParameter BYTES = new SingleEsParameter(
            "bytes",
            new Description(InformationOrigin.None, Operation.None, "Size in bytes"),
            ElasticsearchTypeRegistry.LONG_FIELD);

    SingleEsParameter DISPLAY_BYTES = new SingleEsParameter(
            "display",
            new Description(InformationOrigin.None, Operation.None,
                            "Size in human readable form"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    // ----------------- EXTENSION -----------------------//
    SingleEsParameter EXTENSION_DEFINITION = new SingleEsParameter(
            "extension-definition",
            new Description(InformationOrigin.None, Operation.None, "Definition"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter EXTENSION_IDENTIFIER = new SingleEsParameter(
            "extension-identifier",
            new Description(InformationOrigin.None, Operation.None, "Identifier"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter EXTENSION_VALUE = new SingleEsParameter(
            "extension-value",
            new Description(InformationOrigin.None, Operation.None,
                            "Value object `toString()` version"),
            ElasticsearchTypeRegistry.STRING_ANALYZED_FIELD);

    // --------------- SPATIAL FILTER --------------------//
    SingleEsParameter SPATIAL_FILTER_OPERATOR = new SingleEsParameter(
            "operation",
            new Description(InformationOrigin.None, Operation.None, "Spatial Operator"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter SPATIAL_FILTER_SHAPE = new SingleEsParameter(
            "shape",
            new Description(InformationOrigin.None, Operation.None, "Elasticsearch shape"),
            ElasticsearchTypeRegistry.GEO_SHAPE_FIELD);

    SingleEsParameter SPATIAL_FILTER_VALUE_REF = new SingleEsParameter(
            VALUE_REFERENCE,
            new Description(InformationOrigin.None, Operation.None, "spatial filter value reference"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    // ---------------- TIME -------------------//
    SingleEsParameter TIME_DURARTION = new SingleEsParameter(
            "duration-ms",
            new Description(InformationOrigin.None, Operation.None,
                            "Duration between the END-START timestamp in milliseconds"),
            ElasticsearchTypeRegistry.LONG_FIELD);

    SingleEsParameter TIME_START = new SingleEsParameter(
            "start",
            new Description(InformationOrigin.None, Operation.None, "Start timestamp"),
            ElasticsearchTypeRegistry.DATE_FIELD);

    SingleEsParameter TIME_END = new SingleEsParameter(
            "end",
            new Description(InformationOrigin.None, Operation.None, "End timestamp"),
            ElasticsearchTypeRegistry.DATE_FIELD);

    SingleEsParameter TIME_TIMEINSTANT = new SingleEsParameter(
            "timeInstant",
            new Description(InformationOrigin.None, Operation.None, "Timestamp if the value is TimeInstant type"),
            ElasticsearchTypeRegistry.DATE_FIELD);

    SingleEsParameter TIME_SPAN_AS_DAYS = new SingleEsParameter(
            "span-days",
            new Description(InformationOrigin.None, Operation.None,
                            "This is a computed field based on the start timestamp " +
                            "and end timestamp of the TimePeriod instances. " +
                            "The value is the date for each days which are " +
                            "between the start/end timestamps interval. " +
                            "Intarval ends are included."),
            ElasticsearchTypeRegistry.DATE_FIELD);

    // ---------------- TEMPORAL FILTER INC TIME -------------------//
    SingleEsParameter TEMPORAL_FILTER_OPERATOR = new SingleEsParameter(
            "operator",
            new Description(InformationOrigin.None, Operation.None, "Temporal Operator"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter TEMPORAL_FILTER_VALUE_REF = new SingleEsParameter(
            VALUE_REFERENCE,
            new Description(InformationOrigin.None, Operation.None, "temporal filter value reference"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    // ----------- OmObservationConstellation -----------//
    SingleEsParameter OMOCONSTELL_PROCEDURE = new SingleEsParameter(
            "procedure",
            new Description(InformationOrigin.None, Operation.None, "Procedure"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter OMOCONSTELL_OBSERVABLE_PROPERTY = new SingleEsParameter(
            "observable-property",
            new Description(InformationOrigin.None, Operation.None, "Observable property"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter OMOCONSTELL_FEATURE_OF_INTEREST = new SingleEsParameter(
            "feature-of-interest",
            new Description(InformationOrigin.None, Operation.None, "Feature of interest"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    SingleEsParameter OMOCONSTELL_OBSERVATION_TYPE = new SingleEsParameter(
            "observation-type",
            new Description(InformationOrigin.None, Operation.None, "Observation type"),
            ElasticsearchTypeRegistry.STRING_FIELD);

    // ----------- OmObservation -----------//
    ObjectEsParameter OMOBS_CONSTELLATION = omObservationConstellation(
            "constellation",
            new Description(InformationOrigin.None, Operation.None,
                            "Observation constellation"));

    ObjectEsParameter OMOBS_SAMPLING_GEOMETRY
            = spatialFilter("sampling-geometry",
                            new Description(InformationOrigin.None, Operation.None, "Observation geometry"));

    ObjectEsParameter OMOBS_PHENOMENON_TIME = time("phenomenon-time", null);

    ObjectEsParameter OMOBS_RESULT_TIME = time("result-time", null);

    ObjectEsParameter OMOBS_VALID_TIME = time("valid-time", null);

    // ---------------------------------------//
    // --------- COMPOSITE PARAMETERS --------//
    // ---------------------------------------//
    static ObjectEsParameter geoLocation(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, GEOLOC_COUNTRY_CODE,
                                     GEOLOC_CITY_NAME, GEOLOC_GEO_POINT);
    }

    static ObjectEsParameter bytesWritten(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, BYTES, DISPLAY_BYTES);
    }

    static ObjectEsParameter extension(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, EXTENSION_DEFINITION,
                                     EXTENSION_IDENTIFIER, EXTENSION_VALUE);
    }

    static ObjectEsParameter spatialFilter(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, SPATIAL_FILTER_OPERATOR,
                                     SPATIAL_FILTER_SHAPE, SPATIAL_FILTER_VALUE_REF);
    }

    static ObjectEsParameter time(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, TIME_DURARTION, TIME_START,
                                     TIME_END, TIME_TIMEINSTANT, TIME_SPAN_AS_DAYS);
    }

    static ObjectEsParameter temporalFilter(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, TIME_DURARTION, TIME_START,
                                     TIME_END, TIME_TIMEINSTANT, TIME_SPAN_AS_DAYS,
                                     TEMPORAL_FILTER_OPERATOR, TEMPORAL_FILTER_VALUE_REF);
    }

    static ObjectEsParameter omObservationConstellation(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, OMOCONSTELL_PROCEDURE,
                                     OMOCONSTELL_OBSERVABLE_PROPERTY, OMOCONSTELL_FEATURE_OF_INTEREST,
                                     OMOCONSTELL_OBSERVATION_TYPE);
    }

    static ObjectEsParameter omObservation(String objectName, Description description) {
        Objects.requireNonNull(objectName);

        return new ObjectEsParameter(objectName, description, OMOBS_CONSTELLATION,
                                     OMOBS_SAMPLING_GEOMETRY, OMOBS_PHENOMENON_TIME, OMOBS_RESULT_TIME,
                                     OMOBS_VALID_TIME);
    }

}
