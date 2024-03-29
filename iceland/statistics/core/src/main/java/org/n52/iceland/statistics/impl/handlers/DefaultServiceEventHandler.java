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
package org.n52.iceland.statistics.impl.handlers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.n52.iceland.statistics.api.AbstractElasticSearchDataHolder;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventHandler;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.n52.janmayen.event.Event;

public class DefaultServiceEventHandler extends AbstractElasticSearchDataHolder
        implements StatisticsServiceEventHandler<Event> {

    @Override
    public Map<String, Object> resolveAsMap(Event event) {
        put(ServiceEventDataMapping.UNHANDLED_SERVICEEVENT_TYPE.getName(), event.getClass());
        return new LinkedHashMap<>(dataMap);
    }

}
