/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.janmayen.function;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Throwables;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class ThrowableFunction<F, T> implements Function<F, T> {

    private List<Exception> errors = new LinkedList<>();

    @Override
    public T apply(F input) {
        try {
            return applyThrowable(input);
        } catch (Exception ex) {
            this.errors.add(ex);
            return null;
        }
    }

    public List<Exception> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    public Exception getFirstError() {
        return hasErrors() ? this.errors.iterator().next() : null;
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public <X extends Throwable> void propagateIfPossible(Class<X> declaredType)
            throws X {
        Throwables.propagateIfPossible(getFirstError(), declaredType);
    }

    protected abstract T applyThrowable(F input)
            throws Exception;

}
