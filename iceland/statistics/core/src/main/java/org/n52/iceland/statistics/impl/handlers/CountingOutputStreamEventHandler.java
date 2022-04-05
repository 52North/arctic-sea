/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.statistics.impl.handlers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import org.n52.iceland.event.events.CountingOutputStreamEvent;
import org.n52.iceland.statistics.api.AbstractElasticSearchDataHolder;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventHandler;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.n52.iceland.statistics.api.parameters.ObjectEsParameterFactory;

public class CountingOutputStreamEventHandler extends AbstractElasticSearchDataHolder
        implements StatisticsServiceEventHandler<CountingOutputStreamEvent> {

    @Override
    public Map<String, Object> resolveAsMap(CountingOutputStreamEvent event) {
        Map<String, Object> data = new HashMap<>();
        data.put(ObjectEsParameterFactory.BYTES.getName(), event.getBytesWritten());
        data.put(ObjectEsParameterFactory.DISPLAY_BYTES.getName(),
                 FileUtils.byteCountToDisplaySize(event.getBytesWritten()));
        put(ServiceEventDataMapping.ORE_BYTES_WRITTEN, data);

        return dataMap;
    }

}
