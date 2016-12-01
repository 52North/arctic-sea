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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.Format;

import com.google.common.base.MoreObjects;

import org.n52.shetland.ogc.wps.data.ValueProcessData;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class StringValueProcessData extends ValueProcessData {

    private final String string;

    public StringValueProcessData(OwsCode id, String string) {
        this(id, null, string);
    }

    public StringValueProcessData(OwsCode id, Format format, String string) {
        super(id, format);
        this.string = Objects.requireNonNull(string);
    }

    public StringValueProcessData(String string) {
        this(null, null, string);
    }

    public StringValueProcessData() {
        this(null, null, null);
    }

    @Override
    public InputStream getData() {
        return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

      @Override
    public int hashCode() {
        return Objects.hash(getId(), getFormat(), this.string);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StringValueProcessData other = (StringValueProcessData) obj;
        return Objects.equals(getId(), other.getId()) &&
               Objects.equals(getFormat(), other.getFormat()) &&
               Objects.equals(this.string, other.string);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("id", getId())
                .add("format", getFormat())
                .add("value", this.string)
                .toString();
    }

}
