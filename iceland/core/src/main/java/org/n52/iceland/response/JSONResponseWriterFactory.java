/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.response;

import javax.inject.Inject;

import org.n52.iceland.coding.encode.ResponseWriter;
import org.n52.iceland.coding.encode.ResponseWriterFactory;
import org.n52.iceland.coding.encode.ResponseWriterKey;
import org.n52.janmayen.component.SingleTypeComponentFactory;
import org.n52.svalbard.encode.EncoderRepository;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link ResponseWriterFactory} implementation for {@link JsonNode} and
 * {@link JSONResponseWriter}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 2.0.0
 *
 */
public class JSONResponseWriterFactory
        implements ResponseWriterFactory, SingleTypeComponentFactory<ResponseWriterKey, ResponseWriter<?>> {

    private static final ResponseWriterKey RESPONSE_WRITER_KEY = new ResponseWriterKey(JsonNode.class);
    private EncoderRepository encoderRepository;

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Override
    public ResponseWriterKey getKey() {
        return RESPONSE_WRITER_KEY;
    }

    @Override
    public JSONResponseWriter create() {
        return new JSONResponseWriter(this.encoderRepository);
    }

}
