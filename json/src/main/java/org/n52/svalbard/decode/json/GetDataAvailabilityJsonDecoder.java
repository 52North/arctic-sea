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
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityConstants;
import org.n52.shetland.ogc.sos.gda.GetDataAvailabilityRequest;
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
public class GetDataAvailabilityJsonDecoder extends AbstractSosRequestDecoder<GetDataAvailabilityRequest> {
    public GetDataAvailabilityJsonDecoder() {
        super(GetDataAvailabilityRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                GetDataAvailabilityConstants.OPERATION_NAME);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.GET_DATA_AVAILABILITY;
    }

    @Override
    protected GetDataAvailabilityRequest decodeRequest(JsonNode node) {
        GetDataAvailabilityRequest req = new GetDataAvailabilityRequest();
        if (node.has(JSONConstants.PROCEDURE)) {
            parseStringOrStringList(node.path(JSONConstants.PROCEDURE)).forEach(req::addProcedure);
        }
        if (node.has(JSONConstants.OBSERVED_PROPERTY)) {
            parseStringOrStringList(node.path(JSONConstants.OBSERVED_PROPERTY)).forEach(req::addObservedProperty);
        }
        if (node.has(JSONConstants.FEATURE_OF_INTEREST)) {
            parseStringOrStringList(node.path(JSONConstants.FEATURE_OF_INTEREST)).forEach(req::addFeatureOfInterest);
        }
        if (node.has(JSONConstants.OFFERING)) {
            parseStringOrStringList(node.path(JSONConstants.OFFERING)).forEach(req::addOffering);
        }
        if (node.has(JSONConstants.RESPONSE_FORMAT)) {
            req.setResponseFormat(node.path(JSONConstants.RESPONSE_FORMAT).textValue());
        }
        return req;
    }
}
