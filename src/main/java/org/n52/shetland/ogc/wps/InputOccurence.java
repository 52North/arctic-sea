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
package org.n52.shetland.ogc.wps;

import java.math.BigInteger;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class InputOccurence {

    private final BigInteger min;
    private final Optional<BigInteger> max;

    public InputOccurence(BigInteger min, BigInteger max) {
        this.min = min == null ? BigInteger.ONE : min;

        if (max == null) {
            this.max = Optional.of(BigInteger.ONE);
        } else if (max.compareTo(BigInteger.ZERO) < 0) {
            this.max = Optional.empty();
        } else {
            this.max = Optional.of(max);
        }

        if (this.min.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("minimum < 0");
        }
        if (this.max.isPresent() && this.max.get().compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("maximum <= 0");
        }
        if (this.max.isPresent() && this.min.compareTo(this.max.get()) > 0) {
            throw new IllegalArgumentException("minimum > maximum");
        }
    }

    public BigInteger getMin() {
        return this.min;
    }

    public Optional<BigInteger> getMax() {
        return this.max;
    }

    public boolean isRequired() {
        return this.min.compareTo(BigInteger.ZERO) > 0;
    }

    public boolean isMultiple() {
        return !this.max.isPresent() || this.max.get().compareTo(BigInteger.ONE) > 0;
    }

    public boolean isInBounds(BigInteger occurence) {
        return this.min.compareTo(occurence) <= 0 &&
               (!this.max.isPresent() || this.max.get().compareTo(occurence) >= 0);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.min, this.max.map(Object::toString).orElse(""));
    }
}
