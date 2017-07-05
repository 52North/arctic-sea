/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@Immutable
public class EncodingContext {

    private static final EncodingContext EMTPY = new EncodingContext();

    private static final Object NONE = new Object();
    private static final int DEFAULT_INTEGER_VALUE = 0;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    private final Map<String, Object> properties;

    protected EncodingContext() {
        this.properties = Collections.emptyMap();
    }

    protected EncodingContext(@Nonnull Map<String, Object> properties) {
        this.properties = Collections.unmodifiableMap(properties);
    }

    public boolean has(@Nonnull Enum<?> key) {
        return has(key.name());
    }

    public boolean has(@Nonnull String key) {
        return this.properties.containsKey(key);
    }

    public boolean isEmpty() {
        return this == EMTPY;
    }

    public <T> T require(@Nonnull Enum<?> key) {
        return require(key.name());
    }

    public <T> T require(@Nonnull String key) {
        return this.<T>get(key).get();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(@Nonnull String key) {
        return Optional.ofNullable(this.properties.get(key)).filter(v -> v != NONE).map(v -> (T) v);
    }

    public <T> T get(@Nonnull String key, T defaultValue) {
        return this.<T>get(key).orElse(defaultValue);
    }

    public <T> T get(@Nonnull Enum<?> key, T defaultValue) {
        return get(key.name(), defaultValue);
    }

    public <T> Optional<T> get(@Nonnull Enum<?> key) {
        return get(key.name());
    }

    public Optional<String> getString(@Nonnull Enum<?> key) {
        return get(key.name());
    }

    public Optional<String> getString(@Nonnull String key) {
        return get(key);
    }

    public boolean getBoolean(@Nonnull Enum<?> key) {
        return getBoolean(key.name());
    }

    public boolean getBoolean(@Nonnull String key) {
        return getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }

    public boolean getBoolean(@Nonnull Enum<?> key, boolean defaultValue) {
        return getBoolean(key.name(), defaultValue);
    }

    public boolean getBoolean(@Nonnull String key, boolean defaultValue) {
        Object value = this.properties.get(key);
        if (value == null) {
            return defaultValue;
        }
        if (value == NONE) {
            return true;
        }
        return (boolean) value;
    }

    public int getInteger(@Nonnull Enum<?> key) {
        return getInteger(key.name());
    }

    public int getInteger(@Nonnull String key) {
        return get(key, DEFAULT_INTEGER_VALUE);
    }

    @CheckReturnValue
    public EncodingContext with(@Nonnull String key) {
        return with(key, null);
    }

    @CheckReturnValue
    public EncodingContext with(@Nonnull Enum<?> key) {
        return with(key.name());
    }

    @CheckReturnValue
    public EncodingContext with(@Nonnull Enum<?> key, @Nullable Object value) {
        return with(key.name(), value);
    }

    @CheckReturnValue
    public EncodingContext with(@Nonnull String key, @Nullable Object value) {
        Object nonNullValue = Optional.ofNullable(value).orElse(NONE);

        if (this.properties.containsKey(key) && this.properties.get(key).equals(value)) {
            return this;
        }

        Map<String, Object> map = new HashMap<>(this.properties);
        map.put(key, nonNullValue);
        return new EncodingContext(map);
    }

    @CheckReturnValue
    public EncodingContext without(@Nonnull String key) {
        if (!this.properties.containsKey(key)) {
            return this;
        }
        HashMap<String, Object> map = new HashMap<>(this.properties);
        map.remove(key);
        return map.isEmpty() ? EMTPY : new EncodingContext(map);
    }

    @CheckReturnValue
    public EncodingContext without(@Nonnull Enum<?> key) {
        return without(key.name());
    }

    public String getEncoding() {
        return get(EncoderFlags.ENCODING, StandardCharsets.UTF_8.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.properties);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EncodingContext other = (EncodingContext) obj;
        return Objects.equals(this.properties, other.properties);
    }

    public static EncodingContext of(@Nonnull String key) {
        return EncodingContext.empty().with(key);
    }

    public static EncodingContext of(@Nonnull Enum<?> key) {
        return EncodingContext.empty().with(key);
    }

    public static <T> EncodingContext of(@Nonnull Enum<?> key, @Nullable T value) {
        return EncodingContext.empty().with(key, value);
    }

    public static <T> EncodingContext of(@Nonnull String key, @Nullable T value) {
        return EncodingContext.empty().with(key, value);
    }

    public static EncodingContext empty() {
        return EMTPY;
    }

}
