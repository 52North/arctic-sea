/*
 * Copyright (C) 2012-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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
import org.n52.shetland.util.JavaHelper;

/**
 * @since 4.0.0
 */
public class InsertResultTemplateRequest extends OwsServiceRequest {

    private OmObservationConstellation observationTemplate;
    private SosResultTemplate resultTemplate;

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
            resultTemplate.setIdentifier(new CodeWithAuthority(JavaHelper.generateID(builder.toString())));
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

    public boolean isSetObservatioTenmplate() {
        return getObservationTemplate() != null && !getObservationTemplate().isEmpty();
    }

    public SosResultStructure getResultStructure() {
        return this.resultTemplate.getResultStructure();
    }

    public void setResultStructure(SosResultStructure resultStructure) {
        this.resultTemplate.setResultStructure(resultStructure);
    }

    public boolean isSetResultStructure() {
        return getResultStructure()!=null;
    }

    public SosResultEncoding getResultEncoding() {
        return this.resultTemplate.getResultEncoding();
    }

    public void setResultEncoding(SosResultEncoding resultEncoding) {
        this.resultTemplate.setResultEncoding(resultEncoding);;
    }

    public boolean isSetResultEncoding() {
        return getResultEncoding() != null;
    }

}
