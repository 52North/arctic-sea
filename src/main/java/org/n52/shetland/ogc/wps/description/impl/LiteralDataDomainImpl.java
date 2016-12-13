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

import java.util.Objects;
import java.util.Optional;

import org.n52.shetland.ogc.ows.OwsAnyValue;
import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;

public class LiteralDataDomainImpl implements LiteralDataDomain {

    private final OwsPossibleValues possibleValues;
    private final Optional<OwsDomainMetadata> dataType;
    private final Optional<OwsDomainMetadata> uom;
    private final Optional<OwsValue> defaultValue;

    public LiteralDataDomainImpl(AbstractLiteralDataDomainBuilder<?, ?> builder) {
        this(builder.getPossibleValues(),
             builder.getDataType(),
             builder.getUOM(),
             builder.getDefaultValue());
    }

    public LiteralDataDomainImpl(OwsPossibleValues possibleValues,
                                 OwsDomainMetadata dataType,
                                 OwsDomainMetadata uom,
                                 OwsValue defaultValue) {
        this.possibleValues = Objects.requireNonNull(possibleValues, "possibleValues");
        this.dataType = Optional.ofNullable(dataType);
        this.uom = Optional.ofNullable(uom);
        this.defaultValue = Optional.ofNullable(defaultValue);
    }

    @Override
    public OwsPossibleValues getPossibleValues() {
        return this.possibleValues;
    }

    @Override
    public Optional<OwsDomainMetadata> getDataType() {
        return this.dataType;
    }

    @Override
    public Optional<OwsDomainMetadata> getUOM() {
        return this.uom;
    }

    @Override
    public Optional<OwsValue> getDefaultValue() {
        return this.defaultValue;
    }

    public static abstract class AbstractLiteralDataDomainBuilder<T extends LiteralDataDomain, B extends AbstractLiteralDataDomainBuilder<T, B>>
            implements LiteralDataDomain.Builder<T, B> {

        private OwsPossibleValues possibleValues = OwsAnyValue.instance();
        private OwsDomainMetadata dataType;
        private OwsDomainMetadata uom;
        private OwsValue defaultValue;

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
        @SuppressWarnings(value = "unchecked")
        public B withDataType(OwsDomainMetadata dataType) {
            this.dataType = Objects.requireNonNull(dataType);
            return (B) this;
        }

        @Override
        @SuppressWarnings(value = "unchecked")
        public B withDefaultValue(OwsValue value) {
            this.defaultValue = Objects.requireNonNull(value);
            return (B) this;
        }

        @Override
        @SuppressWarnings(value = "unchecked")
        public B withUOM(OwsDomainMetadata uom) {
            this.uom = uom;
            return (B) this;
        }

        @Override
        @SuppressWarnings(value = "unchecked")
        public B withValueDescription(OwsPossibleValues possibleValues) {
            if (this.possibleValues == null) {
                this.possibleValues = OwsAnyValue.instance();
            } else {
                this.possibleValues = possibleValues;
            }
            return (B) this;
        }

    }

    public static class Builder extends AbstractLiteralDataDomainBuilder<LiteralDataDomain, Builder> {
        @Override
        public LiteralDataDomain build() {
            return new LiteralDataDomainImpl(this);
        }
    }
}
