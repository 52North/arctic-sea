/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.impl;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.n52.iceland.statistics.api.StatisticsLocationUtilSettingsKeys;
import org.springframework.util.Assert;

public class StatisticsLocationUtilIT {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void downloadDefaultDatabases() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_AUTO);
        loc.setDownloadFolderPath(folder.newFolder("tmp").getAbsolutePath());
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_CITY);

        loc.init();
        Assert.notNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void manualCityDatabase() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        String filepath = new File(getClass().getResource("/geolite/city.mmdb").toURI()).getAbsolutePath();
        loc.setCityDbLoc(filepath);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_CITY);

        loc.init();
        Assert.notNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void manualCountryDatabase() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        String filepath = new File(getClass().getResource("/geolite/country.mmdb").toURI()).getAbsolutePath();
        loc.setCountryDbLoc(filepath);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_COUNTRY);

        loc.init();
        Assert.notNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void geolocIsDisabled() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(false);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        String filepath = new File(getClass().getResource("/geolite/country.mmdb").toURI()).getAbsolutePath();
        loc.setCountryDbLoc(filepath);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_COUNTRY);

        loc.init();
        Assert.isNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void manualDatabaseButLocationIsEmpty() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        loc.setCountryDbLoc(null);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_COUNTRY);

        loc.init();
        Assert.isNull(loc.ip2SpatialData("67.20.172.183"));
    }
}
