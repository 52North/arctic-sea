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
package org.n52.iceland.util.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;

/**
 * A composite action consisting of other actions.
 *
 * @param <A> the action type
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 *
 */
public abstract class CompositeAction<A extends Action> extends RunnableAction {

    private final List<A> actions;

    @SafeVarargs
    @SuppressWarnings("varargs")
    public CompositeAction(A... actions) {
        this.actions = Arrays.asList(actions);
    }

    public List<A> getActions() {
        return Collections.unmodifiableList(actions);
    }

    protected abstract void pre(A action);

    protected abstract void post(A action);

    @Override
    public String toString() {
        return String.format("%s[actions=[%s]]", getClass().getSimpleName(), Joiner.on(", ").join(getActions()));
    }
}
