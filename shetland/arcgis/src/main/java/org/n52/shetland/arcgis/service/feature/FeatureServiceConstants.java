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
package org.n52.shetland.arcgis.service.feature;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

public interface FeatureServiceConstants {

    interface Parameter {
        String QUERY = "query";
        String WHERE = "where";
        String FORMAT = "f";
        String OBJECT_IDS = "objectIds";
        String TIME = "time";
        String GEOMETRY = "geometry";
        String GEOMETRY_TYPE = "geometryType";
        String INTPUT_SPATIAL_REFERENCE = "inSR";
        String SPATIAL_RELATIONSHIP = "spatialRel";
        String DISTANCE = "distance";
        String UNITS = "units";
        String RELATION_PARAM = "relationParam";
        String OUT_FIELDS = "outFields";
        String RETURN_GEOMETRY = "returnGeometry";
        String MAX_ALLOWABLE_OFFSET = "maxAllowableOffset";
        String GEOMETRY_PRECISION = "geometryPrecision";
        String OUTPUT_SPATIAL_REFERENCE = "outSR";
        String HAVING_CLAUSE = "havingClause";
        String GEODATABASE_VERSION = "gdbVersion";
        String HISTORIC_MOMENT = "historicMoment";
        String RETURN_DISTINCT_VALUES = "returnDistinctValues";
        String RETURN_IDS_ONLY = "returnIdsOnly";
        String RETURN_COUNT_ONLY = "returnCountOnly";
        String RETURN_EXTENT_ONLY = "returnExtentOnly";
        String ORDER_BY_FIELDS = "orderByFields";
        String GROUP_FIELDS_FOR_STATISTICS = "groupByFieldsForStatistics";
        String OUTPUT_STATISTICS = "outStatistics";
        String RETURN_Z = "returnZ";
        String RETURN_M = "returnM";
        String MULTIPATCH_OPTIONS = "multipatchOption";
        String RESULT_OFFSET = "resultOffset";
        String RESULT_RECORD_COUNT = "resultRecordCount";
        String RETURN_TRUE_CURVES = "returnTrueCurves";
        String RETURN_CENTROID = "returnCentroid";
        String TIME_REFERENCE_UNKNOWN_CLIENT = "timeReferenceUnknownClient";
        String SQL_FORMAT = "sqlFormat";
        String RESULT_TYPE = "resultType";
        String DATUM_TRANSFORMATION = "datumTransformation";
        String LOD_TYPE = "lodType";
        String LOD_LEVEL = "lod";
        String LOD_SPATIAL_REFERENCE = "lodSR";
    }

    interface Values {
        String WILDCARD = "*";
        String DEFAULT_WHERE_VALUE = "1=1";
    }

    interface GeometryTypes {
        String POINT = "esriGeometryPoint";
        String MULTIPOINT = "esriGeometryMultipoint";
        String POLYLINE = "esriGeometryPolyline";
        String POLYGON = "esriGeometryPolygon";
        String ENVELOPE = "esriGeometryEnvelope";
    }

    interface SpatialRelations {
        String INTERSECTS = "esriSpatialRelIntersects";
        String CONTAINS = "esriSpatialRelContains";
        String CROSSES = "esriSpatialRelCrosses";
        String ENVELOPE_INTERSECTS = "esriSpatialRelEnvelopeIntersects";
        String INDEX_INTERSECTS = "esriSpatialRelIndexIntersects";
        String OVERLAPS = "esriSpatialRelOverlaps";
        String TOUCHES = "esriSpatialRelTouches";
        String WITHIN = "esriSpatialRelWithin";
    }

    interface Units {
        String METER = "esriSRUnit_Meter";
        String MILE = "esriSRUnit_StatuteMile";
        String FEET = "esriSRUnit_Foot";
        String KILOMETER = "esriSRUnit_Kilometer";
        String NAUTICAL_MILE = "esriSRUnit_NauticalMile";
        String US_NAUTICAL_MILE = "esriSRUnit_USNauticalMile";
    }

    interface MultipatchOptions {
        String EMBED_MATERIALS = "embedMaterials";
        String XY_FOOTPRINT = "xyFootprint";
        String EXTERNALIZE_TEXTURES = "externalizeTextures";
        String STRIP_MATERIALS = "stripMaterials";
        String EXTENT = "extent";
    }

    interface LodTypes {
        String GEOHASH = "geohash";
        String FLAT_HEXAGON = "flatHexagon";
        String POINTY_HEXAGON = "pointyHexagon";
        String SQUARE = "square";
    }

