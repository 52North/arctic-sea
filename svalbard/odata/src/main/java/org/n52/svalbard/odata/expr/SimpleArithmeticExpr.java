package org.n52.svalbard.odata.expr;

import org.n52.shetland.ogc.filter.FilterConstants.SimpleArithmeticOperator;

import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class SimpleArithmeticExpr extends BinaryExpr<SimpleArithmeticOperator> implements ArithmeticExpr{

    /**
     * Create a new {@code BinaryExpr}.
     *
     * @param operator the operator
     * @param left     the left operand
     * @param right    the right operand
     */
    public SimpleArithmeticExpr(SimpleArithmeticOperator operator, ArithmeticExpr left, ArithmeticExpr right) {
        super(operator, left, right);
    }

    /**
     * Check if this expression is a arithmetic expresion.
     *
     * @return if it is a arithmetic expression
     */
    @Override public boolean isArithmetic() {
        return true;
    }

    /**
     * Get this expression as a arithmetic expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    @Override public Optional<SimpleArithmeticExpr> asArithmetic() {
        return Optional.of(this);
    }

    /**
     * Accepts {@code visitor} for this expression.
     *
     * @param visitor the visitor
     * @return the result of the visit
     * @throws X if the visitor fails to visit this expression
     */
    @Override public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitSimpleArithmetic(this);
    }
}
