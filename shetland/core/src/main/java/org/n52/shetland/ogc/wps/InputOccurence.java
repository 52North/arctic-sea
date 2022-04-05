/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InputOccurence {
    private static final String MIN_OCCURS = "minOccurs";
    private static final String MAX_OCCURS = "maxOccurs";
    private final BigInteger min;
    private final BigInteger max;

    @JsonCreator
    public InputOccurence(@JsonProperty(value = MIN_OCCURS, required = true) BigInteger min,
                          @JsonProperty(MAX_OCCURS) BigInteger max) {
        this.min = min == null ? BigInteger.ONE : min;

        if (max == null) {
            this.max = null;
        } else if (max.compareTo(BigInteger.ZERO) < 0) {
            this.max = null;
        } else {
            this.max = max;
        }

        if (this.min.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("minimum < 0");
        }
        if (this.max != null && this.max.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("maximum <= 0");
        }
        if (this.max != null && this.min.compareTo(this.max) > 0) {
            throw new IllegalArgumentException("minimum > maximum");
        }
    }

    @JsonProperty(MIN_OCCURS)
    public BigInteger getMin() {
        return this.min;
    }

    @JsonProperty(MAX_OCCURS)
    public Optional<BigInteger> getMax() {
        return Optional.ofNullable(this.max);
    }

    @JsonIgnore
    public boolean isRequired() {
        return this.min.compareTo(BigInteger.ZERO) > 0;
    }

    @JsonIgnore
    public boolean isMultiple() {
        return this.max == null || this.max.compareTo(BigInteger.ONE) > 0;
    }

    public boolean isInBounds(BigInteger occurence) {
        return this.min.compareTo(occurence) <= 0 &&
               (this.max == null || this.max.compareTo(occurence) >= 0);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.min, this.max);
    }

    public static InputOccurence zeroOrOne() {
        return new InputOccurence(BigInteger.ZERO, BigInteger.ONE);
    }

    public static InputOccurence one() {
        return new InputOccurence(BigInteger.ONE, BigInteger.ONE);
    }

    public static InputOccurence zeroOrMore() {
        return new InputOccurence(BigInteger.ZERO, null);
    }

    public static InputOccurence oneOrMore() {
        return new InputOccurence(BigInteger.ONE, null);
    }
}
