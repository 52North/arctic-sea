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
import org.n52.shetland.ogc.sensorML.v20.AbstractPhysicalProcess;
import org.n52.svalbard.encode.exception.EncodingException;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public abstract class AbstractPhysicalProcessJsonEncoder<T extends AbstractPhysicalProcess>
    extends AbstractProcessJsonEncoder<T> {

    public AbstractPhysicalProcessJsonEncoder(Class<T> concreteClass) {
        super(concreteClass);
    }

    @Override
    public JsonNode encodeJSON(T abstractPhysicalProcess) throws EncodingException {
        ObjectNode json = (ObjectNode) super.encodeJSON(abstractPhysicalProcess);

        if (abstractPhysicalProcess.isSetAttachedTo()) {
            json.put(ATTACHED_TO, encodeObjectToJson(abstractPhysicalProcess.getAttachedTo()));
        }
        if (abstractPhysicalProcess.getLocalReferenceFrame() != null) {
            json.put(LOCAL_REFERENCE_FRAME, encodeObjectToJson(abstractPhysicalProcess.getLocalReferenceFrame()));
        }
        if (abstractPhysicalProcess.getLocalTimeFrame() != null) {
            json.put(LOCAL_TIME_FRAME, encodeObjectToJson(abstractPhysicalProcess.getLocalTimeFrame()));
        }
        if (abstractPhysicalProcess.isSetPosition()) {
            json.put(LOCAL_TIME_FRAME, encodeObjectToJson(abstractPhysicalProcess.getPosition()));
        }
        if (abstractPhysicalProcess.getTimePosition() != null) {
            json.put(LOCAL_TIME_FRAME, encodeObjectToJson(abstractPhysicalProcess.getTimePosition()));
        }
        return json;
    }

}
