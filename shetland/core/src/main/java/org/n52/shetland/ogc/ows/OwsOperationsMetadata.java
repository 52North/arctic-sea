/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import org.n52.shetland.util.CollectionHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsOperationsMetadata {

    private SortedSet<OwsOperation> operations;
    private SortedSet<OwsDomain> parameters;
    private SortedSet<OwsDomain> constraints;
    private Optional<OwsOperationMetadataExtension> extension;

    public OwsOperationsMetadata(Collection<OwsOperation> operations,
                                 Collection<OwsDomain> parameters,
                                 Collection<OwsDomain> constraints,
                                 OwsOperationMetadataExtension extension) {
        this.operations = CollectionHelper.newSortedSet(operations);
        this.parameters = CollectionHelper.newSortedSet(parameters);
        this.constraints = CollectionHelper.newSortedSet(constraints);
        this.extension = Optional.ofNullable(extension);
    }

    public SortedSet<OwsOperation> getOperations() {
        return Collections.unmodifiableSortedSet(operations);
    }

    public void setOperations(Collection<OwsOperation> operations) {
        this.operations = CollectionHelper.newSortedSet(operations);
    }

    public SortedSet<OwsDomain> getParameters() {
        return Collections.unmodifiableSortedSet(parameters);
    }

    public void setParameters(Collection<OwsDomain> parameters) {
        this.parameters = CollectionHelper.newSortedSet(parameters);
    }

    public SortedSet<OwsDomain> getConstraints() {
        return Collections.unmodifiableSortedSet(constraints);
    }

    public void setConstraints(Collection<OwsDomain> constraints) {
        this.constraints = CollectionHelper.newSortedSet(constraints);
    }

    public Optional<OwsOperationMetadataExtension> getExtension() {
        return this.extension;
    }

    public void setExtension(OwsOperationMetadataExtension extension) {
        this.extension = Optional.ofNullable(extension);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.operations);
        hash = 47 * hash + Objects.hashCode(this.parameters);
        hash = 47 * hash + Objects.hashCode(this.constraints);
        hash = 47 * hash + Objects.hashCode(this.extension);
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
        final OwsOperationsMetadata other = (OwsOperationsMetadata) obj;
        if (!Objects.equals(this.operations, other.operations)) {
            return false;
        }
        if (!Objects.equals(this.parameters, other.parameters)) {
            return false;
        }
        if (!Objects.equals(this.constraints, other.constraints)) {
            return false;
        }
        if (!Objects.equals(this.extension, other.extension)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsOperationsMetadata{" + "operations=" + operations +
               ", parameters=" + parameters + ", constraints=" + constraints +
               '}';
    }
}
