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
package org.n52.svalbard.util;

import java.util.Collections;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import com.google.common.collect.Iterators;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class NamespaceContextBuilder {

    private final ImmutableBiMap.Builder<String, String> bimap = ImmutableBiMap.builder();

    private Builder<String, String> getNamespaces() {
        return this.bimap;
    }

    public NamespaceContextBuilder add(String namespace, String prefix) {
        getNamespaces().put(namespace, prefix);
        return this;
    }

    public NamespaceContextBuilder add(NamespaceContextBuilder other) {
        getNamespaces().putAll(other.getNamespaces().build());
        return this;
    }

    public NamespaceContext build() {
        return new BiMapNamespaceContext(getNamespaces().build());
    }

    private static class BiMapNamespaceContext implements NamespaceContext {
        private final BiMap<String, String> namespaces;

        BiMapNamespaceContext(BiMap<String, String> namespaces) {
            this.namespaces = namespaces;
        }

        @Override
        public String getNamespaceURI(String prefix) {
            return this.namespaces.inverse().get(prefix);
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return this.namespaces.get(namespaceURI);
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            String prefix = this.namespaces.get(namespaceURI);
            if (prefix == null) {
                return Collections.emptyIterator();
            } else {
                return Iterators.singletonIterator(prefix);
            }
        }
    }

}
