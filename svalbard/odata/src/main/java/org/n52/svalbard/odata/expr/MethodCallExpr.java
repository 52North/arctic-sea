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

import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Class to hold a method call expression.
 *
 * @author Christian Autermann
 */
public class MethodCallExpr implements Expr, BooleanExpr {

    private final String name;
    private final List<Expr> parameters;

    /**
     * Create a new {@code MethodCallExpr}.
     *
     * @param name       the method name
     * @param parameters the parameters
     */
    public MethodCallExpr(String name, List<Expr> parameters) {
        this.name = Objects.requireNonNull(name);
        this.parameters = Objects.requireNonNull(parameters);
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
        return String.format("%s(%s)", this.name, this.parameters.stream().map(Expr::toString).collect(joining(", ")));
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitMethodCall(this);
    }
}
