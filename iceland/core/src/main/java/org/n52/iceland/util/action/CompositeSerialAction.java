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
package org.n52.iceland.util.action;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @param <A> the action type
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:shane@axiomalaska.com">Shane StClair</a>
 * @since 1.0.0
 *
 */
public abstract class CompositeSerialAction<A extends Action> extends CompositeAction<A> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompositeSerialAction.class);

    @SafeVarargs
    @SuppressWarnings("varargs")
    public CompositeSerialAction(A... actions) {
        super(actions);
    }

    @Override
    public void execute() {
        Optional.ofNullable(getActions())
                .map(List::stream)
                .orElseGet(Stream::empty)
                .forEach(((Consumer<A>) this::pre)
                        .andThen(a -> LOGGER.debug("Running {}.", a))
                        .andThen(A::execute)
                        .andThen(this::post));
    }
}
