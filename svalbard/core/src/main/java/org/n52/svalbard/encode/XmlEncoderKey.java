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
package org.n52.svalbard.encode;

import org.n52.janmayen.ClassHelper;

import com.google.common.base.Objects;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class XmlEncoderKey implements EncoderKey {
    private final String namespace;

    private final Class<?> type;

    public XmlEncoderKey(String namespace, Object type) {
        this(namespace, type.getClass());
    }

    public XmlEncoderKey(String namespace, Class<?> type) {
        this.namespace = namespace;
        this.type = type;
    }

    public String getNamespace() {
        return namespace;
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("XmlEncoderKey[namespace=%s, type=%s]", getNamespace(), getType().getSimpleName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            final XmlEncoderKey o = (XmlEncoderKey) obj;
            return Objects.equal(getType(), o.getType()) && Objects.equal(getNamespace(), o.getNamespace());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(3, 79, getNamespace(), getType());
    }

    @Override
    public int getSimilarity(EncoderKey key) {
        if (key instanceof XmlEncoderKey) {
            XmlEncoderKey xmlKey = (XmlEncoderKey) key;
            if (Objects.equal(getNamespace(), xmlKey.getNamespace())) {
                return ClassHelper.getSimiliarity(getType() != null ? getType() : Object.class,
                        xmlKey.getType() != null ? xmlKey.getType() : Object.class);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
