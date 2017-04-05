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
package org.n52.shetland.ogc;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractSupportedStringType implements SupportedType {

    private final String value;

    public AbstractSupportedStringType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractSupportedStringType that = (AbstractSupportedStringType) obj;
        return Objects.equals(this.getValue(), that.getValue());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("value", getValue()).toString();
    }

}
