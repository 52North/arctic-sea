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

import org.apache.xmlbeans.XmlObject;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.exi.EXIObject;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.ows.service.ResponseFormat;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class ExiEncoder<T>
        extends AbstractDelegatingEncoder<EXIObject<XmlObject>, T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExiResponseEncoder.class);

    @Override
    public MediaType getContentType() {
        return MediaTypes.APPLICATION_EXI;
    }

    @Override
    public EXIObject<XmlObject> encode(T response, EncodingContext additionalValues)
            throws EncodingException {
        return encode(response);
    }

    @Override
    public EXIObject<XmlObject> encode(T response)
            throws EncodingException {
        Encoder<Object, T> encoder = getEncoder(response);
        Object encode = encoder.encode(response);
        if (encode != null && encode instanceof XmlObject) {
            return new EXIObject<XmlObject>((XmlObject) encode);
        } else {
            throw new UnsupportedEncoderInputException(encoder, response);
        }
    }

    /**
     * Get the {@link Encoder} for the {@link OwsServiceResponse} and the
     * requested contentType
     *
     * @param response
     *            {@link OwsServiceResponse} to get {@link Encoder} for
     *
     * @return {@link Encoder} for the {@link OwsServiceResponse}
     *
     * @throws EncodingException
     *             if no encoder could be found
     */
    protected Encoder<Object, T> getEncoder(T response)
            throws EncodingException {
        EncoderKey key = getKey(response);
        Encoder<Object, T> encoder = getEncoder(key);
        if (encoder == null) {
            throw new NoEncoderForKeyException(key);
        }
        return encoder;
    }

    protected abstract EncoderKey getKey(T object);

    /**
     * Get encoding {@link MediaType} from {@link OwsServiceResponse}
     *
     * @param response
     *            {@link OwsServiceResponse} to get content type from
     *
     * @return Encoding {@link MediaType}
     */
    protected MediaType getEncodedContentType(OwsServiceResponse response) {
        if (response instanceof ResponseFormat) {
            return getEncodedContentType((ResponseFormat) response);
        }
        return MediaTypes.APPLICATION_XML;
    }

    /**
     * Get encoding {@link MediaType} from {@link ResponseFormat}
     *
     * @param responseFormat
     *            {@link ResponseFormat} to get content type from
     *
     * @return Encoding {@link MediaType}
     */
    protected MediaType getEncodedContentType(ResponseFormat responseFormat) {
        if (responseFormat.isSetResponseFormat()) {
            MediaType contentTypeFromResponseFormat = null;
            try {
                contentTypeFromResponseFormat =
                        MediaType.parse(responseFormat.getResponseFormat()).withoutParameters();
            } catch (IllegalArgumentException iae) {
                LOGGER.debug("Requested responseFormat {} is not a MediaType", responseFormat.getResponseFormat());
            }
            if (contentTypeFromResponseFormat != null) {
                if (MediaTypes.COMPATIBLE_TYPES.containsEntry(contentTypeFromResponseFormat,
                        MediaTypes.APPLICATION_XML)) {
                    return MediaTypes.APPLICATION_XML;
                }
                return contentTypeFromResponseFormat;
            }
        }
        return MediaTypes.APPLICATION_XML;
    }

}
