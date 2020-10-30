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
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.sensorML.v20.PhysicalComponent;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * This encoder encodes a SensorML20 PhysicalComponent according to OGC BP 17-011r2.
 * Note: The encoder assumes the input Object to be valid and does not validate syntax nor semantic before encoding.
 * Note: Currently only a subset is implemented.
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class PhysicalComponentJsonEncoder extends AbstractPhysicalProcessJsonEncoder<PhysicalComponent> {

    public PhysicalComponentJsonEncoder() {
        super(PhysicalComponent.class);
    }

    @Override public JsonNode encodeJSON(PhysicalComponent physicalComponent) throws EncodingException {
        ObjectNode json = (ObjectNode) super.encodeJSON(physicalComponent);
        if (physicalComponent.isSetMethod()) {
            json.put(METHOD, encodeObjectToJson(physicalComponent.getMethod()));
        }
        return json;
    }
}
