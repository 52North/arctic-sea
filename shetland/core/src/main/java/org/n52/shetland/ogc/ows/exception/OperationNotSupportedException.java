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

/**
 * Implementation of {@link CodedOwsException} to be used if
 * <p>
 * <i>Request is for an operation that is not supported by this server.</i>
 *
 * @see <a href="http://portal.opengeospatial.org/files/?artifact_id=20040">OGC Web Service Common
 *      Specification 1.1.0, Table 25 — Standard exception codes and meanings</a>
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class OperationNotSupportedException extends CodedOwsException {
    private static final long serialVersionUID = -5985965676110111858L;

    public OperationNotSupportedException() {
        super(OwsExceptionCode.OperationNotSupported);
        setStatus(org.n52.janmayen.http.HTTPStatus.BAD_REQUEST);
    }

    public OperationNotSupportedException(final String operation) {
        super(OwsExceptionCode.OperationNotSupported);
        at(operation).withMessage("The requested operation '%s' is not supported by this service!", operation);
    }
}
