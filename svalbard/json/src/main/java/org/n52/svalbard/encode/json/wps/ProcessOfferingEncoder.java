/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;

public class ProcessOfferingEncoder extends JSONEncoder<ProcessOffering> {

    public ProcessOfferingEncoder() {
        super(ProcessOffering.class);
    }

    @Override
    public JsonNode encodeJSON(ProcessOffering processOffering) throws EncodingException {
        ObjectNode root = nodeFactory().objectNode();
        root.set(JSONConstants.PROCESS, encodeObjectToJson(processOffering.getProcessDescription()));
        processOffering.getProcessVersion().ifPresent(v -> root.put(JSONConstants.PROCESS_VERSION, v));
        root.put(JSONConstants.PROCESS_MODEL, processOffering.getProcessModel());
        root.set(JSONConstants.JOB_CONTROL_OPTIONS, processOffering.getJobControlOptions().stream()
                                                                   .map(this::encodeAsString)
                                                                   .collect(toJsonArray()));
        root.set(JSONConstants.OUTPUT_TRANSMISSION, processOffering.getOutputTransmissionModes().stream()
                                                                   .map(this::encodeAsString)
                                                                   .collect(toJsonArray()));
        return root;

    }
}
