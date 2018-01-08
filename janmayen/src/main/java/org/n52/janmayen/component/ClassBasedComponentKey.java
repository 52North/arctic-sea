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
package org.n52.janmayen.component;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Generic {@link Component} key that is based on a single {@link Class}.
 *
 * @param <T> the type the class of this key has to be a subtype of
 *
 * @since 1.0.0
 *
 * @author Christian Autermann
 */
public class ClassBasedComponentKey<T> {

    private final Class<? extends T> type;

    /**
     * Creates a new key.
     *
     * @param type the type associated with this key
     */
    public ClassBasedComponentKey(Class<? extends T> type) {
        this.type = type;
    }

    /**
     * Gets the type associated with this key.
     *
     * @return the type
     */
    public Class<? extends T> getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        ClassBasedComponentKey<?> that = (ClassBasedComponentKey<?>) other;
        return Objects.equals(this.getType(), that.getType());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", getType())
                .toString();
    }

}
