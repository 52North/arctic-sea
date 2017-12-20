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
package org.n52.janmayen.event;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.n52.janmayen.GroupedAndNamedThreadFactory;

public abstract class AbstractAsyncEventListener implements EventListener {
    private final Executor executor;

    public AbstractAsyncEventListener(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(
                threadPoolSize, new GroupedAndNamedThreadFactory(getClass().getName() + "-worker"));
    }

    public AbstractAsyncEventListener() {
        this(3);
    }

    @Override
    public void handle(Event event) {
        this.executor.execute(create(event));
    }

    protected abstract Runnable create(Event event);
}
