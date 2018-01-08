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
package org.n52.shetland.w3c;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Represents a tri-state object: can be nil (with an optional message), absent
 * (or null) or present. {@link #isNil()}, {@link #isAbsent()} and
 * {@link #isPresent()} are mutually exclusive {@code true}.
 *
 * @author Christian Autermann
 * @param <T> the instance type
 */
public abstract class Nillable<T> {

    private static final String INSTANCE_ABSENT = "instance is absent";

    private Nillable() {
    }

    /**
     * Checks if this {@code Nillable} is {@code nil}.
     *
     * @return whether or not this instance is {@code nil}
     *
     * @see #isPresent()
     * @see #isAbsent()
     *
     */
    public abstract boolean isNil();

    /**
     * Checks if this {@code Nillable} is present.
     *
     * @return whether or not this instance is present
     *
     * @see #isNil()
     * @see #isAbsent()
     */
    public abstract boolean isPresent();

    /**
     * Checks if this {@code Nillable} is {@code null}.
     *
     * @return whether or not this instance is {@code null}
     *
     * @see #isNil()
     * @see #isPresent()
     */
    public abstract boolean isAbsent();

    /**
     * Transforms this {@code Nillable} to the target type.
     *
     * @param <X> the target type
     * @param fun the transformation function
     *
     * @return the {@code Nillable}
     */
    @Deprecated
    public <X> Nillable<X> transform(Function<? super T, X> fun) {
        return map(fun);
    }

    public abstract <X> Nillable<X> map(Function<? super T, X> fun);

    /**
     * Checks if this {@code Nillable} is {@code null}.
     *
     * @return whether or not this instance is {@code null}
     *
     * @see #isAbsent()
     */
    public boolean isNull() {
        return isAbsent();
    }

    /**
     * Checks if this this instance is not absent.
     *
     * @return whether this instance is present or nil
     */
    public boolean isPresentOrNil() {
        return !isAbsent();
    }

    /**
     * Returns the object if present.
     *
     * @return the object
     *
     * @throws UnsupportedOperationException if this instance is not present
     * @see #isPresent()
     */
    public abstract T get();

    /**
     * Checks if this {@code Nillable} is nil and has a reason.
     *
     * @return whether this instance has a nil reason or not
     */
    public boolean hasReason() {
        return isNil() && getNilReason().isPresent();
    }

    /**
     * Returns the optional reason for this object being nil.
     *
     * @return the nil reason
     *
     * @throws UnsupportedOperationException if this instance is not nil
     * @see #isNil()
     */
    public abstract Optional<String> getNilReason();

    /**
     * Creates a new {@code Nillable} from a present instance.
     *
     * @param <T> the type
     * @param t   the object
     *
     * @throws NullPointerException if {@code t} is {@code null}
     * @return the present {@code Nillable}
     */
    public static <T> Nillable<T> present(T t) {
        return new Present<>(t);
    }

    /**
     * Creates a {@code Nillable} for a absent instance.
     *
     * @param <T> the type
     *
     * @return the absent {@code Nillable}
     */
    public static <T> Nillable<T> absent() {
        return Absent.INSTANCE.cast();
    }

    /**
     * Creates a new {@code Nillable} that is {@code nil} because of optionally
     * supplied reason.
     *
     * @param <T>    the type
     * @param reason the reason (may be {@code null})
     *
     * @return the nil {@code Nillable}
     */
    public static <T> Nillable<T> nil(String reason) {
        return new Nil(reason).cast();
    }

    /**
     * Creates a new {@code Nillable} that is {@code nil}.
     *
     * @param <T> the type
     *
     * @return the nil {@code Nillable}
     */
    public static <T> Nillable<T> nil() {
        return nil(null);
    }

    /**
     * Creates a new {@code Nillable}, that is nil because it is inapplicable.
     *
     * @param <T> the type
     *
     * @return the nil {@code Nillable}
     */
    public static <T> Nillable<T> inapplicable() {
        return Nil.INAPPLICABLE.cast();
    }

    /**
     * Creates a new {@code Nillable}, that is nil because it is missing.
     *
     * @param <T> the type
     *
     * @return the nil {@code Nillable}
     */
    public static <T> Nillable<T> missing() {
        return Nil.MISSING.cast();
    }

    /**
     * Creates a new {@code Nillable}, that is nil because it is a template
     * value.
     *
     * @param <T> the type
     *
     * @return the nil {@code Nillable}
     */
    public static <T> Nillable<T> template() {
        return Nil.TEMPLATE.cast();
    }

    /**
     * Creates a new {@code Nillable}, that is nil because it is unknown.
     *
     * @param <T> the type
     *
     * @return the nil {@code Nillable}
     */
    public static <T> Nillable<T> unknown() {
        return Nil.UNKNOWN.cast();
    }

    /**
     * Creates a new {@code Nillable}, that is nil because it is withheld.
     *
     * @param <T> the type
     *
     * @return the nil {@code Nillable}
     */
    public static <T> Nillable<T> withheld() {
        return Nil.WITHHELD.cast();
    }

    /**
     * Creates a new {@code Nillable} that is either present (if {@code obj} is
     * not {@code null}), or absent.
     *
     * @param <T> the type
     * @param obj the object (may be {@code null})
     *
     * @return the {@code Nillable}
     */
    public static <T> Nillable<T> of(T obj) {
        return of(obj, null);
    }

    /**
     * Creates a new {@code Nillable} that is either present (if {@code obj} is
     * not {@code null}), nil (if {@code reason} is not {@code null}) or absent.
     *
     * @param <T>    the type
     * @param obj    the object (may be {@code null})
     * @param reason the nil reason (may be {@code null})
     *
     * @return the {@code Nillable}
     */
    public static <T> Nillable<T> of(T obj, String reason) {
        if (obj == null) {
            if (reason == null) {
                return absent();
            }
            return nil(reason);
        }
        return present(obj);
    }

    private static class Present<T> extends Nillable<T> {
        private final T obj;

        Present(T obj) {
            this.obj = Objects.requireNonNull(obj);
        }

        @Override
        public Optional<String> getNilReason() {
            throw new UnsupportedOperationException("instance is present");
        }

        @Override
        public boolean isNil() {
            return false;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public boolean isAbsent() {
            return false;
        }

        @Override
        public T get() {
            return obj;
        }

        @Override
        public <X> Nillable<X> map(Function<? super T, X> fun) {
            return new Present<>(fun.apply(get()));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.get());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Present && Objects
                   .equals(get(), ((Present) obj).get());
        }

        @Override
        public String toString() {
            return get().toString();
        }
    }

    private static class Nil extends Nillable<Object> {
        private static final Nil INAPPLICABLE = new Nil("inapplicable");
        private static final Nil MISSING = new Nil("missing");
        private static final Nil TEMPLATE = new Nil("template");
        private static final Nil UNKNOWN = new Nil("unknown");
        private static final Nil WITHHELD = new Nil("withheld");
        private final Optional<String> reason;

        Nil(String reason) {
            this.reason = Optional.ofNullable(reason);
        }

        @Override
        public Optional<String> getNilReason() {
            return reason;
        }

        @Override
        public boolean isNil() {
            return true;
        }

        @Override
        public boolean isAbsent() {
            return false;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public Object get() {
            throw new UnsupportedOperationException("instance is nil");
        }

        @Override
        public <X> Nillable<X> map(Function<? super Object, X> fun) {
            return this.cast();
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.getNilReason());
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Nil && Objects
                   .equals(getNilReason(), ((Nil) obj).getNilReason());
        }

        @SuppressWarnings("unchecked")
        <T> Nillable<T> cast() {
            return (Nillable<T>) this;
        }

        @Override
        public String toString() {
            return "Nillable.nil(\"" + getNilReason().orElse("null")+ "\")";
        }
    }

    private static class Absent extends Nillable<Object> {
        private static final Absent INSTANCE = new Absent();

        @Override
        public boolean hasReason() {
            return false;
        }

        @Override
        public Optional<String> getNilReason() {
            throw new UnsupportedOperationException(INSTANCE_ABSENT);
        }

        @Override
        public Object get() {
            throw new UnsupportedOperationException(INSTANCE_ABSENT);
        }

        @Override
        public boolean isNil() {
            return false;
        }

        @Override
        public boolean isAbsent() {
            return true;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public <X> Nillable<X> map(Function<? super Object, X> fun) {
            return this.cast();
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Absent;
        }

        @SuppressWarnings("unchecked")
        <T> Nillable<T> cast() {
            return (Nillable<T>) this;
        }

        @Override
        public String toString() {
            return "Nillable.absent()";
        }
    }
}
