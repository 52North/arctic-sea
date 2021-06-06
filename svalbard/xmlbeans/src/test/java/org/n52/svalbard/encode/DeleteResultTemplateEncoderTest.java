/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.Producer;
import org.n52.shetland.ogc.sos.drt.DeleteResultTemplateResponse;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Lists;

import net.opengis.drt.x10.DeleteResultTemplateResponseDocument;
import net.opengis.drt.x10.DeleteResultTemplateResponseType;

/**
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 * J&uuml;rrens</a>
 */
public class DeleteResultTemplateEncoderTest {

    private static DeleteResultTemplateEncoder instance;

    @BeforeAll
    public static void initInstance() {
        instance = new DeleteResultTemplateEncoder();
        Producer<XmlOptions> options = () -> new XmlOptions();
        instance.setXmlOptions(options);

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(Arrays.asList(instance));
        encoderRepository.init();

        SchemaRepository schemaRepository = new SchemaRepository();
        schemaRepository.setEncoderRepository(encoderRepository);
        schemaRepository.init();

        instance.setSchemaRepository(schemaRepository);
        instance.setEncoderRepository(encoderRepository);
        instance.setXmlOptions(options);

    }

    @Test
    public void shouldThrowExceptionOnNullInput() throws EncodingException {
        UnsupportedEncoderInputException thrown = Assertions.assertThrows(UnsupportedEncoderInputException.class, () -> {
            instance.create(null);
        });
        assertEquals(String.format("Encoder %s can not encode 'null'",
                DeleteResultTemplateEncoder.class.getSimpleName()), thrown.getMessage());
    }

    @Test
    public void shouldEncodeEmptyResponse() throws EncodingException {
        DeleteResultTemplateResponseDocument encodedResponse =
                (DeleteResultTemplateResponseDocument)
                instance.create(new DeleteResultTemplateResponse());
        Assertions.assertNotNull(encodedResponse.getDeleteResultTemplateResponse());
    }

    @Test
    public void shouldEncodeResultTemplateList() throws EncodingException {
        DeleteResultTemplateResponseDocument encodedResponse =
                (DeleteResultTemplateResponseDocument)
                instance.create(new DeleteResultTemplateResponse()
                        .addDeletedResultTemplates(Lists.newArrayList(
                                "test-result-template-1",
                                "test-result-template-2")));

        final DeleteResultTemplateResponseType drtt = encodedResponse.getDeleteResultTemplateResponse();

        Assertions.assertEquals(drtt.sizeOfDeletedTemplateArray(), 2);
        Assertions.assertEquals(drtt.getDeletedTemplateArray(0), "test-result-template-1");
        Assertions.assertEquals(drtt.getDeletedTemplateArray(1), "test-result-template-2");
    }

}
