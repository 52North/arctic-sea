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
package org.n52.shetland.ogc.gml;

import org.n52.shetland.ogc.gml.GmlConstants.SortingOrder;

/**
 * class represents the gml:sortByType
 *
 * @since 1.0.0
 */
public class SortBy {

    /** name of the property, by which should be sorted */
    private String property;

    /**
     * order of the sorting (currently only ascending (ASC) or descending (DESC)
     */
    private SortingOrder order;

    /**
     * constructor
     *
     * @param propertyp
     *            name of property, by which should be sorted
     * @param orderp
     *            sorting order (currently only ascending ('ASC') or descending
     *            ('DESC')
     */
    public SortBy(String propertyp, SortingOrder orderp) {
        this.property = propertyp;
        this.order = orderp;
    }

    /**
     * default constructor
     */
    public SortBy() {
    }

    /**
     *
     * @return Returns String representation with values of this object
     */
    public String toString() {
        return String.format("Sort by [property=%s, order=%s]", getProperty(), getOrder());
    }

    /**
     * Get order
     *
     * @return the order
     */
    public SortingOrder getOrder() {
        return order;
    }

    /**
     * Set ordering
     *
     * @param order
     *            the order to set
     */
    public void setOrder(SortingOrder order) {
        this.order = order;
    }

    /**
     * Get property
     *
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Set property
     *
     * @param property
     *            the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }
}
