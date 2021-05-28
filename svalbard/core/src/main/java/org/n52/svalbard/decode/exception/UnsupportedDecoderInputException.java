/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.decode.exception;

import org.n52.svalbard.decode.Decoder;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class UnsupportedDecoderInputException extends DecodingException {
    private static final long serialVersionUID = 5561451567407304739L;

    public UnsupportedDecoderInputException(Decoder<?, ?> decoder, Object o) {
        this(decoder, o == null ? null : o.getClass().getName());
    }

    public UnsupportedDecoderInputException(Decoder<?, ?> decoder, String o) {
        super(String.format("Decoder %s can not decode '%s'", decoder.getClass().getSimpleName(),
                o == null ? "null" : o));
    }
}
