/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;


import java.util.Objects;

import org.apache.commons.lang.builder.CompareToBuilder;

import org.n52.shetland.ogc.om.values.Value;

import org.locationtech.jts.geom.Point;

public class PointValuePair implements Comparable<PointValuePair> {
    /**
     * Point value pair point
     */
    private Point point;

    /**
     * Point value pair value
     */
    private Value<?> value;

    /**
     * Constructor
     *
     * @param point
     *            Point value pair point
     * @param value
     *            Point value pair value
     */
    public PointValuePair(Point point, Value<?> value) {
        this.point = point;
        this.value = value;
    }

    /**
     * Get point value pair point
     *
     * @return Point value pair point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Get point value pair value
     *
     * @return Point value pair value
     */
    public Value<?> getValue() {
        return value;
    }

    /**
     * Set point value pair point
     *
     * @param point
     *            Point value pair point to set
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Set point value pair value
     *
     * @param value
     *            Point value pair value to set
     */
    public void setValue(Value<?> value) {
        this.value = value;
    }

    public boolean isSetValue() {
        return getValue() != null && getValue().isSetValue();
    }

    public boolean isSetPoint() {
        return getPoint() != null && !getPoint().isEmpty();
    }

    public boolean isEmpty() {
        return isSetPoint() && isSetValue();
    }

    @Override
    public int compareTo(PointValuePair o) {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(this.getPoint(), o.getPoint());
        return compareToBuilder.toComparison();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PointValuePair other = (PointValuePair) obj;
        if ((getPoint() == null) ? (other.getPoint() != null) : !getPoint().equals(other.getPoint())) {
            return false;
        }
        if ((getValue() == null) ? (other.getValue() != null) : !getValue().equals(other.getValue())) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 37 * hash + Objects.hashCode(this.getPoint());
        hash = 37 * hash + Objects.hashCode(this.getValue());
        return hash;
    }
}
