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
package org.n52.shetland.filter;

import java.util.Objects;

import org.n52.shetland.ogc.filter.FilterClause;

public class CountFilter implements FilterClause {

    private final Boolean value;

    public CountFilter(boolean value) {
        this.value = value;
    }

    public CountFilter(String value) {
        this.value = Boolean.valueOf(value);
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof CountFilter)) {
            return false;
        }

        return this.value.equals(((CountFilter) o).getValue());
    }

    @Override
    public String toString() {
        return "$count=" + getValue().toString();
    }
}
