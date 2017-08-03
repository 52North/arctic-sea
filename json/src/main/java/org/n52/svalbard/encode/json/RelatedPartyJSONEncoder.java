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

import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class RelatedPartyJSONEncoder extends JSONEncoder<RelatedParty> {

    public RelatedPartyJSONEncoder() {
        super(RelatedParty.class);
    }

    @Override
    public JsonNode encodeJSON(RelatedParty t)
            throws EncodingException {
        ObjectNode j = nodeFactory().objectNode();
        j.set(AQDJSONConstants.CONTACT, encodeObjectToJson(t.getContact()));
        j.set(AQDJSONConstants.INDIVIDUAL_NAME, encodeObjectToJson(t.getIndividualName()));
        j.set(AQDJSONConstants.ORGANISATION_NAME, encodeObjectToJson(t.getOrganisationName()));
        j.set(AQDJSONConstants.POSITION_NAME, encodeObjectToJson(t.getPositionName()));
        j.set(AQDJSONConstants.ROLES, encodeObjectToJson(t.getRoles()));
        return j;
    }

}
