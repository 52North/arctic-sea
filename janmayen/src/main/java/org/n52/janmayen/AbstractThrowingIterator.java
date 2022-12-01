/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import java.util.NoSuchElementException;

import com.google.common.collect.AbstractIterator;

/**
 * Copy of {@link AbstractIterator} for {@link ThrowingIterator}.
 *
 * @param <T> the element type
 * @param <X> the exception type
 *
 * @author Christian Autermann
 */
public abstract class AbstractThrowingIterator<T, X extends Exception> implements ThrowingIterator<T, X> {
    private State state = State.NOT_READY;
    private Throwable failureReason;
    private T next;

    protected abstract T computeNext() throws X;

    protected T endOfData() {
        state = State.DONE;
        return null;
    }

    @Override
    public T next() throws X {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        state = State.NOT_READY;
        T result = next;
        next = null;
        return result;
    }

    @Override
    public boolean hasNext() throws X {
        switch (state) {
            case DONE:
                return false;
            case READY:
                return true;
            case FAILED:
                return rethrowException();
            case NOT_READY:
                return tryComputeNext();
            default:
                throw new Error();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean rethrowException() throws X {
        if (failureReason instanceof Error) {
            throw (Error) failureReason;
        } else if (failureReason instanceof RuntimeException) {
            throw (RuntimeException) failureReason;
        } else {
            throw (X) failureReason;
        }
    }

    private boolean tryComputeNext() throws X {
        try {
            next = computeNext();
            if (state == State.DONE) {
                return false;
            } else {
                state = State.READY;
                return true;
            }
        } catch (Error | Exception x) {
            state = State.FAILED;
            failureReason = x;
            throw x;
        }
    }

    private enum State {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

}
