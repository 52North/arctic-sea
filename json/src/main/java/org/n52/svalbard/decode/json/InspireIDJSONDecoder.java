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

import org.n52.faroe.Validation;
import org.n52.shetland.inspire.base.Identifier;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class InspireIDJSONDecoder extends AbstractJSONDecoder<Identifier> {

    public InspireIDJSONDecoder() {
        super(Identifier.class);
    }

    @Override
    public Identifier decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        Identifier inspireID = new Identifier(getLocalId(node), getNamespace(node));
        inspireID.setVersionId(parseNillableString(node.path(AQDJSONConstants.VERSION_ID)));
        return inspireID;
    }

    private String getNamespace(JsonNode node) throws DecodingException {
        String namespace = node.path(AQDJSONConstants.NAMESPACE).textValue();
        Validation.notNullOrEmpty("Identifier namespace", namespace);
        return namespace;
    }

    private String getLocalId(JsonNode node) throws DecodingException {
        String localId = node.path(AQDJSONConstants.LOCAL_ID).textValue();
        Validation.notNullOrEmpty("Identifier localId", localId);
        return localId;
    }

}
