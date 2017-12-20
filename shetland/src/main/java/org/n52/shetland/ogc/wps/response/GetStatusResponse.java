/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.response;

import java.util.Objects;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.wps.StatusInfo;
import org.n52.shetland.ogc.wps.WPSConstants;

/**
 * @author Christian Autermann
 */
public class GetStatusResponse extends OwsServiceResponse {
    private StatusInfo status;

    public GetStatusResponse() {
    }

    public GetStatusResponse(String service, String version, StatusInfo status) {
        super(service, version);
        this.status = status;
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetStatus.toString();
    }

    public StatusInfo getStatus() {
        return status;
    }

    public void setStatusInfo(StatusInfo status) {
        this.status = Objects.requireNonNull(status);
    }

}
