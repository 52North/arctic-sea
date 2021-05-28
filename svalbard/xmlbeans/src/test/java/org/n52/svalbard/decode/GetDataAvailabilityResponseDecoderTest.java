/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import java.io.IOException;
import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.svalbard.decode.exception.DecodingException;

public class GetDataAvailabilityResponseDecoderTest {

    private DecoderRepository decoderRepository;
    private GetDataAvailabilityResponseDecoder gda;

    @BeforeEach
    public void init() {

        this.decoderRepository = new DecoderRepository();

        gda = new GetDataAvailabilityResponseDecoder();
        gda.setXmlOptions(XmlOptions::new);
        gda.setDecoderRepository(this.decoderRepository);

        GmlDecoderv321 decoder2 = new GmlDecoderv321();
        decoder2.setDecoderRepository(this.decoderRepository);
        decoder2.setXmlOptions(XmlOptions::new);

        GetDataAvailabilityXmlDecoder decoder3 = new GetDataAvailabilityXmlDecoder();
        decoder3.setDecoderRepository(this.decoderRepository);
        decoder3.setXmlOptions(XmlOptions::new);

        GetDataAvailabilityV20XmlDecoder decoder4 = new GetDataAvailabilityV20XmlDecoder();
        decoder4.setDecoderRepository(this.decoderRepository);
        decoder4.setXmlOptions(XmlOptions::new);

        this.decoderRepository.setDecoders(Arrays.asList(gda, decoder2, decoder3, decoder4));
        this.decoderRepository.init();
    }

    @Test
    public void testGDAv1() throws XmlException, IOException, DecodingException {
        XmlObject xml = XmlObject.Factory.parse(getClass()
                .getResourceAsStream("/GDAResponseV1.xml"));
        gda.decode(xml);

    }

    @Test
    public void testGDAv2() throws XmlException, IOException, DecodingException {
        XmlObject xml = XmlObject.Factory.parse(getClass()
                .getResourceAsStream("/GDAResponseV2.xml"));
        gda.decode(xml);
    }

}
