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

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.io.ParseException;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.features.samplingFeatures.InvalidSridException;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.util.JTSHelper;
import org.n52.svalbard.encode.exception.EncodingException;

import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureDocument;
import net.opengis.samplingSpatial.x20.SFSpatialSamplingFeatureType;

public class SamplingEncoderv20Test {

    private SamplingEncoderv20 encoder;
    private EncodingContext ctx;
    private SamplingFeature featureOfInterest;

    @BeforeEach
    public void setup() throws InvalidSridException, ParseException {
        EncoderRepository encoderRepository = new EncoderRepository();

        OmEncoderv20 omEncoderv20 = new OmEncoderv20();
        omEncoderv20.setXmlOptions(XmlOptions::new);
        omEncoderv20.setEncoderRepository(encoderRepository);

        GmlEncoderv321 gmlEncoderv321 = new GmlEncoderv321();
        gmlEncoderv321.setEncoderRepository(encoderRepository);
        gmlEncoderv321.setXmlOptions(XmlOptions::new);

        SensorMLEncoderv20 sensorMLEncoderv20 = new SensorMLEncoderv20();
        sensorMLEncoderv20.setXmlOptions(XmlOptions::new);
        sensorMLEncoderv20.setEncoderRepository(encoderRepository);

        SweCommonEncoderv20 sweCommonEncoderv20 = new SweCommonEncoderv20();
        sweCommonEncoderv20.setEncoderRepository(encoderRepository);
        sweCommonEncoderv20.setXmlOptions(XmlOptions::new);

        encoder = new SamplingEncoderv20();
        encoder.setEncoderRepository(encoderRepository);
        encoder.setXmlOptions(XmlOptions::new);

        encoderRepository.setEncoders(Arrays.asList(omEncoderv20,
                gmlEncoderv321,
                sensorMLEncoderv20,
                sweCommonEncoderv20,
                encoder));
        encoderRepository.init();

        featureOfInterest = new SamplingFeature(new CodeWithAuthority("test-feature"));
        featureOfInterest.setIdentifier("test-feature-identifier");
        featureOfInterest.setName(new CodeType("test-feature-name"));
        featureOfInterest.setFeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT);
        featureOfInterest.setGeometry(JTSHelper.createGeometryFromWKT("POINT (30 10)" , 4326));
    }

    @Test
    public void shouldEncodeAbstractFeatureWithEncodingContextAsPropertyType() throws EncodingException {
        ctx = EncodingContext.empty().with(XmlBeansEncodingFlags.PROPERTY_TYPE);
        XmlObject encode = encoder.encode(featureOfInterest, ctx);
        MatcherAssert.assertThat(encode, Matchers.instanceOf(SFSpatialSamplingFeatureType.class));
    }

    @Test
    public void shouldEncodeAbstractFeatureWithOutEncodingContextAsDocument() throws EncodingException {
        ctx = EncodingContext.empty();
        XmlObject encode = encoder.encode(featureOfInterest, ctx);
        MatcherAssert.assertThat(encode, Matchers.instanceOf(SFSpatialSamplingFeatureDocument.class));
    }

}
