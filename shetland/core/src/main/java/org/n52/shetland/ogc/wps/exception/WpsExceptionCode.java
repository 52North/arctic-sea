/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.ows.exception.ExceptionCode;
import org.n52.shetland.ogc.wps.WPS200Constants;

/**
 * @author <a href="mailto:b.pross@52north.org">Benjamin Pross</a>
 *
 * @since 5.3.0
 */
public enum WpsExceptionCode implements ExceptionCode {

    NoSuchJob(WPS200Constants.REASON_NO_SUCH_JOB),
    ResultNotReady(WPS200Constants.REASON_RESULT_NOT_READY);

    private final String soapFaultReason;

    WpsExceptionCode(String soapFaultReason) {
        this.soapFaultReason = soapFaultReason;
    }

    @Override
    public String getSoapFaultReason() {
        return soapFaultReason;
    }

}
