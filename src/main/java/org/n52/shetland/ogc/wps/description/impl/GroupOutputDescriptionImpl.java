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



import static java.util.stream.Collectors.groupingBy;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
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
import org.n52.shetland.ogc.wps.description.GroupOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GroupOutputDescriptionImpl extends AbstractProcessOutputDescription
        implements GroupOutputDescription {

    private final Map<OwsCode, ProcessOutputDescription> outputs;

    protected GroupOutputDescriptionImpl(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getOutputs());
    }

    public GroupOutputDescriptionImpl(OwsCode id,
                                      OwsLanguageString title,
                                      OwsLanguageString abstrakt,
                                      Set<OwsKeyword> keywords,
                                      Set<OwsMetadata> metadata,
                                      Set<? extends ProcessOutputDescription> outputs) {
        super(id, title, abstrakt, keywords, metadata);
        Function<ProcessOutputDescription, OwsCode> keyFunc = Description::getId;
        Collector<ProcessOutputDescription, ?, ProcessOutputDescription> outputDownstreamCollector = MoreCollectors.toSingleResult();
        Collector<ProcessOutputDescription, ?, Map<OwsCode, ProcessOutputDescription>> outputCollector = groupingBy(keyFunc, outputDownstreamCollector);
        this.outputs = Optional.ofNullable(outputs).orElseGet(Collections::emptySet).stream().collect(outputCollector);
    }

    @Override
    public <T> T visit(ReturningVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T, X extends Exception> T visit(
            ThrowingReturningVisitor<T, X> visitor)
            throws X {
        return visitor.visit(this);
    }

    @Override
    public ProcessOutputDescription getOutput(OwsCode id) {
        return this.outputs.get(id);
    }

    @Override
    public Collection<? extends ProcessOutputDescription> getOutputDescriptions() {
        return Collections.unmodifiableCollection(outputs.values());
    }

    @Override
    public Set<OwsCode> getOutputs() {
        return Collections.unmodifiableSet(outputs.keySet());
    }

    public static abstract class AbstractBuilder<T extends GroupOutputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessOutputDescription.AbstractBuilder<T, B>
            implements GroupOutputDescription.Builder<T, B> {

        private final ImmutableSet.Builder<ProcessOutputDescription> inputs
                = ImmutableSet.builder();

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withOutput(ProcessOutputDescription input) {
            if (input != null) {
                this.inputs.add(input);
            }
            return (B) this;
        }

        public Set<ProcessOutputDescription> getOutputs() {
            return this.inputs.build();
        }

    }

    public static class Builder extends AbstractBuilder<GroupOutputDescription, Builder> {
        @Override
        public GroupOutputDescription build() {
            return new GroupOutputDescriptionImpl(this);
        }
    }

}
