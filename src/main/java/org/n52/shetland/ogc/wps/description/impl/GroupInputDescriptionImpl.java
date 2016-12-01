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

import org.n52.shetland.ogc.wps.InputOccurence;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.GroupInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.janmayen.stream.MoreCollectors;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;

import com.google.common.collect.ImmutableSet;

public class GroupInputDescriptionImpl extends AbstractProcessInputDescription implements GroupInputDescription {

    private final Map<OwsCode, ProcessInputDescription> inputs;

    protected GroupInputDescriptionImpl(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             new InputOccurence(builder.getMinimalOccurence(),
                                builder.getMaximalOccurence()),
             builder.getInputs());
    }

    public GroupInputDescriptionImpl(OwsCode id,
                                     OwsLanguageString title,
                                     OwsLanguageString abstrakt,
                                     Set<OwsKeyword> keywords,
                                     Set<OwsMetadata> metadata,
                                     InputOccurence occurence,
                                     Set<? extends ProcessInputDescription> inputs) {
        super(id, title, abstrakt, keywords, metadata, occurence);
        Function<ProcessInputDescription, OwsCode> keyFunc = Description::getId;
        Collector<ProcessInputDescription, ?, ProcessInputDescription> outputDownstreamCollector = MoreCollectors.toSingleResult();
        Collector<ProcessInputDescription, ?, Map<OwsCode, ProcessInputDescription>> outputCollector = groupingBy(keyFunc, outputDownstreamCollector);
        this.inputs = Optional.ofNullable(inputs).orElseGet(Collections::emptySet).stream().collect(outputCollector);
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

    public static abstract class AbstractBuilder<T extends GroupInputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessInputDescription.AbstractBuilder<T, B>
            implements GroupInputDescription.Builder<T, B> {

        private final ImmutableSet.Builder<ProcessInputDescription> inputs
                = ImmutableSet.builder();

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withInput(ProcessInputDescription input) {
            if (input != null) {
                this.inputs.add(input);
            }
            return (B) this;
        }

        public Set<ProcessInputDescription> getInputs() {
            return this.inputs.build();
        }

    }

    public static class Builder extends AbstractBuilder<GroupInputDescription, Builder> {
        @Override
        public GroupInputDescription build() {
            return new GroupInputDescriptionImpl(this);
        }
    }
}
