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
import org.n52.shetland.ogc.sos.request.GetResultRequest;
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
public class GetResultRequestDecoder extends AbstractSosRequestDecoder<GetResultRequest> {

    public GetResultRequestDecoder() {
        super(GetResultRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                SosConstants.Operations.GetResult);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.GET_RESULT;
    }

    @Override
    protected GetResultRequest decodeRequest(JsonNode node) throws DecodingException {
        GetResultRequest req = new GetResultRequest();
        req.setFeatureIdentifiers(parseFeatureIdentifiers(node));
        req.setObservationTemplateIdentifier(parseObservationTemplateIdentifier(node));
        req.setObservedProperty(parseObservedProperty(node));
        req.setOffering(parseOffering(node));
        req.setSpatialFilter(parseSpatialFilter(node));
        req.setTemporalFilter(parseTemporalFilters(node));
        return req;
    }

    private List<String> parseFeatureIdentifiers(JsonNode node) {
        return parseStringOrStringList(node.path(JSONConstants.FEATURE_OF_INTEREST));
    }

    private String parseObservationTemplateIdentifier(JsonNode node) {
        return node.path(JSONConstants.OBSERVATION_TEMPLATE).textValue();
    }

    private String parseObservedProperty(JsonNode node) {
        return node.path(JSONConstants.OBSERVED_PROPERTY).textValue();
    }

    private String parseOffering(JsonNode node) {
        return node.path(JSONConstants.OFFERING).textValue();
    }

    private SpatialFilter parseSpatialFilter(JsonNode node) throws DecodingException {
        return decodeJsonToObject(node.path(JSONConstants.SPATIAL_FILTER), SpatialFilter.class);
    }

    private List<TemporalFilter> parseTemporalFilters(JsonNode node) throws DecodingException {
        return decodeJsonToObjectList(node.path(JSONConstants.TEMPORAL_FILTER), TemporalFilter.class);
    }

}
