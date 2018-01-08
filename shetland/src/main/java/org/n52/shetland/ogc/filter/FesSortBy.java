/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.filter;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * class for FES SortBy element
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 *
 */
public class FesSortBy implements AbstractSortingClause {

    private List<FesSortProperty> sortProperties = Lists.newArrayList();

    public FesSortBy(FesSortProperty sortProperty) {
        sortProperties.add(sortProperty);
    }

    public FesSortBy(List<FesSortProperty> sortProperties) {
        sortProperties.addAll(sortProperties);
    }

    /**
     * @return the sortProperties
     */
    public List<FesSortProperty> getSortProperties() {
        return Collections.unmodifiableList(sortProperties);
    }

    public FesSortBy addSortProperty(FesSortProperty sortProperty) {
        this.sortProperties.add(sortProperty);
        return this;
    }

    public FesSortBy addSortProperties(List<FesSortProperty> sortProperties) {
        this.sortProperties.addAll(sortProperties);
        return this;
    }

}
