/*
 * Copyright (C) 2015-2026 52°North Spatial Information Research GmbH
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
        return """
               <om:OM_Observation\s
                   xmlns:om="http://www.opengis.net/om/2.0"
                   xmlns:gml="http://www.opengis.net/gml/3.2"
                   xmlns:swe="http://www.opengis.net/swe/2.0"
                   xmlns:xlink="http://www.w3.org/1999/xlink"
                   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                 <om:type xlink:href="http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_ComplexObservation"/>
                 <om:phenomenonTime>
                   <gml:TimeInstant gml:id="phenomenonTime">
                     <gml:timePosition>2014-08-13T07:26:56.196Z</gml:timePosition>
                   </gml:TimeInstant>
                 </om:phenomenonTime>
                 <om:resultTime xlink:href="#phenomenonTime"/>
                 <om:validTime>
                   <gml:TimePeriod gml:id="validTime">
                     <gml:beginPosition>2014-08-13T07:21:56.196Z</gml:beginPosition>
                     <gml:endPosition>2014-08-13T07:31:56.196Z</gml:endPosition>
                   </gml:TimePeriod>
                 </om:validTime>
                 <om:procedure xlink:href="procedure"/>
                 <om:observedProperty xlink:href="http://example.tld/phenomenon/parent"/>
                 <om:featureOfInterest xlink:href="http://example.tld/feature/1"/>
                 <om:result xsi:type="swe:DataRecordPropertyType">
                   <swe:DataRecord>
                     <swe:field name="child1">
                       <swe:Quantity definition="http://example.tld/phenomenon/child/1">
                         <swe:uom code="unit"/>
                         <swe:value>42.0</swe:value>
                       </swe:Quantity>
                     </swe:field>
                     <swe:field name="child2">
                       <swe:Boolean definition="http://example.tld/phenomenon/child/2">
                         <swe:value>true</swe:value>
                       </swe:Boolean>
                     </swe:field>
                     <swe:field name="child3">
                       <swe:Count definition="http://example.tld/phenomenon/child/3">
                         <swe:value>42</swe:value>
                       </swe:Count>
                     </swe:field>
                     <swe:field name="child4">
                       <swe:Text definition="http://example.tld/phenomenon/child/4">
                         <swe:value>42</swe:value>
                       </swe:Text>
                     </swe:field>
                     <swe:field name="child5">
                       <swe:Category definition="http://example.tld/phenomenon/child/5">
                         <swe:codeSpace xlink:href="codespace"/>
                         <swe:value>52</swe:value>
                       </swe:Category>
                     </swe:field>
                   </swe:DataRecord>
                 </om:result>
               </om:OM_Observation>""";
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
