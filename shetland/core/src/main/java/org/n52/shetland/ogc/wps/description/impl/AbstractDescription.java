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
import org.n52.janmayen.AbstractBuildable;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractDescription
        extends AbstractBuildable<ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        implements Description {

    private final OwsCode id;
    private final OwsLanguageString title;
    private final OwsLanguageString abstrakt;
    private final Set<OwsKeyword> keywords;
    private final Set<OwsMetadata> metadata;

    protected AbstractDescription(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.id = Objects.requireNonNull(builder.getId(), "id");
        this.title = builder.getTitle() == null ? new OwsLanguageString(id.getValue()) : builder.getTitle();
        this.abstrakt = builder.getAbstract();
        this.keywords = Objects.requireNonNull(builder.getKeywords(), "keywords");
        this.metadata = Objects.requireNonNull(builder.getMetadata(), "metadata");
    }

    @Override
    public OwsCode getId() {
        return this.id;
    }

    @Override
    public OwsLanguageString getTitle() {
        return this.title;
    }

    @Override
    public Optional<OwsLanguageString> getAbstract() {
        return Optional.ofNullable(this.abstrakt);
    }

    @Override
    public Set<OwsKeyword> getKeywords() {
        return Collections.unmodifiableSet(this.keywords);
    }

    @Override
    public Set<OwsMetadata> getMetadata() {
        return Collections.unmodifiableSet(this.metadata);
    }

    protected abstract static class AbstractBuilder<T extends Description, B extends AbstractBuilder<T, B>>
            extends
            AbstractBuildable.AbstractBuilder<ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, T, B>
            implements Description.Builder<T, B> {
        private OwsCode id;
        private OwsLanguageString title;
        private OwsLanguageString abstrakt;
        private final ImmutableSet.Builder<OwsKeyword> keywords = ImmutableSet.builder();
        private final ImmutableSet.Builder<OwsMetadata> metadata = ImmutableSet.builder();

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  Description entity) {
            super(factory);
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.abstrakt = entity.getAbstract().orElse(null);
            this.keywords.addAll(entity.getKeywords());
            this.metadata.addAll(entity.getMetadata());
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withIdentifier(OwsCode id) {
            this.id = Objects.requireNonNull(id);
            return self();
        }

        @Override
        public B withTitle(OwsLanguageString title) {
            this.title = title;
            return self();
        }

        @Override
        public B withAbstract(OwsLanguageString abstrakt) {
            this.abstrakt = abstrakt;
            return self();
        }

        @Override
        public B withKeyword(OwsKeyword keyword) {
            this.keywords.add(Objects.requireNonNull(keyword));
            return self();
        }

        @Override
        public B withMetadata(OwsMetadata metadata) {
            this.metadata.add(Objects.requireNonNull(metadata));
            return self();
        }

        public OwsCode getId() {
            return this.id;
        }

        public OwsLanguageString getTitle() {
            return this.title;
        }

        public OwsLanguageString getAbstract() {
            return this.abstrakt;
        }

        public Set<OwsKeyword> getKeywords() {
            return this.keywords.build();
        }

        public Set<OwsMetadata> getMetadata() {
            return this.metadata.build();
        }

    }

}
