package org.n52.shetland.oasis.odata.query.option;

import org.n52.shetland.oasis.odata.ODataConstants;

public class TopOption extends QueryOption<Long> {

    private Long value;

    public TopOption(String value) {
        this.value = Long.parseLong(value);
    }
    @Override
    public String getName() {
        return ODataConstants.QueryOptions.TOP;
    }

    @Override
    public Long getValue() {
        return value;
    }
}
