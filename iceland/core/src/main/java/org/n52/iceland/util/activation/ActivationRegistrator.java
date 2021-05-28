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
package org.n52.iceland.util.activation;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.lifecycle.Constructable;

import com.google.common.collect.Maps;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@SuppressWarnings("rawtypes")
public class ActivationRegistrator implements Constructable {
    private static final Logger LOG = LoggerFactory.getLogger(ActivationRegistrator.class);
    private final Map<ActivationListenable, ActivationListener> listeners;
    private final Map<ActivationSink, ActivationInitializer> initializers;

    public ActivationRegistrator() {
        LOG.debug("Constructor");
        this.listeners = Maps.newHashMap();
        this.initializers = Maps.newHashMap();
    }

    public void setListeners(Map<ActivationListenable, ActivationListener> listeners) {
        Optional.ofNullable(listeners).ifPresent(this.listeners::putAll);
    }

    public void setInitializers(Map<ActivationSink, ActivationInitializer> initializers) {
        Optional.ofNullable(initializers).ifPresent(this.initializers::putAll);
    }

    @Override
    public void init() {
        LOG.debug("Initializing");
        initializeSinks();
        registerListeners();
    }

    @SuppressWarnings("unchecked")
    private void initializeSinks() {
        LOG.debug("Initializing sinks");
        this.initializers.forEach(switchParams(chain(logger(), ActivationInitializer::initialize)));
    }

    @SuppressWarnings("unchecked")
    private void registerListeners() {
        LOG.debug("Registering listeners");
        this.listeners.forEach(chain(logger(), ActivationListenable::registerListener));
    }

    private static <T, U> BiConsumer<T, U> logger() {
        return (a, b) -> LOG.debug("Registering {} for {}", a, b);
    }

    private static <T, U> BiConsumer<T, U> chain(BiConsumer<T, U> first, BiConsumer<T, U> second) {
        return first.andThen(second);
    }

    private static <T, U> BiConsumer<U, T> switchParams(BiConsumer<T, U> consumer) {
        return (a, b) -> consumer.accept(b, a);
    }
}
