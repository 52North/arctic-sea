/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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


import org.n52.shetland.ogc.wps.WPSConstants;

/**
 * @author Christian Autermann
 */
public class GetResultRequest extends AbstractJobIdRequest {
    public GetResultRequest() {
        super(null, null, WPSConstants.Operations.GetResult.name());
    }

    public GetResultRequest(String service, String version) {
        super(service, version, WPSConstants.Operations.GetResult.name());
    }

    public GetResultRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

}
