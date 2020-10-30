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
            json.put(IDENTIFICATION, encodeObjectToJson(abstractSensorML.getIdentifications()));
        }
        if (abstractSensorML.isSetClassifications()) {
            json.put(CLASSIFICATION, encodeObjectToJson(abstractSensorML.getClassifications()));
        }
        if (abstractSensorML.isSetCharacteristics()) {
            json.put(CHARACTERISTICS, encodeObjectToJson(abstractSensorML.getCharacteristics()));
        }
        if (abstractSensorML.isSetCapabilities()) {
            json.put(CAPABILITIES, encodeObjectToJson(abstractSensorML.getCapabilities()));
        }
        if (abstractSensorML.isSetContact()) {
            json.put(CONTACTS, encodeObjectToJson(abstractSensorML.getContact()));
        }
        if (abstractSensorML.isSetDocumentation()) {
            json.put(DOCUMENTATION, encodeObjectToJson(abstractSensorML.getDocumentation()));
        }
        if (abstractSensorML.isSetHistory()) {
            json.put(HISTORY, encodeObjectToJson(abstractSensorML.getHistory()));
        }
        return json;
    }
}
