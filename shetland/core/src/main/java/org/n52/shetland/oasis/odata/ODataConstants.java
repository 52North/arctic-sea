/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.oasis.odata;

public interface ODataConstants {

    interface LogicOperators {
        String EQUALS = "eq";
        String NOT_EQUALS = "ne";
        String GREATER_THAN = "gt";
        String GREATER_THAN_OR_EQUAL = "ge";
        String LESS_THAN = "lt";
        String LESS_THAN_OR_EQUAL = "le";
        String AND = "and";
        String OR = "or";
        String NOT = "not";
        String HAS = "has";
        String IN = "in";
    }

    interface ArithmeticsOperators {
        String ADD = "add";
        String SUB = "sub";
        String MUL = "mul";
        String NEG = "-";
        String DIV = "div";
        String DIVBY = "divby";
        String MOD = "mod";
    }

    interface Grouping {

    }

    interface StringAndCollectionFunctions {
        String CONCAT = "concat";
        String CONTAINS = "contains";
        String ENDSWITH = "endswith";
        String INDEXOF = "indexof";
        String LENGTH = "length";
        String STARTSWITH = "startswith";
        String SUBSTRING = "substring";
        String SUBSTRINGOF = "substringof";
    }

    interface CollectionFunctions extends StringAndCollectionFunctions {
        String HASSUBSET = "hassubset";
        String HASSUBSSEQUENCE = "hassubsequence";
    }

    interface StringFunctions extends StringAndCollectionFunctions {
        String MATCHES_PATTERN = "matchesPattern";
        String TOLOWER = "tolower";
        String TOUPPER = "toupper";
        String TRIM = "trim";
    }

    interface DateAndTimeFunctions {
        String DATE = "date";
        String DAY = "day";
        String FRACTIONALSECONDS = "fractionalseconds";
        String HOUR = "hour";
        String MAXDATETIME = "maxdatetime";
        String MINDATETIME = "mindatetime";
        String MINUTE = "minute";
        String MONTH = "month";
        String NOW = "now";
        String SECOND = "second";
        String TIME = "time";
        String TOTALOFFSETMINUTES = "totaloffsetminutes";
        String TOTALSECONDS = "totalseconds";
        String YEAR = "year";
    }

    interface ArithmeticFunctions {
        String CEILING = "ceiling";
        String FLOOR = "floor";
        String ROUND = "round";
    }

    interface TypeFunctions {
        String CAST = "cast";
        String ISOF = "isof";
    }

    interface GeoFunctions {
        String GEO_DISTANCE = "geo.distance";
        String GEO_INTERSECTS = "geo.intersects";
        String GEO_LENGTH = "geo.length";
    }

    interface SpatialFunctions {
        String ST_EQUALS = "st_equals";
        String ST_DISJOINT = "st_disjoint";
        String ST_TOUCHES = "st_touches";
        String ST_WITHIN = "st_within";
        String ST_OVERLAPS = "st_overlaps";
        String ST_CROSSES = "st_crosses";
        String ST_INTERSECTS = "st_intersects";
        String ST_CONTAINS = "st_contains";
        String ST_RELATE = "st_relate";
    }

    interface ConditionalFunctions {
        String CASE = "case";
    }

    interface LambdaFunctions {
        String ANY = "any";
        String ALL = "all";
    }

    interface Literals {
        String NULL = "null";
        String IT = "$it";
        String ROOT = "$root";
        String THIS = "$this";
    }

    interface QueryOptions {
        String FILTER = "$filter";
        String EXPAND = "$expand";
        String SELECT = "$select";
        String ORDERBY = "$orderby";
        String TOP = "$top";
        String SKIP = "$skip";
        String COUNT = "$count";
        String SEARCH = "$search";
        String FORMAT = "$format";
        String COMPUTE = "$compute";
        String INDEX = "$index";
        String SCHEMAVERSION = "$schemaversion";
    }
}
