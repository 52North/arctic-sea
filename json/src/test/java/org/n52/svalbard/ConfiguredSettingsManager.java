/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ConfiguredSettingsManager
        extends ExternalResource {
    private static final Logger LOG = LoggerFactory.getLogger(ConfiguredSettingsManager.class);

    private File tempDir;

    @Override
    protected void before()
            throws Throwable {
        createDirectory();
    }

    @Override
    protected void after() {
        deleteDirectory();
    }

    protected void createDirectory()
            throws IOException {
        tempDir = File.createTempFile("sos-test-case", "");
        FileUtils.forceDelete(tempDir);
        FileUtils.forceMkdir(tempDir);
    }

    protected void deleteDirectory() {
        try {
            if (tempDir != null) {
                FileUtils.deleteDirectory(tempDir);
            }
        } catch (IOException ex) {
            LOG.error("Error deleting temp dir", ex);
        }
    }
}
