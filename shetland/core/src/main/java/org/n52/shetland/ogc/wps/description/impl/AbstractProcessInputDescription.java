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

import com.google.common.base.Preconditions;
import org.n52.shetland.ogc.wps.InputOccurence;
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;

import java.math.BigInteger;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractProcessInputDescription extends AbstractDataDescription
        implements ProcessInputDescription {

    private final InputOccurence occurence;

    protected AbstractProcessInputDescription(AbstractBuilder<?, ?> builder) {
        super(builder);
        this.occurence = new InputOccurence(builder.getMinimalOccurence(), builder.getMaximalOccurence());
    }

    @Override
    public InputOccurence getOccurence() {
        return this.occurence;
    }

    protected abstract static class AbstractBuilder<T extends ProcessInputDescription, B extends AbstractBuilder<T, B>>
            extends AbstractDataDescription.AbstractBuilder<T, B>
            implements ProcessInputDescription.Builder<T, B> {

        private BigInteger minimalOccurence = BigInteger.ONE;
        private BigInteger maximalOccurence = BigInteger.ONE;

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory,
                                  ProcessInputDescription entity) {
            super(factory, entity);
            this.minimalOccurence = entity.getOccurence().getMin();
            this.maximalOccurence = entity.getOccurence().getMax().orElse(null);
        }

        protected AbstractBuilder(ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory) {
            super(factory);
        }

        @Override
        public B withMinimalOccurence(BigInteger min) {
            if (min != null) {
                Preconditions.checkArgument(min.compareTo(BigInteger.ZERO) >= 0, "minimalOccurence");
                this.minimalOccurence = min;
            } else {
                this.minimalOccurence = BigInteger.ONE;
            }
            return self();
        }

        @Override
        public B withMaximalOccurence(BigInteger max) {
            if (max != null) {
                Preconditions.checkArgument(max.compareTo(BigInteger.ZERO) > 0, "maximalOccurence");
                this.maximalOccurence = max;
            } else {
                this.maximalOccurence = BigInteger.ONE;
            }
            return self();
        }

        public BigInteger getMinimalOccurence() {
            return minimalOccurence;
        }

        public BigInteger getMaximalOccurence() {
            return maximalOccurence;
        }

    }

}
