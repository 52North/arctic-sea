/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Collection;
import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsDomain extends OwsUnNamedDomain implements Comparable<OwsDomain> {
    private String name;

    public OwsDomain(Enum<?> name, OwsPossibleValues possibleValues) {
        this(name.toString(), possibleValues, null, null, null, null, null);
    }

    public OwsDomain(String name, OwsPossibleValues possibleValues) {
        this(name, possibleValues, null, null, null, null, null);
    }

    public OwsDomain(Enum<?> name, OwsPossibleValues possibleValues, OwsValue defaultValue) {
        this(name.toString(), possibleValues, defaultValue, null, null, null, null);
    }

    public OwsDomain(Enum<?> name,
                     OwsPossibleValues possibleValues,
                     OwsValue defaultValue,
                     OwsDomainMetadata meaning,
                     OwsDomainMetadata dataType,
                     OwsValuesUnit valuesUnit,
                     Collection<OwsMetadata> metadata) {
        this(name.toString(), possibleValues, defaultValue, meaning, dataType, valuesUnit, metadata);
    }

    public OwsDomain(String name,
                     OwsPossibleValues possibleValues,
                     OwsValue defaultValue) {
        this(name, possibleValues, defaultValue, null, null, null, null);

    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public OwsDomain(String name,
                     OwsPossibleValues possibleValues,
                     OwsValue defaultValue,
                     OwsDomainMetadata meaning,
                     OwsDomainMetadata dataType,
                     OwsValuesUnit valuesUnit,
                     Collection<OwsMetadata> metadata) {
        super(possibleValues, defaultValue, meaning, dataType, valuesUnit, metadata);
        this.name = Objects.requireNonNull(Strings.emptyToNull(name), "name");
    }

    public String getName() {
        return this.name;
    }

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void setName(String name) {
        this.name = Objects.requireNonNull(Strings.emptyToNull(name));
    }

    public void setName(Enum<?> name) {
        setName(name.name());
    }

    @Override
    public int compareTo(OwsDomain o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 41 * hash + Objects.hashCode(this.name);
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
        final OwsDomain other = (OwsDomain) obj;
        return super.equals(obj) && Objects.equals(this.name, other.name);
    }

    @Override
    public String toString() {
        return "OwsDomain{" + "name=" + name + '}';
    }

}
