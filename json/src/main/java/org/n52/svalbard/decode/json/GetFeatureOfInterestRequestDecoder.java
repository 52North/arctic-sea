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

import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.request.GetFeatureOfInterestRequest;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetFeatureOfInterestRequestDecoder extends AbstractSosRequestDecoder<GetFeatureOfInterestRequest> {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(GetFeatureOfInterestRequestDecoder.class);

    public GetFeatureOfInterestRequestDecoder() {
        super(GetFeatureOfInterestRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                SosConstants.Operations.GetFeatureOfInterest);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.GET_FEATURE_OF_INTEREST;
    }

    @Override
    protected GetFeatureOfInterestRequest decodeRequest(JsonNode node) throws DecodingException {
        GetFeatureOfInterestRequest req = new GetFeatureOfInterestRequest();
        req.setFeatureIdentifiers(decodeFeatureOfInterests(node));
        req.setProcedures(decodeProcedures(node));
        req.setObservedProperties(decodeObservedProperties(node));
        req.setSpatialFilters(decodeSpatialFilters(node));
        req.setTemporalFilters(decodeTemporalFilters(node));
        return req;
    }

    private List<SpatialFilter> decodeSpatialFilters(JsonNode node) throws DecodingException {
        JsonNode path = node.path(JSONConstants.SPATIAL_FILTER);
        return decodeJsonToObjectList(path, SpatialFilter.class);
    }

    private List<TemporalFilter> decodeTemporalFilters(JsonNode node) throws DecodingException {
        JsonNode path = node.path(JSONConstants.TEMPORAL_FILTER);
        return decodeJsonToObjectList(path, TemporalFilter.class);
    }

    private List<String> decodeObservedProperties(JsonNode node) {
        JsonNode path = node.path(JSONConstants.OBSERVED_PROPERTY);
        return parseStringOrStringList(path);
    }

    private List<String> decodeProcedures(JsonNode node) {
        JsonNode path = node.path(JSONConstants.PROCEDURE);
        return parseStringOrStringList(path);
    }

    private List<String> decodeFeatureOfInterests(JsonNode node) {
        JsonNode path = node.path(JSONConstants.FEATURE_OF_INTEREST);
        return parseStringOrStringList(path);
    }
}
