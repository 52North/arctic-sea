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

import java.util.List;
import java.util.Objects;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsDomain extends OwsUnNamedDomain implements Comparable<OwsDomain>{
    private final String name;

    public OwsDomain(String name,
                         OwsPossibleValues possibleValues,
                         OwsValue defaultValue,
                         OwsDomainMetadata meaning,
                         OwsDomainMetadata dataType,
                         OwsValuesUnit valuesUnit,
                         List<OwsMetadata> metadata) {
        super(possibleValues, defaultValue, meaning, dataType, valuesUnit, metadata);
        this.name = Objects.requireNonNull(Strings.emptyToNull(name), "name");
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(OwsDomain o) {
        return getName().compareTo(o.getName());
    }

}
