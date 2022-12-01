/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ApplicationPackage {
    private ProcessOffering processDescription;
    private List<ExecutionUnit> executionUnits = Collections.emptyList();
    private Boolean immediateDeployment;
    private URI deploymentProfileName;

    public ProcessOffering getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(ProcessOffering processDescription) {
        this.processDescription = processDescription;
    }

    public List<ExecutionUnit> getExecutionUnits() {
        return Collections.unmodifiableList(executionUnits);
    }

    public void setExecutionUnits(List<ExecutionUnit> executionUnits) {
        this.executionUnits = Optional.ofNullable(executionUnits).orElseGet(Collections::emptyList);
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
