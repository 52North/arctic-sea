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
package org.n52.shetland.ogc.wps.response;

import java.util.Objects;

import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.wps.Result;
import org.n52.shetland.ogc.wps.WPSConstants;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Christian Autermann
 */
public class GetResultResponse extends OwsServiceResponse {

    private Result result;

    public GetResultResponse() {
        this(null, null, null);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public GetResultResponse(String service, String version, Result result) {
        super(service, version);
        this.result = result;
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.GetResult.toString();
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Result getResult() {
        return this.result;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setResult(Result result) {
        this.result = Objects.requireNonNull(result);
    }

}
