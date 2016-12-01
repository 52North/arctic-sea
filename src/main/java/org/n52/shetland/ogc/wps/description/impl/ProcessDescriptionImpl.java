/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import static com.google.common.base.Strings.emptyToNull;
import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

import org.n52.janmayen.stream.MoreCollectors;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProcessDescriptionImpl
        extends AbstractDescription
        implements ProcessDescription {

    private final Map<OwsCode, ProcessInputDescription> inputs;
    private final Map<OwsCode, ProcessOutputDescription> outputs;
    private final boolean storeSupported;
    private final boolean statusSupported;
    private final String version;

    protected ProcessDescriptionImpl(
            AbstractBuilder<?, ?> builder) {

        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getInputs(),
             builder.getOutputs(),
             builder.getVersion(),
             builder.isStoreSupported(),
             builder.isStatusSupported());
    }

    public ProcessDescriptionImpl(OwsCode id, OwsLanguageString title,
                                  OwsLanguageString abstrakt,
                                  Set<OwsKeyword> keywords,
                                  Set<OwsMetadata> metadata,
                                  Set<? extends ProcessInputDescription> inputs,
                                  Set<? extends ProcessOutputDescription> outputs,
                                  String version,
                                  boolean storeSupported,
                                  boolean statusSupported) {
        super(id, title, abstrakt, keywords, metadata);
        Function<Description, OwsCode> keyFunc = Description::getId;
        Collector<ProcessInputDescription, ?, ProcessInputDescription> inputDownstreamCollector = MoreCollectors.toSingleResult();
        Collector<ProcessOutputDescription, ?, ProcessOutputDescription> outputDownstreamCollector = MoreCollectors.toSingleResult();
        Collector<ProcessInputDescription, ?, Map<OwsCode, ProcessInputDescription>> inputCollector = groupingBy(keyFunc, inputDownstreamCollector);
        Collector<ProcessOutputDescription, ?, Map<OwsCode, ProcessOutputDescription>> outputCollector = groupingBy(keyFunc, outputDownstreamCollector);
        this.inputs = Optional.ofNullable(inputs).orElseGet(Collections::emptySet).stream().collect(inputCollector);
        this.outputs = Optional.ofNullable(outputs).orElseGet(Collections::emptySet).stream().collect(outputCollector);
        this.storeSupported = storeSupported;
        this.statusSupported = statusSupported;
        this.version = Objects.requireNonNull(version, "version");
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
    public boolean isStatusSupported() {
        return statusSupported;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public static abstract class AbstractBuilder<T extends ProcessDescription, B extends ProcessDescription.Builder<T, B>>
            extends AbstractDescription.AbstractBuilder<T, B>
            implements ProcessDescription.Builder<T, B> {

        private final ImmutableSet.Builder<ProcessInputDescription> inputs
                = ImmutableSet.builder();
        private final ImmutableSet.Builder<ProcessOutputDescription> outputs
                = ImmutableSet.builder();
        private boolean storeSupported = false;
        private boolean statusSupported = false;
        private String version;

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withVersion(String version) {
            this.version = Objects.requireNonNull(emptyToNull(version));
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B storeSupported(boolean storeSupported) {
            this.storeSupported = storeSupported;
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B statusSupported(boolean statusSupported) {
            this.statusSupported = statusSupported;
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withInput(ProcessInputDescription input) {
            if (input != null) {
                inputs.add(input);
            }
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withOutput(ProcessOutputDescription output) {
            if (output != null) {
                outputs.add(output);
            }
            return (B) this;
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
        @Override
        public ProcessDescription build() {
            return new ProcessDescriptionImpl(this);
        }
    }

}
