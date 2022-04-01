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

import java.util.Objects;

/**
 * Expression representing a value.
 *
 * @author Christian Autermann
 */
public class StringValueExpr implements DirectTextExpr {

    private final String value;

    /**
     * Creates a new {@code ValueExpr}.
     *
     * @param value the value
     */
    public StringValueExpr(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("'%s'", this.value);
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitString(this);
    }

    @Override public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof StringValueExpr)) {
            return false;
        }
        return Objects.equals(this.value, ((StringValueExpr) o).getValue());
    }
}
