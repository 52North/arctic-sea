/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.XmlHelper;

public class GetDataAvailabilityRequestDecoderTest {
    private DecoderRepository decoderRepository;

    @BeforeEach
    public void init() {

        this.decoderRepository = new DecoderRepository();

        GetDataAvailabilityXmlDecoder decoder1 = new GetDataAvailabilityXmlDecoder();
        decoder1.setXmlOptions(XmlOptions::new);
        decoder1.setDecoderRepository(this.decoderRepository);

        GetDataAvailabilityV20XmlDecoder decoder2 = new GetDataAvailabilityV20XmlDecoder();
        decoder2.setDecoderRepository(this.decoderRepository);
        decoder2.setXmlOptions(XmlOptions::new);

        this.decoderRepository.setDecoders(Arrays.asList(decoder1, decoder2));
        this.decoderRepository.init();
    }

    @Test
    public void testGDAv2() throws DecodingException {
        net.opengis.sosgda.x20.GetDataAvailabilityDocument document
                = net.opengis.sosgda.x20.GetDataAvailabilityDocument.Factory.newInstance();
        net.opengis.sosgda.x20.GetDataAvailabilityType gda = document.addNewGetDataAvailability();

        gda.addFeatureOfInterest("featureOfInterest1");
        gda.addFeatureOfInterest("featureOfInterest2");
        gda.addObservedProperty("observedProperty1");
        gda.addObservedProperty("observedProperty2");
        gda.addOffering("offering1");
        gda.addOffering("offering2");
        gda.addProcedure("procedure1");
        gda.addProcedure("procedure2");
        checkRequest(getDecoder(document).decode(document), GetDataAvailabilityConstants.NS_GDA_20);

    }

    @Test
    public void testGDAv1() throws DecodingException {

        net.opengis.sosgda.x10.GetDataAvailabilityDocument document
                = net.opengis.sosgda.x10.GetDataAvailabilityDocument.Factory.newInstance();
        net.opengis.sosgda.x10.GetDataAvailabilityType gda = document.addNewGetDataAvailability();

        gda.addFeatureOfInterest("featureOfInterest1");
        gda.addFeatureOfInterest("featureOfInterest2");
        gda.addObservedProperty("observedProperty1");
        gda.addObservedProperty("observedProperty2");
        gda.addOffering("offering1");
        gda.addOffering("offering2");
        gda.addProcedure("procedure1");
        gda.addProcedure("procedure2");

        checkRequest(getDecoder(document).decode(document), GetDataAvailabilityConstants.NS_GDA);

    }

    private Decoder<GetDataAvailabilityRequest, XmlObject> getDecoder(XmlObject document) {
        String namespace = XmlHelper.getNamespace(document);
        Class<? extends XmlObject> clazz = document.getClass();
        XmlNamespaceDecoderKey key = new XmlNamespaceDecoderKey(namespace, clazz);
        Decoder<GetDataAvailabilityRequest, XmlObject> decoder = decoderRepository.getDecoder(key);
        assertNotNull(decoder);
        return decoder;

    }

    private void checkRequest(GetDataAvailabilityRequest request, String namespace) {
        assertThat(request, is(notNullValue()));
        assertThat(request.getNamespace(), is(namespace));
        assertThat(request.getResponseFormat(), is(namespace));
        assertThat(request.getFeaturesOfInterest(), contains("featureOfInterest1", "featureOfInterest2"));
        assertThat(request.getOfferings(), contains("offering1", "offering2"));
        assertThat(request.getProcedures(), contains("procedure1", "procedure2"));
        assertThat(request.getObservedProperties(), contains("observedProperty1", "observedProperty2"));
    }

}
