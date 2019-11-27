package org.n52.shetland.oasis.odata.query.option;

import org.n52.shetland.oasis.odata.ODataConstants;

public class SkipOption extends QueryOption<Long> {

    private Long value;

    public SkipOption(String value) {
        this.value = Long.parseLong(value);
    }
    @Override
    public String getName() {
        return ODataConstants.QueryOptions.SKIP;
    }

    @Override
    public Long getValue() {
        return value;
    }

}
