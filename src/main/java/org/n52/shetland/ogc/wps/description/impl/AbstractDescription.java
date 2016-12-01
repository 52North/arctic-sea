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

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;

import com.google.common.collect.ImmutableSet;

import org.n52.shetland.ogc.wps.description.Description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractDescription implements Description {

    private final OwsCode id;
    private final OwsLanguageString title;
    private final Optional<OwsLanguageString> abstrakt;
    private final Set<OwsKeyword> keywords;
    private final Set<OwsMetadata> metadata;

    public AbstractDescription(AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata());
    }

    public AbstractDescription(OwsCode id,
                               OwsLanguageString title,
                               OwsLanguageString abstrakt,
                               Set<OwsKeyword> keywords,
                               Set<OwsMetadata> metadata) {
        this.id = Objects.requireNonNull(id, "id");
        this.title = title == null ? new OwsLanguageString(id.getValue())
                     : title;
        this.abstrakt = Optional.ofNullable(abstrakt);
        this.keywords = keywords == null ? Collections.emptySet() : keywords;
        this.metadata = metadata == null ? Collections.emptySet() : metadata;
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
        return this.abstrakt;
    }

    @Override
    public Set<OwsKeyword> getKeywords() {
        return Collections.unmodifiableSet(this.keywords);
    }

    @Override
    public Set<OwsMetadata> getMetadata() {
        return Collections.unmodifiableSet(this.metadata);
    }

    protected static abstract class AbstractBuilder<T extends Description, B extends Description.Builder<T, B>>
            implements Description.Builder<T, B> {

        private OwsCode id;
        private OwsLanguageString title;
        private OwsLanguageString abstrakt;
        private final ImmutableSet.Builder<OwsKeyword> keywords = ImmutableSet
                .builder();
        private final ImmutableSet.Builder<OwsMetadata> metadata = ImmutableSet
                .builder();

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withIdentifier(OwsCode id) {
            this.id = Objects.requireNonNull(id);
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withTitle(OwsLanguageString title) {
            this.title = title;
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withAbstract(OwsLanguageString abstrakt) {
            this.abstrakt = abstrakt;
            return (B) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public B withKeyword(OwsKeyword keyword) {
            this.keywords.add(Objects.requireNonNull(keyword));
            return (B) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public B withMetadata(OwsMetadata metadata) {
            this.metadata.add(Objects.requireNonNull(metadata));
            return (B) this;
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
