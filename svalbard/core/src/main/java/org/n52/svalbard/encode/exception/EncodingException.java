/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import org.n52.janmayen.exception.LocationHintException;

/**
 *
 * @author Matthes Rieke
 */
public class EncodingException extends LocationHintException {
    private static final long serialVersionUID = -7730668591653900392L;

    public EncodingException(String message, Object... args) {
        super(message, args);
    }

    public EncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncodingException(Throwable cause) {
        super(cause);
    }

    public EncodingException(String location, String message, Object... args) {
        super(location, message, args);
    }

    public EncodingException(Enum<?> location, String message, Object... args) {
        super(location, message, args);
    }

    public EncodingException(Throwable cause, Enum<?> location, String message, Object... args) {
        super(cause, location, message, args);
    }

    public EncodingException(Throwable cause, String location, String message, Object... args) {
        super(cause, location, message, args);
    }

    public EncodingException(Throwable cause, String location) {
        super(cause, location);
    }

    public EncodingException(Throwable cause, Enum<?> location) {
        super(cause, location);
    }

    public EncodingException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

}
