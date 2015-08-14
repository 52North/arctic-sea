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
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.n52.iceland.statistics.api.StatisticsLocationUtilSettingsKeys;
import org.n52.iceland.statistics.api.interfaces.geolocation.IAdminStatisticsLocation.LocationDatabaseType;
import org.n52.iceland.statistics.api.parameters.ObjectEsParameterFactory;
import org.n52.iceland.statistics.api.utils.GeoLiteFileDownloader;
import org.n52.iceland.util.net.IPAddress;

public class StatisticsLocationUtilIT {

    private static String countryDb = null;
    private static String cityDb = null;
    private static StatisticsLocationUtil loc = new StatisticsLocationUtil();

    @ClassRule
    public static final TemporaryFolder folder = new TemporaryFolder();

    @BeforeClass
    public static void init() {
        GeoLiteFileDownloader.downloadDefaultDatabases(folder.getRoot().getAbsolutePath());
        countryDb = folder.getRoot().getAbsolutePath() + "/country.mmdb";
        cityDb = folder.getRoot().getAbsolutePath() + "/city.mmdb";
    }

    @Test
    public void downloadDefaultDatabases() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_AUTO);
        loc.setDownloadFolderPath(folder.newFolder("tmp").getAbsolutePath());
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_CITY);

        loc.init();
        Assert.assertNotNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void manualCityDatabase() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        String filepath = new File(getClass().getResource(cityDb).toURI()).getAbsolutePath();
        loc.setCityDbLoc(filepath);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_CITY);

        loc.init();
        Assert.assertNotNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void manualCountryDatabase() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        String filepath = new File(getClass().getResource(countryDb).toURI()).getAbsolutePath();
        loc.setCountryDbLoc(filepath);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_COUNTRY);

        loc.init();
        Assert.assertNotNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void geolocIsDisabled() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(false);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        String filepath = new File(getClass().getResource(countryDb).toURI()).getAbsolutePath();
        loc.setCountryDbLoc(filepath);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_COUNTRY);

        loc.init();
        Assert.assertNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void manualDatabaseButLocationIsEmpty() throws Exception {
        StatisticsLocationUtil loc = new StatisticsLocationUtil();
        loc.setEnabled(true);
        loc.setAutoDownload(StatisticsLocationUtilSettingsKeys.DATABASE_DOWNLOADER_MANUAL);
        loc.setCountryDbLoc(null);
        loc.setDbType(StatisticsLocationUtilSettingsKeys.DATABASE_TYPE_COUNTRY);

        loc.init();
        Assert.assertNull(loc.ip2SpatialData("67.20.172.183"));
    }

    @Test
    public void getIpAsCountry() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.COUNTRY, countryDb);
        IPAddress ip = new IPAddress("67.20.172.183");

        Map<String, Object> map = loc.ip2SpatialData(ip);
        Assert.assertEquals("US", map.get(ObjectEsParameterFactory.GEOLOC_COUNTRY_CODE.getName()));
    }

    @Test
    public void getIpAsCity() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.CITY, cityDb);
        IPAddress ip = new IPAddress("67.20.172.183");

        Map<String, Object> map = loc.ip2SpatialData(ip);

        Assert.assertNotNull(map);
        Assert.assertEquals("US", map.get(ObjectEsParameterFactory.GEOLOC_COUNTRY_CODE.getName()));
        Assert.assertNotNull(map.get(ObjectEsParameterFactory.GEOLOC_CITY_NAME.getName()));
        Assert.assertNotNull(map.get(ObjectEsParameterFactory.GEOLOC_GEO_POINT.getName()));
    }

    @Test
    public void wrongDatabaseAndType() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.CITY, countryDb);
        IPAddress ip = new IPAddress("67.20.172.183");
        Assert.assertNull(loc.ip2SpatialData(ip));
        // no harm done
    }

    @Test
    public void localhostIPAsCountry() {
        loc.setEnabled(true);
        loc.initDatabase(LocationDatabaseType.COUNTRY, countryDb);
        IPAddress ip = new IPAddress("127.0.0.1");
        Assert.assertNull(loc.ip2SpatialData(ip));
    }
}
