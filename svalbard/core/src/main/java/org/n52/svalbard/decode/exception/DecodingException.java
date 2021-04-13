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
package org.n52.svalbard.decode.exception;

import org.n52.janmayen.exception.LocationHintException;

/**
 *
 * @author Matthes Rieke
 */
public class DecodingException extends LocationHintException {
    private static final long serialVersionUID = 1864784275478310843L;

    public DecodingException(String message, Object... args) {
        super(message, args);
    }

    public DecodingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodingException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public DecodingException(String location, String message, Object... args) {
        super(location, message, args);
    }

    public DecodingException(Enum<?> location, String message, Object... args) {
        super(location, message, args);
    }

    public DecodingException(Throwable cause, Enum<?> location, String message, Object... args) {
        super(cause, location, message, args);
    }

    public DecodingException(Throwable cause, String location, String message, Object... args) {
        super(cause, location, message, args);
    }

    public DecodingException(Throwable cause, String location) {
        super(cause, location);
    }

    public DecodingException(Throwable cause, Enum<?> location) {
        super(cause, location);
    }

    public DecodingException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

}
