/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Predicate;

import org.n52.janmayen.Comparables;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsHttp
        implements OwsDCP {

    private final SortedSet<OwsRequestMethod> requestMethods;

    public OwsHttp(Collection<OwsRequestMethod> requestMethods) {
        this.requestMethods =
                Optional.ofNullable(requestMethods).map(TreeSet<OwsRequestMethod>::new).orElseGet(TreeSet::new);
    }

    public SortedSet<OwsRequestMethod> getRequestMethods() {
        return Collections.unmodifiableSortedSet(requestMethods);
    }

    public void removeRequestMethodIf(Predicate<? super OwsRequestMethod> condition) {
        this.requestMethods.removeIf(condition);
    }

    @Override
    public boolean isHTTP() {
        return true;
    }

    @Override
    public OwsHttp asHTTP() {
        return this;
    }

    @Override
    public int compareTo(OwsDCP o) {
        if (!o.isHTTP()) {
            return Comparables.LESS;
        } else {
            return Comparables.EQUAL;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.requestMethods);
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
        final OwsHttp other = (OwsHttp) obj;
        if (!Objects.equals(this.requestMethods, other.requestMethods)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsHttp{" + "requestMethods=" + requestMethods + '}';
    }

}
