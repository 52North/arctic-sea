/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.BeforeEach;
import org.n52.janmayen.Producer;
import org.n52.shetland.inspire.ompr.Process;
import org.n52.svalbard.decode.exception.DecodingException;

public abstract class AbtractProcessDecodingTest {

    private final String file = "/process.xml";
    private ProcessDocumentDecoder decoder;

    @BeforeEach
    public void initDecoder() {
        DecoderRepository decoderRepository = new DecoderRepository();
        Producer<XmlOptions> options = () -> new XmlOptions();

        decoder = new ProcessDocumentDecoder();
        decoder.setDecoderRepository(decoderRepository);
        decoder.setXmlOptions(options);

        decoderRepository.setDecoders(Arrays.asList(decoder));

        decoderRepository.init();

    }

    protected Process createProcessFromFile() throws XmlException, IOException, DecodingException {
        XmlObject xmlObject = XmlObject.Factory.parse(this.getClass().getResourceAsStream(file));
        Object decodeObject = decoder.decodeXmlElement(xmlObject);
        assertThat(decodeObject, is(instanceOf(Process.class)));
        return (Process) decodeObject;
    }
}
