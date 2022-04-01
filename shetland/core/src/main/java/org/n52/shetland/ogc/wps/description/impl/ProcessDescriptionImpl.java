/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.description.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.n52.janmayen.stream.MoreCollectors;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcessDescriptionImpl extends AbstractDescription implements ProcessDescription {

    private final Map<OwsCode, ProcessInputDescription> inputs;
    private final Map<OwsCode, ProcessOutputDescription> outputs;
    private final boolean storeSupported;
    private final boolean statusSupported;
    private final String version;

    protected ProcessDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.inputs = builder.getInputs().stream()
                             .collect(Collectors.groupingBy(Description::getId, MoreCollectors.toSingleResult()));
        this.outputs = builder.getOutputs().stream()
                              .collect(Collectors.groupingBy(Description::getId, MoreCollectors.toSingleResult()));
        this.storeSupported = builder.isStoreSupported();
        this.statusSupported = builder.isStatusSupported();
        this.version = Objects.requireNonNull(builder.getVersion(), "version");
    }

    @Override
    public ProcessInputDescription getInput(OwsCode id) {
        return this.inputs.get(id);
    }

    @Override
    public ProcessOutputDescription getOutput(OwsCode id) {
        return this.outputs.get(id);
    }

    @Override
    public Set<OwsCode> getInputs() {
        return Collections.unmodifiableSet(inputs.keySet());
    }

    @Override
    public Collection<? extends ProcessInputDescription> getInputDescriptions() {
        return Collections.unmodifiableCollection(inputs.values());
    }

    @Override
    public Set<OwsCode> getOutputs() {
        return Collections.unmodifiableSet(outputs.keySet());
    }

    @Override
    public Collection<? extends ProcessOutputDescription> getOutputDescriptions() {
        return Collections.unmodifiableCollection(outputs.values());
    }

    @Override
    public boolean isStoreSupported() {
        return storeSupported;
    }

    @Override
    public ProcessDescription.Builder<?, ?> newBuilder() {
        return getFactory().process(this);
    }

    @Override
    public boolean isStatusSupported() {
        return statusSupported;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.inputs);
        hash = 97 * hash + Objects.hashCode(this.outputs);
        hash = 97 * hash + (this.storeSupported ? 1 : 0);
        hash = 97 * hash + (this.statusSupported ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProcessDescriptionImpl other = (ProcessDescriptionImpl) obj;

        return this.storeSupported != other.storeSupported &&
               this.statusSupported != other.statusSupported &&
               Objects.equals(this.version, other.version) &&
               Objects.equals(this.inputs, other.inputs) &&
               Objects.equals(this.outputs, other.outputs);
    }

    public abstract static class AbstractBuilder<T extends ProcessDescription, B extends AbstractBuilder<T, B>>
            extends AbstractDescription.AbstractBuilder<T, B>
            implements ProcessDescription.Builder<T, B> {

        private final ImmutableSet.Builder<ProcessInputDescription> inputs = ImmutableSet.builder();
        private final ImmutableSet.Builder<ProcessOutputDescription> outputs = ImmutableSet.builder();
        private boolean storeSupported;
        private boolean statusSupported;
        private String version;

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  ProcessDescription entity) {
            super(factory, entity);
            this.inputs.addAll(entity.getInputDescriptions());
            this.outputs.addAll(entity.getOutputDescriptions());
            this.statusSupported = entity.isStatusSupported();
            this.storeSupported = entity.isStoreSupported();
            this.version = entity.getVersion();
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withVersion(String version) {
            Preconditions.checkArgument(!Objects.requireNonNull(version).isEmpty());
            this.version = version;
            return self();
        }

        @Override
        public B storeSupported(boolean storeSupported) {
            this.storeSupported = storeSupported;
            return self();
        }

        @Override
        public B statusSupported(boolean statusSupported) {
            this.statusSupported = statusSupported;
            return self();
        }

        @Override
        public B withInput(ProcessInputDescription input) {
            if (input != null) {
                inputs.add(input);
            }
            return self();
        }

        @Override
        public B withOutput(ProcessOutputDescription output) {
            if (output != null) {
                outputs.add(output);
            }
            return self();
        }

        public Set<ProcessInputDescription> getInputs() {
            return this.inputs.build();
        }

        public Set<ProcessOutputDescription> getOutputs() {
            return this.outputs.build();
        }

        public boolean isStoreSupported() {
            return this.storeSupported;
        }

        public boolean isStatusSupported() {
            return this.statusSupported;
        }

        public String getVersion() {
            return this.version;
        }

    }

    public static class Builder extends AbstractBuilder<ProcessDescription, Builder> {

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          ProcessDescription entity) {
            super(factory, entity);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public ProcessDescription build() {
            return new ProcessDescriptionImpl(this);
        }
    }

}
