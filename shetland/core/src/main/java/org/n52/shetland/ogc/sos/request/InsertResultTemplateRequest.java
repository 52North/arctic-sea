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
package org.n52.shetland.ogc.sos.request;

import org.joda.time.DateTime;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;
import org.n52.shetland.ogc.sos.SosResultTemplate;
import org.n52.shetland.util.IdGenerator;

/**
 * @since 1.0.0
 */
public class InsertResultTemplateRequest
        extends OwsServiceRequest {

    private OmObservationConstellation observationTemplate;
    private final SosResultTemplate resultTemplate = new SosResultTemplate();

    public InsertResultTemplateRequest() {
        super(null, null, Sos2Constants.Operations.InsertResultTemplate.name());
    }

    public InsertResultTemplateRequest(String service, String version) {
        super(service, version, Sos2Constants.Operations.InsertResultTemplate.name());
    }

    public InsertResultTemplateRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public CodeWithAuthority getIdentifier() {
        if (resultTemplate.getIdentifier() == null) {
            StringBuilder builder = new StringBuilder();
            builder.append(getObservationTemplate().toString());
            builder.append(new DateTime().getMillis());
            resultTemplate.setIdentifier(new CodeWithAuthority(IdGenerator.generate(builder.toString())));
        }
        return resultTemplate.getIdentifier();
    }

    public void setIdentifier(CodeWithAuthority identifier) {
        this.resultTemplate.setIdentifier(identifier);
    }

    public void setIdentifier(String identifier) {
        setIdentifier(new CodeWithAuthority(identifier));

    }

    public OmObservationConstellation getObservationTemplate() {
        return observationTemplate;
    }

    public void setObservationTemplate(OmObservationConstellation observationConstellation) {
        this.observationTemplate = observationConstellation;
    }

    public boolean isSetObservationTemplate() {
        return getObservationTemplate() != null && !getObservationTemplate().isEmpty();
    }

    public SosResultStructure getResultStructure() {
        return this.resultTemplate.getResultStructure();
    }

    public void setResultStructure(SosResultStructure resultStructure) {
        this.resultTemplate.setResultStructure(resultStructure);
    }

    public boolean isSetResultStructure() {
        return getResultStructure() != null;
    }

    public SosResultEncoding getResultEncoding() {
        return this.resultTemplate.getResultEncoding();
    }

    public void setResultEncoding(SosResultEncoding resultEncoding) {
        this.resultTemplate.setResultEncoding(resultEncoding);
    }

    public boolean isSetResultEncoding() {
        return getResultEncoding() != null;
    }

}
