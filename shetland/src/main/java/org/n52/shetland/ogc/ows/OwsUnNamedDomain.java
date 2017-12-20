/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.util.CollectionHelper;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class OwsUnNamedDomain {

    private OwsPossibleValues possibleValues;
    private Optional<OwsValue> defaultValue;
    private Optional<OwsDomainMetadata> meaning;
    private Optional<OwsDomainMetadata> dataType;
    private Optional<OwsValuesUnit> valuesUnit;
    private SortedSet<OwsMetadata> metadata;

    public OwsUnNamedDomain(OwsPossibleValues possibleValues,
                         OwsValue defaultValue,
                         OwsDomainMetadata meaning,
                         OwsDomainMetadata dataType,
                         OwsValuesUnit valuesUnit,
                         Collection<OwsMetadata> metadata) {
        this.possibleValues = Objects.requireNonNull(possibleValues, "possibleValues");
        this.defaultValue = Optional.ofNullable(defaultValue);
        this.meaning = Optional.ofNullable(meaning);
        this.dataType = Optional.ofNullable(dataType);
        this.valuesUnit = Optional.ofNullable(valuesUnit);
        this.metadata = CollectionHelper.newSortedSet(metadata);
    }

    public OwsPossibleValues getPossibleValues() {
        return this.possibleValues;
    }

    public void setPossibleValues(OwsPossibleValues possibleValues) {
        this.possibleValues = Objects.requireNonNull(possibleValues);
    }

    public Optional<OwsValue> getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(OwsValue defaultValue) {
        this.defaultValue = Optional.ofNullable(defaultValue);
    }

    public Optional<OwsDomainMetadata> getMeaning() {
        return this.meaning;
    }

    public void setMeaning(OwsDomainMetadata meaning) {
        this.meaning = Optional.ofNullable(meaning);
    }

    public Optional<OwsDomainMetadata> getDataType() {
        return this.dataType;
    }

    public void setDataType(OwsDomainMetadata dataType) {
        this.dataType = Optional.ofNullable(dataType);
    }

    public Optional<OwsValuesUnit> getValuesUnit() {
        return this.valuesUnit;
    }

    public void setValuesUnit(OwsValuesUnit valuesUnit) {
        this.valuesUnit = Optional.ofNullable(valuesUnit);
    }

    public SortedSet<OwsMetadata> getMetadata() {
        return Collections.unmodifiableSortedSet(this.metadata);
    }

    public void setMetadata(Collection<OwsMetadata> metadata) {
        this.metadata = CollectionHelper.newSortedSet(metadata);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.possibleValues);
        hash = 61 * hash + Objects.hashCode(this.defaultValue);
        hash = 61 * hash + Objects.hashCode(this.meaning);
        hash = 61 * hash + Objects.hashCode(this.dataType);
        hash = 61 * hash + Objects.hashCode(this.valuesUnit);
        hash = 61 * hash + Objects.hashCode(this.metadata);
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
        final OwsUnNamedDomain other = (OwsUnNamedDomain) obj;
        if (!Objects.equals(this.possibleValues, other.possibleValues)) {
            return false;
        }
        if (!Objects.equals(this.defaultValue, other.defaultValue)) {
            return false;
        }
        if (!Objects.equals(this.meaning, other.meaning)) {
            return false;
        }
        if (!Objects.equals(this.dataType, other.dataType)) {
            return false;
        }
        if (!Objects.equals(this.valuesUnit, other.valuesUnit)) {
            return false;
        }
        if (!Objects.equals(this.metadata, other.metadata)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsUnNamedDomain{" + "possibleValues=" + possibleValues +
               ", defaultValue=" + defaultValue + ", meaning=" + meaning +
               ", dataType=" + dataType + ", valuesUnit=" + valuesUnit +
               ", metadata=" + metadata + '}';
    }

}
