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
package org.n52.svalbard.decode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.swe.RangeValue;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweQuantityRange;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.collect.Lists;

import net.opengis.swe.x101.BooleanDocument;
import net.opengis.swe.x101.CategoryDocument;
import net.opengis.swe.x101.CountDocument;
import net.opengis.swe.x101.QuantityDocument;
import net.opengis.swe.x101.QuantityRangeDocument;
import net.opengis.swe.x101.QuantityRangeDocument.QuantityRange;
import net.opengis.swe.x101.TimeRangeDocument;
import net.opengis.swe.x101.TimeRangeDocument.TimeRange;
import net.opengis.swe.x101.UomPropertyType;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 */
public class SweCommonDecoderV101Test {

    @Test
    public void should_decode_Count_with_Quality_Text() throws DecodingException {
        final CountDocument xbCount = CountDocument.Factory.newInstance();

        final String textValue = "quality-text";
        xbCount.addNewCount()
                .addNewQuality()
                .addNewText()
                .setValue(textValue);

        final Object decodedObject = new SweCommonDecoderV101().decode(xbCount);

        assertThat(decodedObject, is(instanceOf(SweCount.class)));
        final SweCount sweCount = (SweCount) decodedObject;
        assertThat(sweCount.isSetQuality(), is(true));
        assertThat(sweCount.getQuality()
                .getQuality()
                .size(), is(1));
        assertThat(sweCount.getQuality()
                .getQuality()
                .iterator()
                .next(), is(instanceOf(SweText.class)));
        assertThat(((SweText) sweCount.getQuality()
                .getQuality()
                .iterator()
                .next()).getValue(), is(textValue));
    }

    @Test
    public void should_decode_Quantity_with_Quality_Category() throws DecodingException {
        final QuantityDocument xbQuantity = QuantityDocument.Factory.newInstance();

        final String categoryValue = "quality-category";
        xbQuantity.addNewQuantity()
                .addNewQuality()
                .addNewCategory()
                .setValue(categoryValue);

        final Object decodedObject = new SweCommonDecoderV101().decode(xbQuantity);

        assertThat(decodedObject, is(instanceOf(SweQuantity.class)));
        final SweQuantity sweQuantity = (SweQuantity) decodedObject;
        assertThat(sweQuantity.isSetQuality(), is(true));
        assertThat(sweQuantity.getQuality()
                .getQuality()
                .size(), is(1));
        assertThat(sweQuantity.getQuality()
                .getQuality()
                .iterator()
                .next(), is(instanceOf(SweCategory.class)));
        assertThat(((SweCategory) sweQuantity.getQuality()
                .getQuality()
                .iterator()
                .next()).getValue(), is(categoryValue));
    }

    @Test
    public void should_decode_Category_with_Quality_QuantityRange() throws DecodingException {
        final CategoryDocument xbQuantity = CategoryDocument.Factory.newInstance();

        final BigDecimal rangeStart = BigDecimal.valueOf(1.0);
        final BigDecimal rangeEnd = BigDecimal.valueOf(2.0);
        final ArrayList<BigDecimal> categoryValue = Lists.newArrayList(rangeStart, rangeEnd);
        xbQuantity.addNewCategory()
                .addNewQuality()
                .addNewQuantityRange()
                .setValue(categoryValue);

        final Object decodedObject = new SweCommonDecoderV101().decode(xbQuantity);

        assertThat(decodedObject, is(instanceOf(SweCategory.class)));
        final SweCategory sweCategory = (SweCategory) decodedObject;
        assertThat(sweCategory.isSetQuality(), is(true));
        assertThat(sweCategory.getQuality()
                .getQuality()
                .size(), is(1));
        assertThat(sweCategory.getQuality()
                .getQuality()
                .iterator()
                .next(), is(instanceOf(SweQuantityRange.class)));
        assertThat(((SweQuantityRange) sweCategory.getQuality()
                .getQuality()
                .iterator()
                .next()).getValue(), is(new RangeValue<BigDecimal>(rangeStart, rangeEnd)));
    }

