/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import java.util.Arrays;
import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.sensorML.SensorML20Constants;
import org.n52.shetland.ogc.sensorML.v20.PhysicalSystem;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import net.opengis.sos.x20.SosInsertionMetadataType;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType.ProcedureDescription;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertSensorRequestEncoderTest {

    private InsertSensorRequest request;
    private InsertSensorRequestEncoder encoder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void prepare() {
        request = new InsertSensorRequest("service", "version");
        request.setProcedureDescriptionFormat(SensorML20Constants.SENSORML_20_OUTPUT_FORMAT_URL);
        request.setProcedureDescription(createProcedureDescription());
        SosInsertionMetadata metadata = new SosInsertionMetadata();
        metadata.setFeatureOfInterestTypes(CollectionHelper.list("test-foi-type-1", "test-foi-type-2"));
        metadata.setObservationTypes(CollectionHelper.list("test-observation-type-1", "test-observation-type-2"));
        request.setMetadata(metadata);
        request.setObservableProperty(CollectionHelper.list("test-property-1", "test-property-2"));

        encoder = new InsertSensorRequestEncoder();
        encoder.setXmlOptions(() -> new XmlOptions());

        SensorMLEncoderv20 sensorMLEncoderv20 = new SensorMLEncoderv20();
        sensorMLEncoderv20.setXmlOptions(() -> new XmlOptions());

        GmlEncoderv321 gmlEncoder = new GmlEncoderv321();
        gmlEncoder.setXmlOptions(() -> new XmlOptions());

        SosInsertionMetadataTypeEncoder metadataEncoder = new SosInsertionMetadataTypeEncoder();
        metadataEncoder.setXmlOptions(() -> new XmlOptions());

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(encoder, sensorMLEncoderv20, gmlEncoder, metadataEncoder));
        encoderRepository.init();

        encoder.setEncoderRepository(encoderRepository);
        sensorMLEncoderv20.setEncoderRepository(encoderRepository);
        gmlEncoder.setEncoderRepository(encoderRepository);
        metadataEncoder.setEncoderRepository(encoderRepository);
    }

    private SosProcedureDescription<PhysicalSystem> createProcedureDescription() {
        PhysicalSystem description = new PhysicalSystem();
        description.setIdentifier("test-identifier");
        return new SosProcedureDescription<>(description);
    }

    @Test
    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertSensorRequestEncoder.class.getSimpleName() +
                " can not encode 'null'"));
        encoder.create(null);
    }

    @Test
    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertSensorRequestEncoder.class.getSimpleName() +
                " can not encode 'missing service'"));

        encoder.create(new InsertSensorRequest());
    }

    @Test
    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertSensorRequestEncoder.class.getSimpleName() +
                " can not encode 'missing version'"));

        encoder.create(new InsertSensorRequest("service", ""));
    }

    @Test
    public void shouldThrowExceptionWhenProcedureDescriptionFormatIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertSensorRequestEncoder.class.getSimpleName() +
                " can not encode 'procedure description format missing'"));
        encoder.create(new InsertSensorRequest("service", "version"));
    }

    @Test
    public void shouldThrowExceptionWhenProcedureDescriptionIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertSensorRequestEncoder.class.getSimpleName() +
                " can not encode 'procedure description missing'"));
        InsertSensorRequest request = new InsertSensorRequest("service", "version");
        request.setProcedureDescriptionFormat("test-format");
        encoder.create(request);
    }

    @Test
    public void shouldThrowExceptionWhenObservablePropertyIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertSensorRequestEncoder.class.getSimpleName() +
                " can not encode 'observed property missing'"));
        InsertSensorRequest request = new InsertSensorRequest("service", "version");
        request.setProcedureDescriptionFormat("test-format");
        request.setProcedureDescription(createProcedureDescription());
        encoder.create(request);
    }

    @Test
    public void shouldThrowExceptionWhenMetadataIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertSensorRequestEncoder.class.getSimpleName() +
                " can not encode 'metadata field missing'"));
        InsertSensorRequest request = new InsertSensorRequest("service", "version");
        request.setProcedureDescriptionFormat("test-format");
        request.setProcedureDescription(createProcedureDescription());
        request.setObservableProperty(CollectionHelper.list("test-property"));
        encoder.create(request);
    }

    @Test
    public void shouldEncodeProcedureDescriptionFormat() throws EncodingException {
        XmlObject xmlObject = encoder.create(request);
        Assert.assertThat(xmlObject, CoreMatchers.instanceOf(InsertSensorDocument.class));
        Assert.assertThat(((InsertSensorDocument)xmlObject).getInsertSensor().getProcedureDescriptionFormat(),
                Is.is(SensorML20Constants.SENSORML_20_OUTPUT_FORMAT_URL));
    }

    @Test
    public void shouldEncodeObservableProperty() throws EncodingException {
        XmlObject xmlObject = encoder.create(request);
        Assert.assertThat(xmlObject, CoreMatchers.instanceOf(InsertSensorDocument.class));
        List<String> observableProperties =
                Arrays.asList(((InsertSensorDocument)xmlObject).getInsertSensor().getObservablePropertyArray());
        Assert.assertThat(observableProperties, Matchers.hasSize(2));
        Assert.assertThat(observableProperties, Matchers.containsInAnyOrder("test-property-1", "test-property-2"));
    }

    @Test
    public void shouldEncodeSosInsertionMetadata() throws EncodingException {
        InsertSensorDocument isd = (InsertSensorDocument) encoder.create(request);

        Assert.assertThat(isd.getInsertSensor().getMetadataArray().length, Is.is(1));
        Assert.assertThat(isd.getInsertSensor().getMetadataArray(0).getInsertionMetadata(),
                CoreMatchers.instanceOf(SosInsertionMetadataType.class));
        SosInsertionMetadataType insertionMetadata =
                (SosInsertionMetadataType) isd.getInsertSensor().getMetadataArray(0).getInsertionMetadata();
        Assert.assertThat(insertionMetadata.getFeatureOfInterestTypeArray(), CoreMatchers.notNullValue());
        List<String> foiTypes = Arrays.asList(insertionMetadata.getFeatureOfInterestTypeArray());
        Assert.assertThat(foiTypes, Matchers.hasSize(2));
        Assert.assertThat(foiTypes, Matchers.containsInAnyOrder("test-foi-type-1", "test-foi-type-2"));
        Assert.assertThat(insertionMetadata.getObservationTypeArray(), CoreMatchers.notNullValue());
        List<String> oTypes = Arrays.asList(insertionMetadata.getObservationTypeArray());
        Assert.assertThat(oTypes, Matchers.hasSize(2));
        Assert.assertThat(oTypes, Matchers.containsInAnyOrder("test-observation-type-1", "test-observation-type-2"));
    }

    @Test
    public void shouldEncodeProcedureDescription() throws EncodingException {
        InsertSensorDocument isd = (InsertSensorDocument) encoder.create(request);
        ProcedureDescription description = isd.getInsertSensor().getProcedureDescription();
        Assert.assertThat(description, CoreMatchers.notNullValue());
        Assert.assertThat(description, CoreMatchers.instanceOf(ProcedureDescription.class));
    }

}
