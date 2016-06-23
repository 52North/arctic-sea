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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class OwsUnNamedDomain {

    private final OwsPossibleValues possibleValues;
    private final Optional<OwsValue> defaultValue;
    private final Optional<OwsDomainMetadata> meaning;
    private final Optional<OwsDomainMetadata> dataType;
    private final Optional<OwsValuesUnit> valuesUnit;
    private final List<OwsMetadata> metadata;

    public OwsUnNamedDomain(OwsPossibleValues possibleValues,
                         OwsValue defaultValue,
                         OwsDomainMetadata meaning,
                         OwsDomainMetadata dataType,
                         OwsValuesUnit valuesUnit,
                         List<OwsMetadata> metadata) {
        this.possibleValues = Objects.requireNonNull(possibleValues, "possibleValues");
        this.defaultValue = Optional.ofNullable(defaultValue);
        this.meaning = Optional.ofNullable(meaning);
        this.dataType = Optional.ofNullable(dataType);
        this.valuesUnit = Optional.ofNullable(valuesUnit);
        this.metadata = metadata == null ? Collections.emptyList() : metadata;
    }

    public OwsPossibleValues getPossibleValues() {
        return this.possibleValues;
    }

    public Optional<OwsValue> getDefaultValue() {
        return this.defaultValue;
    }

    public Optional<OwsDomainMetadata> getMeaning() {
        return this.meaning;
    }

    public Optional<OwsDomainMetadata> getDataType() {
        return this.dataType;
    }

    public Optional<OwsValuesUnit> getValuesUnit() {
        return this.valuesUnit;
    }

    public List<OwsMetadata> getMetadata() {
        return Collections.unmodifiableList(this.metadata);
    }
}
