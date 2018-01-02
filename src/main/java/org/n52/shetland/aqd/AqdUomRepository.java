/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.aqd;

public class AqdUomRepository {

    public static Uom getAqdUom(String v) {
        try {
            return UomConcentration.from(v);
        } catch (Exception e) {
            return null;
        }
    }

    public interface Uom {
        String BASE_UNIT = "http://dd.eionet.europa.eu/vocabulary/uom/";

        String getId();

        String getNotation();

        String getConceptURI();
    }

    public enum UomConcentration implements Uom {
        MilligramsCubicMetre("mg.m-3", "mg/m3"),
        NanogramsSquareMetreDay("ng.m-2.day-1", "ng/m2/day"),
        NanogramsCubicMetre("ng.m-3", "ng/m3"),
        PircogramsCubicMetre("pg.m-3", "pg/m3"),
        MicrogramsSquareMetreDay("ug.m-2.day-1", "ug/m2/day"),
        MicrogramsCubicMetre("ug.m-3", "ug/m3"),
        MicrogramsCubicMetreDay("ug.m-3.day", "ug/m3·day"),
        MicrogramsCubicMetreHour("ug.m-3.h", "ug/m3·h");

        private static final String CONCENTRATION_BASE_UNIT = BASE_UNIT + "concentration/";

        private final String conceptURI;

        private final String id;

        private final String notation;

        UomConcentration(String id, String notation) {
            this.id = id;
            this.notation = notation;
            this.conceptURI = CONCENTRATION_BASE_UNIT + id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getNotation() {
            return notation;
        }

        @Override
        public String getConceptURI() {
            return conceptURI;
        }

        public static UomConcentration from(String v) {
            for (UomConcentration c : UomConcentration.values()) {
                if (c.getNotation().equals(v) || c.getId().equals(v) || c.getConceptURI().equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }
    }

}