    interface Defaults {
        String NONE = "none";
        String STANDARD = "standard";
    }

    interface SqlFormats extends Defaults {
        String NATIVE = "native";
    }

    interface ResultTypes extends Defaults {
        String TILE = "tile";
    }

    interface HavingCLauses {
        String AVG = "AVG";
        String COUNT = "COUNT";
        String SUM = "SUM";
        String STDDEV = "STDDEV";
        String MIN = "MIN";
        String MAX = "MAX";
        String VAR = "VAR";
    }

    interface Formats {
        String HTML = "html";
        String GEO_JSON = "geojson";
        String JSON = "json";
        String PROTOBUF = "pbf";
    }

    interface WhereOperators {

        String GREATER_THAN = ">=";
        String LESS_THAN = "<=";
        String GREATER = ">";
        String LESS = "<";
        String EQUAL = "=";
        String NOT_EQUAL = "<>";
        String LIKE = "LIKE";
        String AND = "AND";
        String OR = "OR";
        String IS = "IS";
        String IS_NOT = "IS_NOT";
        String IN = "IN";
        String NOT_IN = "NOT_IN";
        String BETWEEEN = "BETWEEN";

        default String create(String operator, String field, String value) {
            return create(operator, field, value, "'");
        }

        default String create(String operator, String field, String value, String quote) {
            return new StringBuffer().append(field).append(" ").append(operator).append(" ").append(quote)
                    .append(value).append(quote).toString();
        }

        default String create(String operator, String field, Number value) {
            return new StringBuffer().append(field).append(" ").append(operator).append(" ").append(value).toString();
        }

        default String createIn(String... values) {
            return new StringBuffer().append("(")
                    .append(String.join(",",
                            Arrays.stream(values).map(v -> new StringBuffer("'").append(v.toString()).append("'"))
                                    .collect(Collectors.toCollection(TreeSet::new))))
                    .append(")").toString();
        }

        default String createIn(Number... values) {
            return new StringBuffer().append("(").append(String.join(",",
                    Arrays.stream(values).map(v -> v.toString()).collect(Collectors.toCollection(TreeSet::new))))
                    .append(")").toString();
        }

        default String greaterThan(String field, String value) {
            return create(GREATER_THAN, field, value);
        }

        default String greaterThan(String field, Number value) {
            return create(GREATER_THAN, field, value);
        }

        default String lessThan(String field, String value) {
            return create(LESS_THAN, field, value);
        }

        default String lessThan(String field, Number value) {
            return create(LESS_THAN, field, value);
        }

        default String greater(String field, String value) {
            return create(GREATER, field, value);
        }

        default String greater(String field, Number value) {
            return create(GREATER, field, value);
        }

        default String less(String field, String value) {
            return create(LESS, field, value);
        }

        default String less(String field, Number value) {
            return create(LESS, field, value);
        }

        default String equal(String field, String value) {
            return create(EQUAL, field, value);
        }

        default String equal(String field, Number value) {
            return create(EQUAL, field, value);
        }

        default String notEqual(String field, String value) {
            return create(NOT_EQUAL, field, value);
        }

        default String notEqual(String field, Number value) {
            return create(NOT_EQUAL, field, value);
        }

        default String like(String field, String value) {
            return create(LIKE, field, value);
        }

        default String like(String field, Number value) {
            return create(LIKE, field, value);
        }

        default String and(String field, String value) {
            return create(AND, field, value, "");
        }

        default String and(String field, Number value) {
            return create(AND, field, value);
        }

        default String or(String field, String value) {
            return create(OR, field, value, "");
        }

        default String or(String field, Number value) {
            return create(OR, field, value);
        }

        default String is(String field, String value) {
            return create(IS, field, value);
        }

        default String is(String field, Number value) {
            return create(IS, field, value);
        }

        default String isNot(String field, String value) {
            return create(IS_NOT, field, value);
        }

        default String isNot(String field, Number value) {
            return create(IS_NOT, field, value);
        }

        default String in(String field, String... values) {
            return create(IN, field, createIn(values), "");
        }

        default String in(String field, Number... values) {
            return create(IN, field, createIn(values), "");
        }

        default String notIn(String field, String... values) {
            return create(NOT_IN, field, createIn(values), "");
        }

        default String notIn(String field, Number... values) {
            return create(NOT_IN, field, createIn(values), "");
        }

        default String between(String field, String value1, String value2) {
            return create(AND, create(BETWEEEN, field, value1), value2);
        }

        default String between(String field, Number value1, Number value2) {
            return and(create(BETWEEEN, field, value1), value2);
        }
    }

}
