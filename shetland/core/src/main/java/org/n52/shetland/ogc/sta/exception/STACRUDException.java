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
package org.n52.shetland.ogc.sta.exception;

import org.n52.janmayen.http.HTTPStatus;

public class STACRUDException extends Exception {

    private static final long serialVersionUID = -6227925530093069656L;

    private final HTTPStatus responseStatus;

    public STACRUDException(String msg) {
        super(msg);
        responseStatus = HTTPStatus.INTERNAL_SERVER_ERROR;
    }

    public STACRUDException(String msg, HTTPStatus status) {
        super(msg);
        responseStatus = status;
    }

    public STACRUDException(String msg, Throwable nested) {
        super(msg, nested);
        responseStatus = HTTPStatus.INTERNAL_SERVER_ERROR;
    }

    public HTTPStatus getResponseStatus() {
        return responseStatus;
    }
}
