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

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Test;

import org.n52.janmayen.Producer;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GetFeatureOfInterestResponseDecoderTest {

    private DecoderRepository decoderRepository;

    @Before
    public void setup() {

        decoderRepository = new DecoderRepository();

        Producer<XmlOptions> xmlOptions = XmlOptions::new;

        List<Decoder<?, ?>> decoders = Stream.of(new GetFeatureOfInterestResponseDocumentDecoder(),
                                                 new GmlDecoderv321(),
                                                 new OmDecoderv20(),
                                                 new SweCommonDecoderV20(),
                                                 new SamplingDecoderv20())
                .map(decoder -> {
                    decoder.setDecoderRepository(decoderRepository);
                    decoder.setXmlOptions(xmlOptions);
                    return decoder;
                }).collect(toList());

        decoderRepository.setDecoders(decoders);
        decoderRepository.init();
    }

    @Test
    public void testMultiCurve() throws XmlException, IOException, DecodingException {
        try {
            XmlObject xml = XmlObject.Factory.parse(getClass()
                    .getResourceAsStream("/GetFeatureOfInterestResponse.xml"));
            DecoderKey decoderKey = CodingHelper.getDecoderKey(xml);
            System.out.println(decoderKey);
            Decoder<GetFeatureOfInterestResponse, XmlObject> decoder = decoderRepository.getDecoder(decoderKey);
            GetFeatureOfInterestResponse response = decoder.decode(xml);
            assertThat(response, is(notNullValue()));
            assertThat(response.getAbstractFeature(), is(instanceOf(FeatureCollection.class)));
            FeatureCollection abstractFeature = (FeatureCollection) response.getAbstractFeature();
            assertThat(abstractFeature.getMembers().size(), is(266));

            System.out.println("feature: "+response.getAbstractFeature());
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            throw t;
        }
    }
}
