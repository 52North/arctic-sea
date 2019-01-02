/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.api.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class GeoLiteFileDownloaderIT {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void downloadFiletoCDriveTemp() throws URISyntaxException, IOException {
        String basePath = folder.newFolder().getAbsolutePath();
        GeoLiteFileDownloader.downloadDefaultDatabases(basePath);

        File city = new File(basePath + "/city.mmdb");
        File country = new File(basePath + "/country.mmdb");

        Assert.assertTrue(city.exists());
        Assert.assertTrue(country.exists());

    }
}
