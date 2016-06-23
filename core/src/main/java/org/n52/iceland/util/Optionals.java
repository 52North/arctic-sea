/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Optionals {

    private Optionals() {
    }

    public static <U> Comparator<Optional<U>> nullsLast(Comparator<U> comparator) {
        return Comparator.comparing(Optionals::orElseNull, Comparator.nullsLast(comparator));
    }

    public static <U extends Comparable<U>> Comparator<Optional<U>> nullsLast() {
        return nullsLast(Comparator.<U>naturalOrder());
    }

    public static <U> Comparator<Optional<U>> nullsFirst(Comparator<U> comparator) {
        return Comparator.comparing(Optionals::orElseNull, Comparator.nullsFirst(comparator));
    }

    public static <U extends Comparable<U>> Comparator<Optional<U>> nullsFirst() {
        return nullsFirst(Comparator.<U>naturalOrder());
    }

    public static <U> U orElseNull(Optional<U> optional) {
        return optional.orElse(null);
    }

    public static <U> Optional<U> or(Optional<U> a, Optional<U> b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        if (a.isPresent()) {
            return a;
        } else if (b.isPresent()) {
            return b;
        } else {
            return Optional.empty();
        }
    }
}
