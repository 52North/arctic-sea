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

package org.n52.shetland.filter;

import org.n52.shetland.ogc.filter.FilterClause;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SelectFilter implements FilterClause {

    private final Set<String> items;

    public SelectFilter(String item) {
        this.items = new HashSet<>();
        items.add(item);
    }

    public SelectFilter(Set<String> items) {
        this.items = items;
    }

    public Set<String> getItems() {
        return this.items;
    }

    @Override public int hashCode() {
        return Objects.hash(items);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof SelectFilter)) {
            return false;
        }

        return Objects.equals(this.getItems(), ((SelectFilter) o).getItems());
    }
}
