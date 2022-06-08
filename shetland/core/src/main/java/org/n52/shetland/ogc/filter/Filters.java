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

import org.n52.shetland.ogc.filter.FilterConstants.BinaryLogicOperator;
import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * Convenient methods for OGC FES filters.
 *
 * @author Christian Autermann
 */
public final class Filters {

    /**
     * Private utility class constructor.
     */
    private Filters() {
    }

    /**
     * Creates a new {@code PropertyIsGreaterThanOrEqualTo} filter.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     *
     * @return the filter
     */
    public static ComparisonFilter ge(String property, String value) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsGreaterThanOrEqualTo, property, value);
    }

    /**
     * Creates a new {@code PropertyIsLessThanOrEqualTo} filter.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     *
     * @return the filter
     */
    public static ComparisonFilter le(String property, String value) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsLessThanOrEqualTo, property, value);
    }

    /**
     * Creates a new {@code PropertyIsEqualTo} filter.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     *
     * @return the filter
     */
    public static ComparisonFilter eq(String property, String value) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsEqualTo, property, value);
    }

    /**
     * Creates a new {@code PropertyIsGreaterThan} filter.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     *
     * @return the filter
     */
    public static ComparisonFilter gt(String property, String value) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsGreaterThan, property, value);
    }

    /**
     * Creates a new {@code PropertyIsLessThan} filter.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     *
     * @return the filter
     */
    public static ComparisonFilter lt(String property, String value) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsLessThan, property, value);
    }

    /**
     * Creates a new {@code PropertyIsLike} filter.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     *
     * @return the filter
     */
    public static ComparisonFilter like(String property, String value) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsLike, property, value);
    }

    /**
     * Creates a new {@code PropertyIsNil} filter.
     *
     * @param property
     *            the property
     *
     * @return the filter
     */
    public static ComparisonFilter isNil(String property) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsNil, property, null);
    }

    /**
     * Creates a new {@code PropertyIsNull} filter.
     *
     * @param property
     *            the property
     *
     * @return the filter
     */
    public static ComparisonFilter isNull(String property) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsNull, property, null);
    }

    /**
     * Creates a new {@code PropertyIsNotEqualTo} filter.
     *
     * @param property
     *            the property
     * @param value
     *            the value
     *
     * @return the filter
     */
    public static ComparisonFilter ne(String property, String value) {
        return new ComparisonFilter(ComparisonOperator.PropertyIsNotEqualTo, property, value);
    }

    /**
     * Creates a new {@code PropertyIsBetween} filter.
     *
     * @param property
     *            the property
     * @param lower
     *            the lower boundary
     * @param upper
     *            the upper boundary
     *
     * @return the filter
     */
    public static ComparisonFilter between(String property, String lower, String upper) {
        try {
            return new ComparisonFilter(ComparisonOperator.PropertyIsBetween, property, lower, upper);
        } catch (OwsExceptionReport ex) {
            // the constructor only throws an exception if the operator is not PropertyIsBetween
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a new {@code BinaryLogicFilter}.
     *
     * @param left
     *            the left operand
     * @param right
     *            the right operand
     *
     * @return the filter
     */
    public static BinaryLogicFilter and(Filter<?> left, Filter<?> right) {
        return new BinaryLogicFilter(BinaryLogicOperator.And, left, right);
    }

    /**
     * Creates a new {@code BinaryLogicFilter}.
     *
     * @param left
     *            the left operand
     * @param right
     *            the right operand
     *
     * @return the filter
     */
    public static BinaryLogicFilter or(Filter<?> left, Filter<?> right) {
        return new BinaryLogicFilter(BinaryLogicOperator.Or, left, right);
    }

    /**
     * Creates a new {@code BinaryLogicFilter}.
     *
     * @param filter
     *            the negated filter
     *
     * @return the filter
     */
    public static UnaryLogicFilter not(Filter<?> filter) {
        return new UnaryLogicFilter(filter);
    }

}
