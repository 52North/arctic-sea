/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.data.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.data.ValueProcessData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class FileBasedProcessData extends ValueProcessData {

    private final Path path;

    public FileBasedProcessData(OwsCode id, Format format, Path path) {
        super(id, format);
        this.path = Objects.requireNonNull(path, "path");
    }

    @Override
    public InputStream getData() throws IOException {
        return Files.newInputStream(this.path);
    }

    public Path getPath() {
        return this.path;
    }

}
