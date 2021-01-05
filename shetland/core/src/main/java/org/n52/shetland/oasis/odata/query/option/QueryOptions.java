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
package org.n52.shetland.oasis.odata.query.option;

import org.n52.shetland.filter.CountFilter;
import org.n52.shetland.filter.ExpandFilter;
import org.n52.shetland.filter.FilterFilter;
import org.n52.shetland.filter.OrderByFilter;
import org.n52.shetland.filter.SelectFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class to hold Query Parameters
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptions {

    private static Long DEFAULT_TOP = 100L;

    private String baseURL;

    private CountFilter countFilter;
    private OrderByFilter orderByFilter;
    private SelectFilter selectFilter;
    private ExpandFilter expandFilter;
    private SkipTopFilter skipFilter;
    private SkipTopFilter topFilter;
    private FilterFilter filterFilter;

    public QueryOptions(String baseURL, Set<FilterClause> queryFilters) {
        this.baseURL = baseURL;
        if (queryFilters != null) {
            queryFilters.forEach(input -> {
                if (input instanceof CountFilter) {
                    countFilter = (CountFilter) input;
                } else if (input instanceof OrderByFilter) {
                    orderByFilter = (OrderByFilter) input;
                } else if (input instanceof SelectFilter) {
                    selectFilter = (SelectFilter) input;
                } else if (input instanceof ExpandFilter) {
                    expandFilter = (ExpandFilter) input;
                } else if (input instanceof SkipTopFilter &&
                        ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Skip)) {
                    skipFilter = (SkipTopFilter) input;
                } else if (input instanceof SkipTopFilter &&
                        ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Top)) {
                    topFilter = (SkipTopFilter) input;
                } else if (input instanceof FilterFilter) {
                    filterFilter = (FilterFilter) input;
                }
            });
        }
        if (!hasTopFilter()) {
            this.topFilter = new SkipTopFilter(SkipTopOperator.Top, DEFAULT_TOP);
        }
    }

    public String getBaseURI() {
        return baseURL;
    }

    public boolean hasCountFilter() {
        return countFilter != null;
    }

    public CountFilter getCountFilter() {
        return countFilter;
    }

    public boolean hasTopFilter() {
        return topFilter != null;
    }

    public SkipTopFilter getTopFilter() {
        return topFilter;
    }

    public boolean hasSkipFilter() {
        return skipFilter != null;
    }

    public SkipTopFilter getSkipFilter() {
        return skipFilter;
    }

    public boolean hasOrderByFilter() {
        return orderByFilter != null;
    }

    public OrderByFilter getOrderByFilter() {
        return orderByFilter;
    }

    public boolean hasSelectFilter() {
        return selectFilter != null;
    }

    public SelectFilter getSelectFilter() {
        return selectFilter;
    }

    public boolean hasExpandFilter() {
        return expandFilter != null;
    }

    public ExpandFilter getExpandFilter() {
        return expandFilter;
    }

    public boolean hasFilterFilter() {
        return filterFilter != null;
    }

    public FilterFilter getFilterFilter() {
        return filterFilter;
    }

    public Set<FilterClause> getAllFilters() {
        Set<FilterClause> result = new HashSet<>();
        if (this.hasCountFilter()) {
            result.add(this.getCountFilter());
        }
        if (this.hasSelectFilter()) {
            result.add(this.getSelectFilter());
        }
        if (this.hasExpandFilter()) {
            result.add(this.getExpandFilter());
        }
        if (this.hasSkipFilter()) {
            result.add(this.getSkipFilter());
        }
        if (this.hasTopFilter()) {
            result.add(this.getTopFilter());
        }
        if (this.hasOrderByFilter()) {
            result.add(this.getOrderByFilter());
        }
        if (this.hasFilterFilter()) {
            result.add(this.getFilterFilter());
        }
        return result;
    }

    @Override public int hashCode() {
        return Objects.hash(baseURL,
                            countFilter,
                            topFilter,
                            skipFilter,
                            orderByFilter,
                            selectFilter,
                            expandFilter,
                            filterFilter);
    }

    // BaseURI is not always set -> we need to compare each Filter individually
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QueryOptions)) {
            return false;
        }
        QueryOptions obj = (QueryOptions) o;
        return obj.hasCountFilter() == this.hasCountFilter()
                && obj.hasOrderByFilter() == this.hasOrderByFilter()
                && obj.hasSelectFilter() == this.hasSelectFilter()
                && obj.hasExpandFilter() == this.hasExpandFilter()
                && obj.hasSkipFilter() == this.hasSkipFilter()
                && obj.hasTopFilter() == this.hasTopFilter()
                && obj.hasFilterFilter() == this.hasFilterFilter()
                && Objects.equals(obj.getCountFilter(), this.getCountFilter())
                && Objects.equals(obj.getOrderByFilter(), this.getOrderByFilter())
                && Objects.equals(obj.getSelectFilter(), this.getSelectFilter())
                && Objects.equals(obj.getExpandFilter(), this.getExpandFilter())
                && Objects.equals(obj.getSkipFilter(), this.getSkipFilter())
                && Objects.equals(obj.getTopFilter(), this.getTopFilter())
                && Objects.equals(obj.getFilterFilter(), this.getFilterFilter());
    }

    @Override
    public String toString() {
        return getAllFilters().stream()
                              .map(FilterClause::toString)
                              .collect(Collectors.joining("&"));
    }
}
