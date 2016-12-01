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
package org.n52.shetland.ogc.wps.data;

import java.util.Objects;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.Format;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class FormattedProcessData extends ProcessData {

    private Format format;

    public FormattedProcessData(OwsCode id) {
        this(id, null);
    }

    public FormattedProcessData(OwsCode id, Format format) {
        super(id);
        this.format = format == null ? new Format() : format;
    }

    public FormattedProcessData() {
        this(null, null);
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = Objects.requireNonNull(format);
    }

}
