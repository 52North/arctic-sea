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
package org.n52.shetland.ogc.wps.description.impl;

import com.google.common.collect.ImmutableSet;
import org.n52.janmayen.stream.MoreCollectors;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupInputDescriptionImpl extends AbstractProcessInputDescription implements GroupInputDescription {

    private final Map<OwsCode, ProcessInputDescription> inputs;

    protected GroupInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.inputs = builder.getInputs().stream()
                             .collect(Collectors.groupingBy(Description::getId, MoreCollectors.toSingleResult()));
    }

    @Override
    public <T> T visit(ReturningVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(ThrowingReturningVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public ProcessInputDescription getInput(OwsCode id) {
        return this.inputs.get(id);
    }

    @Override
    public Collection<? extends ProcessInputDescription> getInputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCode> getInputs() {
        return Collections.unmodifiableSet(inputs.keySet());
    }

    @Override
    public GroupInputDescription.Builder<?, ?> newBuilder() {
        return getFactory().groupInput(this);
    }

    public abstract static class AbstractBuilder<T extends GroupInputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessInputDescription.AbstractBuilder<T, B>
            implements GroupInputDescription.Builder<T, B> {

        private final ImmutableSet.Builder<ProcessInputDescription> inputs = ImmutableSet.builder();

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  GroupInputDescription entity) {
            super(factory, entity);
            this.inputs.addAll(entity.getInputDescriptions());
        }

        @Override
        public B withInput(ProcessInputDescription input) {
            if (input != null) {
                this.inputs.add(input);
            }
            return self();
        }

        public Set<ProcessInputDescription> getInputs() {
            return this.inputs.build();
        }

    }

    public static class Builder extends AbstractBuilder<GroupInputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        public Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                       GroupInputDescription entity) {
            super(factory, entity);
        }

        @Override
        public GroupInputDescription build() {
            return new GroupInputDescriptionImpl(this);
        }
    }
}
