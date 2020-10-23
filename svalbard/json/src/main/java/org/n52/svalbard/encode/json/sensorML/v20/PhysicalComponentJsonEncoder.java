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
import org.n52.svalbard.encode.json.JSONEncoder;

/**
 * This encoder encodes a SensorML20 PhysicalComponent according to OGC BP 17-011r2.
 * Note: The encoder assumes the input Object to be valid and does not validate syntax nor semantic before encoding.
 * Note: Currently only a subset is implemented.
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class PhysicalComponentJsonEncoder extends JSONEncoder<PhysicalComponent> implements SensorML20JsonEncoder {

    public PhysicalComponentJsonEncoder() {
        super(PhysicalComponent.class);
    }

    /*
    @Override
    protected void encodeResponse(ObjectNode json, DeleteObservationResponse t)
            throws EncodingException {
        if (t == null) {
            throw new UnsupportedEncoderInputException(this, DeleteObservationResponse.class);
        }
        final CompositeOwsException exceptions = new CompositeOwsException();
        if (t.getService() == null) {
            exceptions.add(new MissingServiceParameterException());
        }
        if (t.getVersion() == null) {
            exceptions.add(new MissingVersionParameterException());
        }
        if (DeleteObservationConstants.NS_SOSDO_1_0.equals(t.getOperationVersion())) {
            if (t.getObservationId() == null || t.getObservationId().isEmpty()) {
                exceptions.add(new MissingParameterValueException(DeleteObservationConstants.PARAM_OBSERVATION));
            } else {
                json.put(JSONConstants.DELETED_OBSERVATION, t.getObservationId());
            }
        }
        try {
            exceptions.throwIfNotEmpty();
        } catch (CompositeOwsException e) {
            throw new EncodingException(e);
        }
    }
    */

    @Override public JsonNode encodeJSON(PhysicalComponent pc) throws EncodingException {
        ObjectNode pcJson = jsonFactory.objectNode();
        pcJson.put(TYPE, "PhysicalComponent");
        if (pc.isSetGmlID()) {
            pcJson.put(ID, pc.getGmlId());
        }
        if (pc.isSetDescription()) {
            pcJson.put(DESCRIPTION, pc.getDescription());
        }
        if (pc.isSetIdentifier()) {
            pcJson.put(IDENTIFIER, pc.getIdentifier());
        }
        if (pc.isSetIdentifications()) {
            pcJson.put(IDENTIFICATION,
                       encodeObjectToJson(pc.getIdentifications()));
        }
        if (pc.isSetClassifications()) {
            pcJson.put(CLASSIFICATION,
                       encodeObjectToJson(pc.getClassifications()));
        }
        /*
        if (pc.isSetOutputs()) {
            encodeObjectToJson(pc.getClassifications());
        }
        if (pc.isSetInputs()) {
            encodeObjectToJson(pc.getInputs());
        }
        */
        if (pc.isSetHistory()) {
            pcJson.put(HISTORY, encodeObjectToJson(pc.getHistory()));
        }
        return pcJson;
    }
}
