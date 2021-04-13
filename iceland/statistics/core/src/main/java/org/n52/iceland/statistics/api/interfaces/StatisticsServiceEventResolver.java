/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.api.interfaces;

import java.util.Map;

import org.n52.janmayen.event.Event;

public interface StatisticsServiceEventResolver<T extends Event> {
    /**
     * Resolves the the request to Elasticsearch format
     *
     * @return Elasticsearch format
     */
    Map<String, Object> resolve();

    /**
     * before the processing the Handlers can be added to the resolver via spring xml
     *
     * @param handlers the handlers
     */
    void setHandlers(Map<String, StatisticsServiceEventHandler<?>> handlers);

    /**
     * Sets the payload to the Resolver class. Before the {@link #resolve()} method is called.
     *
     * @param event the event
     */
    void setEvent(T event);

    T getEvent();
}
