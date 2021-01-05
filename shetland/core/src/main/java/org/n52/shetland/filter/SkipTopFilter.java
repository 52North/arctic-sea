/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;

import java.util.Objects;

public class SkipTopFilter implements FilterClause {

    private final SkipTopOperator operator;
    private final Long value;

    public SkipTopFilter(SkipTopOperator operator, Long value) {
        this.operator = operator;
        this.value = value;
    }

    public SkipTopOperator getOperator() {
        return operator;
    }

    /**
     * @return the value
     */
    public Long getValue() {
        return value;
    }

    @Override public int hashCode() {
        return Objects.hash(operator, value);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof SkipTopFilter)) {
            return false;
        }

        return Objects.equals(this.operator, ((SkipTopFilter) o).getOperator())
                && Objects.equals(this.value, ((SkipTopFilter) o).getValue());
    }

    @Override public String toString() {
        return ((getOperator().equals(SkipTopOperator.Top)) ? "$top" : "$skip") + "=" + getValue().toString();
    }
}
