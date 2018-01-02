/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.response;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosResultEncoding;
import org.n52.shetland.ogc.sos.SosResultStructure;

/**
 * @since 1.0.0
 *
 */
public class GetResultTemplateResponse extends OwsServiceResponse {

    private SosResultEncoding resultEncoding;

    private SosResultStructure resultStructure;

    public GetResultTemplateResponse() {
        super(null, null, Sos2Constants.Operations.GetResultTemplate.name());
    }

    public GetResultTemplateResponse(String service, String version) {
        super(service, version, Sos2Constants.Operations.GetResultTemplate.name());
    }

    public GetResultTemplateResponse(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public void setResultEncoding(SosResultEncoding resultEncoding) {
        this.resultEncoding = resultEncoding;
    }

    public SosResultEncoding getResultEncoding() {
        return resultEncoding;
    }

    public void setResultStructure(SosResultStructure resultStructure) {
        this.resultStructure = resultStructure;
    }

    public SosResultStructure getResultStructure() {
        return resultStructure;
    }

}
