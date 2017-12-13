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
package org.n52.svalbard.encode;

import static org.hamcrest.Matchers.is;

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityResponse.DataAvailability;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetDataAvailabilityXmlEncoderTest {
    @Rule
    public final ErrorCollector errors = new ErrorCollector();
    private GetDataAvailabilityXmlEncoder encoder;

    @Before
    public void init() {
        encoder = new GetDataAvailabilityXmlEncoder();
        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(encoder));
        encoderRepository.init();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);
        schemaRepository.init();

        encoder.setEncoderRepository(encoderRepository);
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setSchemaRepository(schemaRepository);
        encoder.setValidate(true);
    }

    @Test
    public void test() throws EncodingException {

        GetDataAvailabilityResponse response = new GetDataAvailabilityResponse("SOS", "2.0.0");
        response.setNamespace(GetDataAvailabilityConstants.NS_GDA);
        DateTime begin = DateTime.now().withZone(DateTimeZone.UTC);
        DateTime end = begin.plusHours(1).withZone(DateTimeZone.UTC);
        TimePeriod timePeriod = new TimePeriod(begin, end);
        ReferenceType procedure = new ReferenceType("procedure");
        ReferenceType observedProperty = new ReferenceType("observedProperty");
        ReferenceType featureOfInterest = new ReferenceType("featureOfInterest");
        ReferenceType offering = new ReferenceType("offering");
        int count = 100;

        response.addDataAvailability(new DataAvailability(procedure, observedProperty,
                                                          featureOfInterest, offering, timePeriod, count));
        XmlObject encoded = encoder.encode(response);

        System.out.println(encoded.xmlText());

        errors.checkThat(encoded.xmlText(), is(String
                         .format("<gda:GetDataAvailabilityResponse xsi:schemaLocation=\"http://www.opengis.net/sosgda/1.0 http://waterml2.org/schemas/gda/1.0/gda.xsd\" " +
                                 "xmlns:gda=\"http://www.opengis.net/sosgda/1.0\" " +
                                 "xmlns:gml=\"http://www.opengis.net/gml/3.2\" " +
                                 "xmlns:swe=\"http://www.opengis.net/swe/2.0\" " +
                                 "xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                                 "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                                 "  <gda:dataAvailabilityMember gml:id=\"dam_1\">\n" +
                                 "    <gda:procedure xlink:href=\"procedure\" xlink:title=\"procedure\"/>\n" +
                                 "    <gda:observedProperty xlink:href=\"observedProperty\" xlink:title=\"observedProperty\"/>\n" +
                                 "    <gda:featureOfInterest xlink:href=\"featureOfInterest\" xlink:title=\"featureOfInterest\"/>\n" +
                                 "    <gda:phenomenonTime>\n" +
                                 "      <gml:TimePeriod gml:id=\"tp_1\">\n" +
                                 "        <gml:beginPosition>%s</gml:beginPosition>\n" +
                                 "        <gml:endPosition>%s</gml:endPosition>\n" +
                                 "      </gml:TimePeriod>\n" +
                                 "    </gda:phenomenonTime>\n" +
                                 "    <gda:count>100</gda:count>\n" +
                                 "    <gda:extension>\n" +
                                 "      <swe:Text definition=\"offering\">\n" +
                                 "        <swe:value>offering</swe:value>\n" +
                                 "      </swe:Text>\n" +
                                 "    </gda:extension>\n" +
                                 "  </gda:dataAvailabilityMember>\n" +
                                 "</gda:GetDataAvailabilityResponse>",
                                 begin.toString(ISODateTimeFormat.dateTime()),
                                 end.toString(ISODateTimeFormat.dateTime()))));

    }

}
