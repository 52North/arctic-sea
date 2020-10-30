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
