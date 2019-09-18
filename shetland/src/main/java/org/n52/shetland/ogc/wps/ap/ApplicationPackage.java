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
package org.n52.shetland.ogc.wps.ap;

import org.n52.shetland.ogc.wps.ProcessOffering;

import java.net.URI;

public class ApplicationPackage {
    private ProcessOffering processDescription;
    private ExecutionUnit executionUnit;
    private Boolean immediateDeployment;
    private URI deploymentProfileName;

    public ProcessOffering getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(ProcessOffering processDescription) {
        this.processDescription = processDescription;
    }

    public ExecutionUnit getExecutionUnit() {
        return executionUnit;
    }

    public void setExecutionUnit(ExecutionUnit executionUnit) {
        this.executionUnit = executionUnit;
    }

    public Boolean getImmediateDeployment() {
        return immediateDeployment;
    }

    public void setImmediateDeployment(Boolean immediateDeployment) {
        this.immediateDeployment = immediateDeployment;
    }

    public URI getDeploymentProfileName() {
        return deploymentProfileName;
    }

    public void setDeploymentProfileName(URI deploymentProfileName) {
        this.deploymentProfileName = deploymentProfileName;
    }

}
