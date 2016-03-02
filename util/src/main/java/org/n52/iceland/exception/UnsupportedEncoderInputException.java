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

import org.n52.iceland.coding.encode.Encoder;

/**
 * @author Matthes Rieke
 *
 * @since 1.3.0
 */
public class UnsupportedEncoderInputException extends Exception {
    private static final long serialVersionUID = 7033551424176154646L;
    private final Encoder<?, ?> encoder;
    private final Object objectToEncode;

    public UnsupportedEncoderInputException(final Encoder<?, ?> encoder, final Object o) {
        this.encoder = encoder;
        this.objectToEncode = o;
    }

    public Encoder<?, ?> getEncoder() {
        return encoder;
    }

    public Object getObjectToEncode() {
        return objectToEncode;
    }


}
