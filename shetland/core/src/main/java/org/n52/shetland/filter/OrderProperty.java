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

import org.n52.shetland.ogc.filter.FilterConstants.SortOrder;

import java.util.Objects;

public class OrderProperty {

    private final String valueReference;

    private final SortOrder sortOrder;

    public OrderProperty(String valueReference) {
        this.valueReference = valueReference;
        this.sortOrder = null;
    }

    public OrderProperty(String valueReference, SortOrder order) {
        this.valueReference = valueReference;
        this.sortOrder = order;
    }

    /**
     * @return the valueReference
     */
    public String getValueReference() {
        return valueReference;
    }

    /**
     * @return the sortOrder
     */
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public boolean isSetSortOrder() {
        return getSortOrder() != null;
    }

    @Override public int hashCode() {
        return Objects.hash(valueReference, sortOrder);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof OrderProperty)) {
            return false;
        }

        return Objects.equals(this.valueReference, ((OrderProperty) o).getValueReference()) &&
                Objects.equals(this.sortOrder, ((OrderProperty) o).getSortOrder());
    }
}
