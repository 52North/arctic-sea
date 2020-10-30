/*
 * Copyright (C) 2018-2020 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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
