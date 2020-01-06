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

import org.n52.shetland.ogc.filter.FilterClause;
import org.n52.shetland.ogc.filter.FilterConstants.SkipTopOperator;

public class SkipTopFilter implements FilterClause {

    private SkipTopOperator operator;
    private Long value;

    public SkipTopFilter(SkipTopOperator operator, Long value) {
        setOperator(operator);
        setValue(value);
    }

    public SkipTopOperator getOperator() {
        return operator;
    }

    public SkipTopFilter setOperator(SkipTopOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * @return the value
     */
    public Long getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Long value) {
        this.value = value;
    }



}
