/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import org.n52.svalbard.odata.core.expr.arithmetic.NumericValueExpr;
import org.n52.svalbard.odata.core.expr.arithmetic.SimpleArithmeticExpr;
import org.n52.svalbard.odata.core.expr.bool.BooleanBinaryExpr;
import org.n52.svalbard.odata.core.expr.bool.BooleanUnaryExpr;
import org.n52.svalbard.odata.core.expr.bool.ComparisonExpr;
import org.n52.svalbard.odata.core.expr.temporal.TimeValueExpr;

/**
 * Visitor for expressions.
 *
 * @param <T> the type this visitor returns
 * @param <X> the exception type this visitor may throw
 *
 * @author Christian Autermann
 */
public interface ExprVisitor<T, X extends Throwable> {
    /**
     * Visit a boolean binary expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitBooleanBinary(BooleanBinaryExpr expr) throws X;

    /**
     * Visit a boolean unary expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitBooleanUnary(BooleanUnaryExpr expr) throws X;

    /**
     * Visit a comparison expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitComparison(ComparisonExpr expr) throws X;

    /**
     * Visit a method call expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitMethodCall(MethodCallExpr expr) throws X;

    /**
     * Visit a member expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitMember(MemberExpr expr) throws X;

    /**
     * Visit a value expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitString(StringValueExpr expr) throws X;

    /**
     * Visit a arithmetic expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitSimpleArithmetic(SimpleArithmeticExpr expr) throws X;

    /**
     * Visit a time expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitTime(TimeValueExpr expr) throws X;

    /**
     * Visit a geometry expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitGeometry(GeoValueExpr expr) throws X;

    /**
     * Visit a number expression.
     *
     * @param expr the expression
     *
     * @return the result of the visit
     *
     * @throws X if the visit fails
     */
    T visitNumeric(NumericValueExpr expr) throws X;

}
