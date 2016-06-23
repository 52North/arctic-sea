/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsAllowedValues implements OwsPossibleValues, Iterable<OwsValueRestriction> {
    private final Set<OwsValueRestriction> restrictions = new HashSet<>();

    public OwsAllowedValues(Iterable<? extends OwsValueRestriction> restrictions) {
        if (restrictions!= null) {
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

    public Set<OwsValueRestriction> getRestrictions() {
        return Collections.unmodifiableSet(restrictions);
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
