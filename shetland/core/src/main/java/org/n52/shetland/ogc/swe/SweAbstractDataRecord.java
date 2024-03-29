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
package org.n52.shetland.ogc.swe;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.n52.shetland.ogc.swe.simpleType.SweAbstractSimpleType;

import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public abstract class SweAbstractDataRecord extends SweAbstractDataComponent implements DataRecord {
    private List<SweField> fields = new LinkedList<SweField>();

    /**
     *
     */
    public SweAbstractDataRecord() {
        super();
    }

    @Override
    public List<SweField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    @Override
    public SweAbstractDataRecord setFields(final List<SweField> fields) {
        this.fields.clear();
        if (fields != null) {
            this.fields.addAll(fields);
        }
        return this;
    }

    @Override
    public SweAbstractDataRecord addField(final SweField field) {
        if (field != null) {
            fields.add(field);
        }
        return this;
    }

    @Override
    public boolean isSetFields() {
        return fields != null && !fields.isEmpty();
    }

    @Override
    public int getFieldIndexByIdentifier(final String fieldNameOrElementDefinition) {
        int index = 0;
        if (isSetFields()) {
            for (final SweField sweField : fields) {
                if (isElementDefinition(fieldNameOrElementDefinition, sweField)
                        || isFieldName(fieldNameOrElementDefinition, sweField)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    @Override
    public SweField getFieldByIdentifier(final String fieldNameOrElementDefinition) {
        if (existsFieldForIdentifier(fieldNameOrElementDefinition)) {
            return getFields().get(getFieldIndexByIdentifier(fieldNameOrElementDefinition));
        }
        return null;
    }

    @Override
    public boolean existsFieldForIdentifier(final String fieldNameOrElementDefinition) {
        return getFieldIndexByIdentifier(fieldNameOrElementDefinition) >= 0;
    }

    boolean isFieldName(final String fieldNameOrElementDefinition, final SweField sweField) {
        return sweField.isSetName() && sweField.getName().getValue().equalsIgnoreCase(fieldNameOrElementDefinition);
    }

    boolean isElementDefinition(final String fieldNameOrElementDefinition, final SweField sweField) {
        return sweField.getElement() != null && sweField.getElement().isSetDefinition()
                && sweField.getElement().getDefinition().equalsIgnoreCase(fieldNameOrElementDefinition);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (obj instanceof SweAbstractDataRecord) {
            final SweAbstractDataRecord other = (SweAbstractDataRecord) obj;
            if (getFields() != other.getFields() && (getFields() == null || !getFields().equals(other.getFields()))) {
                return false;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        final int prime = 42;
        int hash = 7;
        hash = prime * hash + super.hashCode();
        hash = prime * hash + getFields().hashCode();
        return hash;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Set<SweAbstractSimpleType<?>> getSweAbstractSimpleTypeFromFields(Class clazz) {
        if (isSetFields()) {
            Set<SweAbstractSimpleType<?>> set = Sets.newHashSet();
            for (SweField field : getFields()) {
                SweAbstractDataComponent element = field.getElement();
                if (!element.isSetName() && field.isSetName()) {
                    element.setName(field.getName());
                }
                if (element.getClass() == clazz) {
                    set.add((SweAbstractSimpleType<?>) element);
                }
            }
            return set;
        }
        return Collections.emptySet();
    }

    @Override
    public abstract SweAbstractDataRecord copy();

}
