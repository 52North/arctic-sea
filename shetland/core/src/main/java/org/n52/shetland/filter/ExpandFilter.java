/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
import java.util.stream.Collectors;

public class ExpandFilter implements FilterClause {

    private final Set<ExpandItem> items;

    public ExpandFilter(ExpandItem item) {
        this.items = new HashSet<>();
        this.items.add(item);
    }

    public ExpandFilter(Set<ExpandItem> items) {
        this.items = items;
    }

    public Set<ExpandItem> getItems() {
        return items;
    }

    @Override public int hashCode() {
        return Objects.hash(this.items);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ExpandFilter)) {
            return false;
        }

        return this.items.equals(((ExpandFilter) o).getItems());
    }

    @Override public String toString() {
        return "$expand=" +
                this.items.stream().map(ExpandItem::toString).collect(Collectors.joining(", "));
    }
}
