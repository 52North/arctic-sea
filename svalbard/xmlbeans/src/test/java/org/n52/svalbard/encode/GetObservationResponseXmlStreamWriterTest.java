/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.ObservationStream;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.write.GetObservationResponseXmlStreamWriter;

import net.opengis.sos.x20.GetObservationResponseDocument;


public class GetObservationResponseXmlStreamWriterTest extends AbstractMetadataTest {

    private EncoderRepository encoderRepository;

    @BeforeEach
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
            GetObservationResponseXmlStreamWriter encoder = new GetObservationResponseXmlStreamWriter(
                    EncodingContext.of(EncoderFlags.ENCODER_REPOSITORY, encoderRepository), baos,
                    createResponse());
            encoder.write();
            XmlObject encode = XmlObject.Factory.parse(new String(baos.toByteArray()));
            assertThat(encode, instanceOf(GetObservationResponseDocument.class));
            GetObservationResponseDocument gord = (GetObservationResponseDocument) encode;
            assertThat(gord.getGetObservationResponse() != null, is(true));
            checkMetadataResponse(gord.getGetObservationResponse().getExtensionArray());
        }
    }

    private GetObservationResponse createResponse() {
        GetObservationResponse response = new GetObservationResponse();
        response.setService(SosConstants.SOS);
        response.setVersion(Sos2Constants.SERVICEVERSION);
        response.setResponseFormat(OmConstants.NS_OM_2);
        OmObservation obs = new OmObservation();

        OmObservationConstellation obsConst = new OmObservationConstellation();
        obsConst.setProcedure(new SosProcedureDescriptionUnknownType("procedure", SensorMLConstants.NS_SML, null));
        OmObservableProperty omObservableProperty = new OmObservableProperty("observable_property");
        omObservableProperty.setUnit("°C");
        obsConst.setObservableProperty(omObservableProperty);
        obsConst.setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority("feature")));
        obsConst.setObservationType( OmConstants.OBS_TYPE_MEASUREMENT);
        obsConst.addOffering("offering");
        obs.setObservationConstellation(obsConst);

        obs.setResultTime(new TimeInstant(DateTime.now()));
        SingleObservationValue<BigDecimal> obsVal = new SingleObservationValue<BigDecimal>();
        obsVal.setPhenomenonTime(new TimeInstant(DateTime.now()));
        obsVal.setValue(new QuantityValue(Double.valueOf("52.7"), "°C"));
        obs.setValue(obsVal);
        response.setObservationCollection(ObservationStream.of(obs));

        Extensions swesExtensions = new Extensions();
        swesExtensions.addExtension(createExtension());
        response.setExtensions(swesExtensions);
        return response;
    }
}
