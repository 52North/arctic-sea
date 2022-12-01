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
import org.n52.shetland.ogc.wps.ProcessOfferings;
import org.n52.shetland.ogc.wps.WPSConstants;

/**
 * @author Benjamin Pross
 *
 */
public class DescribeProcessResponse extends OwsServiceResponse {

    private ProcessOfferings offerings;

    public DescribeProcessResponse(String service, String version) {
        this(service, version, null);
    }

    public DescribeProcessResponse(String service, String version, ProcessOfferings offerings) {
        super(service, version);
        this.offerings = offerings;
    }

    public DescribeProcessResponse() {
        this(null, null, null);
    }

    @Override
    public String getOperationName() {
        return WPSConstants.Operations.DescribeProcess.name();
    }

    public ProcessOfferings getProcessOfferings() {
        return offerings;
    }

    public void setOfferings(ProcessOfferings offerings) {
        this.offerings = Objects.requireNonNull(offerings);
    }

}
