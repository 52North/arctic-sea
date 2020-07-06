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

package org.n52.svalbard.odata.core.expr.bool;

import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.svalbard.odata.core.expr.BinaryExpr;
import org.n52.svalbard.odata.core.expr.Expr;
import org.n52.svalbard.odata.core.expr.ExprVisitor;

import java.util.Optional;

/**
 * Class to hold a comparison expression.
 *
 * @author Christian Autermann
 */
public class ComparisonExpr extends BinaryExpr<ComparisonOperator> implements BooleanExpr {

    /**
     * Create a new {@code ComparisonExpr}.
     *
     * @param operator the operator
     * @param left     the left operand
     * @param right    the right operand
     */
    public ComparisonExpr(ComparisonOperator operator, Expr left, Expr right) {
        super(operator, left, right);
    }

    @Override
    public boolean isComparison() {
        return true;
    }

    @Override
    public Optional<ComparisonExpr> asComparison() {
        return Optional.of(this);
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitComparison(this);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ComparisonExpr)) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public String toODataString() {
        return String.format("(%s %s %s)",
                             getLeft().toODataString(),
                             getSTAName(getOperator()),
                             getRight().toODataString());
    }

    private String getSTAName(ComparisonOperator operator) {
        switch (operator) {
            case PropertyIsEqualTo:
                return "eq";
            case PropertyIsNotEqualTo:
                return "ne";
            case PropertyIsLessThan:
                return "lt";
            case PropertyIsGreaterThan:
                return "gt";
            case PropertyIsLessThanOrEqualTo:
                return "le";
            case PropertyIsGreaterThanOrEqualTo:
                return "ge";
            default:
                throw new IllegalArgumentException(String.format("Operators %s is not supported.", operator));
        }
    }
}
