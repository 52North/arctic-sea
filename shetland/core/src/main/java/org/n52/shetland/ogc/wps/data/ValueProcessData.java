/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import java.io.IOException;
import java.io.InputStream;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.Format;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class ValueProcessData extends FormattedProcessData {

    public ValueProcessData(OwsCode id) {
        this(id, null);
    }

    public ValueProcessData(OwsCode id, Format format) {
        super(id, format);
    }

    public ValueProcessData() {
        this(null, null);
    }

    public abstract InputStream getData() throws IOException;

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public ValueProcessData asValue() {
        return this;
    }

}
