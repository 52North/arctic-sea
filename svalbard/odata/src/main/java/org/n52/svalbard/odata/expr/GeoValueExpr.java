package org.n52.svalbard.odata.expr;

import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class GeoValueExpr implements Expr {

    // Geometry/Geography is stored as WKT
    private final String value;

    /**
     * Creates a new {@code ValueExpr}.
     *
     * @param value the value
     */
    public GeoValueExpr(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getGeometry() {
        return this.value;
    }

    @Override
    public boolean isGeometry() {
        return true;
    }

    @Override
    public Optional<GeoValueExpr> asGeometry() {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return String.format("'%s'", this.value);
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitGeometry(this);
    }
}