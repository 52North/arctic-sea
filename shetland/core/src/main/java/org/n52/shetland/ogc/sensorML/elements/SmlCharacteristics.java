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
package org.n52.shetland.ogc.sensorML.elements;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.ogc.swe.DataRecord;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;

import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

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
        return Collections.unmodifiableList(characteristics);
    }

    /**
     * @param characteristics
     *            the characteristics to set
     * @return
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlCharacteristics setCharacteristic(Collection<SmlCharacteristic> characteristics) {
        this.characteristics.clear();
        if (CollectionHelper.isNotEmpty(characteristics)) {
            this.characteristics.addAll(characteristics);
            for (SmlCharacteristic smlCharacteristic : characteristics) {
                addAbstractDataComponents(smlCharacteristic.getAbstractDataComponent());
            }
        }
        return this;
    }

    /**
     * @param characteristics
     *            the characteristics to add
     * @return
     */
    public SmlCharacteristics addCharacteristic(Collection<SmlCharacteristic> characteristics) {
        if (CollectionHelper.isNotEmpty(characteristics)) {
            this.characteristics.addAll(characteristics);
            for (SmlCharacteristic smlCharacteristic : characteristics) {
                addAbstractDataComponents(smlCharacteristic.getAbstractDataComponent());
            }
        }
        return this;
    }

    /**
     * @param characteristic
     *            the characteristic to add
     * @return
     */
    public SmlCharacteristics addCharacteristic(SmlCharacteristic characteristic) {
        if (characteristic != null) {
            this.characteristics.add(characteristic);
            addAbstractDataComponents(characteristic.getAbstractDataComponent());
        }
        return this;
    }

    public boolean isSetCharacteristics() {
        return hasCharacteristics() || isSetAbstractDataComponents();
    }

    private boolean hasCharacteristics() {
        return CollectionHelper.isNotEmpty(characteristics);
    }

}
