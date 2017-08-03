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

import org.n52.janmayen.Json;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetObservationRequest;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetObservationRequestDecoder extends AbstractSosRequestDecoder<GetObservationRequest> {

    public GetObservationRequestDecoder() {
        super(GetObservationRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                SosConstants.Operations.GetObservation);
    }

    @Override
    public String getSchemaURI() {
        return SchemaConstants.Request.GET_OBSERVATION;
    }

    @Override
    public GetObservationRequest decodeRequest(JsonNode node) throws DecodingException {
        GetObservationRequest r = new GetObservationRequest();
        r.setFeatureIdentifiers(parseStringOrStringList(node.path(JSONConstants.FEATURE_OF_INTEREST)));
        r.setObservedProperties(parseStringOrStringList(node.path(JSONConstants.OBSERVED_PROPERTY)));
        r.setOfferings(parseStringOrStringList(node.path(JSONConstants.OFFERING)));
        r.setProcedures(parseStringOrStringList(node.path(JSONConstants.PROCEDURE)));
        r.setResponseFormat(node.path(JSONConstants.RESPONSE_FORMAT).textValue());
        r.setResponseMode(node.path(JSONConstants.RESPONSE_MODE).textValue());
        r.setResultModel(node.path(JSONConstants.RESULT_MODEL).textValue());
        r.setResultFilter(parseComparisonFilter(node.path(JSONConstants.RESULT_FILTER)));
        r.setSpatialFilter(parseSpatialFilter(node.path(JSONConstants.SPATIAL_FILTER)));
        r.setTemporalFilters(parseTemporalFilters(node.path(JSONConstants.TEMPORAL_FILTER)));
        // TODO whats that for?
        r.setRequestString(Json.print(node));
        return r;
    }

    private List<TemporalFilter> parseTemporalFilters(JsonNode node) throws DecodingException {
        return decodeJsonToObjectList(node, TemporalFilter.class);
    }

    private SpatialFilter parseSpatialFilter(JsonNode node) throws DecodingException {
        return decodeJsonToObject(node, SpatialFilter.class);
    }
}
