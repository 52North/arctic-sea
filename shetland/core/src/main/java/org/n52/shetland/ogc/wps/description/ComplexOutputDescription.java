/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.description;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ComplexOutputDescription extends ComplexDescription, ProcessOutputDescription {

    @Override
    default ComplexOutputDescription asComplex() {
        return this;
    }

    @Override
    default boolean isComplex() {
        return true;
    }

    @Override
    default <T> T visit(ReturningVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    default <T, X extends Exception> T visit(ThrowingReturningVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    Builder<?, ?> newBuilder();

    interface Builder<T extends ComplexOutputDescription, B extends Builder<T, B>>
            extends ProcessOutputDescription.Builder<T, B>, ComplexDescription.Builder<T, B> {
    }

}
