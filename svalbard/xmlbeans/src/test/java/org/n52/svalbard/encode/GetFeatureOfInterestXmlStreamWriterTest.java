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
package org.n52.svalbard.encode;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.shetland.ogc.swes.SwesExtensions;
import org.n52.svalbard.encode.AbstractDelegatingEncoder;
import org.n52.svalbard.encode.EncoderFlags;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.GmlEncoderv321;
import org.n52.svalbard.encode.OmEncoderv20;
import org.n52.svalbard.encode.SamplingEncoderv20;
import org.n52.svalbard.encode.SchemaRepository;
import org.n52.svalbard.encode.SensorMLEncoderv20;
import org.n52.svalbard.encode.SweCommonEncoderv20;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.write.GetFeatureOfInterestXmlStreamWriter;

import net.opengis.sos.x20.GetFeatureOfInterestResponseDocument;


public class GetFeatureOfInterestXmlStreamWriterTest extends AbstractMetadataTest {
    private EncoderRepository encoderRepository;

    @Before
    public void init() {
        encoderRepository = new EncoderRepository();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);
        schemaRepository.init();

        OmEncoderv20 omEncoderv20 = new OmEncoderv20();
        omEncoderv20.setXmlOptions(XmlOptions::new);

        GmlEncoderv321 gmlEncoderv321 = new GmlEncoderv321();
        gmlEncoderv321.setXmlOptions(XmlOptions::new);

        SensorMLEncoderv20 sensorMLEncoderv20 = new SensorMLEncoderv20();
        sensorMLEncoderv20.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv20 sweCommonEncoderv20 = new SweCommonEncoderv20();
        sweCommonEncoderv20.setXmlOptions(XmlOptions::new);

        SamplingEncoderv20 samsEncoderv20 = new SamplingEncoderv20();
        samsEncoderv20.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv20 sweEncoderv20 = new SweCommonEncoderv20();
        sweEncoderv20.setXmlOptions(XmlOptions::new);
        encoderRepository.setEncoders(Arrays.asList(
                omEncoderv20,
                gmlEncoderv321,
                sensorMLEncoderv20,
                sweCommonEncoderv20,
                samsEncoderv20,
                sweEncoderv20));
        encoderRepository.init();

        encoderRepository.getEncoders().stream()
            .forEach(e -> ((AbstractDelegatingEncoder<?,?>)e).setEncoderRepository(encoderRepository));
    }

    @Test
    public void testMetadataEncoding() throws XmlException, XMLStreamException, IOException, EncodingException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            GetFeatureOfInterestXmlStreamWriter encoder = new GetFeatureOfInterestXmlStreamWriter(baos,
                    EncodingContext.of(EncoderFlags.ENCODER_REPOSITORY, encoderRepository), createResponse());
            encoder.write();
            XmlObject encode = XmlObject.Factory.parse(new String(baos.toByteArray()));
            assertThat(encode, instanceOf(GetFeatureOfInterestResponseDocument.class));
            GetFeatureOfInterestResponseDocument gord = (GetFeatureOfInterestResponseDocument) encode;
            assertThat(gord.getGetFeatureOfInterestResponse() != null, is(true));
            checkMetadataResponse(gord.getGetFeatureOfInterestResponse().getExtensionArray());
        }
    }

    private GetFeatureOfInterestResponse createResponse() {
        GetFeatureOfInterestResponse response = new GetFeatureOfInterestResponse();
        response.setService(SosConstants.SOS);
        response.setVersion(Sos2Constants.SERVICEVERSION);

        SwesExtensions swesExtensions = new SwesExtensions();
        swesExtensions.addExtension(createExtension());
        response.setExtensions(swesExtensions);
        return response;
    }

}
