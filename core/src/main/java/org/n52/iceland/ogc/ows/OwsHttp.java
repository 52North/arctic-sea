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
import java.util.Set;
import java.util.SortedSet;

import org.n52.iceland.util.CollectionHelper;
import org.n52.iceland.util.Comparables;


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsHttp implements OwsDCP{

    private final SortedSet<OwsRequestMethod> requestMethods;

    public OwsHttp(Set<OwsRequestMethod> requestMethods) {
        this.requestMethods
                = CollectionHelper.newSortedSet(requestMethods);
    }

    public SortedSet<OwsRequestMethod> getRequestMethods() {
        return Collections.unmodifiableSortedSet(requestMethods);
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

}
