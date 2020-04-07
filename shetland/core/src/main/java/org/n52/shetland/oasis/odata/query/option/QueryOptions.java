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

import java.util.Objects;
import java.util.Set;

/**
 * Class to hold Query Parameters
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptions {

    private static Long DEFAULT_TOP = 100L;

    private String baseURL;

    private CountFilter countOption;
    private OrderByFilter OrderByFilter;
    private SelectFilter SelectFilter;
    private ExpandFilter ExpandFilter;
    private SkipTopFilter skipOption;
    private SkipTopFilter topOption;
    private FilterFilter FilterFilter;

    public QueryOptions(String baseURL, Set<FilterClause> queryOptions) {
        this.baseURL = baseURL;
        if (queryOptions != null) {
            queryOptions.forEach(input -> {
                if (input instanceof CountFilter) {
                    countOption = (CountFilter) input;
                } else if (input instanceof OrderByFilter) {
                    OrderByFilter = (OrderByFilter) input;
                } else if (input instanceof SelectFilter) {
                    SelectFilter = (SelectFilter) input;
                } else if (input instanceof ExpandFilter) {
                    ExpandFilter = (ExpandFilter) input;
                } else if (input instanceof SkipTopFilter &&
                        ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Skip)) {
                    skipOption = (SkipTopFilter) input;
                } else if (input instanceof SkipTopFilter &&
                        ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Top)) {
                    topOption = (SkipTopFilter) input;
                } else if (input instanceof FilterFilter) {
                    FilterFilter = (FilterFilter) input;
                }
            });
        }
        if (!hasTopOption()) {
            this.topOption = new SkipTopFilter(SkipTopOperator.Top, DEFAULT_TOP);
        }
    }

    public String getBaseURI() {
        return baseURL;
    }

    public boolean hasCountOption() {
        return countOption != null;
    }

    public CountFilter getCountOption() {
        return countOption;
    }

    public boolean hasTopOption() {
        return topOption != null;
    }

    public SkipTopFilter getTopOption() {
        return topOption;
    }

    public boolean hasSkipOption() {
        return skipOption != null;
    }

    public SkipTopFilter getSkipOption() {
        return skipOption;
    }

    public boolean hasOrderByFilter() {
        return OrderByFilter != null;
    }

    public OrderByFilter getOrderByFilter() {
        return OrderByFilter;
    }

    public boolean hasSelectFilter() {
        return SelectFilter != null;
    }

    public SelectFilter getSelectFilter() {
        return SelectFilter;
    }

    public boolean hasExpandFilter() {
        return ExpandFilter != null;
    }

    public ExpandFilter getExpandFilter() {
        return ExpandFilter;
    }

    public boolean hasFilterFilter() {
        return FilterFilter != null;
    }

    public FilterFilter getFilterFilter() {
        return FilterFilter;
    }

    @Override public int hashCode() {
        return Objects.hash(baseURL,
                            countOption,
                            topOption,
                            skipOption,
                            OrderByFilter,
                            SelectFilter,
                            ExpandFilter,
                            FilterFilter);
    }

    // BaseURI is not always set -> we need to compare each option individually
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof QueryOptions)) {
            return false;
        }
        QueryOptions obj = (QueryOptions) o;
        return obj.hasCountOption() == this.hasCountOption()
                && obj.hasOrderByFilter() == this.hasOrderByFilter()
                && obj.hasSelectFilter() == this.hasSelectFilter()
                && obj.hasExpandFilter() == this.hasExpandFilter()
                && obj.hasSkipOption() == this.hasSkipOption()
                && obj.hasTopOption() == this.hasTopOption()
                && obj.hasFilterFilter() == this.hasFilterFilter()
                && Objects.equals(obj.getCountOption(), this.getCountOption())
                && Objects.equals(obj.getOrderByFilter(), this.getOrderByFilter())
                && Objects.equals(obj.getSelectFilter(), this.getSelectFilter())
                && Objects.equals(obj.getExpandFilter(), this.getExpandFilter())
                && Objects.equals(obj.getSkipOption(), this.getSkipOption())
                && Objects.equals(obj.getTopOption(), this.getTopOption())
                && Objects.equals(obj.getFilterFilter(), this.getFilterFilter());
    }
}
