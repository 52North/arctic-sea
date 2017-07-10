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
package org.n52.iceland.statistics.impl.handlers.exceptions;

import java.util.Map;

import org.n52.iceland.statistics.api.AbstractElasticSearchDataHolder;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventHandler;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.n52.shetland.ogc.ows.exception.CodedException;

public class CodedExceptionEventHandler extends AbstractElasticSearchDataHolder
        implements StatisticsServiceEventHandler<Exception> {

    @Override
    public Map<String, Object> resolveAsMap(Exception rawException) {
        CodedException exception = (CodedException) rawException;
        put(ServiceEventDataMapping.EX_CLASSTYPE, exception.getClass().getSimpleName());
        if (exception.getStatus() != null) {
            put(ServiceEventDataMapping.EX_STATUS, exception.getStatus().getCode());
        }
        put(ServiceEventDataMapping.CEX_LOCATOR, exception.getLocator());
        put(ServiceEventDataMapping.EX_VERSION, exception.getVersion());
        if (exception.getCode() != null) {
            put(ServiceEventDataMapping.CEX_SOAP_FAULT, exception.getCode().getSoapFaultReason());
        }
        put(ServiceEventDataMapping.EX_MESSAGE, exception.getMessage());
        return dataMap;
    }
}
