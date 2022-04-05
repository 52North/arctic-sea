/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.Producer;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.FilterConstants;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;

import net.opengis.fes.x20.BinaryComparisonOpType;
import net.opengis.fes.x20.FilterDocument;
import net.opengis.fes.x20.FilterType;
import net.opengis.fes.x20.LiteralType;
import net.opengis.fes.x20.PropertyIsEqualToDocument;

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

    private DecoderRepository decoderRepository;

    private static final FesDecoderv20 decoder = new FesDecoderv20();

    @BeforeEach
    public void setup() {

        decoderRepository = new DecoderRepository();

        Producer<XmlOptions> xmlOptions = XmlOptions::new;

        List<Decoder<?, ?>> decoders = Stream.of(new GetFeatureOfInterestResponseDocumentDecoder(),
                                                 new GmlDecoderv321(),
                                                 new OmDecoderv20(),
                                                 new SweCommonDecoderV20(),
                                                 new SamplingDecoderv20(),
                                                 new WmlMonitoringPointDecoderv20(),
                                                 decoder)
                .map(decoder -> {
                    decoder.setDecoderRepository(decoderRepository);
                    decoder.setXmlOptions(xmlOptions);
                    return decoder;
                }).collect(toList());

        decoderRepository.setDecoders(decoders);
        decoderRepository.init();
    }

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

    @Test
    public void should_parse_DWithin_Filter() throws DecodingException, XmlException, IOException {
        SpatialFilter spatialFilter = decodeSpatialFilter("/FesDWithin.xml");
        checkDistanceSpatialFilter(spatialFilter, FilterConstants.SpatialOperator.DWithin);
    }

    @Test
    public void should_parse_Beyond_Filter() throws DecodingException, XmlException, IOException {
        SpatialFilter spatialFilter = decodeSpatialFilter("/FesBeyond.xml");
        checkDistanceSpatialFilter(spatialFilter, FilterConstants.SpatialOperator.Beyond);
    }

    @Test
    public void should_parse_Disjoint_Filter() throws DecodingException, XmlException, IOException {
        SpatialFilter spatialFilter = decodeSpatialFilter("/FesDisjoint.xml");
        assertThat(spatialFilter.getGeometry(), is(notNullValue()));
        assertThat(spatialFilter.getOperator(), is(notNullValue()));
        assertEquals(FilterConstants.SpatialOperator.Disjoint, spatialFilter.getOperator());
    }

    @Test
    public void should_parse_Overlaps_Filter() throws DecodingException, XmlException, IOException {
        SpatialFilter spatialFilter = decodeSpatialFilter("/FesOverlaps.xml");
        assertThat(spatialFilter.getGeometry(), is(notNullValue()));
        assertThat(spatialFilter.getOperator(), is(notNullValue()));
        assertEquals(FilterConstants.SpatialOperator.Overlaps, spatialFilter.getOperator());
    }

    private SpatialFilter decodeSpatialFilter(String fileName) throws DecodingException, XmlException, IOException {
        XmlObject xml = XmlObject.Factory.parse(getClass().getResourceAsStream(fileName));
        DecoderKey decoderKey = CodingHelper.getDecoderKey(xml);
        Decoder<SpatialFilter, XmlObject> decoder = decoderRepository.getDecoder(decoderKey);
        SpatialFilter spatialFilter = decoder.decode(xml);
        return spatialFilter;
    }

    private void checkDistanceSpatialFilter(SpatialFilter spatialFilter, FilterConstants.SpatialOperator operator) {
        assertThat(spatialFilter, is(notNullValue()));
        assertThat(spatialFilter.getGeometry(), is(notNullValue()));
        assertThat(spatialFilter.getOperator(), is(notNullValue()));
        assertEquals(operator, spatialFilter.getOperator());
        assertThat(spatialFilter.getDistance(), is(notNullValue()));
        assertEquals(10.0, spatialFilter.getDistance().getValue());
        assertEquals("m", spatialFilter.getDistance().getUnit());
    }
}
