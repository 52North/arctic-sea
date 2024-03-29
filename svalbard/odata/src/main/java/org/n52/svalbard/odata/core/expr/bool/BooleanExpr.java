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
package org.n52.svalbard.odata.core.expr.bool;

import org.n52.svalbard.odata.core.expr.Expr;

import java.util.Optional;

/**
 * Interface to denote that this expression possibly evaluates to a boolean value.
 *
 * @author Christian Autermann
 */
public interface BooleanExpr extends Expr {
    @Override
    default boolean isBoolean() {
        return true;
    }

    @Override
    default Optional<BooleanExpr> asBoolean() {
        return Optional.of(this);
    }

}
