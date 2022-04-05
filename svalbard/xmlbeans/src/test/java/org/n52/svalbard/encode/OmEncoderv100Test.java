/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.sos.response.GetObservationResponse;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.SweSimpleDataRecord;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.SweHelper;

import net.opengis.om.x10.ObservationCollectionDocument;
import net.opengis.swe.x101.DataArrayDocument;
import net.opengis.swe.x101.DataArrayType;
import net.opengis.swe.x101.SimpleDataRecordType;

public class OmEncoderv100Test {

    private OmEncoderv100 encoder = new OmEncoderv100();


    @BeforeEach
    public void setup() {

        encoder = new OmEncoderv100();
        encoder.setXmlOptions(XmlOptions::new);

        GmlEncoderv311 gmlEncoderv311 = new GmlEncoderv311();
        gmlEncoderv311.setXmlOptions(XmlOptions::new);

        SensorMLEncoderv101 sensorMLEncoderv20 = new SensorMLEncoderv101();
        sensorMLEncoderv20.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv101 sweCommonEncoderv20 = new SweCommonEncoderv101();
        sweCommonEncoderv20.setXmlOptions(XmlOptions::new);

        SamplingEncoderv100 samsEncoderv20 = new SamplingEncoderv100();
        samsEncoderv20.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv101 sweEncoderv20 = new SweCommonEncoderv101();
        sweEncoderv20.setXmlOptions(XmlOptions::new);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(encoder,
                gmlEncoderv311,
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
        assertThat(encode, instanceOf(ObservationCollectionDocument.class));
        ObservationCollectionDocument ocd = (ObservationCollectionDocument) encode;
        assertThat(ocd.getObservationCollection().getMetaDataPropertyArray().length, is(1));
        XmlObject parse = XmlObject.Factory.parse(ocd.getObservationCollection().getMetaDataPropertyArray(0).xmlText());
        assertThat(parse, instanceOf(DataArrayDocument.class));
        DataArrayDocument dad = (DataArrayDocument) parse;
        assertThat(dad.getAbstractDataArray1(), instanceOf(DataArrayType.class));
        DataArrayType dat = (DataArrayType) dad.getAbstractDataArray1();
        assertThat(dat.getElementType().isSetAbstractDataRecord(), is (true));
        assertThat(dat.getElementType().getAbstractDataRecord(), instanceOf(SimpleDataRecordType.class));

    }

    private GetObservationResponse createResponse() {
        GetObservationResponse response = new GetObservationResponse();
        response.setService(SosConstants.SOS);
        response.setVersion(Sos1Constants.SERVICEVERSION);
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
