/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.filter;

import org.n52.shetland.ogc.filter.FilterConstants.UnaryLogicOperator;

/**
 * class for unary logic filter "NOT"
 *
 * @since 1.0.0
 *
 */
public class UnaryLogicFilter extends Filter<UnaryLogicOperator> implements LogicFilter {

    private UnaryLogicOperator operator = UnaryLogicOperator.Not;

    private Filter<?> filterPredicate;

    /**
     * Constructor
     *
     * @param filterPredicate
     *            Filter
     */
    public UnaryLogicFilter(Filter<?> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    @Override
    public UnaryLogicOperator getOperator() {
        return operator;
    }

    @Override
    public Filter<UnaryLogicOperator> setOperator(UnaryLogicOperator operator) {
        this.operator = operator;
        return this;
    }

    /**
     * @return the filterPredicate
     */
    public Filter<?> getFilterPredicate() {
        return filterPredicate;
    }

    /**
     * @param filterPredicate
     *            the filterPredicate to set
     */
    public void setFilterPredicate(Filter<?> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

}
