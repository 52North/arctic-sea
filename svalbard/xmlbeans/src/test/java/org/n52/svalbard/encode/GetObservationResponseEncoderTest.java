/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.SweSimpleDataRecord;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.ogc.swes.SwesExtensions;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.SweHelper;

import net.opengis.sos.x20.GetObservationResponseDocument;
import net.opengis.swe.x20.DataArrayPropertyType;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataRecordType;

public class GetObservationResponseEncoderTest {

    private GetObservationResponseEncoder encoder = new GetObservationResponseEncoder();

    @Before
    public void init() {
        encoder = new GetObservationResponseEncoder();
        EncoderRepository encoderRepository = new EncoderRepository();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);
        schemaRepository.init();

        encoder.setEncoderRepository(encoderRepository);
        encoder.setXmlOptions(XmlOptions::new);
        encoder.setSchemaRepository(schemaRepository);
        encoder.setValidate(true);

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
        encoderRepository.setEncoders(Arrays.asList(encoder,
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
    public void testMetadataEncoding() throws EncodingException, XmlException {
        XmlObject encode = encoder.encode(createResponse());
        assertThat(encode, instanceOf(GetObservationResponseDocument.class));
        GetObservationResponseDocument gord = (GetObservationResponseDocument) encode;
        assertThat(gord.getGetObservationResponse() != null, is(true));
        assertThat(gord.getGetObservationResponse().getExtensionArray() != null, is(true));
        assertThat(gord.getGetObservationResponse().getExtensionArray().length, is(1));
        XmlObject parse = XmlObject.Factory.parse(gord.getGetObservationResponse().getExtensionArray(0).xmlText());
        assertThat(parse, instanceOf(DataArrayPropertyType.class));
        DataArrayPropertyType dad = (DataArrayPropertyType) parse;
        assertThat(dad.getDataArray1(), instanceOf(DataArrayType.class));
        DataArrayType dat = dad.getDataArray1();
        assertThat(dat.getElementType().isSetAbstractDataComponent(), is (true));
        assertThat(dat.getElementType().getAbstractDataComponent(), instanceOf(DataRecordType.class));
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

        SwesExtensions swesExtensions = new SwesExtensions();
        swesExtensions.addExtension(createExtension());
        response.setExtensions(swesExtensions);
        return response;
    }

    private Extension<SweDataArray> createExtension() {
        SweDataArray sweDataArray = new SweDataArray();
        sweDataArray.setElementCount(new SweCount().setValue(2));
        sweDataArray.setEncoding(new SweHelper().createTextEncoding(";", ",", "."));

        SweSimpleDataRecord dataRecord = new SweSimpleDataRecord();
        dataRecord.setDefinition("Components");
        dataRecord.addField(new SweField("test_id"));
        dataRecord.addField(new SweField("test_code"));
        dataRecord.addField(new SweField("test_desc"));
        sweDataArray.setElementType(dataRecord);

        LinkedList<List<String>> values = new LinkedList<List<String>>();
        List<String> blockOfTokens_1 = new LinkedList<>();
        blockOfTokens_1.add("1");
        blockOfTokens_1.add("code_1");
        blockOfTokens_1.add("desc_1");
        values.add(blockOfTokens_1);
        List<String> blockOfTokens_2 = new LinkedList<>();
        blockOfTokens_2.add("2");
        blockOfTokens_2.add("code_2");
        blockOfTokens_2.add("desc_2");
        values.add(blockOfTokens_2);

        sweDataArray.setValues(values);
        return new SwesExtension<SweDataArray>().setValue(sweDataArray).setIdentifier("test")
                .setDefinition("test");
    }
}
