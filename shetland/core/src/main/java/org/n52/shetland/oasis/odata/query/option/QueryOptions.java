/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.n52.shetland.filter.CountFilter;
import org.n52.shetland.filter.SkipTopFilter;
import org.n52.shetland.oasis.odata.ODataConstants;
import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;

/**
 * Class to hold Query Parameters
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptions {

    private static Long DEFAULT_TOP = 100L;

    private String baseURL;

    private Set<FilterClause> queryOptions = new LinkedHashSet<>();

    public QueryOptions(String baseURL) {
        this.baseURL = baseURL;
        this.queryOptions.add(new SkipTopFilter(SkipTopOperator.Top, DEFAULT_TOP));
    }

    public QueryOptions(String baseURL, Set<FilterClause> queryOptions) {
        this.baseURL = baseURL;
        if (queryOptions != null) {
            this.queryOptions.addAll(queryOptions);
            if (!hasTopOption()) {
                this.queryOptions.add(new SkipTopFilter(SkipTopOperator.Top, DEFAULT_TOP));
            }
        }
    }

    public String getBaseURI() {
        return baseURL;
    }

    public boolean hasCountOption() {
        return has(ODataConstants.QueryOptions.COUNT);
    }

    public CountFilter getCountOption() {
        return hasCountOption() ? (CountFilter) find(ODataConstants.QueryOptions.COUNT).get() : null;
    }

    public boolean hasTopOption() {
        return has(ODataConstants.QueryOptions.TOP);
    }

    public SkipTopFilter getTopOption() {
        return hasTopOption() ? (SkipTopFilter) find(ODataConstants.QueryOptions.TOP).get() : null;
    }

    public boolean hasSkipOption() {
        return has(ODataConstants.QueryOptions.SKIP);
    }

    public SkipTopFilter getSkipOption() {
        return hasSkipOption() ? (SkipTopFilter) find(ODataConstants.QueryOptions.SKIP).get() : null;
    }

    public boolean hasOrderByOption() {
        return has(ODataConstants.QueryOptions.ORDERBY);
    }

    public OrderByOption getOrderByOption() {
        return hasOrderByOption() ? (OrderByOption) find(ODataConstants.QueryOptions.ORDERBY).get() : null;
    }

    public boolean hasSelectOption() {
        return has(ODataConstants.QueryOptions.SELECT);
    }

    public SelectOption getSelectOption() {
        return hasSelectOption() ? (SelectOption) find(ODataConstants.QueryOptions.SELECT).get() : null;
    }

    public boolean hasExpandOption() {
        return has(ODataConstants.QueryOptions.EXPAND);
    }

    public ExpandOption getExpandOption() {
        return hasExpandOption() ? (ExpandOption) find(ODataConstants.QueryOptions.EXPAND).get() : null;
    }

    public boolean hasFilterOption() {
        return has(ODataConstants.QueryOptions.FILTER);
    }

    public Filter<?> getFilterOption() {
        return hasFilterOption() ? (Filter<?>) find(ODataConstants.QueryOptions.FILTER).get() : null;
    }

    private Optional<FilterClause> find(String name) {
        return find(new QueryOptionPredicate(name));
    }

    private Optional<FilterClause> find(Predicate<FilterClause> predicate) {
        if (hasQueryOptions()) {
            return queryOptions.stream().filter(predicate).findFirst();
        }
        return Optional.empty();
    }

    private boolean has(String name) {
        return find(name).isPresent();
    }

    private boolean hasQueryOptions() {
        return queryOptions != null && !queryOptions.isEmpty();
    }

    private static final class QueryOptionPredicate implements Predicate<FilterClause> {

        private final String name;

        QueryOptionPredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean test(FilterClause input) {
            if (ODataConstants.QueryOptions.COUNT.equalsIgnoreCase(name) && input instanceof CountOption) {
                return true;
            } else if (ODataConstants.QueryOptions.ORDERBY.equalsIgnoreCase(name) && input instanceof OrderByOption) {
                return true;
            } else if (ODataConstants.QueryOptions.SELECT.equalsIgnoreCase(name) && input instanceof SelectOption) {
                return true;
            } else if (ODataConstants.QueryOptions.EXPAND.equalsIgnoreCase(name) && input instanceof ExpandOption) {
                return true;
            } else if (ODataConstants.QueryOptions.SKIP.equalsIgnoreCase(name) && input instanceof SkipTopFilter) {
                return ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Skip);
            } else if (ODataConstants.QueryOptions.TOP.equalsIgnoreCase(name) && input instanceof SkipTopFilter) {
                return ((SkipTopFilter) input).getOperator().equals(FilterConstants.SkipTopOperator.Top);
            } else if (ODataConstants.QueryOptions.FILTER.equalsIgnoreCase(name) && input instanceof Filter) {
                return true;
            }
            return false;
        }
    }
}
