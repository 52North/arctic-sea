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
package org.n52.svalbard.decode.json;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetResultTemplateRequest;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetResultTemplateRequestDecoder extends AbstractSosRequestDecoder<GetResultTemplateRequest> {
    public GetResultTemplateRequestDecoder() {
        super(GetResultTemplateRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.GetResultTemplate);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.GET_RESULT_TEMPLATE;
    }

    @Override
    protected GetResultTemplateRequest decodeRequest(JsonNode node) {
        GetResultTemplateRequest req = new GetResultTemplateRequest();
        req.setObservedProperty(node.path(JSONConstants.OBSERVED_PROPERTY).textValue());
        req.setOffering(node.path(JSONConstants.OFFERING).textValue());
        return req;
    }
}
