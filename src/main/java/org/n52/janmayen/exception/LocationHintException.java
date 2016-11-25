/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.exception;

import java.util.Optional;

import com.google.common.base.Strings;

/**
 * Exception with an additional location hint.
 *
 * @author Christian Autermann
 */
public abstract class LocationHintException extends StringInterpolationException {
    private static final long serialVersionUID = -5034943514826747244L;
    private final String location;

    public LocationHintException(String message, Object... args) {
        this(null, (String) null, message, args);
    }

    public LocationHintException(String message, Throwable cause) {
        this(cause, (String) null, message, (Object[]) null);
    }

    public LocationHintException(Throwable cause) {
        this(cause, (String) null, null, (Object[]) null);
    }

    public LocationHintException(Throwable cause, String location) {
        this(cause, location, null, (Object[]) null);
    }

    public LocationHintException(Throwable cause, Enum<?> location) {
        this(cause, location, null, (Object[]) null);
    }

    public LocationHintException(String location, String message, Object... args) {
        this(null, location, message, args);
    }

    public LocationHintException(Enum<?> location, String message, Object... args) {
        this(null, location, message, args);
    }

    public LocationHintException(Throwable cause, Enum<?> location, String message, Object... args) {
        this(cause, location == null ? (String) null : location.name(), message, args);
    }

    public LocationHintException(Throwable cause, String location, String message, Object... args) {
        super(cause, message, args);
        this.location = Strings.emptyToNull(location);
    }

    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }

}
