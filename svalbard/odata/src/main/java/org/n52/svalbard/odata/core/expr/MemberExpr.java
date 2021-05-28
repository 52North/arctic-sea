/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import java.util.Objects;
import java.util.Optional;

/**
 * Class to hold a member or value reference expression.
 *
 * @author Christian Autermann
 */
public final class MemberExpr implements ArithmeticExpr, DirectTextExpr {

    private final String value;

    /**
     * Create a new {@code MemberExpr}.
     *
     * @param value the value
     */
    public MemberExpr(String value) {
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Get the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean isMember() {
        return true;
    }

    @Override
    public Optional<MemberExpr> asMember() {
        return Optional.of(this);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public <T, X extends Throwable> T accept(ExprVisitor<T, X> visitor) throws X {
        return visitor.visitMember(this);
    }

    @Override public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MemberExpr)) {
            return false;
        }
        return Objects.equals(this.value, ((MemberExpr) o).getValue());
    }
}
