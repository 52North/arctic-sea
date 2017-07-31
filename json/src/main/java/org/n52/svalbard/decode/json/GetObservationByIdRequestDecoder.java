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

import java.util.List;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetObservationByIdRequest;
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
public class GetObservationByIdRequestDecoder extends AbstractSosRequestDecoder<GetObservationByIdRequest> {
    public GetObservationByIdRequestDecoder() {
        super(GetObservationByIdRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                SosConstants.Operations.GetObservationById);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.GET_OBSERVATION_BY_ID;
    }

    @Override
    protected GetObservationByIdRequest decodeRequest(JsonNode node) {
        GetObservationByIdRequest req = new GetObservationByIdRequest();
        req.setObservationIdentifier(parseObservationIdentifiers(node));
        return req;
    }

    private List<String> parseObservationIdentifiers(JsonNode node) {
        return parseStringOrStringList(node.path(JSONConstants.OBSERVATION));
    }
}
