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
package org.n52.shetland.ogc.filter;

import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.ogc.filter.FilterConstants.TimeOperator;

/**
 * class for FES FilterPredicates.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T>
 *            operator type, e.g. {@link TimeOperator},{@link SpatialOperator}
 */
public abstract class Filter<T>
        implements AbstractSelectionClause {

    /**
     * Value reference.
     */
    private String valueReference;

    /**
     * constructor.
     */
    public Filter() {
    }

    /**
     * @param valueReference
     *            the value reference
     */
    public Filter(String valueReference) {
        this.valueReference = valueReference;
    }

    /**
     * Get value reference.
     *
     * @return value reference
     */
    public String getValueReference() {
        return valueReference;
    }

    /**
     * Set value reference.
     *
     * @param valueReference
     *            value reference
     *
     * @return This filter
     */
    public Filter<T> setValueReference(String valueReference) {
        this.valueReference = valueReference;
        return this;
    }

    /**
     * Check if valueReference is set.
     *
     * @return <code>true</code>, if valueReference is set
     */
    public boolean hasValueReference() {
        return this.valueReference != null && !this.valueReference.isEmpty();
    }

    /**
     * Get filter operator.
     *
     * @return filter operator
     */
    public abstract T getOperator();

    /**
     * Set filter operator.
     *
     * @param operator
     *            filter operator
     *
     * @return {@code this}
     */
    public abstract Filter<T> setOperator(T operator);

    /**
     * Check if operator is set.
     *
     * @return <code>true</code>, if operator is set
     */
    public boolean isSetOperator() {
        return getOperator() != null;
    }
}
