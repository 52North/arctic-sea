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
package org.n52.svalbard.decode;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.janmayen.Json;
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
import org.n52.svalbard.ConfiguredSettingsManager;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.FieldDecoder;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.report.ProcessingReport;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class FieldDecoderTest {
    @ClassRule
    public static final ConfiguredSettingsManager csm = new ConfiguredSettingsManager();

    private static final String DEFINITION = "definition";

    private static final String NAME = "name";

    private static final String DESCRIPTION = "description";

    private static final String IDENTIFIER = "identifier";

    private static final String LABEL = "label";

    private static final String UOM = "uom";

    private static final String TIME_START = "2013-08-02T14:43:05+0200";

    private static final String TIME_END = "2013-08-02T14:48:05+0200";

    private static final int COUNT_VALUE_START = 12;

    private static final int COUNT_VALUE_END = 13;

    private static final String OBSERVED_PROPERTY_VALUE = "obsProp";

    private static final String TEXT_VALUE = "text";

    private static final double QUANTITY_VALUE_START = 52.2;

    private static final double QUANTITY_VALUE_END = 52.3;

    private static final String CODESPACE = "codespace";

    private static final String CATEGORY_VALUE = "category";

    private final ErrorCollector errors = new ErrorCollector();

    private DateTime timeStart;

    private DateTime timeEnd;

    private JSONValidator validator;

    private FieldDecoder decoder;

    @Before
    public void before()
            throws DecodingException {
        this.decoder = new FieldDecoder();
        this.timeStart = DateTimeHelper.parseIsoString2DateTime(TIME_START);
        this.timeEnd = DateTimeHelper.parseIsoString2DateTime(TIME_END);
        this.validator = JSONValidator.getInstance();
    }

    @Test
    public void testCountWithValue()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.COUNT_TYPE).put(JSONConstants.VALUE,
                COUNT_VALUE_START);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweCount.class)));
        SweCount swe = (SweCount) field.getElement();
        errors.checkThat(swe.getValue(), is(COUNT_VALUE_START));
    }

    @Test
    public void testCount()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.COUNT_TYPE);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweCount.class)));
    }

    @Test
    public void testBooleanWithValueTrue()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.BOOLEAN_TYPE).put(JSONConstants.VALUE, true);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweBoolean.class)));
        SweBoolean swe = (SweBoolean) field.getElement();
        errors.checkThat(swe.getValue(), is(true));
    }

    @Test
    public void testBooleanWithValueFalse()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.BOOLEAN_TYPE).put(JSONConstants.VALUE, false);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweBoolean.class)));
        SweBoolean swe = (SweBoolean) field.getElement();
        errors.checkThat(swe.getValue(), is(false));
    }

    @Test
    public void testBoolean()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.BOOLEAN_TYPE);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweBoolean.class)));
    }

    @Test
    public void testCountRangeWithValue()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.COUNT_RANGE_TYPE);
        json.putArray(JSONConstants.VALUE).add(COUNT_VALUE_START).add(COUNT_VALUE_END);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweCountRange.class)));
        SweCountRange swe = (SweCountRange) field.getElement();
        assertThat(swe.getValue(), is(notNullValue()));
        errors.checkThat(swe.getValue().getRangeStart(), is(COUNT_VALUE_START));
        errors.checkThat(swe.getValue().getRangeEnd(), is(COUNT_VALUE_END));
    }

    @Test
    public void testCountRange()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.COUNT_RANGE_TYPE);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweCountRange.class)));
        SweCountRange swe = (SweCountRange) field.getElement();
        assertThat(swe.getValue(), is(nullValue()));
    }

    @Test
    public void testObservablePropertyWithValue()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.OBSERVABLE_PROPERTY_TYPE)
                .put(JSONConstants.VALUE, OBSERVED_PROPERTY_VALUE);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweObservableProperty.class)));
        SweObservableProperty swe = (SweObservableProperty) field.getElement();
        errors.checkThat(swe.getValue(), is(OBSERVED_PROPERTY_VALUE));
    }

    @Test
    public void testObservableProperty()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.OBSERVABLE_PROPERTY_TYPE);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweObservableProperty.class)));
        SweObservableProperty swe = (SweObservableProperty) field.getElement();
        errors.checkThat(swe.getValue(), is(nullValue()));
    }

    @Test
    @Ignore("not yet supported")
    public void testQualityWithValue() {
    }

    @Test
    @Ignore("not yet supported")
    public void testQuality() {
    }

    @Test
    public void testTextWithValue()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.TEXT_TYPE).put(JSONConstants.VALUE, TEXT_VALUE);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweText.class)));
        SweText swe = (SweText) field.getElement();
        errors.checkThat(swe.getValue(), is(TEXT_VALUE));
    }

    @Test
    public void testText()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.TEXT_TYPE);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweText.class)));
        SweText swe = (SweText) field.getElement();
        errors.checkThat(swe.getValue(), is(nullValue()));
    }

    @Test
    public void testQuantityWithValue()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.QUANTITY_TYPE)
                .put(JSONConstants.UOM, UOM).put(JSONConstants.VALUE, QUANTITY_VALUE_START);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweQuantity.class)));
        SweQuantity swe = (SweQuantity) field.getElement();
        errors.checkThat(swe.getValue(), is(QUANTITY_VALUE_START));
        errors.checkThat(swe.getUom(), is(UOM));
    }

    @Test
    public void testQuantity()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.QUANTITY_TYPE).put(JSONConstants.UOM, UOM);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweQuantity.class)));
        SweQuantity swe = (SweQuantity) field.getElement();
        errors.checkThat(swe.getUom(), is(UOM));
    }

    @Test
    public void testQuantityRangeWithValue()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.QUANTITY_RANGE_TYPE).put(JSONConstants.UOM, UOM);
        json.putArray(JSONConstants.VALUE).add(QUANTITY_VALUE_START).add(QUANTITY_VALUE_END);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweQuantityRange.class)));
        SweQuantityRange swe = (SweQuantityRange) field.getElement();
        errors.checkThat(swe.getUom(), is(UOM));
        errors.checkThat(swe.getValue(), is(notNullValue()));
        errors.checkThat(swe.getValue().getRangeStart(), is(QUANTITY_VALUE_START));
        errors.checkThat(swe.getValue().getRangeEnd(), is(QUANTITY_VALUE_END));
    }

    @Test
    public void testQuantityRange()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.QUANTITY_RANGE_TYPE).put(JSONConstants.UOM, UOM);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweQuantityRange.class)));
        SweQuantityRange swe = (SweQuantityRange) field.getElement();
        errors.checkThat(swe.getUom(), is(UOM));
        errors.checkThat(swe.getValue(), is(nullValue()));
    }

    @Test
    public void timeWithValue()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.TIME_TYPE).put(JSONConstants.UOM, UOM)
                .put(JSONConstants.VALUE, TIME_START);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweTime.class)));
        SweTime swe = (SweTime) field.getElement();
        errors.checkThat(swe.getValue(), is(timeStart));
        errors.checkThat(swe.getUom(), is(UOM));
    }

    @Test
    public void time()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.TIME_TYPE).put(JSONConstants.UOM, UOM);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweTime.class)));
        SweTime swe = (SweTime) field.getElement();
        errors.checkThat(swe.getValue(), is(nullValue()));
        errors.checkThat(swe.getUom(), is(UOM));
    }

    @Test
    public void timeRangeWithValue()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.TIME_RANGE_TYPE).put(JSONConstants.UOM, UOM);
        json.putArray(JSONConstants.VALUE).add(TIME_START).add(TIME_END);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweTimeRange.class)));
        SweTimeRange swe = (SweTimeRange) field.getElement();
        errors.checkThat(swe.getUom(), is(UOM));
        errors.checkThat(swe.getValue(), is(notNullValue()));
        errors.checkThat(swe.getValue().getRangeStart(), is(timeStart));
        errors.checkThat(swe.getValue().getRangeEnd(), is(timeEnd));

    }

    @Test
    public void timeRange()
            throws DecodingException {
        ObjectNode json =
                createField().put(JSONConstants.TYPE, JSONConstants.TIME_RANGE_TYPE).put(JSONConstants.UOM, UOM);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweTimeRange.class)));
        SweTimeRange swe = (SweTimeRange) field.getElement();
        errors.checkThat(swe.getUom(), is(UOM));
        errors.checkThat(swe.getValue(), is(nullValue()));

    }

    @Test
    public void testCategoryWithValue()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.CATEGORY_TYPE)
                .put(JSONConstants.CODESPACE, CODESPACE).put(JSONConstants.VALUE, CATEGORY_VALUE);
        SweField field = checkCommon(json, true);
        assertThat(field.getElement(), is(instanceOf(SweCategory.class)));
        SweCategory swe = (SweCategory) field.getElement();
        errors.checkThat(swe.getValue(), is(CATEGORY_VALUE));
        errors.checkThat(swe.getCodeSpace(), is(CODESPACE));
    }

    @Test
    public void testCategory()
            throws DecodingException {
        ObjectNode json = createField().put(JSONConstants.TYPE, JSONConstants.CATEGORY_TYPE)
                .put(JSONConstants.CODESPACE, CODESPACE);
        SweField field = checkCommon(json, false);
        assertThat(field.getElement(), is(instanceOf(SweCategory.class)));
        SweCategory swe = (SweCategory) field.getElement();
        errors.checkThat(swe.getValue(), is(nullValue()));
        errors.checkThat(swe.getCodeSpace(), is(CODESPACE));
    }

    protected SweField validateWithValueAndDecode(ObjectNode json, boolean withValue)
            throws DecodingException {
        ProcessingReport report = validator.validate(json,
                withValue ? SchemaConstants.Common.FIELD_WITH_VALUE : SchemaConstants.Common.FIELD);
        if (!report.isSuccess()) {
            System.err.println(validator.encode(report, json));
            fail("Invalid generated field!");
        }
        return decoder.decode(json);
    }

    protected ObjectNode createField() {
        return Json.nodeFactory().objectNode().put(JSONConstants.NAME, NAME).put(JSONConstants.LABEL, LABEL)
                .put(JSONConstants.DEFINITION, DEFINITION).put(JSONConstants.DESCRIPTION, DESCRIPTION)
                .put(JSONConstants.IDENTIFIER, IDENTIFIER);
    }

    protected SweField checkCommon(ObjectNode json, boolean withValue)
            throws DecodingException {
        SweField field = validateWithValueAndDecode(json, withValue);
        assertThat(field, is(notNullValue()));
        errors.checkThat(field.getName().getValue(), is(NAME));
        assertThat(field.getElement(), is(notNullValue()));
        errors.checkThat(field.getElement().getDefinition(), is(DEFINITION));
        errors.checkThat(field.getElement().getDescription(), is(DESCRIPTION));
        errors.checkThat(field.getElement().getIdentifier(), is(IDENTIFIER));
        errors.checkThat(field.getElement().getLabel(), is(LABEL));
        return field;
    }

    @Rule
    public ErrorCollector getErrorCollectorRule() {
        return errors;
    }
}
