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
package org.n52.shetland.ogc.sensorML.elements;

import org.n52.shetland.ogc.gml.AbstractReferenceType;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;

/**
 * SOS internal representation of SensorML IOs
 *
 * @since 1.0.0
 */
public class SmlIo extends AbstractReferenceType {

    private String ioName;

    private SweAbstractDataComponent ioValue;

    /**
     * default constructor
     */
    public SmlIo() {
        super();
    }

    /**
     * constructor
     *
     * @param ioValue
     *            The IO value
     */
    public SmlIo(final SweAbstractDataComponent ioValue) {
        super();
        this.ioValue = ioValue;
    }

    /**
     * @return the inputName
     */
    public String getIoName() {
        return ioName;
    }

    /**
     * @param inputName
     *            the inputName to set
     * @return This object
     */
    public SmlIo setIoName(final String inputName) {
        this.ioName = inputName;
        return this;
    }

    /**
     * @return the input
     */
    public SweAbstractDataComponent getIoValue() {
        return ioValue;
    }

    /**
     * @param ioValue
     *            the input to set
     * @return This object
     */
    public SmlIo setIoValue(final SweAbstractDataComponent ioValue) {
        this.ioValue = ioValue;
        return this;
    }

    public boolean isSetName() {
        return ioName != null && !ioName.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("SosSMLIo [ioName=%s, ioValue=%s]", ioName, ioValue);
    }

    public Boolean isSetValue() {
        return ioValue != null;
    }

}
