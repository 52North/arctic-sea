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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sos.x10.GetObservationDocument;

public class OgcDecoderv100Test {

    OgcDecoderv100 decoder = new OgcDecoderv100();

    @Test
    public void parsePropertyIsLike() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsLike.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsLike));
        assertThat(filter.getWildCard(), is("*"));
        assertThat(filter.getSingleChar(), is("?"));
        assertThat(filter.getEscapeString(), is("\\"));
        assertThat(filter.getValueReference(), is("QualityFlag"));
        assertThat(filter.getValue(), is("4/_(2)%"));
    }

    @Test
    public void parsePropertyIsNull() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsNull.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsNull));
        assertThat(filter.getValueReference(), is("IsNull"));
    }

    @Test
    public void parsePropertyIsBetween() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsBetween.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsBetween));
        assertThat(filter.getValueReference(), is("IsBetween"));
        assertThat(filter.getValue(), is("1"));
        assertThat(filter.getValueUpper(), is("10"));
    }

    @Test
    public void parsePropertyIsEqualTo() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsEqualTo.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsEqualTo));
        assertThat(filter.getValueReference(), is("IsEqualTo"));
        assertThat(filter.getValue(), is("52"));
        assertThat(filter.isMatchCase(), is(false));
    }

    @Test
    public void parsePropertyIsNotEqualTo() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsNotEqualTo.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsNotEqualTo));
        assertThat(filter.getValueReference(), is("IsNotEqualTo"));
        assertThat(filter.getValue(), is("52"));
        assertThat(filter.isMatchCase(), is(true));
    }

    @Test
    public void parsePropertyIsLessThan() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsLessThan.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsLessThan));
        assertThat(filter.getValueReference(), is("IsLessThan"));
        assertThat(filter.getValue(), is("7.52"));
        assertThat(filter.isMatchCase(), is(false));
    }

    @Test
    public void parsePropertyIsGreaterThan() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsGreaterThan.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsGreaterThan));
        assertThat(filter.getValueReference(), is("IsGreaterThan"));
        assertThat(filter.getValue(), is("52.7"));
        assertThat(filter.isMatchCase(), is(false));
    }

    @Test
    public void parsePropertyIsLessThanOrEqualTo() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsLessThanOrEqualTo.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsLessThanOrEqualTo));
        assertThat(filter.getValueReference(), is("IsLessThanOrEqualTo"));
        assertThat(filter.getValue(), is("7.52"));
        assertThat(filter.isMatchCase(), is(true));
    }

    @Test
    public void parsePropertyIsGreaterThanOrEqualTo() throws XmlException, IOException, DecodingException {
        XmlObject parse = XmlObject.Factory.parse(getClass().getResourceAsStream("/GetObsPropertyIsGreaterThanOrEqualTo.xml"));
        Object decode = decoder.decode(((GetObservationDocument)parse).getGetObservation().getResult().getComparisonOps());
        assertThat(decode, instanceOf(ComparisonFilter.class));
        ComparisonFilter filter = (ComparisonFilter) decode;
        assertThat(filter.getOperator(), is(ComparisonOperator.PropertyIsGreaterThanOrEqualTo));
        assertThat(filter.getValueReference(), is("IsGreaterThanOrEqualTo"));
        assertThat(filter.getValue(), is("52.7"));
        assertThat(filter.isMatchCase(), is(true));
    }

}
