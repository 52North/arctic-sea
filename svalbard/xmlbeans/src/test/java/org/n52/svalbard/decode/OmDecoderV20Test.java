/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.values.ComplexValue;
import org.n52.shetland.ogc.swe.SweAbstractDataRecord;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OmDecoderV20Test {

    String getComplexObservationXml() {
        return "<om:OM_Observation \n" +
               "    xmlns:om=\"http://www.opengis.net/om/2.0\"\n" +
               "    xmlns:gml=\"http://www.opengis.net/gml/3.2\"\n" +
               "    xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n" +
               "    xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" +
               "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
               "  <om:type xlink:href=\"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation\"/>\n" +
               "  <om:phenomenonTime>\n" +
               "    <gml:TimeInstant gml:id=\"phenomenonTime\">\n" +
               "      <gml:timePosition>2014-08-13T07:26:56.196Z</gml:timePosition>\n" +
               "    </gml:TimeInstant>\n" +
               "  </om:phenomenonTime>\n" +
               "  <om:resultTime xlink:href=\"#phenomenonTime\"/>\n" +
               "  <om:validTime>\n" +
               "    <gml:TimePeriod gml:id=\"validTime\">\n" +
               "      <gml:beginPosition>2014-08-13T07:21:56.196Z</gml:beginPosition>\n" +
               "      <gml:endPosition>2014-08-13T07:31:56.196Z</gml:endPosition>\n" +
               "    </gml:TimePeriod>\n" +
               "  </om:validTime>\n" +
               "  <om:procedure xlink:href=\"procedure\"/>\n" +
               "  <om:observedProperty xlink:href=\"http://example.tld/phenomenon/parent\"/>\n" +
               "  <om:featureOfInterest xlink:href=\"http://example.tld/feature/1\"/>\n" +
               "  <om:result xsi:type=\"swe:DataRecordPropertyType\">\n" +
               "    <swe:DataRecord>\n" +
               "      <swe:field name=\"child1\">\n" +
               "        <swe:Quantity definition=\"http://example.tld/phenomenon/child/1\">\n" +
               "          <swe:uom code=\"unit\"/>\n" +
               "          <swe:value>42.0</swe:value>\n" +
               "        </swe:Quantity>\n" +
               "      </swe:field>\n" +
               "      <swe:field name=\"child2\">\n" +
               "        <swe:Boolean definition=\"http://example.tld/phenomenon/child/2\">\n" +
               "          <swe:value>true</swe:value>\n" +
               "        </swe:Boolean>\n" +
               "      </swe:field>\n" +
               "      <swe:field name=\"child3\">\n" +
               "        <swe:Count definition=\"http://example.tld/phenomenon/child/3\">\n" +
               "          <swe:value>42</swe:value>\n" +
               "        </swe:Count>\n" +
               "      </swe:field>\n" +
               "      <swe:field name=\"child4\">\n" +
               "        <swe:Text definition=\"http://example.tld/phenomenon/child/4\">\n" +
               "          <swe:value>42</swe:value>\n" +
               "        </swe:Text>\n" +
               "      </swe:field>\n" +
               "      <swe:field name=\"child5\">\n" +
               "        <swe:Category definition=\"http://example.tld/phenomenon/child/5\">\n" +
               "          <swe:codeSpace xlink:href=\"codespace\"/>\n" +
               "          <swe:value>52</swe:value>\n" +
               "        </swe:Category>\n" +
               "      </swe:field>\n" +
               "    </swe:DataRecord>\n" +
               "  </om:result>\n" +
               "</om:OM_Observation>";
    }

    private OmDecoderv20 omDecoderv20;

    @BeforeEach
    public void setup() {
        DecoderRepository decoderRepository = new DecoderRepository();

        omDecoderv20 = new OmDecoderv20();
        omDecoderv20.setDecoderRepository(decoderRepository);
        omDecoderv20.setXmlOptions(XmlOptions::new);

        GmlDecoderv321 gmlDecoderv321 = new GmlDecoderv321();
        gmlDecoderv321.setXmlOptions(XmlOptions::new);
        gmlDecoderv321.setDecoderRepository(decoderRepository);

        SweCommonDecoderV20 sweCommonDecoderV20 = new SweCommonDecoderV20();
        sweCommonDecoderV20.setXmlOptions(XmlOptions::new);
        sweCommonDecoderV20.setDecoderRepository(decoderRepository);

        decoderRepository.setDecoders(Arrays.asList(omDecoderv20, sweCommonDecoderV20, gmlDecoderv321));
        decoderRepository.init();
    }

    @Test
    public void testComplexObservation()
            throws XmlException, DecodingException {
        XmlObject xml = XmlObject.Factory.parse(getComplexObservationXml());

        // FIXME
//        Object decoded = CodingHelper.decodeXmlObject(xml);
        Object decoded = omDecoderv20.decode(xml);

        assertThat(decoded, is(instanceOf(OmObservation.class)));

        OmObservation observation = (OmObservation) decoded;

        assertThat(observation.getValue(), is(instanceOf(SingleObservationValue.class)));
        assertThat(observation.getValue().getValue(), is(instanceOf(ComplexValue.class)));

        ComplexValue value = (ComplexValue) observation.getValue().getValue();

        assertThat(value.getValue(), is(notNullValue()));

        SweAbstractDataRecord dataRecord = value.getValue();

        assertThat(dataRecord.getFields(), hasSize(5));

        SweField field1 = dataRecord.getFields().get(0);
        SweField field2 = dataRecord.getFields().get(1);
        SweField field3 = dataRecord.getFields().get(2);
        SweField field4 = dataRecord.getFields().get(3);
        SweField field5 = dataRecord.getFields().get(4);

        assertThat(field1.getElement().getDefinition(), is("http://example.tld/phenomenon/child/1"));
        assertThat(field2.getElement().getDefinition(), is("http://example.tld/phenomenon/child/2"));
        assertThat(field3.getElement().getDefinition(), is("http://example.tld/phenomenon/child/3"));
        assertThat(field4.getElement().getDefinition(), is("http://example.tld/phenomenon/child/4"));
        assertThat(field5.getElement().getDefinition(), is("http://example.tld/phenomenon/child/5"));

        assertThat(field1.getElement().getDataComponentType(), is(SweDataComponentType.Quantity));
        assertThat(field2.getElement().getDataComponentType(), is(SweDataComponentType.Boolean));
        assertThat(field3.getElement().getDataComponentType(), is(SweDataComponentType.Count));
        assertThat(field4.getElement().getDataComponentType(), is(SweDataComponentType.Text));
        assertThat(field5.getElement().getDataComponentType(), is(SweDataComponentType.Category));

    }
}
