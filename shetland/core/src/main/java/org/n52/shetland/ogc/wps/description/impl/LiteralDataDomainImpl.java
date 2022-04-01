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

import org.n52.janmayen.AbstractBuildable;
import org.n52.shetland.ogc.ows.OwsAnyValue;
import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;

import java.util.Objects;
import java.util.Optional;

public class LiteralDataDomainImpl
        extends AbstractBuildable<ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        implements LiteralDataDomain {
    private final OwsPossibleValues possibleValues;
    private final OwsDomainMetadata dataType;
    private final OwsDomainMetadata uom;
    private final OwsValue defaultValue;

    protected LiteralDataDomainImpl(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.possibleValues = Objects.requireNonNull(builder.getPossibleValues(), "possibleValues");
        this.dataType = builder.getDataType();
        this.uom = builder.getUOM();
        this.defaultValue = builder.getDefaultValue();
    }

    @Override
    public OwsPossibleValues getPossibleValues() {
        return this.possibleValues;
    }

    @Override
    public Optional<OwsDomainMetadata> getDataType() {
        return Optional.ofNullable(this.dataType);
    }

    @Override
    public Optional<OwsDomainMetadata> getUOM() {
        return Optional.ofNullable(this.uom);
    }

    @Override
    public Optional<OwsValue> getDefaultValue() {
        return Optional.ofNullable(this.defaultValue);
    }

    @Override
    public LiteralDataDomain.Builder<?, ?> newBuilder() {
        return getFactory().literalDataDomain(this);
    }

    public abstract static class AbstractBuilder<T extends LiteralDataDomain, B extends AbstractBuilder<T, B>>
            extends
            AbstractBuildable.AbstractBuilder<ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?>, T, B>
            implements LiteralDataDomain.Builder<T, B> {
        private OwsPossibleValues possibleValues = OwsAnyValue.instance();
        private OwsDomainMetadata dataType;
        private OwsDomainMetadata uom;
        private OwsValue defaultValue;

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  LiteralDataDomain entity) {
            super(factory);
            this.possibleValues = entity.getPossibleValues();
            this.dataType = entity.getDataType().orElse(null);
            this.uom = entity.getUOM().orElse(null);
            this.defaultValue = entity.getDefaultValue().orElse(null);
        }

        OwsPossibleValues getPossibleValues() {
            return possibleValues;
        }

        OwsDomainMetadata getDataType() {
            return dataType;
        }

        OwsValue getDefaultValue() {
            return defaultValue;
        }

        OwsDomainMetadata getUOM() {
            return uom;
        }

        @Override
        public B withDataType(OwsDomainMetadata dataType) {
            this.dataType = Objects.requireNonNull(dataType);
            return self();
        }

        @Override
        public B withDefaultValue(OwsValue value) {
            this.defaultValue = Objects.requireNonNull(value);
            return self();
        }

        @Override
        public B withUOM(OwsDomainMetadata uom) {
            this.uom = uom;
            return self();
        }

        @Override
        public B withValueDescription(OwsPossibleValues possibleValues) {
            if (this.possibleValues == null) {
                this.possibleValues = OwsAnyValue.instance();
            } else {
                this.possibleValues = possibleValues;
            }
            return self();
        }

    }

    public static class Builder extends AbstractBuilder<LiteralDataDomain, Builder> {

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        protected Builder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                          LiteralDataDomain entity) {
            super(factory, entity);
        }

        @Override
        public LiteralDataDomain build() {
            return new LiteralDataDomainImpl(this);
        }
    }
}
