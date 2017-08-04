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

import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.n52.janmayen.Producer;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.shetland.ogc.sos.request.GetObservationRequest;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.swe.simpleType.SweText;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sos.x20.GetObservationDocument;
import net.opengis.sos.x20.InsertResultTemplateDocument;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SosDecoderv20Test {

    private SosDecoderv20 decoder;

    @Before
    public void initDecoder() {
        DecoderRepository decoderRepository = new DecoderRepository();
        Producer<XmlOptions> options = () -> new XmlOptions();

        SosDecoderv20 sosDecoderv20 = new SosDecoderv20();
        sosDecoderv20.setDecoderRepository(decoderRepository);
        sosDecoderv20.setXmlOptions(options);

        SwesExtensionDecoderv20 swesExtensionDecoderv20 = new SwesExtensionDecoderv20();
        swesExtensionDecoderv20.setDecoderRepository(decoderRepository);
        swesExtensionDecoderv20.setXmlOptions(options);

        SweCommonDecoderV20 sweCommonDecoderV20 = new SweCommonDecoderV20();
        sweCommonDecoderV20.setDecoderRepository(decoderRepository);
        sweCommonDecoderV20.setXmlOptions(options);

        decoderRepository.setDecoders(Arrays.asList(sweCommonDecoderV20, swesExtensionDecoderv20, sosDecoderv20));

        decoderRepository.init();

        decoder = sosDecoderv20;
    }

    @After
    public void nullDecoder() {
        decoder = null;
    }

    @Test
    public void should_decode_boolean_swesExtensions()
            throws XmlException, OwsExceptionReport, DecodingException {
        final GetObservationDocument doc =
                GetObservationDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<sos:GetObservation service=\"SOS\" version=\"2.0.0\"\n"
                        + "    xmlns:sos=\"http://www.opengis.net/sos/2.0\"\n"
                        + "    xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n"
                        + "    xmlns:swes=\"http://www.opengis.net/swes/2.0\">\n" + "    <swes:extension>\n"
                        + "        <swe:Boolean definition=\"MergeObservationsIntoDataArray\">\n"
                        + "            <swe:value>true</swe:value>\n" + "        </swe:Boolean>\n"
                        + "    </swes:extension>\n" + "</sos:GetObservation>");

        final OwsServiceCommunicationObject decodedObject = decoder.decode(doc);

        assertThat(decodedObject, instanceOf(GetObservationRequest.class));

        final GetObservationRequest request = (GetObservationRequest) decodedObject;
        assertThat(request.getBooleanExtension("MergeObservationsIntoDataArray"), is(TRUE));
    }

    @Test
    public void should_decode_text_swesExtensions()
            throws XmlException, OwsExceptionReport, DecodingException {
        final GetObservationDocument doc =
                GetObservationDocument.Factory.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<sos:GetObservation service=\"SOS\" version=\"2.0.0\"\n"
                        + "    xmlns:sos=\"http://www.opengis.net/sos/2.0\"\n"
                        + "    xmlns:swe=\"http://www.opengis.net/swe/2.0\"\n"
                        + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n"
                        + "    xmlns:swes=\"http://www.opengis.net/swes/2.0\"\n"
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/sos/2.0 http://schemas.opengis.net/sos/2.0/sos.xsd\">\n"
                        + "    <swes:extension>\n" + "        <swe:Text definition=\"my-text-extension\">\n"
                        + "            <swe:value>true</swe:value>\n" + "        </swe:Text>\n"
                        + "    </swes:extension>\n" + "</sos:GetObservation>");

        final OwsServiceCommunicationObject decodedObject = decoder.decode(doc);

        assertThat(decodedObject, instanceOf(GetObservationRequest.class));

        final GetObservationRequest request = (GetObservationRequest) decodedObject;
        assertThat(request.getExtension("my-text-extension").map(e -> e.getValue()).map(v -> (SweText) v)
                .map(v -> v.getValue()).orElse(null), is("true"));

    }

}