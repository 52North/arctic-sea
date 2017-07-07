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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.opengis.fes.x20.BinaryComparisonOpType;
import net.opengis.fes.x20.FilterDocument;
import net.opengis.fes.x20.FilterType;
import net.opengis.fes.x20.LiteralType;
import net.opengis.fes.x20.PropertyIsEqualToDocument;

import org.apache.xmlbeans.XmlString;
import org.junit.Test;
import org.n52.svalbard.decode.FesDecoderv20;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * FES 2.0 decoder test class
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 *
 */
public class FesDecoderV20Test {

    private static final String TEST_VALUE_REFERENCE = "testValueReference";

    private static final String TEST_LITERAL = "testLiteral";

    private static final FesDecoderv20 decoder = new FesDecoderv20();

    /**
     * Test PropertyIsEqualTo filter decoding
     *
     * @throws OwsExceptionReport
     */
    @Test
    public void should_parse_PropertyIsEqualTo_Filter() throws DecodingException {
        PropertyIsEqualToDocument propertyIsEqualToDoc = PropertyIsEqualToDocument.Factory.newInstance();
        BinaryComparisonOpType propertyIsEqualToType = propertyIsEqualToDoc.addNewPropertyIsEqualTo();
        // valueReference
        XmlString valueReference =
                (XmlString) propertyIsEqualToType.addNewExpression().substitute(FilterConstants.QN_VALUE_REFERENCE,
                        XmlString.type);
        valueReference.setStringValue(TEST_VALUE_REFERENCE);
        // literal
        LiteralType literalType =
                (LiteralType) propertyIsEqualToType.addNewExpression().substitute(FilterConstants.QN_LITERAL,
                        LiteralType.type);
        XmlString newInstance = XmlString.Factory.newInstance();
        newInstance.setStringValue(TEST_LITERAL);
        literalType.set(newInstance);
        // create document
        FilterDocument filterDoc = FilterDocument.Factory.newInstance();
        FilterType filterType = filterDoc.addNewFilter();
        filterType.setComparisonOps(propertyIsEqualToType);
        filterType.getComparisonOps().substitute(FilterConstants.QN_PROPERTY_IS_EQUAL_TO, BinaryComparisonOpType.type);
        ComparisonFilter comparisonFilter = (ComparisonFilter) decoder.decode(filterDoc);
        // test
        assertThat(comparisonFilter.getOperator(), is(FilterConstants.ComparisonOperator.PropertyIsEqualTo));
        assertThat(comparisonFilter.getValueReference(), is(TEST_VALUE_REFERENCE));
        assertThat(comparisonFilter.getValue(), is(TEST_LITERAL));
    }

}
