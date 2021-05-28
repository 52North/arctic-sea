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
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralInputDescriptionImpl extends AbstractProcessInputDescription implements LiteralInputDescription {

    private final LiteralDataDomain defaultLiteralDataDomain;
    private final Set<LiteralDataDomain> supportedLiteralDataDomains;

    protected LiteralInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.defaultLiteralDataDomain = Objects.requireNonNull(builder.getDefaultLiteralDataDomain(),
                                                               "defaultLiteralDataDomain");
        this.supportedLiteralDataDomains = Objects.requireNonNull(builder.getSupportedLiteralDataDomains(),
                                                                  "supportedLiteralDataDomains");
    }

    @Override
    public LiteralDataDomain getDefaultLiteralDataDomain() {
        return this.defaultLiteralDataDomain;
    }

    @Override
    public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
        return Collections.unmodifiableSet(supportedLiteralDataDomains);
    }

    @Override
    public LiteralInputDescription.Builder<?, ?> newBuilder() {
        return getFactory().literalInput(this);
    }

    public abstract static class AbstractBuilder<T extends LiteralInputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessInputDescription.AbstractBuilder<T, B>
            implements LiteralInputDescription.Builder<T, B> {

        private LiteralDataDomain defaultLiteralDataDomain;
        private final ImmutableSet.Builder<LiteralDataDomain> supportedLiteralDataDomains = ImmutableSet.builder();

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  LiteralInputDescription entity) {
            super(factory, entity);
            this.defaultLiteralDataDomain = entity.getDefaultLiteralDataDomain();
            this.supportedLiteralDataDomains.addAll(entity.getSupportedLiteralDataDomains());
        }

        public LiteralDataDomain getDefaultLiteralDataDomain() {
            return this.defaultLiteralDataDomain;
        }

        public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
            return this.supportedLiteralDataDomains.build();
        }

        @Override
        public B withDefaultLiteralDataDomain(LiteralDataDomain literalDataDomain) {
            this.defaultLiteralDataDomain = Objects.requireNonNull(literalDataDomain);
            return self();
        }

        @Override
        public B withSupportedLiteralDataDomain(LiteralDataDomain domain) {
            if (domain != null) {
                this.supportedLiteralDataDomains.add(domain);
            }
            return self();
        }

    }

    public static class Builder extends AbstractBuilder<LiteralInputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          LiteralInputDescription entity) {
            super(factory, entity);
        }

        @Override
        public LiteralInputDescription build() {
            return new LiteralInputDescriptionImpl(this);
        }
    }

}
