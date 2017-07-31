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

import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.inspire.base2.Contact;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class ContactJSONDecoder
        extends AbstractJSONDecoder<Contact> {

    public ContactJSONDecoder() {
        super(Contact.class);
    }

    @Override
    public Contact decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        Contact contact = new Contact();
        contact.setAddress(decodeJsonToNillable(node.path(AQDJSONConstants.ADDRESS), AddressRepresentation.class));
        contact.setContactInstructions(
                parseNillableString(node.path(AQDJSONConstants.CONTACT_INSTRUCTIONS)).transform(this::parseFreeText));
        contact.setElectronicMailAddress(parseNillableString(node.path(AQDJSONConstants.ELECTRONIC_MAIL_ADDRESS)));
        contact.setHoursOfService(
                parseNillableString(node.path(AQDJSONConstants.HOURS_OF_SERVICE)).transform(this::parseFreeText));
        contact.setWebsite(parseNillableString(node.path(AQDJSONConstants.WEBSITE)));
        for (JsonNode n : node.path(AQDJSONConstants.TELEPHONE_FACSIMILE)) {
            contact.addTelephoneFacsimile(parseNillableString(n));
        }
        for (JsonNode n : node.path(AQDJSONConstants.TELEPHONE_VOICE)) {
            contact.addTelephoneVoice(parseNillableString(n));
        }
        return contact;
    }

    private PT_FreeText parseFreeText(String s) {
        return new PT_FreeText().addTextGroup(new LocalisedCharacterString(s));
    }
}
