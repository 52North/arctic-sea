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
package org.n52.shetland.ogc.wps.request;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.wps.WPSConstants;

public class DescribeProcessRequest extends OwsServiceRequest {
    public static final String ALL_KEYWORD = "ALL";

    private List<OwsCode> identifiers;

    public DescribeProcessRequest() {
        super(null, null, WPSConstants.Operations.DescribeProcess.name());
    }

    public DescribeProcessRequest(String service, String version) {
        super(service, version, WPSConstants.Operations.DescribeProcess.name());
    }

    public DescribeProcessRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public List<OwsCode> getProcessIdentifier() {
        return identifiers == null ? null : Collections.unmodifiableList(identifiers);
    }

    public void addProcessIdentifier(String identifier) {
        addProcessIdentifier(new OwsCode(identifier));
    }

    public void addProcessIdentifier(OwsCode identifier) {
        if (identifiers == null) {
            identifiers = new LinkedList<>();
        }
        this.identifiers.add(Objects.requireNonNull(identifier));
    }

    public void addProcessIdentifiers(List<String> identifiers) {
        if (identifiers.isEmpty()) {
            this.identifiers = new LinkedList<>();
        } else {
            identifiers.forEach(this::addProcessIdentifier);
        }
    }

    public boolean isAll() {
        return getProcessIdentifier().stream().filter(id -> !id.getCodeSpace().isPresent())
                .anyMatch(id -> id.getValue().equalsIgnoreCase(ALL_KEYWORD));
    }

}
