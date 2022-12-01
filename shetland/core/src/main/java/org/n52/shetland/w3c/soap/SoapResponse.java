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
package org.n52.shetland.w3c.soap;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

/**
 * Representation of a SOAP response
 *
 * @since 1.0.0
 *
 */
public class SoapResponse extends AbstractSoap<OwsServiceResponse> {

    private OwsExceptionReport exception;

    public SoapResponse() {
    }

    public SoapResponse(String soapNamespace, String soapVersion) {
        super(soapNamespace, soapVersion);
    }

    public void setException(OwsExceptionReport owse) {
        this.exception = owse;
    }

    public OwsExceptionReport getException() {
        return exception;
    }

    public boolean hasException() {
        return exception != null;
    }
}
