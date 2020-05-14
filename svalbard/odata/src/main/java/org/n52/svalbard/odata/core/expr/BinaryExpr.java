/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

package org.n52.svalbard.odata.core.expr;

import java.util.Objects;
import java.util.Optional;

/**
 * Class to hold a binary expression.
 *
 * @param <T> the operator type
 * @author Christian Autermann
 */
public abstract class BinaryExpr<T> implements Expr {
    
    private final T operator;
    private final Expr left;
    private final Expr right;

    /**
     * Create a new {@code BinaryExpr}.
     *
     * @param operator the operator
     * @param left     the left operand
     * @param right    the right operand
     */
    public BinaryExpr(T operator, Expr left, Expr right) {
        this.operator = Objects.requireNonNull(operator);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    /**
     * Get the operator.
     *
     * @return the operator
     */
    public T getOperator() {
        return operator;
    }

    /**
     * Get the left operand.
     *
     * @return the left operand
     */
    public Expr getLeft() {
        return left;
    }

    /**
     * Get the right operand.
     *
     * @return the right operand
     */
    public Expr getRight() {
        return right;
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public Optional<BinaryExpr<?>> asBinary() {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return format(this.operator.toString());
    }

    @Override public String toODataString() {
        return format(this.operator.toString().toLowerCase());
    }

    private String format(String operator) {
        return String.format("(%s %s %s)", this.left, operator, this.right);
    }

    @Override public int hashCode() {
        return Objects.hash(this.operator, this.left, this.right);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BinaryExpr)) {
            return false;
        }
        return Objects.equals(this.operator, ((BinaryExpr) o).getOperator())
                && Objects.equals(this.left, ((BinaryExpr) o).getLeft())
                && Objects.equals(this.right, ((BinaryExpr) o).getRight());
    }

}
