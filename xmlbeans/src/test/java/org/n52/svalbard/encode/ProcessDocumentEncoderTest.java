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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.janmayen.Producer;
import org.n52.shetland.inspire.ompr.Process;
import org.n52.svalbard.decode.AbtractProcessDecodingTest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;

import eu.europa.ec.inspire.schemas.ompr.x30.ProcessDocument;
import eu.europa.ec.inspire.schemas.ompr.x30.ProcessType;

public class ProcessDocumentEncoderTest extends AbtractProcessDecodingTest {

    private ProcessDocumentEncoder docEncoder;
    private ProcessTypeEncoder typeEncoder;

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Before
    public void initDecoder() {
        EncoderRepository encoderRepository = new EncoderRepository();
        Producer<XmlOptions> options = () -> new XmlOptions();

        docEncoder = new ProcessDocumentEncoder();
        docEncoder.setEncoderRepository(encoderRepository);
        docEncoder.setXmlOptions(options);

        typeEncoder = new ProcessTypeEncoder();
        typeEncoder.setEncoderRepository(encoderRepository);
        typeEncoder.setXmlOptions(options);

        encoderRepository.setEncoders(Arrays.asList(docEncoder, typeEncoder));

        encoderRepository.init();

    }

//    @Test
//    public void test_type_encoding() throws XmlException, IOException, EncodingException, DecodingException {
//        Process process = createProcessFromFile();
//        XmlObject encodeObjectToXml = typeEncoder.encode(process);
//        assertThat(encodeObjectToXml, is(instanceOf(ProcessType.class)));
//    }
//
//    @Test
//    public void test_document_encoding() throws XmlException, IOException, EncodingException, DecodingException {
//        Process process = createProcessFromFile();
//        XmlObject encodeObjectToXml = docEncoder.encode(process);
//        assertThat(encodeObjectToXml, is(instanceOf(ProcessDocument.class)));
//    }

}
