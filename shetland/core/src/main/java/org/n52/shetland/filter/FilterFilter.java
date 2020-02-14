package org.n52.shetland.filter;

import org.n52.shetland.oasis.odata.query.option.FilterOption;
import org.n52.shetland.ogc.filter.FilterClause;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class FilterFilter implements FilterOption, FilterClause {

    private Object filter;

    public FilterFilter(Object filter) {
        this.filter = filter;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return filter;
    }
}
