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

/**
 * Represents a supplier of results.
 *
 * There is no requirement that a new or distinct result be returned each time the supplier is invoked.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <X> the type of the exception that might be thrown
 */
@FunctionalInterface
public interface ThrowingSupplier<T, X extends Exception> {

    /**
     * Gets a result.
     *
     * @return a result
     *
     * @throws X if the operation fails
     */
    T get() throws X;
}
