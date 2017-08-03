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

import org.n52.shetland.inspire.base2.Contact;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ContactJSONEncoder extends JSONEncoder<Contact> {

    public ContactJSONEncoder() {
        super(Contact.class);
    }

    @Override
    public JsonNode encodeJSON(Contact t)
            throws EncodingException {
        ObjectNode j = nodeFactory().objectNode();
        j.set(AQDJSONConstants.ADDRESS, encodeObjectToJson(t.getAddress()));
        j.set(AQDJSONConstants.CONTACT_INSTRUCTIONS, encodeObjectToJson(t.getContactInstructions()));
        j.set(AQDJSONConstants.ELECTRONIC_MAIL_ADDRESS, encodeObjectToJson(t.getElectronicMailAddress()));
        j.set(AQDJSONConstants.HOURS_OF_SERVICE, encodeObjectToJson(t.getHoursOfService()));
        j.set(AQDJSONConstants.TELEPHONE_FACSIMILE, encodeObjectToJson(t.getTelephoneFacsimile()));
        j.set(AQDJSONConstants.TELEPHONE_VOICE, encodeObjectToJson(t.getTelephoneVoice()));
        j.set(AQDJSONConstants.WEBSITE, encodeObjectToJson(t.getWebsite()));
        return j;
    }

}
