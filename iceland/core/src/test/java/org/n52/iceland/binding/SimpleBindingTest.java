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
package org.n52.iceland.binding;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.response.TestResponse;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.OperationResponseEncoderKey;

/**
 * @since 1.0.0
 *
 */
public class SimpleBindingTest {
    private static final List<MediaType> XML = newArrayList(MediaTypes.APPLICATION_XML);

    private static final List<MediaType> JSON = newArrayList(MediaTypes.APPLICATION_JSON);

    private static final List<MediaType> ANYTHING = newArrayList(MediaType.any());

    private static final List<MediaType> XML_AND_JSON = newArrayList(MediaTypes.APPLICATION_XML, MediaTypes.APPLICATION_JSON);

    private static final List<MediaType> NOTHING = newArrayList();

    private TestBinding binding;

    private TestResponse response;

    private MediaType defaultContentType;

    @BeforeEach
    public void setUp() {
        this.response = new TestResponse();
        this.binding = new TestBinding();
        this.binding.setEncoderRepository(mockEncoderRepository());
        this.defaultContentType = binding.getDefaultContentType();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private EncoderRepository mockEncoderRepository() {
        EncoderRepository encoderRepository = mock(EncoderRepository.class);
        OwsOperationKey operationKey = new OwsOperationKey(response);
        OperationResponseEncoderKey operationEncoderKey =
                new OperationResponseEncoderKey(operationKey, MediaTypes.APPLICATION_JSON);
        Encoder encoder = Mockito.mock(Encoder.class);
        when(encoderRepository.getEncoder(operationEncoderKey)).thenReturn(encoder);
        return encoderRepository;
    }

    @Test
    public void should_use_default_ContentType() throws HTTPException {
        assertThat(chosenContentTypeWithAccept(NOTHING), is(defaultContentType));
    }

    @Test
    public void should_Accept_Defaul_ContentType() throws HTTPException {
        assertThat(chosenContentTypeWithAccept(XML), is(defaultContentType));
    }

    @Test
    public void should_Accept_NotSupported_ContentType() throws HTTPException {
        assertThrows(HTTPException.class, () -> {
            assertThat(chosenContentTypeWithAccept(JSON), is(MediaTypes.APPLICATION_JSON));
        });
    }

    @Test
    public void should_Accept_Wildcard_ContentType() throws HTTPException {
        assertThat(chosenContentTypeWithAccept(ANYTHING), is(defaultContentType));
    }

    @Test
    public void should_ResponseFormat_NotSupported_ContentType() throws HTTPException {
        assertThrows(HTTPException.class, () -> {
            response.setContentType(MediaTypes.APPLICATION_NETCDF);
            assertThat(chosenContentTypeWithAccept(NOTHING), is(defaultContentType));
        });
    }

    @Test
    public void should_Acept_Equals_ResponseFormat_ContentType() throws HTTPException {
        response.setContentType(MediaTypes.APPLICATION_XML);
        assertThat(chosenContentTypeWithAccept(XML), is(defaultContentType));
    }

    @Test
    public void should_Accept_NotContains_ResponseFormat_ContentType() throws HTTPException {
        assertThrows(HTTPException.class, () -> {
            response.setContentType(MediaTypes.APPLICATION_NETCDF);
            assertThat(chosenContentTypeWithAccept(XML_AND_JSON), is(defaultContentType));
        });
    }

    @Test
    public void should_Accept_Wildcard_ResponseFormat_ContentType() throws HTTPException {
        response.setContentType(MediaTypes.APPLICATION_NETCDF);
        assertThat(chosenContentTypeWithAccept(ANYTHING), is(MediaTypes.APPLICATION_NETCDF));
    }

    private MediaType chosenContentTypeWithAccept(List<MediaType> accept) throws HTTPException {
        return binding.chooseResponseContentType(response, accept, defaultContentType);
    }

}
