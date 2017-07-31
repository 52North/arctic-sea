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

import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class AddressJSONEncoder extends JSONEncoder<AddressRepresentation> {

    public AddressJSONEncoder() {
        super(AddressRepresentation.class);
    }

    @Override
    public JsonNode encodeJSON(AddressRepresentation t)
            throws EncodingException {
        ObjectNode j = nodeFactory().objectNode();
        j.set(AQDJSONConstants.ADDRESS_AREAS, encodeObjectToJson(t.getAddressAreas()));
        j.set(AQDJSONConstants.ADDRESS_FEATURE, encodeObjectToJson(t.getAddressFeature()));
        j.set(AQDJSONConstants.ADMIN_UNITS, encodeObjectToJson(t.getAdminUnits()));
        j.set(AQDJSONConstants.LOCATOR_DESIGNATORS, encodeObjectToJson(t.getLocatorDesignators()));
        j.set(AQDJSONConstants.LOCATOR_NAMES, encodeObjectToJson(t.getLocatorNames()));
        j.set(AQDJSONConstants.POST_CODE, encodeObjectToJson(t.getPostCode()));
        j.set(AQDJSONConstants.POST_NAMES, encodeObjectToJson(t.getPostNames()));
        j.set(AQDJSONConstants.THOROUGHFARES, encodeObjectToJson(t.getThoroughfares()));
        return j;
    }

}
