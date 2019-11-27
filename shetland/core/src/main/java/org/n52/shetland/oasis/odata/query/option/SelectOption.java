package org.n52.shetland.oasis.odata.query.option;

import org.n52.shetland.oasis.odata.ODataConstants;

public class SelectOption extends QueryOption {

    @Override
    public String getName() {
        return ODataConstants.QueryOptions.SELECT;
    }

}
