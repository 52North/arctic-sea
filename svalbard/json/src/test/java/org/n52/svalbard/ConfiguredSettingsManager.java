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
package org.n52.svalbard;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ConfiguredSettingsManager implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static final Logger LOG = LoggerFactory.getLogger(ConfiguredSettingsManager.class);

    private File tempDir;

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        deleteDirectory();
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        createDirectory();
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
