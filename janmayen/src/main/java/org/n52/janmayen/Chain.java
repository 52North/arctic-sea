/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Chain<T> implements Iterable<T> {

    private final List<T> chain;

    public Chain(T t) {
        this(Arrays.asList(t));

    }

    public Chain(List<T> chain) {
        chain.forEach(Objects::requireNonNull);
        this.chain = Collections.unmodifiableList(chain);
    }

    public T first() {
        return chain.get(0);
    }

    public T last() {
        return this.chain.get(this.chain.size() - 1);
    }

    public Optional<Chain<T>> tail() {
        if (this.chain.size() == 1) {
            return Optional.empty();
        } else {
            return Optional.of(new Chain<>(this.chain.subList(1, this.chain.size())));
        }
    }

    public Chain<T> child(T t) {
        Objects.requireNonNull(t);
        return new Chain<>(Stream.concat(stream(), Stream.of(t)).collect(toList()));
    }

    public Chain<T> child(Chain<T> t) {
        return new Chain<>(Stream.concat(stream(), t.stream()).collect(toList()));
    }

    public Optional<Chain<T>> parent() {
        if (this.chain.size() == 1) {
            return Optional.empty();
        }
        return Optional.of(new Chain<>(this.chain.subList(0, this.chain.size() - 1)));
    }

    @Override
    public Iterator<T> iterator() {
        return this.chain.iterator();
    }

    public Stream<T> stream() {
        return this.chain.stream();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.chain);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Chain<?> other = (Chain<?>) obj;
        return Objects.equals(this.chain, other.chain);
    }

    @Override
    public String toString() {
        return this.chain.stream().map(T::toString).collect(Collectors.joining("/"));
    }

}
