/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.api;

public class StatisticsLocationUtilSettingsKeys {

    public static final String ENABLED = "statistics.geoloc.enabled";
    public static final String DOWNLOAD_FOLDERPATH = "statistics.geoloc.download_folder";
    // CHOICE
    public static final String DATABASE_DOWNLOADER = "statistics.geoloc.db_downloader";
    public static final String DATABASE_DOWNLOADER_AUTO = "statistics.geoloc.db_downloader.auto";
    public static final String DATABASE_DOWNLOADER_MANUAL = "statistics.geoloc.db_downloader.manual";

    public static final String MANUAL_CITY_LOC = "statistics.geoloc.city_location";
    public static final String MANUAL_COUNTRY_LOC = "statistics.geoloc.country_location";

    // CHOICE
    public static final String DATABASE_TYPE = "statistics.geoloc.db_type";
    public static final String DATABASE_TYPE_CITY = "statistics.geoloc.db_type.city";
    public static final String DATABASE_TYPE_COUNTRY = "statistics.geoloc.db_type.country";

}