    @Test
    public void should_decode_Boolean_with_Quality_Quantity() throws DecodingException {
        final BooleanDocument xbBoolean = BooleanDocument.Factory.newInstance();

        final BigDecimal quantityValue = BigDecimal.valueOf(42.5);
        xbBoolean.addNewBoolean()
                .addNewQuality()
                .addNewQuantity()
                .setValue(quantityValue.doubleValue());

        final Object decodedObject = new SweCommonDecoderV101().decode(xbBoolean);

        assertThat(decodedObject, is(instanceOf(SweBoolean.class)));
        final SweBoolean sweBoolean = (SweBoolean) decodedObject;
        assertThat(sweBoolean.isSetQuality(), is(true));
        assertThat(sweBoolean.getQuality()
                .getQuality()
                .size(), is(1));
        assertThat(sweBoolean.getQuality()
                .getQuality()
                .iterator()
                .next(), is(instanceOf(SweQuantity.class)));
        assertThat(((SweQuantity) sweBoolean.getQuality()
                .getQuality()
                .iterator()
                .next()).getValue(), is(quantityValue));
    }

    @Test
    public void should_decode_QuantityRange() throws DecodingException {
        final QuantityRangeDocument xbQuantityRange = QuantityRangeDocument.Factory.newInstance();

        final ArrayList<BigDecimal> values = Lists.newArrayList(BigDecimal.valueOf(1.0), BigDecimal.valueOf(2.0));
        final QuantityRange xbQuantityRangeType = xbQuantityRange.addNewQuantityRange();
        xbQuantityRangeType.setValue(values);
        final String definition = "definition";
        xbQuantityRangeType.setDefinition(definition);
        final String axisId = "axis-id";
        xbQuantityRangeType.setAxisID(axisId);
        final String description = "description";
        xbQuantityRangeType.addNewDescription()
                .setStringValue(description);
        final UomPropertyType xbUom = xbQuantityRangeType.addNewUom();
        final String uomCode = "uom-code";
        xbUom.setCode(uomCode);
        final Object decodedObject = new SweCommonDecoderV101().decode(xbQuantityRange);

        assertThat(decodedObject, is(instanceOf(SweQuantityRange.class)));
        final SweQuantityRange sweQuantityRange = (SweQuantityRange) decodedObject;
        assertThat(sweQuantityRange.isSetDefinition(), is(true));
        assertThat(sweQuantityRange.getDefinition(), is(definition));
        assertThat(sweQuantityRange.isSetUom(), is(true));
        assertThat(sweQuantityRange.getUom(), is(uomCode));
        assertThat(sweQuantityRange.isSetAxisID(), is(true));
        assertThat(sweQuantityRange.getAxisID(), is(axisId));
        assertThat(sweQuantityRange.isSetDescription(), is(true));
        assertThat(sweQuantityRange.getDescription(), is(description));
        assertThat(sweQuantityRange.isSetValue(), is(true));
        assertThat(sweQuantityRange.getValue()
                .getRangeStart(), is(values.get(0)));
        assertThat(sweQuantityRange.getValue()
                .getRangeEnd(), is(values.get(1)));
    }

    @Test
    public void should_decode_TimeRange() throws DecodingException {
        final TimeRangeDocument xbTimeRangeDoc = TimeRangeDocument.Factory.newInstance();
        TimeRange xbTimeRange = xbTimeRangeDoc.addNewTimeRange();
        final DateTime startDate = new DateTime(1970, 1, 1, 0, 0, DateTimeZone.UTC);
        final DateTime endDate = new DateTime(2013, 12, 31, 23, 59, DateTimeZone.UTC);
        final List<String> values = Lists.newArrayList(startDate.toString(), endDate.toString());
        xbTimeRange.setValue(values);
        final String iso8601Uom = "urn:ogc:def:unit:ISO:8601";
        xbTimeRange.addNewUom()
                .setHref(iso8601Uom);
        final Object decodedObject = new SweCommonDecoderV101().decode(xbTimeRange);
        assertThat(decodedObject, is(instanceOf(SweTimeRange.class)));
        final SweTimeRange sweTimeRange = (SweTimeRange) decodedObject;
        assertThat(sweTimeRange.isSetUom(), is(true));
        assertThat(sweTimeRange.getUom(), is(iso8601Uom));
        assertThat(sweTimeRange.isSetValue(), is(true));
        assertThat(sweTimeRange.getValue()
                .getRangeStart(), is(startDate));
        assertThat(sweTimeRange.getValue()
                .getRangeEnd(), is(endDate));
    }
}
