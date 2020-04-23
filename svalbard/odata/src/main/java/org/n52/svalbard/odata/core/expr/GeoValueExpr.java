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
package org.n52.svalbard.odata.core.expr;

import java.util.Objects;
import java.util.Optional;

/**
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public class GeoValueExpr implements Expr {

    // Geometry/Geography is stored as WKT
    private final String value;

    /**
     * Creates a new {@code ValueExpr}.
     *
     * @param value the value
     */
    public GeoValueExpr(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getGeometry() {
        return this.value;
    }

    @Override
    public boolean isGeometry() {
        return true;
    }

    @Override
    public Optional<GeoValueExpr> asGeometry() {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return String.format("%s", this.value);
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitGeometry(this);
    }

    @Override public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GeoValueExpr)) {
            return false;
        }
        return Objects.equals(this.value, ((GeoValueExpr) o).getGeometry());
    }
}
