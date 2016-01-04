/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.api.interfaces.geolocation;

public interface IAdminStatisticsLocation {
    /**
     * The country or the city database indicator.
     */
    public enum LocationDatabaseType {
        // Maybe these string values will change in the future.
        // If there is any version change at the Maxmind GeoLite's side.
        CITY("GeoLite2-City"), COUNTRY("GeoLite2-Country");

        private final String geoLite2Name;

        private LocationDatabaseType(String geoLite2Name) {
            this.geoLite2Name = geoLite2Name;
        }

        public String getGeoLite2Name() {
            return geoLite2Name;
        }

    }

    /**
     * Initialize the memory database. The path and the
     * {@code LocationDatabaseType} MUST match.
     *
     * @param type
     *            type of the loaded database from file.
     * @param pathToDatabase
     *            can be <code>classpath:[path]</code> or
     *            <code>[absolute path]</code> string to the appropriate file.
     */
    public void initDatabase(LocationDatabaseType type,
            String pathToDatabase);
}
