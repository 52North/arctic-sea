/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.filter.AbstractSortingClause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * class for OrderBy element
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
public class OrderByFilter implements AbstractSortingClause {

    private final List<OrderProperty> sortProperties;

    public OrderByFilter(OrderProperty sortProperty) {
        this.sortProperties = new ArrayList<>();
        if (sortProperty != null) {
            sortProperties.add(sortProperty);
        }
    }

    public OrderByFilter(List<OrderProperty> sortProperties) {
        this.sortProperties = new ArrayList<>();
        if (sortProperties != null) {
            this.sortProperties.addAll(sortProperties);
        }
    }

    /**
     * @return the sortProperties
     */
    public List<OrderProperty> getSortProperties() {
        return Collections.unmodifiableList(sortProperties);
    }

    @Override public int hashCode() {
        return Objects.hash(sortProperties);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof OrderByFilter)) {
            return false;
        }

        return this.sortProperties.equals(((OrderByFilter) o).getSortProperties());
    }

    @Override public String toString() {
        return "$orderby=" +
                this.sortProperties.stream().map(OrderProperty::toString).collect(Collectors.joining(", "));
    }
}
