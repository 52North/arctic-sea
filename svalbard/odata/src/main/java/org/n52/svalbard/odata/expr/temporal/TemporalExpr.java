package org.n52.svalbard.odata.expr.temporal;

import org.n52.svalbard.odata.expr.Expr;

import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface TemporalExpr extends Expr {

    @Override
    default boolean isTime() {
        return true;
    }

    @Override
    default Optional<TemporalExpr> asTime() {
        return Optional.of(this);
    }
}
