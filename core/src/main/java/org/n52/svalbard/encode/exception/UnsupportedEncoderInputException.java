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
package org.n52.svalbard.encode.exception;

import org.n52.svalbard.encode.Encoder;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class UnsupportedEncoderInputException extends EncodingException {
    private static final long serialVersionUID = 7033551424176154646L;

    public UnsupportedEncoderInputException(Encoder<?, ?> encoder, Class<?> c) {
        this(encoder, c == null ? null : c.getName());
    }

    public UnsupportedEncoderInputException(Encoder<?, ?> encoder, Object o) {
        this(encoder, o == null ? null : o.getClass().getName());
    }

    public UnsupportedEncoderInputException(Encoder<?, ?> encoder, String o) {
        super(String.format("Encoder %s can not encode '%s'", encoder.getClass().getSimpleName(),
                (o == null || o.isEmpty()) ? "null" : o));
    }

    public UnsupportedEncoderInputException(Encoder<?, ?> encoder) {
        this(encoder, "");
    }
}
