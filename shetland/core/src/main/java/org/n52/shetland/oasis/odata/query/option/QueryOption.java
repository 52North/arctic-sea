package org.n52.shetland.oasis.odata.query.option;

public abstract class QueryOption<T> {

    public abstract String getName();

    public abstract T getValue();
}
