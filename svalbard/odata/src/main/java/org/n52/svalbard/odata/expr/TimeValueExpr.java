package org.n52.svalbard.odata.expr;

import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class TimeValueExpr implements Expr {

    private final Object value;

    /**
     * Creates a new {@code ValueExpr}.
     *
     * @param value the value
     */
    public TimeValueExpr(Object value) {
        //TODO: parse into valid java date class
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getTime() {
        return this.value;
    }

    @Override
    public boolean isTime() {
        return true;
    }

    @Override
    public Optional<TimeValueExpr> asTime() {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        //TODO: format correctly
        return value.toString();
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitTime(this);
    }


}
