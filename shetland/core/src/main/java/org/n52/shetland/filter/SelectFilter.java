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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.n52.shetland.ogc.filter.FilterClause;

public class SelectFilter implements FilterClause {

    private final Set<String> items = new LinkedHashSet<>();

    public SelectFilter(String item) {
        if (item != null) {
            this.items.add(item);
        }
    }

    public SelectFilter(Collection<String> items) {
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public Set<String> getItems() {
        return Collections.unmodifiableSet(items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof SelectFilter)) {
            return false;
        }

        return Objects.equals(this.getItems(), ((SelectFilter) o).getItems());
    }

    @Override
    public String toString() {
        return "$select=" + String.join(",", this.items);
    }
}
