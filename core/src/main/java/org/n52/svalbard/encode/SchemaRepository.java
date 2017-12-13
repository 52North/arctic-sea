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

import static java.util.stream.Collectors.groupingBy;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.n52.shetland.w3c.SchemaLocation;

import com.google.common.collect.Maps;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SchemaRepository {
    @Deprecated
    private static SchemaRepository instance;

    private final Map<String, Set<SchemaLocation>> schemaLocations = Maps.newHashMap();

    private EncoderRepository encoderRepository;

    @SuppressFBWarnings("ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public void init(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
        this.schemaLocations.clear();
        this.schemaLocations
                .putAll(this.encoderRepository.getEncoders().stream().filter(e -> e instanceof SchemaAwareEncoder)
                        .map(e -> (SchemaAwareEncoder<?, ?>) e).map(SchemaAwareEncoder::getSchemaLocations)
                        .filter(Objects::nonNull).flatMap(Set<SchemaLocation>::stream)
                        .collect(groupingBy(SchemaLocation::getNamespace, Collectors.toSet())));
        SchemaRepository.instance = this;
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
        this.encoderRepository.getEncoders().stream().filter(encoder -> encoder instanceof SchemaAwareEncoder)
                .map(encoder -> (SchemaAwareEncoder<?, ?>) encoder)
                .forEach(encoder -> encoder.addNamespacePrefixToMap(prefixMap));
        return prefixMap;
    }

    @Deprecated
    public static SchemaRepository getInstance() {
        return instance;
    }

}
