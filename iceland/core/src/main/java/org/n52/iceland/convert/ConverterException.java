/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.convert;

/**
 * Exception that should be use in the {@link Converter}.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class ConverterException extends Exception {

    private static final long serialVersionUID = -8274356164290385880L;

    /**
     * Creates a new {@code ConverterException}.
     */
    public ConverterException() {
        super();
    }

    /**
     * Creates a new {@code ConverterException}.
     *
     * @param message the message
     */
    public ConverterException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code ConverterException}.
     *
     * @param exception the cause
     */
    public ConverterException(Throwable exception) {
        super(exception);
    }

    /**
     * Creates a new {@code ConverterException}.
     *
     * @param message   the message
     * @param exception the cause
     */
    public ConverterException(String message, Throwable exception) {
        super(message, exception);
    }

}
