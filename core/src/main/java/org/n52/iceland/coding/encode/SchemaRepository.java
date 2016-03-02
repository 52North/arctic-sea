/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding.encode;


import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.n52.iceland.lifecycle.Constructable;
import org.n52.iceland.w3c.SchemaLocation;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SchemaRepository implements Constructable {
    @Deprecated
    private static SchemaRepository instance;

    private final Map<String, Set<SchemaLocation>> schemaLocations = Maps.newHashMap();
    private EncoderRepository encoderRepository;

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Deprecated
    public static SchemaRepository getInstance() {
        return instance;
    }

    @Override
    public void init() {
        SchemaRepository.instance = this;
        this.schemaLocations.clear();
        for (ConformanceClassEncoder<?, ?> encoder : this.encoderRepository.getEncoders()) {
            for (EncoderKey key : encoder.getKeys()) {
                if (key instanceof XmlEncoderKey) {
                    Set<SchemaLocation> locations = encoder.getSchemaLocations();
                    if (locations != null && !locations.isEmpty()) {
                        for (SchemaLocation schemaLocation : locations) {
                            this.schemaLocations.put(schemaLocation.getNamespace(), Sets.newHashSet(schemaLocation));
                        }
                    }
                }
            }
        }
    }

     public Set<SchemaLocation> getSchemaLocation(String namespace) {
        if (this.schemaLocations.containsKey(namespace)) {
            return this.schemaLocations.get(namespace);
        }
        return Collections.emptySet();
    }

    public String getNamespaceFor(String prefix) {
        Map<String, String> prefixNamspaceMap = getPrefixNamspaceMap();
        for (String namespace : prefixNamspaceMap.keySet()) {
            if (prefix.equals(prefixNamspaceMap.get(prefix))) {
                return namespace;
            }
        }
        return null;
    }

    public String getPrefixFor(String namespace) {
        return getPrefixNamspaceMap().get(namespace);
    }

    private Map<String, String> getPrefixNamspaceMap() {
        Map<String, String> prefixMap = Maps.newHashMap();
        for (ConformanceClassEncoder<?, ?> encoder : this.encoderRepository.getEncoders()) {
            encoder.addNamespacePrefixToMap(prefixMap);
        }
        return prefixMap;
    }

}
