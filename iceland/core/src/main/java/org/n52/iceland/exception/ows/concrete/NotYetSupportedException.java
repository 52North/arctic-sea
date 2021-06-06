/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import com.google.common.base.Joiner;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class NotYetSupportedException extends NoApplicableCodeException {
    private static final long serialVersionUID = 2406018601586191L;

    public NotYetSupportedException(String feature) {
        withMessage("%s is not yet supported", feature);
        setStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
    }

    public NotYetSupportedException(String type, Object feature) {
        withMessage("The %s %s is not yet supported", type, feature);
        setStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
    }

    public NotYetSupportedException(String type, Object feature, Object... supportedFeatures) {
        withMessage("The %s %s is not yet supported. Currently supported: %s",
                    type, feature, Joiner.on(", ").join(supportedFeatures));
        setStatus(HTTPStatus.INTERNAL_SERVER_ERROR);
    }

}
