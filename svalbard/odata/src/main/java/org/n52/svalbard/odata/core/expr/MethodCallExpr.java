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
package org.n52.svalbard.odata.core.expr;

import org.n52.svalbard.odata.core.expr.arithmetic.ArithmeticExpr;
import org.n52.svalbard.odata.core.expr.bool.BooleanExpr;
import org.n52.svalbard.odata.core.expr.temporal.TemporalExpr;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * Class to hold a method call expression.
 *
 * @author Christian Autermann
 */
public class MethodCallExpr implements BooleanExpr, ArithmeticExpr, TemporalExpr, TextExpr {

    private final String name;
    private final List<Expr> parameters;

    /**
     * Create a new {@code MethodCallExpr}.
     *
     * @param name       the method name
     * @param parameters the parameters
     */
    @SuppressFBWarnings({"EI_EXPOSE_REP2"})
    public MethodCallExpr(String name, List<Expr> parameters) {
        this.name = Objects.requireNonNull(name);
        this.parameters = Objects.requireNonNull(parameters);
    }

    /**
     * Create a new {@code MethodCallExpr}.
     *
     * @param name      the method name
     * @param parameter the parameter
     */
    public MethodCallExpr(String name, Expr... parameter) {
        this.name = Objects.requireNonNull(name);
        this.parameters = Objects.requireNonNull(Arrays.asList(parameter));
    }

    /**
     * Get the name of the method.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the parameters of this method call.
     *
     * @return the parameters
     */
    public List<Expr> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    @Override
    public boolean isMethodCall() {
        return true;
    }

    @Override
    public Optional<MethodCallExpr> asMethodCall() {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return format(this.parameters.stream().map(Expr::toString).collect(joining(", ")));
    }

    @Override
    public String toODataString() {
        return format(this.parameters.stream().map(Expr::toODataString).collect(joining(", ")));
    }

    private String format(String parameters) {
        return String.format("%s(%s)", this.name, parameters);
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitMethodCall(this);
    }

    @Override public int hashCode() {
        return Objects.hash(this.parameters, this.name);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MethodCallExpr)) {
            return false;
        }
        return Objects.equals(this.name, ((MethodCallExpr) o).getName())
                && Objects.equals(this.parameters, ((MethodCallExpr) o).getParameters());
    }

}
