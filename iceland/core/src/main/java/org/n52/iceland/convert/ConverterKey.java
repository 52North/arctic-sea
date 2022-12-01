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
package org.n52.iceland.convert;

import java.util.Comparator;
import java.util.Objects;

/**
 * Key class for {@link Converter}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
public class ConverterKey implements Comparable<ConverterKey> {
    private static final Comparator<ConverterKey> COMPARATOR
            = Comparator.nullsFirst(Comparator.comparing(ConverterKey::getFromNamespace,
                                                         Comparator.nullsFirst(String::compareTo))
                    .thenComparing(Comparator.comparing(ConverterKey::getToNamespace,
                                                        Comparator.nullsFirst(String::compareTo))));

    private final String fromNamespace;
    private final String toNamespace;

    /**
     * Constructor
     *
     * @param fromNamespace The source namespace
     * @param toNamespace   The target namespace
     */
    public ConverterKey(String fromNamespace, String toNamespace) {
        this.fromNamespace = fromNamespace;
        this.toNamespace = toNamespace;
    }

    /**
     * @return the source namespace
     */
    public String getFromNamespace() {
        return fromNamespace;
    }

    /**
     * @return the target namespace
     */
    public String getToNamespace() {
        return toNamespace;
    }

    @Override
    public int compareTo(ConverterKey o) {
        return compare(this, o);
    }

    @Override
    public boolean equals(Object paramObject) {
        if (paramObject instanceof ConverterKey) {
            ConverterKey toCheck = (ConverterKey) paramObject;
            return Objects.equals(getFromNamespace(), toCheck.getFromNamespace()) &&
                   Objects.equals(getToNamespace(), toCheck.getToNamespace());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromNamespace(), getToNamespace());
    }

    @Override
    public String toString() {
        return String.format("%s[from=%s, to=%s]", getClass().getSimpleName(), fromNamespace, toNamespace);
    }

    public static int compare(ConverterKey o1, ConverterKey o2) {
        return COMPARATOR.compare(o1, o2);
    }

}
