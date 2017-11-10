/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.exception.ows.concrete;

import org.n52.janmayen.http.HTTPStatus;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class ServiceOperatorNotFoundException extends NoApplicableCodeException {

    private static final long serialVersionUID = -6246970662804536844L;

    /**
     * @param request
     *            The class of the request that could not be handled
     */
    public ServiceOperatorNotFoundException(final OwsServiceRequest request) {
        withMessage("Submission of core request '%s' failed. Could not get request operator for request type.",
                request != null ? request.getClass().getName() : null);
        setStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
    }

}
