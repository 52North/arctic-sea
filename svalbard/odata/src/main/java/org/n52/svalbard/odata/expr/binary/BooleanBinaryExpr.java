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
package org.n52.svalbard.odata.expr.binary;

import java.util.Optional;

import org.n52.shetland.ogc.filter.FilterConstants.BinaryLogicOperator;
import org.n52.svalbard.odata.expr.ExprVisitor;

/**
 * Class to hold a binary boolean expression
 *
 * @author Christian Autermann
 */
public class BooleanBinaryExpr extends BinaryExpr<BinaryLogicOperator> implements BooleanExpr {

    /**
     * Create a new {@code BooleanBinaryExpr}.
     *
     * @param operator the operator
     * @param left     the left operand
     * @param right    the right operand
     */
    public BooleanBinaryExpr(BinaryLogicOperator operator, BooleanExpr left, BooleanExpr right) {
        super(operator, left, right);
    }

    @Override
    public boolean isBooleanBinary() {
        return true;
    }

    @Override
    public Optional<BooleanBinaryExpr> asBooleanBinary() {
        return Optional.of(this);
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitBooleanBinary(this);
    }

    @Override
    public BooleanExpr getLeft() {
        return (BooleanExpr) super.getLeft();
    }

    @Override
    public BooleanExpr getRight() {
        return (BooleanExpr) super.getRight();
    }

}
