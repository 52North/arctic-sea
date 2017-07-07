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
package org.n52.svalbard.decode;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * {@link NamespaceDecoderKey} implementation for XML namespace and operation name {@link String}.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class XmlNamespaceOperationDecoderKey extends NamespaceDecoderKey<String> {

    private String type;

    public XmlNamespaceOperationDecoderKey(String namespace, String type) {
        super(namespace, type);
    }

    @Override
    protected void setType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    protected String getTypeName() {
        return type;
    }

    @Override
    public int getSimilarity(DecoderKey key) {
        return getSimilarity(key, getType());
    }

    @Override
    protected int getSimilarity(DecoderKey key, String operation) {
        if (key != null && key.getClass() == getClass()) {
            NamespaceDecoderKey<?> xmlKey = (NamespaceDecoderKey<?>) key;
            if (Objects.equal(getNamespace(), xmlKey.getNamespace()) &&
                xmlKey.getType() instanceof String &&
                !Strings.isNullOrEmpty(operation) && xmlKey.getType() instanceof String &&
                !Strings.isNullOrEmpty((String) xmlKey.getType()) &&
                operation.equalsIgnoreCase((String) xmlKey.getType())) {
                return 0;
            }
        }
        return -1;
    }

}
