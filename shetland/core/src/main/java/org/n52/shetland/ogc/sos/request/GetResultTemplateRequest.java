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
package org.n52.shetland.ogc.sos.request;

import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;

import com.google.common.base.Strings;

/**
 * @since 1.0.0
 *
 */
public class GetResultTemplateRequest extends OwsServiceRequest {

    private String offering;

    private String observedProperty;

    public GetResultTemplateRequest() {
        super(null, null, Sos2Constants.Operations.GetResultTemplate.name());
    }

    public GetResultTemplateRequest(String service, String version) {
        super(service, version, Sos2Constants.Operations.GetResultTemplate.name());
    }

    public GetResultTemplateRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public String getOffering() {
        return offering;
    }

    public void setOffering(String offering) {
        this.offering = offering;
    }

    public boolean isSetOffering() {
        return !Strings.isNullOrEmpty(getOffering());
    }

    public String getObservedProperty() {
        return observedProperty;
    }

    public void setObservedProperty(String observedProperty) {
        this.observedProperty = observedProperty;
    }

    public boolean isSetObservedProperty() {
        return !Strings.isNullOrEmpty(getObservedProperty());
    }

}
