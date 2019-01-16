/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.InputOccurence;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;

import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractProcessInputDescription extends AbstractDataDescription
        implements ProcessInputDescription {

    private final InputOccurence occurence;

    protected AbstractProcessInputDescription(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             new InputOccurence(builder.getMinimalOccurence(),
                                builder.getMaximalOccurence()));
    }

    public AbstractProcessInputDescription(OwsCode id,
                                           OwsLanguageString title,
                                           OwsLanguageString abstrakt,
                                           Set<OwsKeyword> keywords,
                                           Set<OwsMetadata> metadata,
                                           InputOccurence occurence) {
        super(id, title, abstrakt, keywords, metadata);
        this.occurence = Objects.requireNonNull(occurence, "occurence");
    }

    @Override
    public InputOccurence getOccurence() {
        return this.occurence;
    }

    protected abstract static class AbstractBuilder<T extends ProcessInputDescription,
                                                    B extends ProcessInputDescription.Builder<T, B>>
            extends AbstractDataDescription.AbstractBuilder<T, B>
            implements ProcessInputDescription.Builder<T, B> {

        private BigInteger minimalOccurence = BigInteger.ONE;
        private BigInteger maximalOccurence = BigInteger.ONE;

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withMinimalOccurence(BigInteger min) {
            if (min != null) {
                Preconditions.checkArgument(min.compareTo(BigInteger.ZERO) >= 0, "minimalOccurence");
                this.minimalOccurence = min;
            } else {
                this.minimalOccurence = BigInteger.ONE;
            }
            return (B) this;
        }

        @SuppressWarnings(value = "unchecked")
        @Override
        public B withMaximalOccurence(BigInteger max) {
            if (max != null) {
                Preconditions.checkArgument(max.compareTo(BigInteger.ZERO) > 0, "maximalOccurence");
                this.maximalOccurence = max;
            } else {
                this.maximalOccurence = BigInteger.ONE;
            }
            return (B) this;
        }

        public BigInteger getMinimalOccurence() {
            return minimalOccurence;
        }

        public BigInteger getMaximalOccurence() {
            return maximalOccurence;
        }

    }

}
