/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import java.util.Optional;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.StatusInfo;
import org.n52.shetland.ogc.wps.WPSConstants;

/**
 * @author Christian Autermann
 */
public class ExecuteResponse extends OwsServiceResponse {

    private Optional<Result> result;
    private Optional<StatusInfo> status;

    public ExecuteResponse() {
        this(null, null, null, null);
    }

    public ExecuteResponse(String service, String version, Result result) {
        this(service, version, result, null);
    }

    public ExecuteResponse(String service, String version, StatusInfo status) {
        this(service, version, null, status);
    }

    private ExecuteResponse(String service, String version, Result result,
                            StatusInfo status) {
        super(service, version);
        this.result = Optional.ofNullable(result);
        this.status = Optional.ofNullable(status);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.Execute.toString();
    }

    public Optional<Result> getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = Optional.ofNullable(result);
    }

    public Optional<StatusInfo> getStatus() {
        return this.status;
    }

    public void setStatus(StatusInfo status) {
        this.status = Optional.ofNullable(status);
    }

}
