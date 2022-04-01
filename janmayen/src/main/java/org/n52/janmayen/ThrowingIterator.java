/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.janmayen;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.annotation.CheckReturnValue;

import org.n52.janmayen.function.ThrowingConsumer;

/**
 * A throwing variant of {@link Iterator}.
 *
 * @author Christian Autermann
 * @param <T> the element's type
 * @param <X> the type of the exception that may be thrown
 *
 * @see Iterator
 */
public interface ThrowingIterator<T, X extends Exception> {

    /**
     * Performs the given action for each remaining element until all elements have been processed or the action throws
     * an exception. Actions are performed in the order of iteration, if that order is specified. Exceptions thrown by
     * the action are relayed to the caller.
     *
     * @param action The action to be performed for each element
     *
     * @throws NullPointerException if the specified action is null
     * @throws X                    if iteration fails
     * @see Iterator#forEachRemaining(java.util.function.Consumer)
     */
    default void forEachRemaining(ThrowingConsumer<? super T, ? extends X> action) throws X {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     *
     * @throws NoSuchElementException if the iteration has no more elements
     * @throws X                      if the next element could not be acquired
     * @see Iterator#next()
     */
    @CheckReturnValue
    T next() throws NoSuchElementException, X;

    /**
     * Returns {@code true} if the iteration has more elements. (In other words, returns {@code true} if {@link #next}
     * would return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     *
     * @throws X if the check for the next value fails
     * @see Iterator#hasNext()
     */
    @CheckReturnValue
    boolean hasNext() throws X;

    /**
     * Removes from the underlying collection the last element returned by this iterator (optional operation). This
     * method can be called only once per call to {@link #next}. The behavior of an iterator is unspecified if the
     * underlying collection is modified while the iteration is in progress in any way other than by calling this
     * method.
     *
     * The default implementation throws an instance of {@link UnsupportedOperationException} and performs no other
     * action.
     *
     * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this iterator
     *
     * @throws IllegalStateException         if the {@code next} method has not yet been called, or the {@code remove}
     *                                       method has already been called after the last call to the {@code next}
     *                                       method
     * @throws X                             if the remove operations fails
     *
     * @see Iterator#remove()
     */
    default void remove() throws X {
        throw new UnsupportedOperationException("remove");
    }

}
