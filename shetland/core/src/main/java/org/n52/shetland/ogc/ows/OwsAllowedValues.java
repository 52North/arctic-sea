/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.ows;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsAllowedValues implements OwsPossibleValues, Iterable<OwsValueRestriction> {

    private final SortedSet<OwsValueRestriction> restrictions = new TreeSet<>();

    public OwsAllowedValues(OwsValueRestriction... restrictions) {
        this(Arrays.asList(restrictions));
    }

    public OwsAllowedValues(Iterable<? extends OwsValueRestriction> restrictions) {
        if (restrictions != null) {
            for (OwsValueRestriction restriction : restrictions) {
                this.restrictions.add(Objects.requireNonNull(restriction));
            }
        }
    }

    public OwsAllowedValues(Stream<? extends OwsValueRestriction> restrictions) {
        if (restrictions != null) {
            restrictions.forEach(x -> this.restrictions.add(Objects.requireNonNull(x)));
        }
    }

    public OwsAllowedValues() {
    }

    public OwsAllowedValues setRestrictions(OwsValueRestriction... restrictions) {
        return setRestrictions(Arrays.stream(restrictions));
    }

    public OwsAllowedValues setRestrictions(Iterable<? extends OwsValueRestriction> restrictions) {
        return setRestrictions(StreamSupport.stream(restrictions.spliterator(), false));
    }

    public OwsAllowedValues setRestrictions(Stream<? extends OwsValueRestriction> restrictions) {
        if (restrictions != null) {
            this.restrictions.clear();
            restrictions.map(Objects::requireNonNull)
                    .forEach(this.restrictions::add);
        }
        return this;
    }

    @Override
    public Iterator<OwsValueRestriction> iterator() {
        return this.restrictions.iterator();
    }

    public Stream<OwsValueRestriction> stream() {
        return this.restrictions.stream();
    }

    @Override
    public boolean isAllowedValues() {
        return true;
    }

    @Override
    public OwsAllowedValues asAllowedValues() {
        return this;
    }

    public SortedSet<OwsValueRestriction> getRestrictions() {
        return Collections.unmodifiableSortedSet(restrictions);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.restrictions);
        return hash;
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
        final OwsAllowedValues other = (OwsAllowedValues) obj;
        if (!Objects.equals(this.restrictions, other.restrictions)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsAllowedValues{" + "restrictions=" + restrictions + '}';
    }

}
