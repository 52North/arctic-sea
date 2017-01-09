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
package org.n52.svalbard;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        return get(key) != null;
    }

    public boolean isEmpty() {
        return this == EMTPY;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(@Nonnull String key) {
        return (T) this.properties.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(@Nonnull String key, T defaultValue) {
        return (T) this.properties.getOrDefault(key, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(@Nonnull Enum<?> key, T defaultValue) {
        return get(key.name(), defaultValue);
    }

    public <T> T get(@Nonnull Enum<?> key) {
        return get(key.name());
    }

    public boolean getBoolean(@Nonnull Enum<?> key) {
        return getBoolean(key.name());
    }

    public boolean getBoolean(@Nonnull String key) {
        return get(key);
    }

    public int getInteger(@Nonnull Enum<?> key) {
        return get(key.name());
    }

    public int getInteger(@Nonnull String key) {
        return get(key);
    }

    public String getString(@Nonnull Enum<?> key) {
        return get(key.name());
    }

    public String getString(@Nonnull String key) {
        return get(key);
    }

    @CheckReturnValue
    public EncodingContext with(@Nonnull String key) {
        return with(key, NONE);
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
        Map<String, Object> map = new HashMap<>(this.properties);
        if (value == null) {
            map.put(key, NONE);
        } else {
            map.put(key, value);
        }
        map.put(key, value);
        return new EncodingContext(map);
    }

    @CheckReturnValue
    public EncodingContext without(@Nonnull String key) {
        if (!this.properties.containsKey(key)) {
            return this;
        } else {
            HashMap<String, Object> map = new HashMap<>(this.properties);
            map.remove(key);
            return map.isEmpty() ? EMTPY : new EncodingContext(map);
        }
    }

    @CheckReturnValue
    public EncodingContext without(@Nonnull Enum<?> key) {
        return without(key.name());
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
