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
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public abstract class DescribedObjectJsonEncoder<T extends AbstractSensorML> extends JSONEncoder<T>
    implements SensorML20JsonEncoder {

    DescribedObjectJsonEncoder(Class<T> concreteClass) {
        super(concreteClass);
    }

    @Override public JsonNode encodeJSON(T abstractSensorML) throws EncodingException {
        ObjectNode json = JSON_FACTORY.objectNode();
        if (abstractSensorML.isSetGmlID()) {
            json.put(ID, abstractSensorML.getGmlId());
        }
        if (abstractSensorML.isSetDescription()) {
            json.put(DESCRIPTION, abstractSensorML.getDescription());
        }
        if (abstractSensorML.isSetIdentifier()) {
            json.put(IDENTIFIER, abstractSensorML.getIdentifier());
        }
        if (abstractSensorML.isSetKeywords()) {
            json.put(KEYWORDS, encodeObjectsToJson(abstractSensorML.getKeywords()));
        }
        if (abstractSensorML.isSetIdentifications()) {
            json.put(IDENTIFICATION, encodeListObjectToJson(abstractSensorML.getIdentifications()));
        }
        if (abstractSensorML.isSetClassifications()) {
            json.put(CLASSIFICATION, encodeListObjectToJson(abstractSensorML.getClassifications()));
        }
        if (abstractSensorML.isSetCharacteristics()) {
            json.put(CHARACTERISTICS, encodeListObjectToJson(abstractSensorML.getCharacteristics()));
        }
        if (abstractSensorML.isSetCapabilities()) {
            json.put(CAPABILITIES, encodeListObjectToJson(abstractSensorML.getCapabilities()));
        }
        if (abstractSensorML.isSetContact()) {
            json.put(CONTACTS, encodeListObjectToJson(abstractSensorML.getContact()));
        }
        if (abstractSensorML.isSetDocumentation()) {
            json.put(DOCUMENTATION, encodeListObjectToJson(abstractSensorML.getDocumentation()));
        }
        if (abstractSensorML.isSetHistory()) {
            json.put(HISTORY, encodeListObjectToJson(abstractSensorML.getHistory()));
        }
        return json;
    }
}
