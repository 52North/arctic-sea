/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

/**
 * Interface for expressions.
 *
 * @author Christian Autermann
 */
public interface Expr {

    /**
     * Check if this expression is a value expression.
     *
     * @return if it is a value
     */
    default boolean isValue() {
        return false;
    }

    /**
     * Get this expression as a value expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<ValueExpr> asValue() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a member expression.
     *
     * @return if it is a member
     */
    default boolean isMember() {
        return false;
    }

    /**
     * Get this expression as a member expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<MemberExpr> asMember() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a binary expression.
     *
     * @return if it is a binary expression
     */
    default boolean isBinary() {
        return false;
    }

    /**
     * Get this expression as a binary expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<BinaryExpr<?>> asBinary() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a boolean binary expression.
     *
     * @return if it is a boolean binary expression
     */
    default boolean isBooleanBinary() {
        return false;
    }

    /**
     * Get this expression as a boolean binary expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<BooleanBinaryExpr> asBooleanBinary() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a comparison expression.
     *
     * @return if it is a comparison expression
     */
    default boolean isComparison() {
        return false;
    }

    /**
     * Get this expression as a comparison expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<ComparisonExpr> asComparison() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a unary expression.
     *
     * @return if it is a unary expression
     */
    default boolean isUnary() {
        return false;
    }

    /**
     * Get this expression as a unary expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<UnaryExpr<?>> asUnary() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a boolean unary expression.
     *
     * @return if it is a unary boolean expression
     */
    default boolean isBooleanUnary() {
        return false;
    }

    /**
     * Get this expression as a boolean unary expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<BooleanUnaryExpr> asBooleanUnary() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a method call expression.
     *
     * @return if it is a method call expression
     */
    default boolean isMethodCall() {
        return false;
    }

    /**
     * Get this expression as a method call expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<MethodCallExpr> asMethodCall() {
        return Optional.empty();
    }

    /**
     * Check if this expression is a boolean expresion.
     *
     * @return if it is a boolean expression
     */
    default boolean isBoolean() {
        return false;
    }

    /**
     * Get this expression as a boolean expression
     *
     * @return the expression or {@code Optional.empty()} if the type does not match
     */
    default Optional<BooleanExpr> asBoolean() {
        return Optional.empty();
    }

    /**
     * Accepts {@code visitor} for this expression.
     *
     * @param <T>     the visitor's return type
     * @param <X>     the exception type the visitor may throw
     * @param visitor the visitor
     *
     * @return the result of the visit
     *
     * @throws X if the visitor fails to visit this expression
     */
    <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X;
}
