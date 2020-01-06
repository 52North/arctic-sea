/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.statistics.api.utils;

import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.statistics.api.interfaces.StatisticsServiceEventHandler;

public final class EventHandlerFinder {

    private static final Logger LOG = LoggerFactory.getLogger(EventHandlerFinder.class);

    private EventHandlerFinder() {
    }

    @SuppressWarnings("unchecked")
    public static <T> StatisticsServiceEventHandler<T> findHandler(
            Object object, Map<String, StatisticsServiceEventHandler<?>> handlers) {
        // Find concrete class
        String key = object.getClass().getSimpleName();
        LOG.debug("Searching handler for object by key {} ", key);
        StatisticsServiceEventHandler<T> handler = (StatisticsServiceEventHandler<T>) handlers.get(key);

        // Find super class as handler
        if (handler == null) {
            Class<?> superclass = object.getClass().getSuperclass();
            while (superclass != null) {
                key = superclass.getSimpleName();
                LOG.debug("Try super class as key {}", key);
                handler = (StatisticsServiceEventHandler<T>) handlers.get(key);
                if (handler != null) {
                    break;
                } else {
                    superclass = superclass.getSuperclass();
                }
            }
        }

        // Find default handler
        if (handler == null) {
            LOG.debug("Not found using default handler by key 'default' if registered.");
            key = "default";
            handler = (StatisticsServiceEventHandler<T>) handlers.get(key);
        }

        Objects.requireNonNull(handler, "Can not find handler for object: " + key);
        LOG.debug("Key {} found.", key);
        return handler;
    }
}
