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
package org.n52.shetland.ogc.wps.exception;

import org.n52.janmayen.http.HTTPStatus;

/**
 * Implementation of {@link CodedWpsException} to be used if
 * <p>
 * <i>The JobID from the request does not match any of the Jobs running on this
 * server.</i>
 *
 * @see <a href="http://docs.opengeospatial.org/is/14-065/14-065.html#65">OGC
 *      WPS 2.0.2 Interface Standard, Table 48 — Additional exception codes for
 *      the GetStatus operation</a>
 * @author <a href="mailto:b.pross@52north.org">Benjamin Pross</a>
 *
 * @since 5.3.0
 */
public class NoSuchJobException extends CodedWpsException {

    private static final long serialVersionUID = 6934397439933833216L;

    public NoSuchJobException() {
        super(WpsExceptionCode.NoSuchJob);
        setStatus(HTTPStatus.BAD_REQUEST);
    }

    public NoSuchJobException(final String parameterName, final String value) {
        super(WpsExceptionCode.NoSuchJob);
        withMessage("The value '%s' of the parameter '%s' is invalid", value, parameterName).at(parameterName);
        setStatus(HTTPStatus.BAD_REQUEST);
    }

    public NoSuchJobException(final Enum<?> parameterName, final String value) {
        this(parameterName.name(), value);
    }
}
