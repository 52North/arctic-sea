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
package org.n52.shetland.ogc.wps.description.impl;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

import com.google.common.collect.ImmutableSet;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ComplexInputDescriptionImpl extends AbstractProcessInputDescription implements ComplexInputDescription {

    private final Format defaultFormat;
    private final Set<Format> supportedFormats;
    private final BigInteger maximumMegabytes;

    protected ComplexInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.defaultFormat = Objects.requireNonNull(builder.getDefaultFormat(), "defaultFormat");
        this.supportedFormats = Objects.requireNonNull(builder.getSupportedFormats(), "supportedFormats");
        this.maximumMegabytes = builder.getMaximumMegabytes();
    }

    @Override
    public Set<Format> getSupportedFormats() {
        return Collections.unmodifiableSet(supportedFormats);
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Format getDefaultFormat() {
        return defaultFormat;
    }

    @Override
    public Optional<BigInteger> getMaximumMegabytes() {
        return Optional.ofNullable(this.maximumMegabytes);
    }

    @Override
    public ComplexInputDescription.Builder<?, ?> newBuilder() {
        return getFactory().complexInput(this);
    }

    public abstract static class AbstractBuilder<
            T extends ComplexInputDescription,
            B extends AbstractBuilder<T, B>> extends AbstractProcessInputDescription.AbstractBuilder<T, B>
            implements ComplexInputDescription.Builder<T, B> {

        private final ImmutableSet.Builder<Format> supportedFormats = ImmutableSet.builder();
        private Format defaultFormat;
        private BigInteger maximumMegabytes;

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                ComplexInputDescription entity) {
            super(factory, entity);
            this.defaultFormat = entity.getDefaultFormat();
            this.supportedFormats.addAll(entity.getSupportedFormats());
            this.maximumMegabytes = entity.getMaximumMegabytes().orElse(null);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withMaximumMegabytes(BigInteger maximumMegabytes) {
            if (maximumMegabytes == null || maximumMegabytes.compareTo(BigInteger.ZERO) > 0) {
                this.maximumMegabytes = maximumMegabytes;
            }
            return self();
        }

        @Override
        @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
        public B withDefaultFormat(Format format) {
            this.defaultFormat = Objects.requireNonNull(format);
            this.supportedFormats.add(format);
            return self();
        }

        @Override
        public B withSupportedFormat(Format format) {
            if (format != null) {
                this.supportedFormats.add(format);
            }
            return self();
        }

        public Set<Format> getSupportedFormats() {
            return supportedFormats.build();
        }

        @SuppressFBWarnings({ "EI_EXPOSE_REP" })
        public Format getDefaultFormat() {
            return defaultFormat;
        }

        public BigInteger getMaximumMegabytes() {
            return maximumMegabytes;
        }

    }

    public static class Builder extends AbstractBuilder<ComplexInputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                ComplexInputDescription entity) {
            super(factory, entity);
        }

        @Override
        public ComplexInputDescription build() {
            return new ComplexInputDescriptionImpl(this);
        }
    }

}
