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

import java.util.Set;

import org.n52.shetland.ogc.filter.FilterClause;

public class PathFilterItem {

    private String path;

    private Set<FilterClause> filters;

    public PathFilterItem(String path) {
        this.path = path;
    }

    public PathFilterItem(String path, Set<FilterClause> filters) {
        this(path);
        this.filters = filters;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the filters
     */
    public Set<FilterClause> getFilters() {
        return filters;
    }

    /**
     * @param filters
     *            the filters to set
     */
    public void setFilters(Set<FilterClause> filters) {
        this.filters = filters;
    }
}
