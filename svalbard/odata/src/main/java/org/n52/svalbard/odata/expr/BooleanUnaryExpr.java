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
package org.n52.svalbard.odata.expr;

import java.util.Optional;

import org.n52.shetland.ogc.filter.FilterConstants.UnaryLogicOperator;

/**
 * Class to hold a unary boolean expression.
 *
 * @author Christian Autermann
 */
public class BooleanUnaryExpr extends UnaryExpr<UnaryLogicOperator> implements BooleanExpr {

    /**
     * Create a new {@code BooleanUnaryExpr}.
     *
     * @param operator the operator
     * @param operand  the operand
     */
    public BooleanUnaryExpr(UnaryLogicOperator operator, BooleanExpr operand) {
        super(operator, operand);
    }

    @Override
    public boolean isBooleanUnary() {
        return true;
    }

    @Override
    public Optional<BooleanUnaryExpr> asBooleanUnary() {
        return Optional.of(this);
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitBooleanUnary(this);
    }

    @Override
    public BooleanExpr getOperand() {
        return (BooleanExpr) super.getOperand();

    }

}
