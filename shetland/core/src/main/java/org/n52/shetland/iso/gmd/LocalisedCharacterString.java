/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class LocalisedCharacterString extends AbtractGmd {

    private String value;

    private String locale;

    public LocalisedCharacterString(String value) {
        setValue(value);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public LocalisedCharacterString setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * @return the locale
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public LocalisedCharacterString setLocale(String locale) {
        this.locale = locale;
        return this;
    }

    public boolean isSetLocale() {
        return !Strings.isNullOrEmpty(getLocale());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalisedCharacterString) {
            LocalisedCharacterString that = (LocalisedCharacterString) obj;
            return Objects.equal(getValue(), that.getValue())
                    && Objects.equal(getLocale(), that.getLocale());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("Value", getValue())
                .add("Locale", getLocale()).toString();
    }

}
