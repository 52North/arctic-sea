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
package org.n52.svalbard.decode.json;

import org.joda.time.DateTime;
import org.n52.shetland.ogc.swe.RangeValue;
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
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class FieldDecoder extends JSONDecoder<SweField> {

    public FieldDecoder() {
        super(SweField.class);
    }

    @Override
    public SweField decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        if (validate) {
            JSONValidator.getInstance().validateAndThrow(node, SchemaConstants.Common.FIELD);
        }
        return decodeJSON(node);
    }

    public SweField decodeJSON(JsonNode node) throws DecodingException {
        final String type = node.path(JSONConstants.TYPE).textValue();
        final SweAbstractDataComponent element;

        if (type.equals(JSONConstants.BOOLEAN_TYPE)) {
            element = decodeBoolean(node);
        } else if (type.equals(JSONConstants.COUNT_TYPE)) {
            element = decodeCount(node);
        } else if (type.equals(JSONConstants.COUNT_RANGE_TYPE)) {
            element = decodeCountRange(node);
        } else if (type.equals(JSONConstants.OBSERVABLE_PROPERTY_TYPE)) {
            element = decodeObservableProperty(node);
        } else if (type.equals(JSONConstants.QUALITY_TYPE)) {
            element = decodeQuality(node);
        } else if (type.equals(JSONConstants.TEXT_TYPE)) {
            element = decodeText(node);
        } else if (type.equals(JSONConstants.QUANTITY_TYPE)) {
            element = decodeQuantity(node);
        } else if (type.equals(JSONConstants.QUANTITY_RANGE_TYPE)) {
            element = decodeQuantityRange(node);
        } else if (type.equals(JSONConstants.TIME_TYPE)) {
            element = decodeTime(node);
        } else if (type.equals(JSONConstants.TIME_RANGE_TYPE)) {
            element = decodeTimeRange(node);
        } else if (type.equals(JSONConstants.CATEGORY_TYPE)) {
            element = decodeCategory(node);
        } else {
            throw new UnsupportedDecoderInputException(this, node);
        }
        final String name = node.path(JSONConstants.NAME).textValue();
        element.setDescription(node.path(JSONConstants.DESCRIPTION).textValue());
        element.setIdentifier(node.path(JSONConstants.IDENTIFIER).textValue());
        element.setDefinition(node.path(JSONConstants.DEFINITION).textValue());
        element.setLabel(node.path(JSONConstants.LABEL).textValue());
        return new SweField(name, element);
    }

    protected SweAbstractDataComponent decodeBoolean(JsonNode node) {
        SweBoolean swe = new SweBoolean();
        if (node.hasNonNull(JSONConstants.VALUE)) {
            swe.setValue(node.path(JSONConstants.VALUE).booleanValue());
        }
        return swe;
    }

    protected SweAbstractDataComponent decodeCount(JsonNode node) {
        SweCount swe = new SweCount();
        if (node.hasNonNull(JSONConstants.VALUE)) {
            swe.setValue(node.path(JSONConstants.VALUE).intValue());
        }
        return swe;
    }

    protected SweAbstractDataComponent decodeCountRange(JsonNode node) {
        SweCountRange swe = new SweCountRange();
        if (node.hasNonNull(JSONConstants.VALUE)) {
            int start = node.path(JSONConstants.VALUE).path(0).intValue();
            int end = node.path(JSONConstants.VALUE).path(1).intValue();
            swe.setValue(new RangeValue<Integer>(start, end));
        }
        return swe;
    }

    protected SweAbstractDataComponent decodeQuantity(JsonNode node) {
        SweQuantity swe = new SweQuantity();
        if (node.hasNonNull(JSONConstants.VALUE)) {
            swe.setValue(node.path(JSONConstants.VALUE).doubleValue());
        }
        return swe.setUom(node.path(JSONConstants.UOM).textValue());
    }

    protected SweAbstractDataComponent decodeText(JsonNode node) {
        return new SweText().setValue(node.path(JSONConstants.VALUE).textValue());
    }

    protected SweAbstractDataComponent decodeQuality(JsonNode node) throws DecodingException {
        // TODO quality
        throw new UnsupportedDecoderInputException(this, node);
    }

    protected SweAbstractDataComponent decodeObservableProperty(JsonNode node) {
        SweObservableProperty swe = new SweObservableProperty();
        return swe.setValue(node.path(JSONConstants.VALUE).textValue());
    }

    protected SweAbstractDataComponent decodeTime(JsonNode node) throws DecodingException {
        SweTime swe = new SweTime();
        if (node.hasNonNull(JSONConstants.VALUE)) {
            String value = node.path(JSONConstants.VALUE).textValue();
            swe.setValue(parseDateTime(value));
        }
        return swe.setUom(node.path(JSONConstants.UOM).textValue());
    }

    protected SweAbstractDataComponent decodeCategory(JsonNode node) {
        String value = node.path(JSONConstants.VALUE).textValue();
        String codespace = node.path(JSONConstants.CODESPACE).textValue();
        return new SweCategory().setValue(value).setCodeSpace(codespace);
    }

    protected SweAbstractDataComponent decodeQuantityRange(JsonNode node) {
        SweQuantityRange swe = new SweQuantityRange();
        if (node.hasNonNull(JSONConstants.VALUE)) {
            double start = node.path(JSONConstants.VALUE).path(0).doubleValue();
            double end = node.path(JSONConstants.VALUE).path(1).doubleValue();
            swe.setValue(new RangeValue<Double>(start, end));
        }
        return swe.setUom(node.path(JSONConstants.UOM).textValue());
    }

    protected SweAbstractDataComponent decodeTimeRange(JsonNode node) throws DecodingException {
        SweTimeRange swe = new SweTimeRange();
        if (node.hasNonNull(JSONConstants.VALUE)) {
            String start = node.path(JSONConstants.VALUE).path(0).textValue();
            String end = node.path(JSONConstants.VALUE).path(1).textValue();
            swe.setValue(new RangeValue<DateTime>(parseDateTime(start), parseDateTime(end)));
        }
        return swe.setUom(node.path(JSONConstants.UOM).textValue());
    }
}
