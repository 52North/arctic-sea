package org.n52.svalbard.odata.expr.arithmetic;

import org.n52.svalbard.odata.expr.Expr;
import org.n52.svalbard.odata.expr.binary.BooleanExpr;

import java.util.Optional;

/**
 * Interface to denote that Expression can possibly be used in arithmetic operations.
 * Does not guarantee that the result will be semantically correct!
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface ArithmeticExpr extends Expr {

    @Override
    default boolean isArithmetic() {
        return true;
    }

    @Override
    default Optional<ArithmeticExpr> asArithmetic() {
        return Optional.of(this);
    }

}
