/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode;

import org.n52.janmayen.ClassHelper;

import com.google.common.base.Objects;

/**
 * {@link NamespaceDecoderKey} implementation for XML namespace and
 * {@link Class}.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 *
 * @since 1.0.0
 */
public class XmlNamespaceDecoderKey extends NamespaceDecoderKey<Class<?>> {

    private Class<?> type;

    public XmlNamespaceDecoderKey(String namespace, Class<?> type) {
        super(namespace, type);
    }

    @Override
    public Class<?> getType() {
        return this.type;
    }

    @Override
    protected void setType(Class<?> type) {
        this.type = type;
    }

    @Override
    protected String getTypeName() {
        return getType().getSimpleName();
    }

    @Override
    public int getSimilarity(DecoderKey key) {
        return getSimilarity(key, Object.class);
    }

    @Override
    protected int getSimilarity(DecoderKey key, Class<?> t) {
        if (key != null && key.getClass() == getClass()) {
            NamespaceDecoderKey<?> xmlKey = (NamespaceDecoderKey<?>) key;
            if (Objects.equal(getNamespace(), xmlKey.getNamespace()) && xmlKey.getType() instanceof Class<?>) {
                return ClassHelper.getSimiliarity(getType() != null ? getType() : t,
                        xmlKey.getType() != null ? (Class<?>) xmlKey.getType() : t);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }
}
