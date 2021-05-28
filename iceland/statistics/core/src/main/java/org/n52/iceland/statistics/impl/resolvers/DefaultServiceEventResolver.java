/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventHandler;
import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventResolver;
import org.n52.iceland.statistics.api.utils.EventHandlerFinder;
import org.n52.janmayen.event.Event;

public class DefaultServiceEventResolver implements StatisticsServiceEventResolver<Event> {

    // private static final Logger logger =
    // LoggerFactory.getLogger(DefaultServiceEventResolver.class);
    private Event event;
    private Map<String, StatisticsServiceEventHandler<?>> handlers;

    @Override
    public Map<String, Object> resolve() {
        if (event == null) {
            return null;
        }
        StatisticsServiceEventHandler<Event> handler = EventHandlerFinder.findHandler(event, handlers);

        return handler.resolveAsMap(event);
    }

    @Override
    public void setEvent(Event payload) {
        this.event = payload;
    }

    @Override
    public Event getEvent() {
        return event;
    }

    @Override
    public void setHandlers(Map<String, StatisticsServiceEventHandler<?>> handlers) {
        this.handlers = handlers;
    }

}
