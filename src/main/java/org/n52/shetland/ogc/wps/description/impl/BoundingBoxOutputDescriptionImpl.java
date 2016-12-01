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
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCRS;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class BoundingBoxOutputDescriptionImpl
        extends AbstractProcessOutputDescription
        implements BoundingBoxOutputDescription {

    private final OwsCRS defaultCRS;
    private final Set<OwsCRS> supportedCRS;

    protected BoundingBoxOutputDescriptionImpl(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             builder.getDefaultCRS(),
             builder.getSupportedCRS());
    }

    public BoundingBoxOutputDescriptionImpl(OwsCode id,
                                            OwsLanguageString title,
                                            OwsLanguageString abstrakt,
                                            Set<OwsKeyword> keywords,
                                            Set<OwsMetadata> metadata,
                                            OwsCRS defaultCRS,
                                            Set<OwsCRS> supportedCRS) {
        super(id, title, abstrakt, keywords, metadata);
        this.defaultCRS = Objects.requireNonNull(defaultCRS, "defaultCRS");
        this.supportedCRS = supportedCRS == null ? Collections.emptySet() : supportedCRS;
    }

    @Override
    public Set<OwsCRS> getSupportedCRS() {
        return Collections.unmodifiableSet(this.supportedCRS);
    }

    @Override
    public OwsCRS getDefaultCRS() {
        return this.defaultCRS;
    }

    public static abstract class AbstractBuilder<T extends BoundingBoxOutputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractProcessOutputDescription.AbstractBuilder<T, B>
            implements BoundingBoxOutputDescription.Builder<T, B> {

        private OwsCRS defaultCRS;
        private final ImmutableSet.Builder<OwsCRS> supportedCRS = ImmutableSet.builder();

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withDefaultCRS(OwsCRS defaultCRS) {
            this.defaultCRS = defaultCRS;
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withSupportedCRS(OwsCRS uom) {
            if (uom != null) {
                this.supportedCRS.add(uom);
            }
            return (B) this;
        }

        public OwsCRS getDefaultCRS() {
            return defaultCRS;
        }

        public Set<OwsCRS> getSupportedCRS() {
            return supportedCRS.build();
        }

    }

    public static class Builder extends AbstractBuilder<BoundingBoxOutputDescription, Builder> {

        @Override
        public BoundingBoxOutputDescription build() {
            return new BoundingBoxOutputDescriptionImpl(this);
        }
    }

}
