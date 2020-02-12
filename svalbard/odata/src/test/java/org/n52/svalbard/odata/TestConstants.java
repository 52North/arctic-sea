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
package org.n52.svalbard.odata;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface TestConstants {

    String[] EXAMPLE_FUNCTIONS = {
            "substringof('Sensor Things',description)",
            "endswith(description,'Things')",
            "startswith(description,'Sensor')",
            "length(description) eq 13",
            "indexof(description,'Sensor') eq 1",
            "substring(description,1) eq 'ensor Things'",
            "tolower(description) eq 'sensor things'",
            "toupper(description) eq 'SENSOR THINGS'",
            "trim(description) eq 'Sensor Things'",
            "concat(concat(unitOfMeasurement/symbol,', '), unitOfMeasurement/name) eq 'degree, Celsius'",
            "year(resultTime) eq 2015",
            "month(resultTime) eq 12",
            "day(resultTime) eq 8",
            "hour(resultTime) eq 1",
            "minute(resultTime) eq 0",
            "second(resultTime) eq 0",
            "second(resultTime) eq 0",
            "date(resultTime) ne date(validTime)",
            "time(resultTime) le validTime",
            "totaloffsetminutes(resultTime) eq 60",
            "resultTime ge now()",
            "resultTime eq mindatetime()",
            "resultTime eq maxdatetime()",
            "round(result) eq 32",
            "floor(result) eq 32",
            "ceiling(result) eq 33",
            "geo.distance(location, geography'POINT (30 10) ') eq 52",
            "geo.length(geography'LINESTRING (30 10, 10 30, 40 40) ') eq 52",
            "geo.intersects(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "st_equals(location, geography'POINT (30 10)')",
            "st_disjoint(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "st_touches(location, geography'LINESTRING (30 10, 10 30, 40 40)')",
            "st_within(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "st_overlaps(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "st_crosses(location, geography'LINESTRING (30 10, 10 30, 40 40)')",
            "st_intersects(location, geography'LINESTRING (30 10, 10 30, 40 40)')",
            "st_contains(location, geography'POINT (30 10)')",
            "st_relate(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))', 'T********')"
    };

    String[] EXAMPLE_QUERIES = {
//            "$expand=Datastreams",
//            "$expand=Datastreams/ObservedProperty ",
//            "$expand=Observations,ObservedProperty ",
//            "$expand=Observations($filter=result eq 1)",
//            "$expand=Observations($select=result)",
//            "$expand=Datastream&$orderby=Datastreams/id desc, phenomenonTime",
//
//            "$select=result,resultTime",
//            "$select=id,Observations&$expand=Observations/FeatureOfInterest",
//
//            "$orderby=result",
//
//            "$top=5",
//            "$top=5&$orderby=phenomenonTime%20desc",
//
//            "$skip=5",
//
//            "$count=true",
//
//            "$filter=unitOfMeasurement/name eq 'degree Celsius'",
//            "$filter=unitOfMeasurement/name ne 'degree Celsius'",
//            "$filter=result gt 20.0",
//            "$filter=result ge 20.0",
//            "$filter=result lt 100",
//            "$filter=result le 100",
            "$filter=result le 3.5 and FeatureOfInterest/id eq '1'",
            "$filter=result gt 20 or result le 3.5",
            "$filter=not startswith(description,'test')",
            "$filter=result add 5 gt 10",
            "$filter=result sub 5 gt 10",
            "$filter=result mul 2 gt 2000",
            "$filter=result div 2 gt 4",
            "$filter=result mod 2 eq 0",
            "$filter=(result sub 5) gt 10",

            "$filter=geo.distance(Locations/location, geography'POINT(-122 43)') gt 1",
            "$expand=Observations($filter=result eq 1) ",
    };
}
