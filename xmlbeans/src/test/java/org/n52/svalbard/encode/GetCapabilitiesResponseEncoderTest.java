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

import java.io.IOException;

import org.apache.xmlbeans.XmlObject;
import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXException;
import org.n52.svalbard.decode.exception.XmlDecodingException;
import org.n52.svalbard.encode.GetCapabilitiesResponseEncoder;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesResponse;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;


public class GetCapabilitiesResponseEncoderTest {

    private GetCapabilitiesResponseEncoder encoder;

    @Before
    public void setUp(){
        encoder = new GetCapabilitiesResponseEncoder();
    }

    @Test public void
    should_create_static_capabilities()
            throws OwsExceptionReport, SAXException, IOException, EncodingException {
        XmlObject encodedResponse = encoder.encode(minimalCapabilities());

        Diff d = new Diff (encodedResponse.xmlText(), minimalCapabilities().getXmlString());

        assertThat(d.identical(), is(true));
        assertThat(d.similar(), is(true));
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test public void
    should_throw_Exception_when_static_content_is_invalid()
        throws OwsExceptionReport, EncodingException {
        expectedEx.expect(XmlDecodingException.class);
        expectedEx.expectMessage("Error while decoding Static Capabilities:\nBAD XML STRING");

        encoder.encode(badCapabilities());
    }

    private GetCapabilitiesResponse minimalCapabilities() {
        GetCapabilitiesResponse response = new GetCapabilitiesResponse();
        response.setService("SOS");
        response.setVersion("2.0.0");
        response.setXmlString("<sos:Capabilities version=\"2.0.0\" xmlns:sos=\"http://www.opengis.net/sos/2.0\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xsi:schemaLocation=\"http://www.opengis.net/sos/2.0 http://schemas.opengis.net/sos/2.0/sosGetCapabilities.xsd\"/>");
        return response;
    }

    private GetCapabilitiesResponse badCapabilities() {
        GetCapabilitiesResponse response = new GetCapabilitiesResponse();
        response.setService("SOS");
        response.setVersion("2.0.0");
        response.setXmlString("BAD XML STRING");
        return response;
    }
}
