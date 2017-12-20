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
package org.n52.shetland.ogc.sensorML.elements;

import java.util.List;

import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.ogc.swe.DataRecord;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;

import com.google.common.collect.Lists;

/**
 * SOS internal representation of SensorML characteristics
 *
 * @since 1.0.0
 */
public class SmlCharacteristics extends AbstractSmlDataComponentContainer<SmlCharacteristics> {

    private List<SmlCharacteristic> characteristics = Lists.newArrayList();

    /**
     * default constructor
     */
    public SmlCharacteristics() {
        super();
    }

    /**
     * constructor
     *
     * @param dataRecord
     *            dataRecord
     */
    public SmlCharacteristics(DataRecord dataRecord) {
        super(dataRecord);
    }


    /**
     * @return the characteristics
     */
    public List<SmlCharacteristic> getCharacteristic() {
        if (!hasCharacteristics() && isSetAbstractDataComponents()) {
            List<SmlCharacteristic> c = Lists.newArrayList();
            for (SweAbstractDataComponent component : getAbstractDataComponents()) {
                SmlCharacteristic smlCharacteristic = new SmlCharacteristic(component.getName().getValue());
                smlCharacteristic.setAbstractDataComponent(component);
                c.add(smlCharacteristic);
            }
            return c;
        }
        return this.characteristics;
    }

    /**
     * @param characteristics the characteristics to set
     */
    public void setCharacteristic(List<SmlCharacteristic> characteristics) {
        if (CollectionHelper.isNotEmpty(characteristics)) {
            this.characteristics = characteristics;
            for (SmlCharacteristic smlCharacteristic : characteristics) {
                addAbstractDataComponents(smlCharacteristic.getAbstractDataComponent());
            }
        }
    }

    /**
     * @param characteristics the characteristics to add
     */
    public void addCharacteristic(List<SmlCharacteristic> characteristics) {
        this.characteristics.addAll(characteristics);
        for (SmlCharacteristic smlCharacteristic : characteristics) {
            addAbstractDataComponents(smlCharacteristic.getAbstractDataComponent());
        }
    }

    /**
     * @param characteristic the characteristic to add
     */
    public void addCharacteristic(SmlCharacteristic characteristic) {
        this.characteristics.add(characteristic);
        addAbstractDataComponents(characteristic.getAbstractDataComponent());
    }

    public boolean isSetCharacteristics() {
        return hasCharacteristics() || isSetAbstractDataComponents();
    }

    private boolean hasCharacteristics() {
        return CollectionHelper.isNotEmpty(characteristics);
    }

}
