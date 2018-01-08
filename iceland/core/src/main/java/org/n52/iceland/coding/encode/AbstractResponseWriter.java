/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding.encode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.service.ResponseFormat;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;

/**
 * Abstract {@link ResponseWriter} class for response streaming
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> generic for the element to write
 */
public abstract class AbstractResponseWriter<T> implements ResponseWriter<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResponseWriter.class);
    private MediaType contentType;
    private final EncoderRepository encoderRepository;

    public AbstractResponseWriter(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Override
    public MediaType getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    /**
     * Getter for encoder, encapsulates the instance call
     *
     * @param <D> the encoders output type
     * @param <S> the encoders input type
     * @param key Encoder key
     *
     * @return Matching encoder
     */
    protected <D, S> Encoder<D, S> getEncoder(EncoderKey key) {
        return encoderRepository.getEncoder(key);
    }

    @Override
    public MediaType getEncodedContentType(ResponseFormat responseFormat) {
        if (responseFormat.isSetResponseFormat()) {
            MediaType contentTypeFromResponseFormat = null;
            try {
                contentTypeFromResponseFormat = MediaType.parse(responseFormat.getResponseFormat());
            } catch (IllegalArgumentException iae) {
                LOGGER.debug("Requested responseFormat {} is not a MediaType", responseFormat.getResponseFormat());
            }
            if (contentTypeFromResponseFormat != null) {
                if (MediaTypes.COMPATIBLE_TYPES.containsEntry(contentTypeFromResponseFormat.withoutParameters(),
                                                              getContentType())) {
                    return getContentType();
                }
                return contentTypeFromResponseFormat;
            }
        }
        return getContentType();
    }

    protected EncoderRepository getEncoderRepository() {
        return encoderRepository;
    }
}
