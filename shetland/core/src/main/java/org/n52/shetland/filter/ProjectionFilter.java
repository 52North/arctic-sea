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

package org.n52.shetland.filter;

import org.n52.shetland.oasis.odata.query.option.SelectOption;
import org.n52.shetland.ogc.filter.AbstractProjectionClause;

import java.util.Objects;
import java.util.Set;

public class ProjectionFilter implements AbstractProjectionClause, SelectOption {

    private final Set<String> value;

    public ProjectionFilter(Set<String> value) {
        this.value = value;
    }

    public Set<String> getValue() {
        return value;
    }

    @Override public int hashCode() {
        return Objects.hash(value);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ProjectionFilter)) {
            return false;
        }

        return this.getValue().equals(((ProjectionFilter) o).getValue());
    }
}
