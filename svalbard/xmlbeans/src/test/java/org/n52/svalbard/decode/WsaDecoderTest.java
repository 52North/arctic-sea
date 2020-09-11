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

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.Producer;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;

public class WsaDecoderTest {

    private DecoderRepository decoderRepository;

    @BeforeEach
    public void setup() {

        decoderRepository = new DecoderRepository();

        Producer<XmlOptions> xmlOptions = XmlOptions::new;

        List<Decoder<?, ?>> decoders = Stream.of(new SosDecoderv20(),
                                                 new GmlDecoderv321(),
                                                 new OmDecoderv20(),
                                                 new SweCommonDecoderV20(),
                                                 new Soap12Decoder(),
                                                 new Soap11Decoder(),
                                                 new WsaDecoder())
                .map(decoder -> {
                    decoder.setDecoderRepository(decoderRepository);
                    decoder.setXmlOptions(xmlOptions);
                    return decoder;
                }).collect(toList());

        decoderRepository.setDecoders(decoders);
        decoderRepository.init();
    }

    @Test
    public void test_decoding_wsa() throws XmlException, IOException, DecodingException {
        XmlObject xml = XmlObject.Factory.parse(getClass().getResourceAsStream("/SoapWsa.xml"));
        DecoderKey decoderKey = CodingHelper.getDecoderKey(xml);
        Decoder<Object, XmlObject> decoder = decoderRepository.getDecoder(decoderKey);
        Object response = decoder.decode(xml);
    }

    @Test
    public void test_decoding_wsa_soap11() throws XmlException, IOException, DecodingException {
        XmlObject xml = XmlObject.Factory.parse(getClass().getResourceAsStream("/SoapWsa11.xml"));
        DecoderKey decoderKey = CodingHelper.getDecoderKey(xml);
        Decoder<Object, XmlObject> decoder = decoderRepository.getDecoder(decoderKey);
        Object response = decoder.decode(xml);
    }
}
