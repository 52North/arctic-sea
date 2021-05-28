/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.wps.ap.ApplicationPackage;
import org.n52.shetland.ogc.wps.ap.ExecutionUnit;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;

public class ApplicationPackageEncoder extends JSONEncoder<ApplicationPackage> {

    public ApplicationPackageEncoder() {
        super(ApplicationPackage.class);
    }

    @Override
    public JsonNode encodeJSON(ApplicationPackage applicationPackage) throws EncodingException {
        if (applicationPackage == null) {
            return nodeFactory().nullNode();
        }
        ObjectNode root = nodeFactory().objectNode();
        root.set(JSONConstants.PROCESS_DESCRIPTION, encodeObjectToJson(applicationPackage.getProcessDescription()));
        if (applicationPackage.getDeploymentProfileName() != null) {
            root.put(JSONConstants.DEPLOYMENT_PROFILE_NAME, applicationPackage.getDeploymentProfileName().toString());
        }
        if (applicationPackage.getImmediateDeployment() != null) {
            root.put(JSONConstants.IMMEDIATE_DEPLOYMENT, applicationPackage.getImmediateDeployment());
        }

        ArrayNode executionUnits = root.putArray(JSONConstants.EXECUTION_UNIT);
        for (ExecutionUnit executionUnit : applicationPackage.getExecutionUnits()) {
            executionUnits.add(encodeObjectToJson(executionUnit));
        }
        return root;
    }

}
