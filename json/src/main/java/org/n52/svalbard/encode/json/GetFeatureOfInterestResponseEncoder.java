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
package org.n52.svalbard.encode.json;

import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GetFeatureOfInterestResponseEncoder extends AbstractSosResponseEncoder<GetFeatureOfInterestResponse> {
    public GetFeatureOfInterestResponseEncoder() {
        super(GetFeatureOfInterestResponse.class, SosConstants.Operations.GetFeatureOfInterest);
    }

    @Override
    protected void encodeResponse(ObjectNode json, GetFeatureOfInterestResponse t) throws EncodingException {
        json.set(JSONConstants.FEATURE_OF_INTEREST, encodeObjectToJson(t.getAbstractFeature()));
    }
}
