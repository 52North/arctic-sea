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
package org.n52.shetland.ogc.swe;

import org.n52.janmayen.Copyable;
import org.n52.shetland.ogc.swe.simpleType.SweAbstractSimpleType;
import org.n52.shetland.ogc.swe.simpleType.SweAbstractUomType;

/**
 * SOS internal representation of SWE coordinates
 *
 * @param <T>
 *            Value type
 * @since 1.0.0
 */
public class SweCoordinate<T extends Number>
        implements Copyable<SweCoordinate<T>> {

    /**
     * Coordinate name
     */
    private String name;

    /**
     * Coordinate value TODO is this assignment to generic? maybe, we switch to
     * {@link SweAbstractUomType}?
     */
    private SweAbstractSimpleType<T> value;

    /**
     * constructor
     *
     * @param name
     *            Coordinate name
     * @param value
     *            Coordinate value
     */
    public SweCoordinate(String name, SweAbstractSimpleType<T> value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public SweAbstractSimpleType<T> getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(SweAbstractSimpleType<T> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("SosSweCoordinate[name=%s, value=%s]", getName(), getValue());
    }

    @Override
    @SuppressWarnings("unchecked")
    public SweCoordinate<T> copy() {
        return new SweCoordinate<>(getName(), (SweAbstractSimpleType<T>) getValue().copy());
    }
}
