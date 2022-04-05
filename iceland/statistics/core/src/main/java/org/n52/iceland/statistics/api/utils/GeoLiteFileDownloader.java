/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.statistics.api.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.annotation.Configurable;

@Configurable
public class GeoLiteFileDownloader {
    public static final String CITY_GZ_FILE_NAME = "city.mmdb.gz";
    public static final String COUNTRY_GZ_FILE_NAME = "country.mmdb.gz";
    public static final String CITY_FILE_NAME = "city.mmdb";
    public static final String COUNTRY_FILE_NAME = "country.mmdb";
    private static final Logger LOG = LoggerFactory.getLogger(GeoLiteFileDownloader.class);

    public static void downloadDefaultDatabases(String folderPath) {
        LOG.info("Downloading default databases to {}", folderPath);

        try {

            // create folder
            File folder = new File(folderPath);
            if (!folder.exists()) {
                try {
                    FileUtils.forceMkdir(folder);
                } catch (IOException e) {
                    LOG.error("Can not create folder", e);
                    return;
                }
            }

            Properties prop = new Properties();
            try (InputStream stream = GeoLiteFileDownloader.class
                    .getResourceAsStream("/statistics/geolitepaths.properties")) {
                prop.load(stream);
            }

            String cityUrl = prop.getProperty("url.city");
            String countryUrl = prop.getProperty("url.country");

            if (cityUrl == null || countryUrl == null) {
                LOG.error("Urls not found in geolitepaths.properties file");
                return;
            }

            String cityOutPath = folder.getPath().concat("/").concat(CITY_GZ_FILE_NAME);
            String countryOutPath = folder.getPath().concat("/").concat(COUNTRY_GZ_FILE_NAME);

            download(cityUrl, cityOutPath);
            download(countryUrl, countryOutPath);
            unzip(cityOutPath);
            unzip(countryOutPath);

        } catch (IOException e) {
            LOG.error("Error during default download", e);
        } catch (Throwable e) {
            LOG.error(null, e);
        }
    }

    private static void download(String cityUrl, String cityOutPath) throws IOException {
        LOG.info("Downloading {} to {}", cityUrl, cityOutPath);
        FileDownloader.downloadFile(cityUrl, cityOutPath);
    }

    private static void unzip(String cityOutPath) throws IOException {
        LOG.info("Gunzip {}", cityOutPath);
        FileDownloader.gunzipFile(cityOutPath);
    }
}
