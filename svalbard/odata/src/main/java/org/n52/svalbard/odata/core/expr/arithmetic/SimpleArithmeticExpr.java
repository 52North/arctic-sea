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
package org.n52.svalbard.odata.core.expr.arithmetic;

import org.n52.shetland.ogc.filter.FilterConstants.SimpleArithmeticOperator;
import org.n52.svalbard.odata.core.expr.binary.BinaryExpr;
import org.n52.svalbard.odata.core.expr.ExprVisitor;

import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class SimpleArithmeticExpr extends BinaryExpr<SimpleArithmeticOperator> implements ArithmeticExpr {

    /**
     * Create a new {@code BinaryExpr}.
     *
     * @param operator the operator
     * @param left     the left operand
     * @param right    the right operand
     */
    public SimpleArithmeticExpr(SimpleArithmeticOperator operator, ArithmeticExpr left, ArithmeticExpr right) {
        super(operator, left, right);
    }

    /**
     * Check if this expression is a arithmetic expresion.
     *
     * @return if it is a arithmetic expression
     */
    @Override public boolean isArithmetic() {
        return true;
    }

    /**
     * Get this expression as a arithmetic expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    @Override public Optional<ArithmeticExpr> asArithmetic() {
        return Optional.of(this);
    }

    /**
     * Accepts {@code visitor} for this expression.
     *
     * @param visitor the visitor
     * @return the result of the visit
     * @throws X if the visitor fails to visit this expression
     */
    @Override public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitSimpleArithmetic(this);
    }
}
