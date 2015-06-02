/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.component;

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ClassBasedComponentKey<T> {

    private final Class<? extends T> type;

    public ClassBasedComponentKey(Class<? extends T> type) {
        this.type = type;
    }

    public Class<? extends T> getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClassBasedComponentKey<?> other = (ClassBasedComponentKey<?>) obj;
        return Objects.equals(this.getType(), other.getType());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", getType())
                .toString();
    }

}
