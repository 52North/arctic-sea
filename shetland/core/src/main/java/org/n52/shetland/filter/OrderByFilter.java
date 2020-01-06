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

import java.util.Collections;
import java.util.List;

import org.n52.shetland.oasis.odata.query.option.OrderByOption;
import org.n52.shetland.ogc.filter.AbstractSortingClause;

import com.google.common.collect.Lists;

/**
 * class for OrderBy element
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 *
 */
public class OrderByFilter implements AbstractSortingClause, OrderByOption {

    private List<OrderProperty> sortProperties = Lists.newArrayList();

    public OrderByFilter(OrderProperty sortProperty) {
        if (sortProperty != null) {
            sortProperties.add(sortProperty);
        }
    }

    public OrderByFilter(List<OrderProperty> sortProperties) {
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

    public OrderByFilter addSortProperty(OrderProperty sortProperty) {
        this.sortProperties.add(sortProperty);
        return this;
    }

    public OrderByFilter addSortProperties(List<OrderProperty> sortProperties) {
        if (sortProperties != null) {
            this.sortProperties.addAll(sortProperties);
        }
        return this;
    }

}
