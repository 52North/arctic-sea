package org.n52.shetland.oasis.odata.query.option;

import org.n52.shetland.oasis.odata.ODataConstants;

public class CountOption extends QueryOption<Boolean> {

    private Boolean value;

    public CountOption(String value) {
        this.value = Boolean.valueOf(value);
    }

    @Override
    public String getName() {
        return ODataConstants.QueryOptions.COUNT;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

}
