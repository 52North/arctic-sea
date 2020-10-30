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
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlProperty;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;

/**
 * JSON Encoder for SmlTerm
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 * @see SmlClassifier
 */
public class PropertyJsonEncoder extends JSONEncoder<SmlProperty> implements SensorML20JsonEncoder {

    public PropertyJsonEncoder() {
        super(SmlProperty.class);
    }

    @Override public JsonNode encodeJSON(SmlProperty property) throws EncodingException {
        ObjectNode json = JSON_FACTORY.objectNode();
        if (property.isSetAbstractDataComponent()) {
            SweAbstractDataComponent component = property.getAbstractDataComponent();
            json.put(TYPE, component.getDataComponentType().name());
            if (component.isSetDefinition()) {
                json.put(DEFINITION, component.getDefinition());
            }
            if (component.isSetLabel()) {
                json.put(LABEL, component.getLabel());
            }
            if (component.isSetDescription()) {
                json.put(DESCRIPTION, component.getDescription());
            }
            if (component.isSetName()) {
                json.put(NAME, component.getName().getValue());
            }
            if (component.isSetIdentifier()) {
                json.put(IDENTIFIER, component.getIdentifier());
            }
        }
        return json;
    }
}
