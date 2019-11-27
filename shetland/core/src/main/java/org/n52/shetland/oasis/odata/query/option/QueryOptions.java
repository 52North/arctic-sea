package org.n52.shetland.oasis.odata.query.option;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.n52.shetland.oasis.odata.ODataConstants;

/**
 * Abstract Interface to hold Query Parameters
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class QueryOptions {

    int DEFAULT_TOP = 100;

    private String baseURL;

    private Set<QueryOption> queryOptions = new LinkedHashSet<>();

    public QueryOptions(String baseURL2) {

    }

    public QueryOptions(String baseURL, Set<QueryOption> queryOptions) {
        this.baseURL = baseURL;
        if (queryOptions != null) {
            this.queryOptions.addAll(queryOptions);
        }
    }

    public String getBaseURI() {
        return baseURL;
    }

    public boolean hasCountOption() {
        return has(ODataConstants.QueryOptions.COUNT);
    }

    public CountOption getCountOption() {
        return hasCountOption() ? (CountOption) find(ODataConstants.QueryOptions.COUNT).get() : null;
    }

    public boolean hasTopOption() {
        return has(ODataConstants.QueryOptions.TOP);
    }

    public TopOption getTopOption() {
        return hasTopOption() ? (TopOption) find(ODataConstants.QueryOptions.TOP).get() : null;
    }

    public boolean hasSkipOption() {
        return has(ODataConstants.QueryOptions.SKIP);
    }


    public SkipOption getSkipOption() {
        return hasSkipOption() ? (SkipOption) find(ODataConstants.QueryOptions.SKIP).get() : null;
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


    public FilterOption getFilterOption() {
        return hasFilterOption() ? (FilterOption) find(ODataConstants.QueryOptions.FILTER).get() : null;
    }

    private Optional<QueryOption> find(String name) {
        return find(new QueryOptionPredicate(name));
    }

    private Optional<QueryOption> find(Predicate<QueryOption> predicate) {
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

    private final class QueryOptionPredicate implements Predicate<QueryOption> {

        private final String name;

        QueryOptionPredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean test(QueryOption input) {
            return input.getName().equalsIgnoreCase(name);
        }
    }
}
