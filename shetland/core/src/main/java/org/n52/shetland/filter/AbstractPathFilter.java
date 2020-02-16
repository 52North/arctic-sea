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

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.n52.shetland.ogc.filter.FilterClause;

public abstract class AbstractPathFilter implements FilterClause {

    private final List<PathFilterItem> items = new LinkedList<>();

    public AbstractPathFilter(PathFilterItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public AbstractPathFilter(List<PathFilterItem> items) {
        this.items.addAll(items);
    }

    /**
     * @return the items
     */
    public List<PathFilterItem> getItems() {
        return items;
    }


    @Override public int hashCode() {
        return Objects.hash(items);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof AbstractPathFilter)) {
            return false;
        }

        return this.items.equals(((AbstractPathFilter) o).getItems());
    }
}
