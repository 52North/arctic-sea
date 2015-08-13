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
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n52.iceland.statistics.api.interfaces.geolocation.IAdminStatisticsLocation.LocationDatabaseType;
import org.n52.iceland.statistics.api.parameters.ObjectEsParameterFactory;
import org.n52.iceland.util.net.IPAddress;

/**
 * TODO This test class needs some geolite database to be downloaded before hand
 * but they are around ~20MBs and it is not nice thing to upload such a big
 * files on github. What should be the course of action here?
 */
public class StatisticsLocationUtilTestNotYet {

    private static String countryDb = null;
    private static String cityDb = null;
    private static StatisticsLocationUtil loc = new StatisticsLocationUtil();

    @BeforeClass
    public static void setUp() throws URISyntaxException {
        countryDb = new File(StatisticsLocationUtilTestNotYet.class.getResource("/geolite/country.mmdb").toURI()).getAbsolutePath();
        cityDb = new File(StatisticsLocationUtilTestNotYet.class.getResource("/geolite/city.mmdb").toURI()).getAbsolutePath();
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
