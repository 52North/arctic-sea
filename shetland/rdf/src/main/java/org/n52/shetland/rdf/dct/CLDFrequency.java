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
package org.n52.shetland.rdf.dct;

public enum CLDFrequency {
    TRIENNIAL("triennial"),
    BIENNIAL("biennial"),
    ANNUAL("annual"),
    SEMIANNUAL("semiannual"),
    THREE_TIMES_A_YEAR("threeTimesAYear"),
    QUARTERLY("quarterly"),
    BIMONTHLY("bimonthly"),
    MONTHLY("monthly"),
    SEMIMONTHLY("semimonthly"),
    BIWEEKLY("biweekly"),
    THREE_TIMES_A_MONTH("threeTimesAMonth"),
    WEEKLY("weekly"),
    SEMIWEEKLY("semiweekly"),
    THREE_TIMES_A_WEEK("threeTimesAWeek"),
    DAILY("daily"),
    CONTINUOUS("continuous"),
    IRREGULAR("irregular");
    private static final String NS = "http://purl.org/cld/freq/";
    private final String uri;

    CLDFrequency(String name) {
        this.uri = NS + name;
    }

    public String getURI() {
        return uri;
    }

}
