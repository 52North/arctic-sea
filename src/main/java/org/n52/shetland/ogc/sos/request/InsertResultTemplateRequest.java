/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import org.joda.time.DateTime;

import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.util.JavaHelper;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;

import com.google.common.base.Strings;

import org.n52.shetland.ogc.sos.Sos2Constants;

/**
 * @since 4.0.0
 */
public class InsertResultTemplateRequest extends OwsServiceRequest {

    private String identifier;
    private OmObservationConstellation observationTemplate;
    private SosResultStructure resultStructure;
    private SosResultEncoding resultEncoding;

    public InsertResultTemplateRequest() {
        super(null, null, Sos2Constants.Operations.InsertResultTemplate.name());
    }

    public InsertResultTemplateRequest(String service, String version) {
        super(service, version, Sos2Constants.Operations.InsertResultTemplate.name());
    }

    public InsertResultTemplateRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public String getIdentifier() {
        if (Strings.isNullOrEmpty(identifier)) {
            StringBuilder builder = new StringBuilder();
            builder.append(getObservationTemplate().toString());
            builder.append(new DateTime().getMillis());
            identifier = JavaHelper.generateID(builder.toString());
        }
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isSetIdentifier() {
        return !Strings.isNullOrEmpty(getIdentifier());
    }

    public OmObservationConstellation getObservationTemplate() {
        return observationTemplate;
    }

    public void setObservationTemplate(OmObservationConstellation observationConstellation) {
        this.observationTemplate = observationConstellation;
    }

    public boolean isSetObservatioTenmplate() {
        return getObservationTemplate() != null && !getObservationTemplate().isEmpty();
    }

    public SosResultStructure getResultStructure() {
        return resultStructure;
    }

    public void setResultStructure(SosResultStructure resultStructure) {
        this.resultStructure = resultStructure;
    }

    public boolean isSetResultStructure() {
        return getResultStructure() != null && !getResultStructure().isEmpty();
    }

    public SosResultEncoding getResultEncoding() {
        return resultEncoding;
    }

    public void setResultEncoding(SosResultEncoding resultEncoding) {
        this.resultEncoding = resultEncoding;
    }

    public boolean isSetResultEncoding() {
        return getResultEncoding() != null && !getResultEncoding().isEmpty();
    }

}
