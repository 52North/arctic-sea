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
package org.n52.svalbard.odata.expr.temporal;

import org.n52.svalbard.odata.expr.Expr;
import org.n52.svalbard.odata.expr.ExprVisitor;

import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class TimeValueExpr implements TemporalExpr {

    private final Object value;

    /**
     * Creates a new {@code ValueExpr}.
     *
     * @param value the value
     */
    public TimeValueExpr(Object value) {
        //TODO: parse into valid java date class
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getTime() {
        return this.value;
    }

    @Override
    public String toString() {
        //TODO: format correctly
        return value.toString();
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitTime(this);
    }
}
