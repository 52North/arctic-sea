/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.ogc.ows.OWSConstants;

/**
 * ExceptionCodes are defined in
 * <a href="http://portal.opengeospatial.org/files/?artifact_id=20040">OGC Web
 * Service Common Specification 1.1.0</a>.
 *
 * @since 1.0.0
 */
public enum OwsExceptionCode implements ExceptionCode {
    InvalidParameterValue(OWSConstants.SOAP_REASON_INVALID_PARAMETER_VALUE),
    InvalidUpdateSequence(OWSConstants.SOAP_REASON_INVALID_UPDATE_SEQUENCES),
    MissingParameterValue(OWSConstants.SOAP_REASON_MISSING_PARAMETER_VALUE),
    NoApplicableCode(OWSConstants.SOAP_REASON_NO_APPLICABLE_CODE),
    OperationNotSupported(OWSConstants.SOAP_REASON_OPTION_NOT_SUPPORTED),
    OptionNotSupported(OWSConstants.SOAP_REASON_OPERATION_NOT_SUPPORTED),
    VersionNegotiationFailed(OWSConstants.SOAP_REASON_VERSION_NEGOTIATION_FAILED);
    private final String soapFaulReason;

    OwsExceptionCode(String soapFaultReason) {
        this.soapFaulReason = soapFaultReason;
    }

    @Override
    public String getSoapFaultReason() {
        return soapFaulReason;
    }
}
