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

import org.n52.shetland.inspire.base2.Contact;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class RelatedPartyJSONDecoder
        extends AbstractJSONDecoder<RelatedParty> {

    public RelatedPartyJSONDecoder() {
        super(RelatedParty.class);
    }

    @Override
    public RelatedParty decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        RelatedParty relatedParty = new RelatedParty();
        relatedParty.setContact(decodeJsonToNillable(node.path(AQDJSONConstants.CONTACT), Contact.class));
        relatedParty.setIndividualName(
                parseNillableString(node.path(AQDJSONConstants.INDIVIDUAL_NAME)).transform(this::parseFreeText));
        relatedParty.setOrganisationName(
                parseNillableString(node.path(AQDJSONConstants.ORGANISATION_NAME)).transform(this::parseFreeText));
        relatedParty.setPositionName(
                parseNillableString(node.path(AQDJSONConstants.POSITION_NAME)).transform(this::parseFreeText));
        for (JsonNode n : node.path(AQDJSONConstants.ROLES)) {
            relatedParty.addRole(parseNillableReference(n));
        }
        return relatedParty;
    }

    private PT_FreeText parseFreeText(String s) {
        return new PT_FreeText().addTextGroup(new LocalisedCharacterString(s));
    }

}