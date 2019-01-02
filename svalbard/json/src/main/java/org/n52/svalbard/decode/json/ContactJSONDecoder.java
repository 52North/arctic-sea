/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.inspire.base2.Contact;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class ContactJSONDecoder extends AbstractJSONDecoder<Contact> {

    public ContactJSONDecoder() {
        super(Contact.class);
    }

    @Override
    public Contact decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        Contact contact = new Contact();
        contact.setAddress(decodeJsonToNillable(node.path(AQDJSONConstants.ADDRESS), AddressRepresentation.class));
        contact.setContactInstructions(
                parseNillable(node.path(AQDJSONConstants.CONTACT_INSTRUCTIONS)).map(this::parseFreeText));
        contact.setElectronicMailAddress(parseNillableString(node.path(AQDJSONConstants.ELECTRONIC_MAIL_ADDRESS)));
        contact.setHoursOfService(
                parseNillable(node.path(AQDJSONConstants.HOURS_OF_SERVICE)).map(this::parseFreeText));
        contact.setWebsite(parseNillableString(node.path(AQDJSONConstants.WEBSITE)));
        JsonNode tfNode = node.path(AQDJSONConstants.TELEPHONE_FACSIMILE);
        if (tfNode.isArray()) {
            for (JsonNode n : tfNode) {
                contact.addTelephoneFacsimile(parseNillableString(n));
            }
        } else {
            contact.addTelephoneFacsimile(parseNillableString(tfNode));
        }
        JsonNode tvNode = node.path(AQDJSONConstants.TELEPHONE_VOICE);
        if (tvNode.isArray()) {
            for (JsonNode n : tvNode) {
                contact.addTelephoneVoice(parseNillableString(n));
            }
        } else {
            contact.addTelephoneVoice(parseNillableString(tvNode));
        }
        return contact;
    }

}
