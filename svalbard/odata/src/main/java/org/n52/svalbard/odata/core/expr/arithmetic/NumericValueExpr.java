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
package org.n52.svalbard.odata.core.expr.arithmetic;

import org.n52.svalbard.odata.core.expr.ExprVisitor;

import java.util.Objects;
import java.util.Optional;

/**
 * Expression representing a value.
 *
 * @author Christian Autermann
 */
public class NumericValueExpr implements ArithmeticExpr {

    private final Number value;

    /**
     * Creates a new {@code ValueExpr}.
     *
     * @param value the value
     */
    public NumericValueExpr(Number value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Creates a new {@code ValueExpr}.
     *
     * @param value the value
     */
    public NumericValueExpr(String value) {
        this.value = Objects.requireNonNull(Double.valueOf(value));
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Number getValue() {
        return this.value;
    }

    @Override
    public boolean isNumericValue() {
        return true;
    }

    @Override
    public Optional<NumericValueExpr> asNumericValue() {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return String.format("As float: '%f'", this.value.floatValue());
    }

    @Override public String toODataString() {
        return Float.toString(this.value.floatValue());
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitNumeric(this);
    }

    @Override public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NumericValueExpr)) {
            return false;
        }
        return Objects.equals(this.value, ((NumericValueExpr) o).getValue());
    }

}
