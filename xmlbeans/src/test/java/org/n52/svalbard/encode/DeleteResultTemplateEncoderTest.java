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

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.n52.shetland.ogc.ows.exception.MissingServiceParameterException;
import org.n52.shetland.ogc.ows.exception.MissingVersionParameterException;
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
//
//    @Rule
//    public ExpectedException thrown = ExpectedException.none();
//
//    @Test
//    public void shouldThrowExceptionOnNullInput() throws EncodingException {
//        thrown.expect(UnsupportedEncoderInputException.class);
//        thrown.expectMessage(String.format("Encoder %s can not encode 'null'",
//                DeleteResultTemplateEncoder.class.getName()));
//
//        new DeleteResultTemplateEncoder().create(null);
//    }
//
//    @Test
//    public void shouldThrowExceptionOnMissingServiceAndVersionParameter() throws EncodingException {
//        thrown.expect(new CompositeExceptionMatcher()
//                .with(MissingServiceParameterException.class)
//                .with(MissingVersionParameterException.class));
//
//        new DeleteResultTemplateEncoder().create(new DeleteResultTemplateResponse());
//    }
//
//    @Test
//    public void shouldEncodeEmptyResponse() throws EncodingException {
//        DeleteResultTemplateResponseDocument encodedResponse =
//                (DeleteResultTemplateResponseDocument)
//                new DeleteResultTemplateEncoder().create(
//                (DeleteResultTemplateResponse) new DeleteResultTemplateResponse()
//                        .setService("test-service")
//                        .setVersion("test-version"));
//
//        Assert.assertThat(encodedResponse.getDeleteResultTemplateResponse(), CoreMatchers.notNullValue());
//    }
//
//    @Test
//    public void shouldEncodeResultTemplateList() throws EncodingException {
//        DeleteResultTemplateResponseDocument encodedResponse =
//                (DeleteResultTemplateResponseDocument)
//                new DeleteResultTemplateEncoder().create(
//                (DeleteResultTemplateResponse) new DeleteResultTemplateResponse()
//                        .addDeletedResultTemplates(Lists.newArrayList(
//                                "test-result-template-1",
//                                "test-result-template-2"))
//                        .setService("test-service")
//                        .setVersion("test-version"));
//
//        final DeleteResultTemplateResponseType drtt = encodedResponse.getDeleteResultTemplateResponse();
//
//        Assert.assertThat(drtt.sizeOfDeletedTemplateArray(), Is.is(2));
//        Assert.assertThat(drtt.getDeletedTemplateArray(0), Is.is("test-result-template-1"));
//        Assert.assertThat(drtt.getDeletedTemplateArray(1), Is.is("test-result-template-2"));
//    }

}
