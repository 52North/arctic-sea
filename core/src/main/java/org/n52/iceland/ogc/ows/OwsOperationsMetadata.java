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

import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;

import org.n52.iceland.util.CollectionHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsOperationsMetadata {

    private final SortedSet<OwsOperation> operations;
    private final SortedSet<OwsDomain> parameters;
    private final SortedSet<OwsDomain> constraints;

    public OwsOperationsMetadata(Collection<OwsOperation> operations,
                                 Collection<OwsDomain> parameters,
                                 Collection<OwsDomain> constraints) {
        this.operations = CollectionHelper.newSortedSet(operations);
        this.parameters = CollectionHelper.newSortedSet(parameters);
        this.constraints = CollectionHelper.newSortedSet(constraints);
    }

    public SortedSet<OwsOperation> getOperations() {
        return Collections.unmodifiableSortedSet(operations);
    }

    public SortedSet<OwsDomain> getParameters() {
        return Collections.unmodifiableSortedSet(parameters);
    }

    public SortedSet<OwsDomain> getConstraints() {
        return Collections.unmodifiableSortedSet(constraints);
    }
}
