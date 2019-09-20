/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import com.google.common.collect.ImmutableSet;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralOutputDescriptionImpl extends AbstractProcessOutputDescription implements LiteralOutputDescription {

    private final LiteralDataDomain defaultLiteralDataDomain;
    private final Set<LiteralDataDomain> supportedLiteralDataDomains;

    protected LiteralOutputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.defaultLiteralDataDomain = Objects.requireNonNull(builder.getDefaultLiteralDataDomain(),
                                                               "defaultLiteralDataDomain");
        this.supportedLiteralDataDomains = Objects.requireNonNull(builder.getSupportedLiteralDataDomains(),
                                                                  "supportedLiteralDataDomains");
    }

    @Override
    public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
        return Collections.unmodifiableSet(this.supportedLiteralDataDomains);
    }

    @Override
    public LiteralDataDomain getDefaultLiteralDataDomain() {
        return this.defaultLiteralDataDomain;
    }

    @Override
    public LiteralOutputDescription.Builder<?, ?> newBuilder() {
        return getFactory().literalOutput(this);
    }

    public abstract static class AbstractBuilder<T extends LiteralOutputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessOutputDescription.AbstractBuilder<T, B>
            implements LiteralOutputDescription.Builder<T, B> {

        private LiteralDataDomain defaultLiteralDataDomain;
        private final ImmutableSet.Builder<LiteralDataDomain> supportedLiteralDataDomains = ImmutableSet.builder();

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  LiteralOutputDescription entity) {
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

    public static class Builder extends AbstractBuilder<LiteralOutputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          LiteralOutputDescription entity) {
            super(factory, entity);
        }

        @Override
        public LiteralOutputDescription build() {
            return new LiteralOutputDescriptionImpl(this);
        }
    }

}
