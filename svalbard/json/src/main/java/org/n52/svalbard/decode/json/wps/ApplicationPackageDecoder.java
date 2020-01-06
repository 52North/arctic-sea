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
package org.n52.svalbard.decode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.shetland.ogc.wps.ProcessOffering;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.shetland.ogc.wps.ap.ExecutionUnit;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.JSONDecoder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ApplicationPackageDecoder extends JSONDecoder<ApplicationPackage> {
    public ApplicationPackageDecoder() {
        super(ApplicationPackage.class);
    }

    @Override
    public ApplicationPackage decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        ApplicationPackage ap = new ApplicationPackage();

        List<ExecutionUnit> executionUnits = new ArrayList<>();
        for (JsonNode executionUnit : node.path(JSONConstants.EXECUTION_UNIT)) {
            executionUnits.add(decodeJsonToObject(executionUnit, ExecutionUnit.class));
        }
        ap.setExecutionUnits(executionUnits);

        if (node.path(JSONConstants.DEPLOYMENT_PROFILE_NAME).isValueNode()) {
            ap.setDeploymentProfileName(URI.create(node.path(JSONConstants.DEPLOYMENT_PROFILE_NAME).asText()));
        }

        if (node.path(JSONConstants.IMMEDIATE_DEPLOYMENT).isValueNode()) {
            ap.setImmediateDeployment(node.path(JSONConstants.IMMEDIATE_DEPLOYMENT).asBoolean());
        }

        ap.setProcessDescription(decodeJsonToObject(node.path(JSONConstants.PROCESS_DESCRIPTION),
                                                    ProcessOffering.class));
        return ap;
    }
}
