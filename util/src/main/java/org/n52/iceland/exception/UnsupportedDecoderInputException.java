/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.exception;

import org.n52.iceland.coding.decode.Decoder;

/**
 * @author Matthes Rieke
 *
 * @since 1.3.0
 */
public class UnsupportedDecoderInputException extends Exception {
    private static final long serialVersionUID = 5561451567407304739L;
    private final Decoder<?, ?> decoder;
    private final Object objectToDecode;

    public UnsupportedDecoderInputException(Decoder<?, ?> decoder, Object o) {
        this.decoder = decoder;
        this.objectToDecode = o;
    }

    protected String getObjectName(Object o) {
        return o.getClass().getName();
    }

    public Decoder<?, ?> getDecoder() {
        return decoder;
    }

    public Object getObjectToDecode() {
        return objectToDecode;
    }
    
}
