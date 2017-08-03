/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.json;

import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweCountRange;
import org.n52.shetland.ogc.swe.simpleType.SweObservableProperty;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTime;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.shetland.util.DateTimeHelper;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class FieldEncoder
        extends JSONEncoder<SweField> {
    public FieldEncoder() {
        super(SweField.class);
    }

    @Override
    public JsonNode encodeJSON(SweField field)
            throws EncodingException {
        switch (field.getElement().getDataComponentType()) {
            case Count:
                return encodeSweCountField(field);
            case Boolean:
                return encodeSweBooleanField(field);
            case CountRange:
                return encodeSweCountRangeField(field);
            case ObservableProperty:
                return encodeSweObservableProperyField(field);
            case Text:
                return encodeSweTextField(field);
            case Quantity:
                return encodeSweQuantityField(field);
            case QuantityRange:
                return encodeSweQuantityRangeField(field);
            case Time:
                return encodeSweTimeField(field);
            case TimeRange:
                return encodeSweTimeRangeField(field);
            case Category:
                return encodeSweCategoryField(field);
            default:
                throw new UnsupportedEncoderInputException(this, field);
        }
    }

    private ObjectNode createField(SweField field) {
        ObjectNode jfield = nodeFactory().objectNode();
        jfield.put(JSONConstants.NAME, field.getName().getValue());
        SweAbstractDataComponent element = field.getElement();
        if (element.isSetDefinition()) {
            jfield.put(JSONConstants.DEFINITION, element.getDefinition());
        }
        if (element.isSetDescription()) {
            jfield.put(JSONConstants.DESCRIPTION, element.getDescription());
        }
        if (element.isSetIdentifier()) {
            jfield.put(JSONConstants.IDENTIFIER, element.getIdentifier());
        }
        if (element.isSetLabel()) {
            jfield.put(JSONConstants.LABEL, element.getLabel());
        }
        return jfield;
    }

    private ObjectNode encodeSweCountField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.COUNT_TYPE);
        SweCount sweCount = (SweCount) field.getElement();
        if (sweCount.isSetValue()) {
            jfield.put(JSONConstants.VALUE, sweCount.getValue());
        }
        return jfield;
    }

    private ObjectNode encodeSweBooleanField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.BOOLEAN_TYPE);
        SweBoolean sweBoolean = (SweBoolean) field.getElement();
        if (sweBoolean.isSetValue()) {
            jfield.put(JSONConstants.VALUE, sweBoolean.getValue());
        }
        return jfield;
    }

    private ObjectNode encodeSweCountRangeField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.COUNT_RANGE_TYPE);
        SweCountRange sweCountRange = (SweCountRange) field.getElement();
        if (sweCountRange.isSetValue()) {
            ArrayNode av = jfield.putArray(JSONConstants.VALUE);
            av.add(sweCountRange.getValue().getRangeStart());
            av.add(sweCountRange.getValue().getRangeEnd());
        }
        return jfield;
    }

    private ObjectNode encodeSweObservableProperyField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.OBSERVABLE_PROPERTY_TYPE);
        SweObservableProperty sweObservableProperty = (SweObservableProperty) field.getElement();
        if (sweObservableProperty.isSetValue()) {
            jfield.put(JSONConstants.VALUE, sweObservableProperty.getValue());
        }
        return jfield;
    }

    private ObjectNode encodeSweTextField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.TEXT_TYPE);
        SweText sweText = (SweText) field.getElement();
        if (sweText.isSetValue()) {
            jfield.put(JSONConstants.VALUE, sweText.getValue());
        }
        return jfield;
    }

    private ObjectNode encodeSweQuantityField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.QUANTITY_TYPE);
        SweQuantity sweQuantity = (SweQuantity) field.getElement();
        if (sweQuantity.isSetValue()) {
            jfield.put(JSONConstants.VALUE, sweQuantity.getValue());
        }
        jfield.put(JSONConstants.UOM, sweQuantity.getUom());
        return jfield;
    }

    private ObjectNode encodeSweQuantityRangeField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.QUANTITY_RANGE_TYPE);
        SweQuantityRange sweQuantityRange = (SweQuantityRange) field.getElement();
        jfield.put(JSONConstants.UOM, sweQuantityRange.getUom());
        if (sweQuantityRange.isSetValue()) {
            ArrayNode av = jfield.putArray(JSONConstants.VALUE);
            av.add(sweQuantityRange.getValue().getRangeStart());
            av.add(sweQuantityRange.getValue().getRangeEnd());
        }
        return jfield;
    }

    private ObjectNode encodeSweTimeField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.TIME_TYPE);
        SweTime sweTime = (SweTime) field.getElement();
        jfield.put(JSONConstants.UOM, sweTime.getUom());
        if (sweTime.isSetValue()) {
            jfield.put(JSONConstants.VALUE, DateTimeHelper.formatDateTime2IsoString(sweTime.getValue()));
        }
        return jfield;
    }

    private ObjectNode encodeSweTimeRangeField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.TIME_RANGE_TYPE);
        SweTimeRange sweTimeRange = (SweTimeRange) field.getElement();
        jfield.put(JSONConstants.UOM, sweTimeRange.getUom());
        if (sweTimeRange.isSetValue()) {
            ArrayNode av = jfield.putArray(JSONConstants.VALUE);
            av.add(DateTimeHelper.formatDateTime2IsoString(sweTimeRange.getValue().getRangeStart()));
            av.add(DateTimeHelper.formatDateTime2IsoString(sweTimeRange.getValue().getRangeEnd()));
        }
        return jfield;
    }

    private ObjectNode encodeSweCategoryField(SweField field) {
        ObjectNode jfield = createField(field);
        jfield.put(JSONConstants.TYPE, JSONConstants.CATEGORY_TYPE);
        SweCategory sweCategory = (SweCategory) field.getElement();
        jfield.put(JSONConstants.CODESPACE, sweCategory.getCodeSpace());
        if (sweCategory.isSetValue()) {
            jfield.put(JSONConstants.VALUE, sweCategory.getValue());
        }
        return jfield;
    }
}
