/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @deprecated use {@link Collections#emptyIterator() }
 * @since 1.0.0
 */
@Deprecated
@SuppressWarnings(value = "unchecked")
public class EmptyIterator<T> implements Iterator<T> {
    private static final EmptyIterator<?> INSTANCE = new EmptyIterator<Object>();

    /**
     *
     * @param <T> the iterator's element type
     * @return the instance
     * @deprecated use {@link Collections#emptyIterator() }
     */
    @Deprecated
    @SuppressWarnings(value = "unchecked")
    public static <T> Iterator<T> instance() {
        return (Iterator<T>) INSTANCE;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public T next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
