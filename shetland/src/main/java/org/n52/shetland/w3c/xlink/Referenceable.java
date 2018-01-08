/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.w3c.xlink;

import java.net.URI;

import org.n52.shetland.w3c.Nillable;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public abstract class Referenceable<T> {

    public abstract Reference getReference();

    public abstract Nillable<T> getInstance();

    public abstract boolean isInstance();

    public abstract boolean isReference();

    public abstract boolean isAbsent();

    public abstract <X> Referenceable<X> transform(Function<T, X> fun);

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public static <T> Referenceable<T> of(URI reference) {
        return new Ref(new Reference().setHref(reference)).cast();
    }

    public static <T> Referenceable<T> of(Reference reference) {
        return new Ref(reference).cast();
    }

    public static <T> Referenceable<T> of(T obj) {
        return of(Nillable.of(obj));
    }

    public static <T> Referenceable<T> of(Nillable<T> obj) {
        return new Instance<>(obj);
    }

    private static class Instance<T> extends Referenceable<T> {
        private final Nillable<T> obj;

        Instance(Nillable<T> obj) {
            this.obj = Preconditions.checkNotNull(obj);
        }

        @Override
        public Reference getReference() {
            throw new NullPointerException();
        }

        @Override
        public Nillable<T> getInstance() {
            return obj;
        }

        @Override
        public boolean isInstance() {
            return true;
        }

        @Override
        public boolean isReference() {
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getInstance());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Instance && Objects
                   .equal(this.getInstance(), ((Instance) obj).getInstance());
        }

        @Override
        public boolean isAbsent() {
            return getInstance().isAbsent();
        }

        @Override
        public String toString() {
            return getInstance().toString();
        }

        @Override
        public <X> Referenceable<X> transform(Function<T, X> fun) {
            return Referenceable.of(getInstance().transform(fun));
        }

    }

    private static class Ref extends Referenceable<Object> {
        private final Reference reference;

        Ref(Reference reference) {
            this.reference = reference;
        }

        @Override
        public Reference getReference() {
            return reference;
        }

        @Override
        public Nillable<Object> getInstance() {
            throw new NullPointerException();
        }

        @Override
        public boolean isInstance() {
            return false;
        }

        @Override
        public boolean isReference() {
            return true;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(getReference());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Ref && Objects
                   .equal(getReference(), ((Ref) obj).getReference());
        }

        @Override
        public boolean isAbsent() {
            return false;
        }

        @Override
        public String toString() {
            return getReference().toString();
        }

        @SuppressWarnings("unchecked")
        <T> Referenceable<T> cast() {
            return (Referenceable<T>) this;
        }

        @Override
        public <X> Referenceable<X> transform(Function<Object, X> fun) {
            return cast();
        }
    }
}
