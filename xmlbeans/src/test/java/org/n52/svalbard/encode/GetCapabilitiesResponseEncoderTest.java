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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.n52.shetland.ogc.ows.service.GetCapabilitiesResponse;
import org.n52.svalbard.encode.exception.EncodingException;

public class GetCapabilitiesResponseEncoderTest {

//    private GetCapabilitiesResponseEncoder encoder;
//    @Rule
//    public final ExpectedException expectedEx = ExpectedException.none();
//
//    @Before
//    public void setUp() {
//        EncoderRepository encoderRepository = new EncoderRepository();
//        SchemaRepository schemaRepository = new SchemaRepository();
//
//        encoder = new GetCapabilitiesResponseEncoder();
//        encoder.setXmlOptions(XmlOptions::new);
//        encoder.setEncoderRepository(encoderRepository);
//        encoder.setSchemaRepository(schemaRepository);
//
//        encoderRepository.setEncoders(Arrays.asList(encoder));
//        encoderRepository.init();
//        schemaRepository.setEncoderRepository(encoderRepository);
//        schemaRepository.init();
//    }
//
//    @Test
//    public void should_create_static_capabilities() throws Exception {
//        XmlObject encodedResponse = encoder.encode(minimalCapabilities());
//
//        Diff d = new Diff(encodedResponse.xmlText(), minimalCapabilities().getXmlString());
//
//        assertThat(d.identical(), is(true));
//        assertThat(d.similar(), is(true));
//    }
//
//    @Test
//    public void should_throw_Exception_when_static_content_is_invalid() throws Exception {
//        expectedEx.expect(EncodingException.class);
//        expectedEx.expectMessage("Error encoding static capabilities");
//        encoder.encode(badCapabilities());
//    }
//
//    private GetCapabilitiesResponse minimalCapabilities() {
//        GetCapabilitiesResponse response = new GetCapabilitiesResponse();
//        response.setService("SOS");
//        response.setVersion("2.0.0");
//        response.setXmlString("<sos:Capabilities version=\"2.0.0\" xmlns:sos=\"http://www.opengis.net/sos/2.0\" " +
//                              "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
//                              "xsi:schemaLocation=\"http://www.opengis.net/sos/2.0 http://schemas.opengis.net/sos/2.0/sosGetCapabilities.xsd\"/>");
//        return response;
//    }
//
//    private GetCapabilitiesResponse badCapabilities() {
//        GetCapabilitiesResponse response = new GetCapabilitiesResponse();
//        response.setService("SOS");
//        response.setVersion("2.0.0");
//        response.setXmlString("BAD XML STRING");
//        return response;
//    }
}
