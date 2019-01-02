/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import javax.annotation.Nullable;

/**
 * Exception with additional formatting parameters.
 *
 * @author Christian Autermann
 */
public abstract class StringInterpolationException extends Exception {
    private static final long serialVersionUID = -3972285029743751087L;

    public StringInterpolationException(@Nullable String message,
                                        @Nullable Object... args) {
        this(null, message, args);
    }

    public StringInterpolationException(@Nullable Throwable cause) {
        this(cause, null, (Object[]) null);
    }

    public StringInterpolationException(@Nullable Throwable cause,
                                        @Nullable String message,
                                        @Nullable Object... args) {
        super(format(message, args), cause);
    }

    @Nullable
    private static String format(@Nullable String message,
                                 @Nullable Object[] args) {
        if (message == null || message.isEmpty()) {
            return null;
        }
        if (args != null && args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }
}
