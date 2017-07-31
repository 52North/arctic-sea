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

import org.n52.shetland.inspire.GeographicalName;
import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.svalbard.coding.json.AQDJSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

public class AddressJSONDecoder extends AbstractJSONDecoder<AddressRepresentation> {

    public AddressJSONDecoder() {
        super(AddressRepresentation.class);
    }

    @Override
    public AddressRepresentation decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        AddressRepresentation address = new AddressRepresentation();
        address.setAddressFeature(parseNillableReference(node
                .path(AQDJSONConstants.ADDRESS_FEATURE)));
        address.setPostCode(parseNillableString(node
                .path(AQDJSONConstants.POST_CODE)));
        for (JsonNode n : node.path(AQDJSONConstants.ADDRESS_AREAS)) {
            address.addAddressArea(decodeJsonToNillable(n, GeographicalName.class));
        }
        for (JsonNode n : node.path(AQDJSONConstants.ADMIN_UNITS)) {
            address.addAdminUnit(decodeJsonToObject(n, GeographicalName.class));
        }
        for (JsonNode n : node.path(AQDJSONConstants.LOCATOR_DESIGNATORS)) {
            address.addLocatorDesignator(n.textValue());
        }
        for (JsonNode n : node.path(AQDJSONConstants.LOCATOR_NAMES)) {
            address.addLocatorName(decodeJsonToObject(n, GeographicalName.class));
        }
        for (JsonNode n : node.path(AQDJSONConstants.POST_NAMES)) {
            address.addPostName(decodeJsonToNillable(n, GeographicalName.class));
        }
        for (JsonNode n : node.path(AQDJSONConstants.THOROUGHFARES)) {
            address.addThoroughfare(decodeJsonToNillable(n, GeographicalName.class));
        }
        return address;
    }
}
