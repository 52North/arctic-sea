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

import com.google.common.collect.ImmutableSet;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxInputDescriptionImpl extends AbstractProcessInputDescription
        implements BoundingBoxInputDescription {

    private final Set<OwsCRS> supportedCRS;
    private final OwsCRS defaultCRS;

    protected BoundingBoxInputDescriptionImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.supportedCRS = Objects.requireNonNull(builder.getSupportedCRS(), "supportedCRS");
        this.defaultCRS = Objects.requireNonNull(builder.getDefaultCRS(), "defaultCRS");
    }

    @Override
    public Set<OwsCRS> getSupportedCRS() {
        return Collections.unmodifiableSet(this.supportedCRS);
    }

    @Override
    public OwsCRS getDefaultCRS() {
        return this.defaultCRS;
    }

    @Override
    public BoundingBoxInputDescription.Builder<?, ?> newBuilder() {
        return getFactory().boundingBoxInput(this);
    }

    public abstract static class AbstractBuilder<T extends BoundingBoxInputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessInputDescription.AbstractBuilder<T, B>
            implements BoundingBoxInputDescription.Builder<T, B> {

        private OwsCRS defaultCRS;
        private final ImmutableSet.Builder<OwsCRS> supportedCRS = ImmutableSet.builder();

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  BoundingBoxInputDescription entity) {
            super(factory, entity);
            this.defaultCRS = entity.getDefaultCRS();
            this.supportedCRS.addAll(entity.getSupportedCRS());
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withDefaultCRS(OwsCRS defaultCRS) {
            this.defaultCRS = defaultCRS;
            return self();
        }

        @Override
        public B withSupportedCRS(OwsCRS uom) {
            if (uom != null) {
                this.supportedCRS.add(uom);
            }
            return self();
        }

        public OwsCRS getDefaultCRS() {
            return defaultCRS;
        }

        public Set<OwsCRS> getSupportedCRS() {
            return supportedCRS.build();
        }
    }

    public static class Builder extends AbstractBuilder<BoundingBoxInputDescription, Builder> {
        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          BoundingBoxInputDescription entity) {
            super(factory, entity);
        }

        @Override
        public BoundingBoxInputDescription build() {
            return new BoundingBoxInputDescriptionImpl(this);
        }
    }

}
