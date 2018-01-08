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

import org.n52.janmayen.http.HTTPStatus;

/**
 * Implementation of {@link CodedOwsException} to be used if <p> <i>Operation
 * request does not include a parameter value, and this server did not declare a
 * default value for that parameter.</i>
 *
 * @see <a href="http://portal.opengeospatial.org/files/?artifact_id=20040">OGC
 *      Web Service Common Specification 1.1.0, Table 25 — Standard exception codes and
 *      meanings</a>
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class MissingParameterValueException extends CodedOwsException {
    private static final long serialVersionUID = 236478803986562631L;

    public MissingParameterValueException(final String parameter) {
        super(OwsExceptionCode.MissingParameterValue);
        at(parameter).withMessage("The value for the parameter '%s' is missing in the request!", parameter);
        setStatus(HTTPStatus.BAD_REQUEST);
    }

    public MissingParameterValueException(final Enum<?> parameter) {
        this(parameter.name());
    }

    public MissingParameterValueException() {
        super(OwsExceptionCode.MissingParameterValue);
        setStatus(HTTPStatus.BAD_REQUEST);
    }
}
