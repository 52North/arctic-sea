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
package org.n52.shetland.ogc.ows.exception;

import java.util.Iterator;
import java.util.List;

import org.n52.janmayen.http.HTTPStatus;
import org.n52.shetland.ogc.ows.OWSConstants;

/**
 * Implementation of the OWS service exception. The exception codes are defined
 * according the <a
 * href="http://portal.opengeospatial.org/files/?artifact_id=20040">OGC Web
 * Service Common Specification 1.1.0</a>
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public abstract class OwsExceptionReport extends Exception {

    private static final long serialVersionUID = 52L;
    private HTTPStatus status;
    private String version;

    /**
     * @return Returns the ExceptionTypes of this exception
     */
    public abstract List<? extends CodedException> getExceptions();

    /**
     * Set service version
     *
     * @param version
     *            the version to set
     *
     * @return this
     */
    public final OwsExceptionReport setVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * Get service version
     *
     * @return service version
     */
    public final String getVersion() {
        if (version == null) {
            this.version = OWSConstants.VERSION;
        }
        return version;
    }

    public String getNamespace() {
        return OWSConstants.NS_OWS;
    }

    @Override
    public String getMessage() {
        final StringBuilder faultString = new StringBuilder();
        final Iterator<? extends CodedException> i = getExceptions().iterator();
        boolean first = true;
        while (i.hasNext()) {
            if (first) {
                first = false;
            } else {
                faultString.append('\n');
            }

            CodedException e = i.next();
            faultString.append(e.getMessage());
            if (e.getCause() != null) {
                faultString.append(" (caused by ").append(e.getCause().getMessage()).append(")");
            }
        }
        return faultString.toString();
    }

    /**
     * @return the HTTP response code of this {@code OwsExceptionReport} or<br>
     *         {@code getExceptions().get(0).getStatus()} if it is not set and
     *         {@code getExceptions().get(0) != this}.
     */
    public final HTTPStatus getStatus() {
        if (status == null && getExceptions().get(0) != this) {
            return getExceptions().get(0).getStatus();
        }
        return status;
    }

    /**
     * @return <tt>true</tt>, if a HTTP response code for this
     *         {@code OwsExceptionReport} or any sub exception is available
     */
    public final boolean hasStatus() {
        return getStatus() != null;
    }

    /**
     * Sets the HTTP response code for this {@code OwsExceptionReport}.
     *
     * @param status
     *            the code
     *
     * @return this (for method chaining)
     */
    public final OwsExceptionReport setStatus(HTTPStatus status) {
        this.status = status;
        return this;
    }
}
