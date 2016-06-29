package org.n52.iceland.util.function;

import java.util.function.Function;

/**
 *
 * @author Christian Autermann
 */

/**
 * Represents an operation on a single operand that produces a result of the
 * same type as its operand.  This is a specialization of {@code Function} for
 * the case where the operand and result are of the same type.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the operand and result of the operator
 * @param <X> the type of the exception that might be thrown
 *
 * @see Function
 * @since 1.8
 */
@FunctionalInterface
public interface ThrowingUnaryOperator<T, X extends Throwable> extends ThrowingFunction<T, T, X> {

    /**
     * Returns a unary operator that always returns its input argument.
     *
     * @param <T> the type of the input and output of the operator
     * @param <X> the type of the exception that might be thrown
     * @return a unary operator that always returns its input argument
     */
    static <T, X extends Throwable> ThrowingUnaryOperator<T, X> identity() {
        return t -> t;
    }
}
