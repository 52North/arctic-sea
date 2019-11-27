package org.n52.shetland.oasis.odata.query.option;

import org.n52.shetland.oasis.odata.ODataConstants;

public class FilterOption extends QueryOption {

    @Override
    public String getName() {
        return ODataConstants.QueryOptions.FILTER;
    }

}
