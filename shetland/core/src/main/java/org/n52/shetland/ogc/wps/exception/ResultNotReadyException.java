/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
 * <i>The result for the requested JobID has not yet been generated</i>
 *
 * @see <a href="http://docs.opengeospatial.org/is/14-065/14-065.html#69">OGC
 *      WPS 2.0.2 Interface Standard, Table 50 — Additional exception codes for
 *      the GetResult operation</a>
 * @author <a href="mailto:b.pross@52north.org">Benjamin Pross</a>
 *
 * @since 5.3.0
 */
public class ResultNotReadyException extends CodedWpsException {

    private static final long serialVersionUID = 8025824035646499811L;

    public ResultNotReadyException() {
        super(WpsExceptionCode.ResultNotReady);
        setStatus(HTTPStatus.BAD_REQUEST);
    }

    public ResultNotReadyException(final String parameterName, final String value) {
        super(WpsExceptionCode.ResultNotReady);
        withMessage("The result for the job with id '%s' is not ready.", value).at(parameterName);
        setStatus(HTTPStatus.BAD_REQUEST);
    }

    public ResultNotReadyException(final Enum<?> parameterName, final String value) {
        this(parameterName.name(), value);
    }
}
