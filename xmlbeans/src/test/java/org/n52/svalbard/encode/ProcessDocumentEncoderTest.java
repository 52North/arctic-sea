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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.junit.Before;
import org.junit.Test;
import org.n52.svalbard.coding.AbtractProcessCodingTest;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.shetland.inspire.ompr.Process;

import eu.europa.ec.inspire.schemas.ompr.x30.ProcessDocument;
import eu.europa.ec.inspire.schemas.ompr.x30.ProcessType;

public class ProcessDocumentEncoderTest extends AbtractProcessCodingTest {

//    @Before
//    public void init() {
//        Configurator configurator = mock(Configurator.class);
//        when(configurator.getProfileHandler()).thenReturn(new DefaultProfileHandler());
//        Configurator.setInstance(configurator);
//    }
//
//    @Test
//    public void test_type_encoding() throws XmlException, IOException, EncodingException {
//        Process process = createProcessFromFile();
//        XmlObject encodeObjectToXml = encodeObjectToXml(process.get(), process);
//        assertThat(encodeObjectToXml, is(instanceOf(ProcessType.class)));
//    }
//
//    @Test
//    public void test_document_encoding() throws XmlException, IOException, EncodingException {
//        Process process = createProcessFromFile();
//        XmlObject encodeObjectToXml = encodeObjectToXmlDocument(process.getDescriptionFormat(), process);
//        assertThat(encodeObjectToXml, is(instanceOf(ProcessDocument.class)));
//    }

}
