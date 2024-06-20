/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

package org.n52.svalbard.encode.json.sensorML.v20;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.sensorML.SmlContact;
import org.n52.shetland.ogc.sensorML.SmlPerson;
import org.n52.shetland.ogc.sensorML.SmlResponsibleParty;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;

/**
 * JSON Encoder for SmlContact
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 * @see SmlContact
 */
public class ContactJsonEncoder extends JSONEncoder<SmlContact> implements SensorML20JsonEncoder {

    protected static final String EMAIL = "email";

    public ContactJsonEncoder() {
        super(SmlContact.class);
    }

    @Override public JsonNode encodeJSON(SmlContact contact) throws EncodingException {
        ObjectNode json = JSON_FACTORY.objectNode();

        switch (contact.getClass().getSimpleName()) {
            case "SmlPerson":
                SmlPerson person = (SmlPerson) contact;
                if (person.isSetUserID()) {
                    json.put("userID", person.getUserID());
                }
                if (person.isSetAffiliation()) {
                    json.put("affiliation", person.getAffiliation());
                }
                if (person.isSetEmail()) {
                    json.put(EMAIL, person.getEmail());
                }
                if (person.isSetName()) {
                    json.put("name", person.getName());
                }
                if (person.isSetPhoneNumber()) {
                    json.put("phoneNumber", person.getPhoneNumber());
                }
                if (person.isSetSurname()) {
                    json.put("surname", person.getSurname());
                }
                break;
            case "SmlResponsibleParty":
                SmlResponsibleParty rp = (SmlResponsibleParty) contact;
                if (rp.isSetIndividualName()) {
                    json.put("individualName", rp.getIndividualName());
                }
                if (rp.isSetOrganizationName()) {
                    json.put("organizationName", rp.getOrganizationName());
                }
                if (rp.isSetPositionName()) {
                    json.put("positionName", rp.getPositionName());
                }
                if (rp.isSetPhoneVoice()) {
                    ArrayNode array = JSON_FACTORY.arrayNode();
                    rp.getPhoneVoice().forEach(array::add);
                    json.put("phoneVoice", array);
                }
                if (rp.isSetPhoneFax()) {
                    ArrayNode array = JSON_FACTORY.arrayNode();
                    rp.getPhoneFax().forEach(array::add);
                    json.put("phoneFax", array);
                }
                if (rp.isSetDeliveryPoint()) {
                    ArrayNode array = JSON_FACTORY.arrayNode();
                    rp.getDeliveryPoint().forEach(array::add);
                    json.put("deliveryPoints", array);
                }
                if (rp.isSetCity()) {
                    json.put("city", rp.getCity());
                }
                if (rp.isSetAdministrativeArea()) {
                    json.put("administrativeArea", rp.getAdministrativeArea());
                }
                if (rp.isSetPostalCode()) {
                    json.put("postalCode", rp.getPostalCode());
                }
                if (rp.isSetCountry()) {
                    json.put("country", rp.getCountry());
                }
                if (rp.isSetEmail()) {
                    json.put(EMAIL, rp.getEmail());
                }
                if (rp.isSetOnlineResources()) {
                    ArrayNode array = JSON_FACTORY.arrayNode();
                    rp.getOnlineResources().forEach(array::add);
                    json.put("onlineResources", array);
                }
                if (rp.isSetHoursOfService()) {
                    json.put("hoursOfService", rp.getHoursOfService());
                }
                if (rp.isSetContactInstructions()) {
                    json.put("contactInstructions", rp.getContactInstructions());
                }
                break;
            default:
                throw new EncodingException(
                    "Could not recognize sml:Contact subtype: " + contact.getClass().getSimpleName());
        }
        return json;
    }
}
