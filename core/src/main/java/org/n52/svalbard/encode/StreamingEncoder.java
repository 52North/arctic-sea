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

import java.io.OutputStream;

import org.n52.svalbard.encode.exception.EncodingException;

/**
 * Generic interface for StreamingEncoders.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> the resulting type, the "Target"
 * @param <S> the input type, the "Source"
 */
public interface StreamingEncoder<T, S> extends Encoder<T, S> {

    default void encode(S objectToEncode, OutputStream outputStream) throws EncodingException {
        encode(objectToEncode, outputStream, EncodingContext.empty());
    }

    void encode(S objectToEncode, OutputStream outputStream, EncodingContext context)
            throws EncodingException;

}
