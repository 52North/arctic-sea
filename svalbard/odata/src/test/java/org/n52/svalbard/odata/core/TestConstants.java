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
package org.n52.svalbard.odata.core;

/**
 * List of all Function call examples from the official OGC Standard spec. Amended with additional common calls.
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface TestConstants {

    String[] EXAMPLE_FUNCTIONS = {
            "$filter=substringof('Sensor Things',description)",
            "$filter=endswith(description,'Things')",
            "$filter=startswith(description,'Sensor')",
            "$filter=length(description) eq 13",
            "$filter=indexof(description,'Sensor') eq 1",
            "$filter=substring(description,1) eq 'ensor Things'",
            "$filter=tolower(description) eq 'sensor things'",
            "$filter=toupper(description) eq 'SENSOR THINGS'",
            "$filter=trim(description) eq 'Sensor Things'",
            "$filter=concat(concat(unitOfMeasurement/symbol,', '), unitOfMeasurement/name) eq 'degree, Celsius'",
            "$filter=year(resultTime) eq 2015",
            "$filter=month(resultTime) eq 12",
            "$filter=day(resultTime) eq 8",
            "$filter=hour(resultTime) eq 1",
            "$filter=minute(resultTime) eq 0",
            "$filter=second(resultTime) eq 0",
            "$filter=second(resultTime) eq 0",
            "$filter=date(resultTime) ne date(validTime)",
            "$filter=time(resultTime) le validTime",
            "$filter=totaloffsetminutes(resultTime) eq 60",
            "$filter=resultTime ge now()",
            "$filter=resultTime eq mindatetime()",
            "$filter=resultTime eq maxdatetime()",
            "$filter=round(result) eq 32",
            "$filter=floor(result) eq 32",
            "$filter=ceiling(result) eq 33",
            "$filter=geo.distance(location, geography'POINT (30 10) ') eq 52",
            "$filter=geo.length(geography'LINESTRING (30 10, 10 30, 40 40) ') eq 52",
            "$filter=geo.intersects(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "$filter=st_equals(location, geography'POINT (30 10)')",
            "$filter=st_disjoint(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "$filter=st_touches(location, geography'LINESTRING (30 10, 10 30, 40 40)')",
            "$filter=st_within(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "$filter=st_overlaps(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))')",
            "$filter=st_crosses(location, geography'LINESTRING (30 10, 10 30, 40 40)')",
            "$filter=st_intersects(location, geography'LINESTRING (30 10, 10 30, 40 40)')",
            "$filter=st_contains(location, geography'POINT (30 10)')",
            "$filter=st_relate(location, geography'POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))', 'T********')",
            "$filter=unitOfMeasurement/name eq 'degree Celsius'",
            "$filter=unitOfMeasurement/name ne 'degree Celsius'",
            "$filter=result gt 20.0",
            "$filter=result ge 20.0",
            "$filter=result lt 100",
            "$filter=result le 100",
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

            "$select=result,resultTime",
            "$select=id,Observations&$expand=Observations/FeatureOfInterest",

            "$orderby=result",

            "$top=5",
            "$top=5&$orderby=phenomenonTime desc",

            "$skip=5",

            "$count=true",
            "$count=false",
            "$count=true&$top=1",

            "$expand=Observations($filter=result eq 1)",
            "$expand=Datastreams",
            "$expand=Datastreams/ObservedProperty ",
            "$expand=Observations,ObservedProperty ",
            "$expand=Observations($filter=result eq 1)",
            "$expand=Observations($select=result)",
            "$expand=Observations($top=52)",
            "$expand=Observations($skip=52)",
            "$expand=Observations($expand=result)",
            "$expand=Observations($orderby=result asc)",
            "$expand=Datastream&$orderby=Datastreams/id desc, phenomenonTime",
            "$orderby=phenomenonTime&$filter=phenomenonTime ge 2018-05-04T00:22:54.738+02:00 and " +
                    "2018-05-05T09:58:53.338+02:00 le 2018-05-05T09:58:53.338+02:00&$top=200",

            "$orderby=phenomenonTime&$filter=(phenomenonTime ge 2018-05-04T00:22:54.738+02:00) and " +
                    "(2018-05-05T09:58:53.338+02:00 le 2018-05-05T09:58:53.338+02:00)&$top=200",

            "$orderby=phenomenonTime&$filter=((phenomenonTime ge 2018-05-04T00:22:54.738+02:00) and " +
                    "(2018-05-05T09:58:53.338+02:00 le 2018-05-05T09:58:53.338+02:00))&$top=200"
    };
}
