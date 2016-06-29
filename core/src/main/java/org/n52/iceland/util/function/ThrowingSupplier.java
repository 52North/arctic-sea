package org.n52.iceland.util.function;

/**
 * Represents a supplier of results.
 *
 * <p>
 * There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <X> the type of the exception that might be thrown
 *
 */
@FunctionalInterface
public interface ThrowingSupplier<T, X extends Throwable> {

    /**
     * Gets a result.
     *
     * @return a result
     *
     * @throws X if the operation fails
     */
    T get() throws X;
}
