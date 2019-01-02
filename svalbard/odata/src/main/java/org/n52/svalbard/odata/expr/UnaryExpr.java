/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.odata.expr;

import java.util.Objects;
import java.util.Optional;

/**
 * Class to represent a unary expression.
 *
 * @param <T> the operator type
 *
 * @author Christian Autermann
 */
public abstract class UnaryExpr<T> implements Expr {

    private final T operator;
    private final Expr operand;

    /**
     * Create a new {@code UnaryExpr}.
     *
     * @param operator the operator
     * @param operand  the operand
     */
    public UnaryExpr(T operator, Expr operand) {
        this.operator = Objects.requireNonNull(operator);
        this.operand = Objects.requireNonNull(operand);
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
     * Get the operand
     *
     * @return the operand
     */
    public Expr getOperand() {
        return operand;
    }

    @Override
    public Optional<UnaryExpr<?>> asUnary() {
        return Optional.of((UnaryExpr<?>) this);
    }

    @Override
    public String toString() {
        return String.format("(%s %s)", this.operator, this.operand);
    }

}
