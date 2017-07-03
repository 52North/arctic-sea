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

import java.util.Collections;
import java.util.Set;

import org.n52.janmayen.component.Component;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.SupportedType;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * Generic interface for Encoders.
 *
 * @param <T> the resulting type, the "Target"
 * @param <S> the input type, the "Source"
 *
 * @since 1.0.0
 */
public interface Encoder<T, S> extends Component<EncoderKey> {

    /**
     * Encodes the specified object.
     *
     * @param objectToEncode
     *            the object to encode
     *
     * @return the encoded object
     *
     * @throws EncodingException
     *             if an error occurs
     */
    T encode(S objectToEncode) throws EncodingException;

    /**
     * Encodes the specified object with the specified {@linkplain EncodingContext}
     * .
     *
     * @param objectToEncode
     *            the object to encode
     * @param additionalValues
     *            the helper values
     *
     * @return the encoded object
     *
     * @throws EncodingException
     *             if an error occurs
     */
    T encode(S objectToEncode, EncodingContext additionalValues) throws EncodingException;

    /**
     * Get the {@link SupportedType}
     *
     * @return the supported key types
     */
    default Set<SupportedType> getSupportedTypes() {
        return Collections.emptySet();
    }

    /**
     * @return the content type of the encoded response.
     */
    MediaType getContentType();

}
