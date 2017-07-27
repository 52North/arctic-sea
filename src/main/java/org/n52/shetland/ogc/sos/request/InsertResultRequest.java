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
package org.n52.shetland.ogc.sos.request;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;

import com.google.common.base.Strings;

/**
 * @since 1.0.0
 *
 */
public class InsertResultRequest
        extends OwsServiceRequest {

    private String templateIdentifier;
    private String resultValues;

    public InsertResultRequest() {
        super(null, null, Sos2Constants.Operations.InsertResult.name());
    }

    public InsertResultRequest(String service, String version) {
        super(service, version, Sos2Constants.Operations.InsertResult.name());
    }

    public InsertResultRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public void setTemplateIdentifier(String templateIdentifier) {
        this.templateIdentifier = templateIdentifier;
    }

    public String getTemplateIdentifier() {
        return templateIdentifier;
    }

    public boolean isSetTemplateIdentifier() {
        return !Strings.isNullOrEmpty(getTemplateIdentifier());
    }

    public void setResultValues(String resultValues) {
        this.resultValues = resultValues;
    }

    public String getResultValues() {
        return resultValues;
    }

    public boolean isSetResultValues() {
        return !Strings.isNullOrEmpty(getResultValues());
    }

}
