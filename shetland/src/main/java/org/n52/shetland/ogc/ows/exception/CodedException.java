/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows.exception;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public abstract class CodedException extends OwsExceptionReport {
    private static final long serialVersionUID = 8213294348107603436L;
    private final List<CodedException> exceptions = Collections.singletonList(this);
    private final ExceptionCode code;
    private String locator;
    private String message;

    public CodedException(final ExceptionCode code) {
        this.code = code;
    }

    public final ExceptionCode getCode() {
        return this.code;
    }

    public final String getLocator() {
        return this.locator;
    }

    @Override
    public final String getMessage() {
        return this.message;
    }

    public final boolean hasMessage() {
        return getMessage() != null && !getMessage().isEmpty();
    }

    @Override
    public final List<CodedException> getExceptions() {
        return this.exceptions;
    }

    public final CodedException at(final String locator) {
        this.locator = locator;
        return this;
    }

    public final CodedException at(final Enum<?> locator) {
        return at(locator.name());
    }

    /**
     * @param message
     *            the message format
     * @param args
     *            the optional formatting arguments
     *
     * @return this
     *
     * @see String#format(java.lang.String, java.lang.Object[])
     */
    public final CodedException withMessage(final String message, final Object... args) {
        this.message = (args != null && args.length > 0) ? String.format(message, args) : message;
        return this;
    }

    public final CodedException causedBy(final Throwable exception) {
        return (CodedException) initCause(exception);
    }
}
