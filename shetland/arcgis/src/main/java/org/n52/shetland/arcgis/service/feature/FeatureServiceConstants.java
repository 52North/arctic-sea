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

}
