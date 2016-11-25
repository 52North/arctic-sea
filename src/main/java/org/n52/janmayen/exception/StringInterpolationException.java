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

import com.google.common.base.Strings;

/**
 * Exception with additional formatting parameters.
 *
 * @author Christian Autermann
 */
public abstract class StringInterpolationException extends Exception {
    private static final long serialVersionUID = -3972285029743751087L;

    public StringInterpolationException(String message, Object... args) {
        this(null, message, args);
    }

    public StringInterpolationException(Throwable cause) {
        this(cause, null, (Object[]) null);
    }

    public StringInterpolationException(Throwable cause, String message, Object... args) {
        super(format(message, args), cause);
    }

    private static String format(String message, Object[] args) {
        return args != null && args.length > 0 ? String.format(message, args) : Strings.emptyToNull(message);
    }
}
