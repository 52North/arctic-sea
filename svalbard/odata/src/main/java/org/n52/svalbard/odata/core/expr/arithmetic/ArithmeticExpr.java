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
package org.n52.svalbard.odata.core.expr.arithmetic;

import org.n52.svalbard.odata.core.expr.Expr;

import java.util.Optional;

/**
 * Interface to denote that Expression can possibly be used in arithmetic operations.
 * Does not guarantee that the result will be semantically correct!
 *
 * @author <a href="mailto:j.speckamp@52north.org">Jan Speckamp</a>
 */
public interface ArithmeticExpr extends Expr {

    @Override
    default boolean isArithmetic() {
        return true;
    }

    @Override
    default Optional<ArithmeticExpr> asArithmetic() {
        return Optional.of(this);
    }

}
