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
package org.n52.shetland.ogc.wps;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import org.n52.shetland.util.CollectionHelper;


/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ProcessOfferings implements Iterable<ProcessOffering> {

    private final SortedSet<ProcessOffering> offerings;

    public ProcessOfferings(Set<ProcessOffering> offerings) {
        this.offerings = CollectionHelper.newSortedSet(offerings);
    }

    @Override
    public Iterator<ProcessOffering> iterator() {
        return this.offerings.iterator();
    }

    public SortedSet<ProcessOffering> getOfferings() {
        return Collections.unmodifiableSortedSet(offerings);
    }

}
