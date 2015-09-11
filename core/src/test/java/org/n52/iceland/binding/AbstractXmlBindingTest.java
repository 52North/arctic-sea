/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

import org.junit.Test;
import org.n52.iceland.coding.decode.DecoderKey;
import org.n52.iceland.coding.decode.OperationDecoderKey;
import org.n52.iceland.coding.decode.XmlNamespaceOperationDecoderKey;
import org.n52.iceland.exception.CodedException;
import org.n52.iceland.w3c.soap.SoapConstants;

public class AbstractXmlBindingTest {

    private TestXmlBinding binding = new TestXmlBinding();
    
    private String characterEncoding = "UTF-8";

    private String xmlStringGetObs =
            new StringBuilder()
                    .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                    .append("<sos:GetObservation service=\"SOS\" version=\"2.0.0\"")
                    .append(" xmlns:sos=\"http://www.opengis.net/sos/2.0\"")
                    .append(" xmlns:fes=\"http://www.opengis.net/fes/2.0\"")
                    .append(" xmlns:gml=\"http://www.opengis.net/gml/3.2\"")
                    .append(" xmlns:swe=\"http://www.opengis.net/swe/2.0\"")
                    .append(" xmlns:xlink=\"http://www.w3.org/1999/xlink\"")
                    .append(" xmlns:swes=\"http://www.opengis.net/swes/2.0\"")
                    .append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"")
                    .append(" xsi:schemaLocation=\"http://www.opengis.net/sos/2.0 http://schemas.opengis.net/sos/2.0/sos.xsd\">")
                    .append("</sos:GetObservation>").toString();

    private String xmlStringGetCaps =
            new StringBuilder()
                    .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                    .append("<sos:GetCapabilities service=\"SOS\" version=\"2.0.0\"")
                    .append(" xmlns:sos=\"http://www.opengis.net/sos/2.0\"")
                    .append(" xsi:schemaLocation=\"http://www.opengis.net/sos/2.0 http://schemas.opengis.net/sos/2.0/sos.xsd\">")
                    .append("</sos:GetCapabilities>").toString();
    
    private String xmlStringSoapPrefix= new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<env:Envelope xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\">").append("<env:Body/>")
            .append("</env:Envelope>").toString();
    
    private String xmlStringSoapNoPrefix = new StringBuilder().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">").append("<env:Body/>")
            .append("</Envelope>").toString();

    @Test
    public void test_SoapPrefix() throws CodedException {
        DecoderKey decoderKey = binding.getDecoderKey(xmlStringSoapPrefix, characterEncoding);
        assertTrue(decoderKey instanceof XmlNamespaceOperationDecoderKey);
        assertTrue(SoapConstants.NS_SOAP_12.equals(((XmlNamespaceOperationDecoderKey)decoderKey).getNamespace()));
    }
    
    @Test
    public void test_SoapNoPrefix() throws CodedException {
        DecoderKey decoderKey = binding.getDecoderKey(xmlStringSoapNoPrefix, characterEncoding);
        assertTrue(decoderKey instanceof XmlNamespaceOperationDecoderKey);
        assertTrue(SoapConstants.NS_SOAP_12.equals(((XmlNamespaceOperationDecoderKey)decoderKey).getNamespace()));
    }

    @Test
    public void test_GetObs() throws CodedException {
        DecoderKey decoderKey = binding.getDecoderKey(xmlStringGetObs, characterEncoding);
        if (decoderKey instanceof OperationDecoderKey) {
            assertThat(((OperationDecoderKey) decoderKey).getService(), is("SOS"));
            assertThat(((OperationDecoderKey) decoderKey).getVersion(), is("2.0.0"));
            assertThat(((OperationDecoderKey) decoderKey).getOperation(), is("GetObservation"));
        }
    }

    @Test
    public void test_GetCaps() throws CodedException {
        DecoderKey decoderKey = binding.getDecoderKey(xmlStringGetCaps, characterEncoding);
        if (decoderKey instanceof OperationDecoderKey) {
            assertThat(((OperationDecoderKey) decoderKey).getService(), is("SOS"));
            assertThat(((OperationDecoderKey) decoderKey).getOperation(), is("GetCapabilities"));
        }
    }

}
