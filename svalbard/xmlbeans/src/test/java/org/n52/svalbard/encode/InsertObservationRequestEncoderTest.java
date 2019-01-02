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

import java.util.Arrays;
import java.util.Date;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.sensorML.v20.PhysicalSystem;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.request.InsertObservationRequest;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JTSHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.n52.svalbard.util.XmlHelper;
import org.locationtech.jts.io.ParseException;

import net.opengis.sos.x20.InsertObservationDocument;
import net.opengis.sos.x20.InsertObservationType;
import net.opengis.swe.x20.BooleanPropertyType;
import net.opengis.swe.x20.BooleanType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertObservationRequestEncoderTest {

    private static final String OFFERING_ID = "test-offering";

    private InsertObservationRequestEncoder encoder;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        encoder = new InsertObservationRequestEncoder();
        encoder.setXmlOptions(XmlOptions::new);

        OmEncoderv20 omEncoder = new OmEncoderv20();
        omEncoder.setXmlOptions(XmlOptions::new);

        GmlEncoderv321 gml321Encoder = new GmlEncoderv321();
        gml321Encoder.setXmlOptions(XmlOptions::new);

        SamplingEncoderv20 samsEncoder = new SamplingEncoderv20();
        samsEncoder.setXmlOptions(XmlOptions::new);

        SensorMLEncoderv20 sml20Encoder = new SensorMLEncoderv20();
        sml20Encoder.setXmlOptions(XmlOptions::new);

        SwesExtensionEncoderv20 swesExtensionEncoder = new SwesExtensionEncoderv20();
        swesExtensionEncoder.setXmlOptions(XmlOptions::new);

        SweCommonEncoderv20 sweCommonEncoder = new SweCommonEncoderv20();
        sweCommonEncoder.setXmlOptions(XmlOptions::new);

        EncoderRepository repo = new EncoderRepository();
        repo.setEncoders(CollectionHelper.list(encoder, omEncoder, gml321Encoder, samsEncoder, sml20Encoder, swesExtensionEncoder, sweCommonEncoder));
        repo.init();

        repo.getEncoders().forEach(e -> ((AbstractDelegatingEncoder<?, ?>)e).setEncoderRepository(repo));
    }

    @Test
    public void shouldThrowExceptionWhenNullValueReceived() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertObservationRequestEncoder.class.getSimpleName() +
                " can not encode 'null'"));

        encoder.create(null);
    }

    @Test
    public void shouldThrowExceptionIfServiceIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertObservationRequestEncoder.class.getSimpleName() +
                " can not encode 'missing service'"));

        encoder.create(new InsertObservationRequest());
    }

    @Test
    public void shouldThrowExceptionIfVersionIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertObservationRequestEncoder.class.getSimpleName() +
                " can not encode 'missing version'"));

        encoder.create(new InsertObservationRequest("SOS", ""));
    }

    @Test
    public void shouldThrowExceptionIfVersionIsNot200Missing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertObservationRequestEncoder.class.getSimpleName() +
                " can not encode 'SOS 1.0.0 insert observation request'"));

        encoder.create(new InsertObservationRequest("SOS", "1.0.0"));
    }

    @Test
    public void shouldThrowExceptionIfOfferingsAreMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertObservationRequestEncoder.class.getSimpleName() +
                " can not encode 'missing offering(s)'"));

        encoder.create(new InsertObservationRequest("SOS", "2.0.0"));
    }

    @Test
    public void shouldThrowExceptionIfObservationsAreMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertObservationRequestEncoder.class.getSimpleName() +
                " can not encode 'missing observation(s)'"));

        InsertObservationRequest request = new InsertObservationRequest("SOS", "2.0.0");
        request.setOfferings(Arrays.asList(OFFERING_ID));
        encoder.create(request);
    }

    @Test
    public void shouldEncodeInsertObservationRequest() throws EncodingException, InvalidSridException, ParseException, DecodingException {
        InsertObservationRequest request = createInsertObservationRequest();

        XmlObject encodedRequest = encoder.create(request);

        XmlHelper.validateDocument(encodedRequest);
        Assert.assertThat(encodedRequest, Matchers.notNullValue());
        Assert.assertThat(encodedRequest, Matchers.instanceOf(InsertObservationDocument.class));
        InsertObservationType insertObservation = ((InsertObservationDocument) encodedRequest).getInsertObservation();
        Assert.assertThat(insertObservation.getOfferingArray(), Matchers.notNullValue());
        Assert.assertThat(insertObservation.getOfferingArray().length, Is.is(1));
        Assert.assertThat(insertObservation.getOfferingArray(0), Is.is(OFFERING_ID));
        Assert.assertThat(insertObservation.getObservationArray(), Matchers.notNullValue());
        Assert.assertThat(insertObservation.getObservationArray().length, Is.is(1));
        // no check for observation values, because that MUST be part of OmEncoderv20Test
    }

    @Test
    public void shouldEncodeSwesExtensions() throws InvalidSridException, ParseException, EncodingException, DecodingException {
        String definition = Sos2Constants.Extensions.SplitDataArrayIntoObservations.name();
        boolean value = true;

        SweBoolean sweBoolean = new SweBoolean();
        sweBoolean.setValue(value);
        sweBoolean.setDefinition(definition);

        SwesExtension<SweBoolean> swesExtension = new SwesExtension<>();
        swesExtension.setValue(sweBoolean);

        InsertObservationRequest request = createInsertObservationRequest();
        request.addExtension(swesExtension);

        XmlObject encodedRequest = encoder.create(request);

        encodedRequest.xmlText();
        XmlHelper.validateDocument(encodedRequest);
        InsertObservationType insertObservation = ((InsertObservationDocument) encodedRequest).getInsertObservation();
        Assert.assertThat(insertObservation.sizeOfExtensionArray(), Is.is(1));
        XmlObject xbExtension = insertObservation.getExtensionArray(0);
        Assert.assertThat(xbExtension, Matchers.instanceOf(BooleanPropertyType.class));
        BooleanType xbBoolean = ((BooleanPropertyType) xbExtension).getBoolean();
        Assert.assertThat(xbBoolean.getDefinition(), Is.is(definition));
        Assert.assertThat(xbBoolean.getValue(), Is.is(value));
        // no check for observation values, because that MUST be part of OmEncoderv20Test
    }

    private InsertObservationRequest createInsertObservationRequest() throws InvalidSridException, ParseException {
        SamplingFeature samplingFeature =
                new SamplingFeature(new CodeWithAuthority("test-feature-uri"));
        samplingFeature.setName(new CodeType("test-feature-name"));
        samplingFeature.setSampledFeatures(Arrays.asList(new SamplingFeature(
                new CodeWithAuthority("test-parent-feature-uri"))));
        samplingFeature.setGeometry(JTSHelper.createGeometryFromWKT("POINT(52.0 42.0)", 4326));

        PhysicalSystem procedure = new PhysicalSystem();
        procedure.setIdentifier("test-procedure");

        OmObservationConstellation observationConstellation = new OmObservationConstellation();
        observationConstellation.setGmlId("o1");
        observationConstellation.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
        observationConstellation.setObservableProperty(new OmObservableProperty("test-property"));
        observationConstellation.setFeatureOfInterest(samplingFeature);
        observationConstellation.setProcedure(procedure);

        TimeInstant time = new TimeInstant(new Date(0));
        QuantityValue quantity = new QuantityValue(23.0, "test-uom");

        ObservationValue<?> value = new SingleObservationValue<>(time, quantity);

        OmObservation omObservation = new OmObservation();
        omObservation.setObservationConstellation(observationConstellation);
        omObservation.setResultTime(time);
        omObservation.setValue(value);

        InsertObservationRequest request = new InsertObservationRequest("SOS", "2.0.0");
        request.setOfferings(Arrays.asList(OFFERING_ID));
        request.addObservation(omObservation);
        return request;
    }

}
