/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import java.util.Collections;
import java.util.Set;

import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.ogc.gml.AbstractReferenceType;
import org.n52.shetland.ogc.swe.DataRecord;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.SweSimpleDataRecord;
import org.n52.shetland.ogc.swe.simpleType.SweAbstractSimpleType;

import com.google.common.collect.Sets;

/**
 * Abstract container class for SensorML data components.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.0.0
 *
 * @param <T> Implemented class
 */
public class AbstractSmlDataComponentContainer<T> extends AbstractReferenceType {

    private String name;

    private String typeDefinition;

    private DataRecord dataRecord;

    private Set<SweAbstractDataComponent> abstractDataComponents = Sets.newHashSet();

    public AbstractSmlDataComponentContainer() {
    }

    public AbstractSmlDataComponentContainer(DataRecord dataRecord) {
        this.dataRecord = dataRecord;
    }

    public AbstractSmlDataComponentContainer(Set<SweAbstractDataComponent> abstractDataComponents) {
        this.abstractDataComponents = abstractDataComponents;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public boolean isSetName() {
        return name != null && !name.isEmpty();
    }

    /**
     * @return the typeDefinition
     */
    public String getTypeDefinition() {
        return typeDefinition;
    }

    /**
     * @param typeDefinition
     *            the typeDefinition to set
     */
    public void setTypeDefinition(String typeDefinition) {
        this.typeDefinition = typeDefinition;
    }

    public boolean isSetTypeDefinition() {
        return typeDefinition != null && !typeDefinition.isEmpty();
    }

    /**
     * @return the dataRecord
     */
    public DataRecord getDataRecord() {
        if (!isSetDataRecord() && isSetDataComponents()) {
            SweSimpleDataRecord sdr = new SweSimpleDataRecord();
            int counter = 1;
            for (SweAbstractDataComponent element : abstractDataComponents) {
                String name = "field_" + counter++;
                if (element.isSetName()) {
                    name = element.getName().getValue();
                }
                SweField field = new SweField(name, element);
                sdr.addField(field);
            }
            return sdr;
        }
        return dataRecord;
    }

    /**
     * @param dataRecord
     *            the dataRecord to set
     */
    @SuppressWarnings("unchecked")
    public T setDataRecord(DataRecord dataRecord) {
        this.dataRecord = dataRecord;
        return (T) this;
    }

    public boolean isSetAbstractDataRecord() {
        return isSetDataRecord() || isSetDataComponents();
    }

    private boolean isSetDataRecord() {
        return dataRecord != null;
    }

    public Set<SweAbstractDataComponent> getAbstractDataComponents() {
        if (!isSetDataComponents() && isSetAbstractDataRecord()) {
            Set<SweAbstractDataComponent> components = Sets.newHashSet();
            for (SweField field : getDataRecord().getFields()) {
                components.add(field.getElement());
            }
            return components;
        }
        return abstractDataComponents;
    }

    @SuppressWarnings("unchecked")
    public T setAbstractDataComponents(Set<SweAbstractDataComponent> abstractDataComponents) {
        this.abstractDataComponents = abstractDataComponents;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addAbstractDataComponents(Set<SweAbstractDataComponent> abstractDataComponents) {
        this.abstractDataComponents.addAll(abstractDataComponents);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addAbstractDataComponents(SweAbstractDataComponent abstractDataComponent) {
        this.abstractDataComponents.add(abstractDataComponent);
        return (T) this;
    }

    public boolean isSetAbstractDataComponents() {
        return isSetDataComponents() || isSetDataRecord();
    }

    private boolean isSetDataComponents() {
        return CollectionHelper.isNotEmpty(abstractDataComponents);
    }

    @SuppressWarnings("rawtypes")
    public Set<SweAbstractSimpleType<?>> getSweAbstractSimpleTypeFromFields(Class clazz) {
        if (isSetAbstractDataRecord()) {
            return getDataRecord().getSweAbstractSimpleTypeFromFields(clazz);
        }
        return Collections.emptySet();
    }
}
