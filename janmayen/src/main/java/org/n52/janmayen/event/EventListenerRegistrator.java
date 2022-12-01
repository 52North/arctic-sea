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
package org.n52.janmayen.event;

import java.util.Collection;

import javax.inject.Inject;

import org.n52.janmayen.lifecycle.Constructable;
import org.n52.janmayen.lifecycle.Destroyable;

/**
 * Class used to decouple {@link EventListener} and {@link EventBus} creation.
 *
 * @see EventBus
 * @see EventListener
 * @since 1.0.0
 * @author Christian Autermann
 */
public class EventListenerRegistrator implements Constructable, Destroyable {

    private EventBus serviceEventBus;
    private Collection<EventListener> listeners;

    @Inject
    public void setListeners(Collection<EventListener> listeners) {
        this.listeners = listeners;
    }

    @Inject
    public void setServiceEventBus(EventBus serviceEventBus) {
        this.serviceEventBus = serviceEventBus;
    }

    @Override
    public void init() {
        this.listeners.forEach(this.serviceEventBus::register);
    }

    @Override
    public void destroy() {
        this.listeners.forEach(this.serviceEventBus::unregister);
    }

}
