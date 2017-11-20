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

import java.util.Arrays;
import java.util.function.Supplier;

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
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.sensorML.SensorML;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;
import org.n52.shetland.ogc.sos.request.InsertResultTemplateRequest;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.util.JTSHelper;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.vividsolutions.jts.io.ParseException;

import net.opengis.om.x20.OMObservationType;
import net.opengis.sampling.x20.SFSamplingFeatureType;
import net.opengis.sos.x20.InsertResultTemplateDocument;
import net.opengis.sos.x20.InsertResultTemplateType;
import net.opengis.sos.x20.ResultTemplateType;
import net.opengis.sos.x20.ResultTemplateType.ObservationTemplate;

public class InsertResultTemplateRequestEncoderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String templateIdentifier = "test-template-identifier";

    private String offering = "test-offering";

    private OmObservationConstellation observationTemplate;

    private InsertResultTemplateRequestEncoder encoder;

    private InsertResultTemplateRequest request;

    private String procedureIdentifier = "test-procedure-identifier";

    private String observedProperty = "test-observed-property";

    private String featureIdentifier = "test-feature-identifier";

    private String featureName = "test-feature-name";

    @Before
    public void setup() throws InvalidSridException, ParseException {
        encoder = new InsertResultTemplateRequestEncoder();
        Supplier<XmlOptions> xmlOptions = () -> new XmlOptions();
        encoder.setXmlOptions(xmlOptions);

        SensorML procedure = new SensorML();
        procedure.setIdentifier(procedureIdentifier);

        SamplingFeature featureOfInterest = new SamplingFeature(new CodeWithAuthority(featureIdentifier));
        featureOfInterest.setIdentifier(featureIdentifier);
        featureOfInterest.setName(new CodeType(featureName));
        featureOfInterest.setFeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT);
        featureOfInterest.setGeometry(JTSHelper.createGeometryFromWKT("POINT(30 10)", 4326));

        observationTemplate = new OmObservationConstellation();
        observationTemplate.addOffering(offering);
        observationTemplate.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
        observationTemplate.setProcedure(procedure);
        observationTemplate.setObservableProperty(new OmObservableProperty(observedProperty));
        observationTemplate.setFeatureOfInterest(featureOfInterest);

        request = new InsertResultTemplateRequest(SosConstants.SOS,
                Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.InsertResultTemplate.name());
        request.setResultStructure(new SosResultStructure(new SweDataRecord()));
        request.setResultEncoding(new SosResultEncoding(new SweTextEncoding()));
        request.setIdentifier(templateIdentifier);
        request.setObservationTemplate(observationTemplate);

        OmEncoderv20 omEncoder = new OmEncoderv20();
        omEncoder.setXmlOptions(xmlOptions);

        SamplingEncoderv20 samsEncoder = new SamplingEncoderv20();
        samsEncoder.setXmlOptions(xmlOptions);

        GmlEncoderv321 gml32Encoder = new GmlEncoderv321();
        gml32Encoder.setXmlOptions(xmlOptions);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(encoder, omEncoder, samsEncoder, gml32Encoder));
        encoderRepository.init();

        encoder.setEncoderRepository(encoderRepository);
        omEncoder.setEncoderRepository(encoderRepository);
        samsEncoder.setEncoderRepository(encoderRepository);
        gml32Encoder.setEncoderRepository(encoderRepository);
    }

    @Test
    public void shouldThrowExceptionOnNullInput() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'null'"));

        encoder.create(null);
    }

    @Test
    public void shouldThrowExceptionWhenObservationTemplateIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'missing ObservationTemplate'"));

        encoder.create(new InsertResultTemplateRequest());
    }

    @Test
    public void shouldThrowExceptionWhenOfferingIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'missing offering'"));

        request = new InsertResultTemplateRequest();
        request.setObservationTemplate(new OmObservationConstellation());
        encoder.create(request);
    }

    @Test
    public void shouldThrowExceptionWhenResultStructureIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'missing resultStructure'"));

        request = new InsertResultTemplateRequest();
        request.setObservationTemplate(new OmObservationConstellation());
        OmObservationConstellation observationTemplate = new OmObservationConstellation();
        observationTemplate.addOffering(offering);
        request.setObservationTemplate(observationTemplate);
        encoder.create(request);
    }

    @Test
    public void shouldThrowExceptionWhenResultEncodingIsMissing() throws EncodingException {
        thrown.expect(UnsupportedEncoderInputException.class);
        thrown.expectMessage(Is.is("Encoder " +
                InsertResultTemplateRequestEncoder.class.getSimpleName() +
                " can not encode 'missing resultEncoding'"));

        request = new InsertResultTemplateRequest();
        request.setObservationTemplate(new OmObservationConstellation());
        request.setResultStructure(new SosResultStructure(new SweDataRecord()));
        OmObservationConstellation observationTemplate = new OmObservationConstellation();
        observationTemplate.addOffering(offering);
        request.setObservationTemplate(observationTemplate);
        encoder.create(request);
    }

    @Test
    public void shouldSetRequestDefaults() throws EncodingException {
        InsertResultTemplateType encodedRequest = ((InsertResultTemplateDocument) encoder.create(request))
                .getInsertResultTemplate();

        Assert.assertThat(encodedRequest.getService(), Is.is(SosConstants.SOS));
        Assert.assertThat(encodedRequest.getVersion(), Is.is(Sos2Constants.SERVICEVERSION));
    }

    @Test
    public void shouldSetOptionalIdentifier() throws EncodingException {
        ResultTemplateType template = ((InsertResultTemplateDocument) encoder.create(request))
                .getInsertResultTemplate().getProposedTemplate().getResultTemplate();

        Assert.assertThat(template.isSetIdentifier(), Is.is(true));
        Assert.assertThat(template.getIdentifier(), Is.is(templateIdentifier));
    }

    @Test
    public void shouldSetOffering() throws EncodingException {
        ResultTemplateType template = ((InsertResultTemplateDocument) encoder.create(request))
                .getInsertResultTemplate().getProposedTemplate().getResultTemplate();

        Assert.assertThat(template.getOffering(), Matchers.notNullValue());
        Assert.assertThat(template.getOffering(), Is.is(offering));
    }

    @Test
    public void shouldSetObservationTemplate() throws EncodingException {
        ResultTemplateType template = ((InsertResultTemplateDocument) encoder.create(request))
                .getInsertResultTemplate().getProposedTemplate().getResultTemplate();

        Assert.assertThat(template.getObservationTemplate(), Matchers.notNullValue());
    }

    @Test
    public void shouldEncodeObservationTemplate() throws EncodingException {
        ResultTemplateType template = ((InsertResultTemplateDocument) encoder.create(request))
                .getInsertResultTemplate().getProposedTemplate().getResultTemplate();

        ObservationTemplate observationTemplate = template.getObservationTemplate();
        Assert.assertThat(observationTemplate, Matchers.notNullValue());
        Assert.assertThat(observationTemplate, Matchers.instanceOf(ObservationTemplate.class));

        OMObservationType omObservation = observationTemplate.getOMObservation();
        Assert.assertThat(omObservation, Matchers.instanceOf(OMObservationType.class));
        Assert.assertThat(omObservation.getType().getHref(), Is.is(OmConstants.OBS_TYPE_MEASUREMENT));
        Assert.assertThat(omObservation.getPhenomenonTime().isNil(), Is.is(true));
        Assert.assertThat(omObservation.getPhenomenonTime().isSetNilReason(), Is.is(true));
        Assert.assertThat(omObservation.getPhenomenonTime().getNilReason(), Is.is("template"));
        Assert.assertThat(omObservation.getResultTime().isNil(), Is.is(true));
        Assert.assertThat(omObservation.getResultTime().isSetNilReason(), Is.is(true));
        Assert.assertThat(omObservation.getResultTime().getNilReason(), Is.is("template"));
        Assert.assertThat(omObservation.getProcedure().isNil(), Is.is(false));
        Assert.assertThat(omObservation.getProcedure().getHref(), Is.is(procedureIdentifier));
        Assert.assertThat(omObservation.getObservedProperty().isNil(), Is.is(false));
        Assert.assertThat(omObservation.getObservedProperty().getHref(), Is.is(observedProperty));
        Assert.assertThat(omObservation.getFeatureOfInterest(), Matchers.notNullValue());
        Assert.assertThat(omObservation.getFeatureOfInterest().getAbstractFeature(),
                Matchers.instanceOf(SFSamplingFeatureType.class));
        SFSamplingFeatureType feature =
                (SFSamplingFeatureType) omObservation.getFeatureOfInterest().getAbstractFeature();
        Assert.assertThat(feature.getIdentifier().getStringValue(), Is.is(featureIdentifier));
        Assert.assertThat(feature.getNameArray().length, Is.is(1));
        Assert.assertThat(feature.getNameArray(0).getStringValue(), Is.is(featureName));
    }
}
