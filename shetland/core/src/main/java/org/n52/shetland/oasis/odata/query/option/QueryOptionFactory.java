package org.n52.shetland.oasis.odata.query.option;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.n52.shetland.oasis.odata.ODataConstants;

import java.util.Set;

public class QueryOptionFactory {

    public QueryOptions createQueryOptions(Map<String, String> parameters, String baseURL) {
        if (parameters != null && !parameters.isEmpty()) {
            return new QueryOptions(baseURL, getOptions(parameters));
        }
        return new QueryOptions(baseURL);
    }

    private Set<QueryOption> getOptions(Map<String, String> parameters) {
        Set<QueryOption> options = new LinkedHashSet<>();
        for (Entry<String, String> entry : parameters.entrySet()) {
            switch (entry.getKey()) {
            case ODataConstants.QueryOptions.COUNT:
                options.add(new CountOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.EXPAND:
                options.add(new ExpandOption());
                break;
            case ODataConstants.QueryOptions.FILTER:
                options.add(new FilterOption());
                break;
            case ODataConstants.QueryOptions.ORDERBY:
                options.add(new OrderByOption());
                break;
            case ODataConstants.QueryOptions.SELECT:
                options.add(new SelectOption());
                break;
            case ODataConstants.QueryOptions.SKIP:
                options.add(new SkipOption(entry.getValue()));
                break;
            case ODataConstants.QueryOptions.TOP:
                options.add(new TopOption(entry.getValue()));
                break;
            default:
                break;
            }
        }
        return options;
    }
}
