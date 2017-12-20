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
package org.n52.iceland.statistics.impl.resolvers;

import java.util.Map;

import org.n52.iceland.event.events.CountingOutputStreamEvent;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventHandler;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventResolver;
import org.n52.iceland.statistics.api.utils.EventHandlerFinder;

public class CountingOutputStreamEventResolver implements StatisticsServiceEventResolver<CountingOutputStreamEvent> {

    private CountingOutputStreamEvent event;
    private Map<String, StatisticsServiceEventHandler<?>> handlers;

    @Override
    public Map<String, Object> resolve() {
        if (event == null) {
            return null;
        }
        StatisticsServiceEventHandler<CountingOutputStreamEvent> handler = EventHandlerFinder
                .findHandler(event, handlers);

        return handler.resolveAsMap(event);
    }

    @Override
    public void setHandlers(Map<String, StatisticsServiceEventHandler<?>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void setEvent(CountingOutputStreamEvent payload) {
        this.event = payload;
    }

    @Override
    public CountingOutputStreamEvent getEvent() {
        return event;
    }

}
