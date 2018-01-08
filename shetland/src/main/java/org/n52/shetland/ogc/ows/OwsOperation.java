/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
import java.util.SortedSet;

import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsOperation implements Comparable<OwsOperation> {
    private String name;
    private SortedSet<OwsDomain> parameters;
    private SortedSet<OwsDomain> constraints;
    private SortedSet<OwsMetadata> metadata;
    private SortedSet<OwsDCP> dcp;

    public OwsOperation(String name,
                        Collection<OwsDomain> parameters,
                        Collection<OwsDomain> constraints,
                        Collection<OwsMetadata> metadata,
                        Collection<OwsDCP> dcp) {
        this.name = Objects.requireNonNull(Strings.emptyToNull(name));
        this.parameters = CollectionHelper.newSortedSet(parameters);
        this.constraints = CollectionHelper.newSortedSet(constraints);
        this.metadata = CollectionHelper.newSortedSet(metadata);
        this.dcp = CollectionHelper.newSortedSet(dcp);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(Strings.emptyToNull(name));
    }

    public SortedSet<OwsDomain> getParameters() {
        return Collections.unmodifiableSortedSet(this.parameters);
    }

    public void setParameters(Collection<OwsDomain> parameters) {
        this.parameters = CollectionHelper.newSortedSet(parameters);
    }

    public void addParameter(OwsDomain parameter) {
        this.parameters.add(Objects.requireNonNull(parameter));
    }

    public SortedSet<OwsDomain> getConstraints() {
        return Collections.unmodifiableSortedSet(this.constraints);
    }

    public void setConstraints(Collection<OwsDomain> constraints) {
        this.constraints = CollectionHelper.newSortedSet(constraints);
    }

    public void addConstraint(OwsDomain constraint) {
        this.constraints.add(Objects.requireNonNull(constraint));
    }

    public SortedSet<OwsMetadata> getMetadata() {
        return Collections.unmodifiableSortedSet(this.metadata);
    }

    public void setMetadata(Collection<OwsMetadata> metadata) {
        this.metadata = CollectionHelper.newSortedSet(metadata);
    }

    public void addMetadata(OwsMetadata constraint) {
        this.metadata.add(Objects.requireNonNull(constraint));
    }

    public SortedSet<OwsDCP> getDCP() {
        return Collections.unmodifiableSortedSet(this.dcp);
    }

    public void setDCP(Collection<OwsDCP> dcp) {
        this.dcp = CollectionHelper.newSortedSet(dcp);
    }

    public void addDCP(OwsDCP dcp) {
        this.dcp.add(Objects.requireNonNull(dcp));
    }

    @Override
    public int compareTo(OwsOperation o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + Objects.hashCode(this.parameters);
        hash = 83 * hash + Objects.hashCode(this.constraints);
        hash = 83 * hash + Objects.hashCode(this.metadata);
        hash = 83 * hash + Objects.hashCode(this.dcp);
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
        final OwsOperation other = (OwsOperation) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.parameters, other.parameters)) {
            return false;
        }
        if (!Objects.equals(this.constraints, other.constraints)) {
            return false;
        }
        if (!Objects.equals(this.metadata, other.metadata)) {
            return false;
        }
        if (!Objects.equals(this.dcp, other.dcp)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsOperation{" + "name=" + name + ", parameters=" + parameters +
               ", constraints=" + constraints + ", metadata=" + metadata +
               ", dcp=" + dcp + '}';
    }

}
