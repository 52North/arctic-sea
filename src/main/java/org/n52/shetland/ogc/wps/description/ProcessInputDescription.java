/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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

import java.math.BigInteger;
import java.util.Objects;

import org.n52.shetland.ogc.wps.InputOccurence;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessInputDescription extends DataDescription {

    default BoundingBoxInputDescription asBoundingBox() {
        throw new UnsupportedOperationException();
    }

    default ComplexInputDescription asComplex() {
        throw new UnsupportedOperationException();
    }

    default LiteralInputDescription asLiteral() {
        throw new UnsupportedOperationException();
    }

    default GroupInputDescription asGroup() {
        throw new UnsupportedOperationException();
    }

    InputOccurence getOccurence();

    <T> T visit(ReturningVisitor<T> visitor);

    default void visit(Visitor visitor) {
        Objects.requireNonNull(visitor);
        visit(new ReturningVisitor<Void>() {
            @Override
            public Void visit(BoundingBoxInputDescription input) {
                visitor.visit(input);
                return null;
            }

            @Override
            public Void visit(ComplexInputDescription input) {
                visitor.visit(input);
                return null;
            }

            @Override
            public Void visit(LiteralInputDescription input) {
                visitor.visit(input);
                return null;
            }

            @Override
            public Void visit(GroupInputDescription input) {
                visitor.visit(input);
                return null;
            }
        });
    }

    default <X extends Exception> void visit(
            ThrowingVisitor<X> visitor)
            throws X {
        visit(new ThrowingReturningVisitor<Void, X>() {
            @Override
            public Void visit(BoundingBoxInputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(ComplexInputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(LiteralInputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }

            @Override
            public Void visit(GroupInputDescription output)
                    throws X {
                visitor.visit(output);
                return null;
            }
        });
    }

    <T, X extends Exception> T visit(
            ThrowingReturningVisitor<T, X> visitor)
            throws X;

    interface Visitor {
        void visit(BoundingBoxInputDescription input);
        void visit(ComplexInputDescription input);
        void visit(LiteralInputDescription input);
        void visit(GroupInputDescription input);
    }

    interface ThrowingVisitor<X extends Exception> {
        void visit(BoundingBoxInputDescription input) throws X;
        void visit(ComplexInputDescription input) throws X;
        void visit(LiteralInputDescription input) throws X;
        void visit(GroupInputDescription input) throws X;
    }

    interface ReturningVisitor<T> {
        T visit(BoundingBoxInputDescription input);
        T visit(ComplexInputDescription input);
        T visit(LiteralInputDescription input);
        T visit(GroupInputDescription input);
    }

    interface ThrowingReturningVisitor<T, X extends Exception> {
        T visit(BoundingBoxInputDescription input) throws X;
        T visit(ComplexInputDescription input) throws X;
        T visit(LiteralInputDescription input) throws X;
        T visit(GroupInputDescription input) throws X;
    }


    interface Builder<T extends ProcessInputDescription, B extends Builder<T, B>>
            extends DataDescription.Builder<T, B> {

        B withMaximalOccurence(BigInteger max);

        default B withMaximalOccurence(long max) {
            return withMaximalOccurence(BigInteger.valueOf(max));
        }

        B withMinimalOccurence(BigInteger min);

        default B withMinimalOccurence(long min) {
            return withMinimalOccurence(BigInteger.valueOf(min));
        }

        default B withOccurence(InputOccurence occurence) {
            Objects.requireNonNull(occurence);
            return withMaximalOccurence(occurence.getMax().orElse(null))
                    .withMinimalOccurence(occurence.getMin());
        }

    }

}
