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
package org.n52.shetland.ogc.swe.simpleType;

import java.util.List;

import org.n52.shetland.ogc.swes.AbstractSWES;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

public class SweAllowedTokens
        extends AbstractSWES {

    private List<String> value;
    private String pattern;

    /**
     * @return the value
     */
    public List<String> getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(List<String> value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return CollectionHelper.isNotEmpty(getValue());
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern
     *            the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isSetPattern() {
        return !Strings.isNullOrEmpty(getPattern());
    }

}
