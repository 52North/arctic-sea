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
import org.n52.shetland.ogc.sensorML.SmlContact;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlEvent;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlProperty;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;
import org.n52.svalbard.encode.json.ListEncoderKey;

import java.util.List;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public abstract class ListJsonEncoder extends JSONEncoder<List<?>> implements SensorML20JsonEncoder {

    public ListJsonEncoder(ListEncoderKey listEncoderKey) {
        super(listEncoderKey);
    }

    protected JsonNode encode(List<?> list, String type, String elemKey) throws EncodingException {
        ObjectNode json = JSON_FACTORY.objectNode();
        json.put(TYPE, type);
        if (!list.isEmpty()) {
            json.put(elemKey, encodeObjectsToJson(list));
        }
        return json;
    }

    public static class IdentifierListJsonEncoder extends ListJsonEncoder {

        public IdentifierListJsonEncoder() {
            super(new ListEncoderKey(SmlIdentifier.class));
        }

        @Override public JsonNode encodeJSON(List<?> objects) throws EncodingException {
            return encode(objects, "IdentifierList", "identifier");
        }
    }


    public static class PropertyListJsonEncoder extends ListJsonEncoder {

        public PropertyListJsonEncoder() {
            super(new ListEncoderKey(SmlProperty.class));
        }

        @Override public JsonNode encodeJSON(List<?> objects) throws EncodingException {
            return encode(objects, "AbstractDataComponent", "property");
        }
    }


    public static class ClassifierListJsonEncoder extends ListJsonEncoder {

        public ClassifierListJsonEncoder() {
            super(new ListEncoderKey(SmlClassifier.class));
        }

        @Override public JsonNode encodeJSON(List<?> objects) throws EncodingException {
            return encode(objects, "ClassifierList", "classifier");
        }
    }


    public static class HistoryJsonEncoder extends ListJsonEncoder {

        public HistoryJsonEncoder() {
            super(new ListEncoderKey(SmlEvent.class));
        }

        @Override public JsonNode encodeJSON(List<?> objects) throws EncodingException {
            return encode(objects, "EventList", "event");
        }
    }


    public static class ContactListJsonEncoder extends ListJsonEncoder {

        public ContactListJsonEncoder() {
            super(new ListEncoderKey(SmlContact.class));
        }

        @Override public JsonNode encodeJSON(List<?> objects) throws EncodingException {
            return encode(objects, "ContactList", "contact");
        }
    }
}
