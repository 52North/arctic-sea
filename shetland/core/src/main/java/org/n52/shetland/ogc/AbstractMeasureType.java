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
package org.n52.shetland.ogc;

/**
 * Class represents an abstract MeasureType element
 *
 * @since 7.5.0
 *
 */
public abstract class AbstractMeasureType {
    /**
     * Measured value
     */
    private Double value;

    /**
     * Unit of measure
     */
    private String unit;

    /**
     * constructor
     *
     * @param value
     *              Measured value
     */
    public AbstractMeasureType(Double value) {
        this(value, null);
    }

    /**
     * constructor
     *
     * @param value
     *              Measured value
     * @param unit
     *              Unit of measure
     */
    public AbstractMeasureType(Double value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * @param value
     *              Measured value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * @return Measured value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Set unit of measure
     *
     * @param unit
     *             Unit of measure to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Get unit of measure
     *
     * @return Unit of measure
     */
    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return String.format("GmlMeasureType [value=%s, unit=%s]", getValue(), getUnit());
    }

    /**
     * Check whether measured value is set
     *
     * @return <code>true</code>, if measured value is set
     */
    public boolean isSetValue() {
        return value != null;
    }

    /**
     * Check whether unit of measure is set
     *
     * @return <code>true</code>, if unit of measure is set
     */
    public boolean isSetUnit() {
        return this.unit != null && !this.unit.isEmpty();
    }

}
