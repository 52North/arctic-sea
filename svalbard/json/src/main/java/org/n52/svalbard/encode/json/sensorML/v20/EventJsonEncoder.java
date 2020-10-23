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
import org.n52.shetland.ogc.sensorML.elements.SmlEvent;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;

/**
 * JSON Encoder for SmlEvent
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 * @see SmlEvent
 */
public class EventJsonEncoder extends JSONEncoder<SmlEvent> implements SensorML20JsonEncoder {

    public EventJsonEncoder() {
        super(SmlEvent.class);
    }

    @Override public JsonNode encodeJSON(SmlEvent event) throws EncodingException {
        ObjectNode json = jsonFactory.objectNode();
        json.put(TYPE, TERM);
        if (event.isSetDescription()) {
            json.put(DESCRIPTION, event.getDescription());
        }
        if (event.isSetLabel()) {
            json.put(LABEL, event.getLabel());
        }
        if (event.isSetIdentifier()) {
            json.put(IDENTIFIER, event.getIdentifier());
        }
        if (event.isSetDefinition()) {
            json.put(DEFINITION, event.getDefinition());
        }
        if (event.getClassification() != null) {
            json.put(CLASSIFICATION, encodeObjectToJson(event.getClassification().getClassification()));
        }
        if (event.getIdentification() != null) {
            json.put(IDENTIFICATION, encodeObjectToJson(event.getIdentification()));
        }
        if (event.isSetTime()) {
            json.put(TIME, event.getTime().toString());
        }

        return json;
    }
}
