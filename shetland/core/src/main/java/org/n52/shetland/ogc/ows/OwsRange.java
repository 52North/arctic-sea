/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import org.n52.janmayen.Optionals;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/**
 * A range of values of a numeric parameter. This range can be continuous or
 * discrete, defined by a fixed spacing between adjacent valid values. If the
 * MinimumValue or MaximumValue is not included, there is no value limit in that
 * direction. Inclusion of the specified minimum and maximum values in the range
 * shall be defined by the rangeClosure.
 *
 * @author Christian Autermann
 */
public class OwsRange
        implements OwsValueRestriction {
    public static final Comparator<OwsRange> COMPARATOR =
            Comparator.comparing(OwsRange::getLowerBound, Optionals.nullsFirst())
                    .thenComparing(Comparator.comparing(OwsRange::getUpperBound, Optionals.nullsLast()));

    public static final String CLOSED = "closed";
    public static final String CLOSED_OPEN = "closed-open";
    public static final String OPEN_CLOSED = "open-closed";
    public static final String OPEN = "open";
    private final Bound lowerBound;
    private final Bound upperBound;
    private final Optional<OwsValue> spacing;

    public OwsRange(OwsValue lowerBound, OwsValue upperBound, String type) {
        this(lowerBound, upperBound, type, null);
    }

    public OwsRange(OwsValue lowerBound, OwsValue upperBound, String type, OwsValue spacing) {
        this(lowerBound, getLowerType(type), upperBound, getUpperType(type), spacing);
    }

    public OwsRange(OwsValue lowerBound, OwsValue upperBound) {
        this(lowerBound, BoundType.CLOSED, upperBound, BoundType.CLOSED, null);
    }

    public OwsRange(OwsValue lowerBound, BoundType lowerBoundType, OwsValue upperBound, BoundType upperBoundType) {
        this(lowerBound, lowerBoundType, upperBound, upperBoundType, null);
    }

    public OwsRange(
            OwsValue lowerBound, BoundType lowerBoundType, OwsValue upperBound, BoundType upperBoundType,
            OwsValue spacing) {
        this(new Bound(lowerBoundType, lowerBound), new Bound(upperBoundType, upperBound), spacing);
    }

    private OwsRange(Bound lowerBound, Bound upperBound, OwsValue spacing) {
        this.lowerBound = Objects.requireNonNull(lowerBound);
        this.upperBound = Objects.requireNonNull(upperBound);
        this.spacing = Optional.ofNullable(spacing);
    }

    public Optional<OwsValue> getLowerBound() {
        return this.lowerBound.getValue();
    }

    public Optional<OwsValue> getUpperBound() {
        return this.upperBound.getValue();
    }

    public BoundType getLowerBoundType() {
        return this.lowerBound.getType();
    }

    public BoundType getUpperBoundType() {
        return this.upperBound.getType();
    }

    public Optional<OwsValue> getSpacing() {
        return spacing;
    }

    public String getType() {
        if (getLowerBoundType() == BoundType.OPEN) {
            if (getUpperBoundType() == BoundType.OPEN) {
                return OPEN;
            } else {
                return OPEN_CLOSED;
            }
        } else {
            if (getUpperBoundType() == BoundType.OPEN) {
                return CLOSED_OPEN;
            } else {
                return CLOSED;
            }
        }
    }

    @Override
    public OwsRange asRange() {
        return this;
    }

    @Override
    public boolean isRange() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lowerBound, this.upperBound, this.spacing);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            OwsRange that = (OwsRange) obj;
            return Objects.equals(this.lowerBound, that.getLowerBound().orElse(null))
                    && Objects.equals(this.upperBound, that.getUpperBound().orElse(null))
                    && Objects.equals(this.spacing, that.getSpacing());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).addValue(this.lowerBound.asLower() + ", " + this.upperBound.asUpper())
                .toString();
    }

    private static BoundType getLowerType(String type) {
        switch (Strings.nullToEmpty(type)) {
            case CLOSED:
            case CLOSED_OPEN:
                return BoundType.CLOSED;
            case OPEN_CLOSED:
            case OPEN:
                return BoundType.OPEN;
            default:
                return BoundType.CLOSED;
        }
    }

    private static BoundType getUpperType(String type) {
        switch (Strings.nullToEmpty(type)) {
            case CLOSED:
            case OPEN_CLOSED:
                return BoundType.CLOSED;
            case CLOSED_OPEN:
            case OPEN:
                return BoundType.OPEN;
            default:
                return BoundType.CLOSED;
        }
    }

    private static class Bound {
        private static final String INFINITY = "\u221e";
        private final BoundType type;
        private final Optional<OwsValue> value;

        Bound(BoundType type, OwsValue value) {
            this.type = Objects.requireNonNull(type);
            this.value = Optional.ofNullable(value);
        }

        BoundType getType() {
            return type;
        }

        Optional<OwsValue> getValue() {
            return value;
        }

        String asUpper() {
            return this.getType().asUpper() + getValue().map(OwsValue::getValue).orElse(INFINITY);
        }

        String asLower() {
            return this.getType().asLower() + getValue().map(OwsValue::getValue).orElse("-" + INFINITY);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.type, this.value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null && obj.getClass() == getClass()) {
                Bound that = (Bound) obj;
                return Objects.equals(this.type, that.getType()) && Objects.equals(this.value, that.getValue());
            }
            return false;
        }
    }

    public enum BoundType {
        OPEN('(', ')'), CLOSED('[', ']');

        private final char upper;
        private final char lower;

        BoundType(char upper, char lower) {
            this.upper = upper;
            this.lower = lower;
        }

        private char asUpper() {
            return this.upper;
        }

        private char asLower() {
            return this.lower;
        }
    }

}
