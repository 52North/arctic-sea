/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.wps.DataTransmissionMode;
import org.n52.shetland.ogc.wps.JobControlOption;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.JSONDecoder;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ProcessOfferingDecoder extends JSONDecoder<ProcessOffering> {

    public ProcessOfferingDecoder() {
        super(ProcessOffering.class);
    }

    @Override
    public ProcessOffering decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        ProcessDescription processDescription = decodeJsonToObject(node.path(JSONConstants.PROCESS),
                                                                   ProcessDescription.class);

        JsonNode jobControlOptionsNode = node.path(JSONConstants.JOB_CONTROL_OPTIONS);
        List<JobControlOption> jobControlOptions = Streams.stream(jobControlOptionsNode)
                                                          .map(JsonNode::asText)
                                                          .map(JobControlOption::new)
                                                          .collect(toList());
        JsonNode outputTransmissionNode = node.path(JSONConstants.OUTPUT_TRANSMISSION);
        List<DataTransmissionMode> outputTransmissionModes = Streams.stream(outputTransmissionNode)
                                                                    .map(JsonNode::asText)
                                                                    .map(DataTransmissionMode::fromString)
                                                                    .filter(Optional::isPresent)
                                                                    .map(Optional::get)
                                                                    .collect(toList());
        if (node.path(JSONConstants.PROCESS_VERSION).isValueNode()) {
            processDescription = processDescription.newBuilder()
                                                   .withVersion(node.path(JSONConstants.PROCESS_VERSION).asText())
                                                   .build();
        }
        String processModel = node.path(JSONConstants.PROCESS_MODEL).asText();
        return new ProcessOffering(processDescription, jobControlOptions, outputTransmissionModes, processModel);
    }

}
