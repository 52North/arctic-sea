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
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import net.opengis.swe.x20.BooleanType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.TimeRangeDocument;
import net.opengis.swe.x20.TimeRangeType;

import org.apache.xmlbeans.XmlException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swe.simpleType.SweCategory;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.svalbard.decode.SweCommonDecoderV20;
import org.n52.svalbard.decode.exception.DecodingException;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SweCommonDecoderV20Test {

    private SweCommonDecoderV20 decoder;

    private String definition = "test-definition";

    @Before
    public void initDecoder() {
        decoder = new SweCommonDecoderV20();
    }

    @After
    public void nullDecoder() {
        decoder = null;
    }

    @Test
    public void should_encode_xbBoolean_into_SosSweBoolean_with_correct_value_and_definition()
            throws DecodingException {
        BooleanType xbBoolean = BooleanType.Factory.newInstance();
        final boolean value = true;
        xbBoolean.setValue(value);
        xbBoolean.setDefinition(definition);

        Object decodedObject = decoder.decode(xbBoolean);

        assertThat(decodedObject.getClass().getName(), is(SweBoolean.class.getName()));

        SweBoolean sosBoolean = (SweBoolean) decodedObject;

        assertThat(sosBoolean.getValue(), is(value));
        assertThat(sosBoolean.getDefinition(), is(definition));
    }

    @Test
    public void should_encode_xbCategory_into_SosSweCategory_with_correct_value_definition_and_codespace()
            throws DecodingException, XmlException {
        final String codeSpace = "test-codespace";
        final String value = "test-category-value";

        CategoryType xbCategory = CategoryType.Factory.newInstance();
        xbCategory.addNewCodeSpace().setHref(codeSpace);
        xbCategory.setValue(value);
        xbCategory.setDefinition(definition);

        Object decodedObject = decoder.decode(xbCategory);

        assertThat(decodedObject.getClass().getName(), is(SweCategory.class.getName()));

        SweCategory sosCategory = (SweCategory) decodedObject;

        assertThat(sosCategory.getValue(), is(value));
        assertThat(sosCategory.getDefinition(), is(definition));
        assertThat(sosCategory.getCodeSpace(), is(codeSpace));
    }

    @Test
    public void should_decode_TimeRange() throws DecodingException {
         final TimeRangeDocument xbTimeRangeDoc = TimeRangeDocument.Factory.newInstance();
         TimeRangeType xbTimeRange = xbTimeRangeDoc.addNewTimeRange();
         final DateTime startDate = new DateTime(1970, 1, 1, 0, 0, DateTimeZone.UTC);
         final DateTime endDate = new DateTime(2013, 12, 31, 23, 59, DateTimeZone.UTC);
         final List<String> values = Lists.newArrayList(startDate.toString(), endDate.toString());
         xbTimeRange.setValue(values);
         final String iso8601Uom = "urn:ogc:def:unit:ISO:8601";
         xbTimeRange.addNewUom().setHref(iso8601Uom);
         final Object decodedObject = new SweCommonDecoderV20().decode(xbTimeRange);
         assertThat(decodedObject, is(instanceOf(SweTimeRange.class)));
         final SweTimeRange sweTimeRange = (SweTimeRange) decodedObject;
         assertThat(sweTimeRange.isSetUom(), is(true));
         assertThat(sweTimeRange.getUom(), is(iso8601Uom));
         assertThat(sweTimeRange.isSetValue(), is(true));
         assertThat(sweTimeRange.getValue().getRangeStart(), is(startDate));
         assertThat(sweTimeRange.getValue().getRangeEnd(), is(endDate));
     }
}
