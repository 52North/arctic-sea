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

import org.n52.janmayen.ClassHelper;

import com.google.common.base.Objects;

public class ClassToClassEncoderKey implements EncoderKey {

    private final Class<?> internalClass;

    private final Class<?> encodedClass;

    public ClassToClassEncoderKey(Class<?> internalClass, Class<?> encodedClass) {
        this.internalClass = internalClass;
        this.encodedClass = encodedClass;
    }

    public ClassToClassEncoderKey(Object internalClass, Object encodedClass) {
        this(internalClass.getClass(), encodedClass.getClass());
    }

    public Class<?> getInternalClass() {
        return internalClass;
    }

    public Class<?> getEncodedClass() {
        return encodedClass;
    }

    @Override
    public String toString() {
        return String.format("ClassToClassEncoderKey[internalClass=%s, encodedClass=%s]",
                getInternalClass().getSimpleName(), getEncodedClass().getSimpleName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            final ClassToClassEncoderKey o = (ClassToClassEncoderKey) obj;
            return Objects.equal(getEncodedClass(), o.getEncodedClass())
                    && Objects.equal(getInternalClass(), o.getInternalClass());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(3, 79, getInternalClass(), getEncodedClass());
    }

    @Override
    public int getSimilarity(EncoderKey key) {
        if (key instanceof ClassToClassEncoderKey) {
            ClassToClassEncoderKey xmlKey = (ClassToClassEncoderKey) key;
            if (Objects.equal(getInternalClass(), xmlKey.getInternalClass())) {
                return ClassHelper.getSimiliarity(getEncodedClass() != null ? getEncodedClass() : Object.class,
                        xmlKey.getEncodedClass() != null ? xmlKey.getEncodedClass() : Object.class);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

}
