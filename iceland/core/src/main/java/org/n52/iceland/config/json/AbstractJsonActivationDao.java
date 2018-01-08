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
package org.n52.iceland.config.json;

import static java.util.stream.Collectors.toSet;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.n52.faroe.json.AbstractJsonDao;
import org.n52.iceland.ogc.AbstractComparableServiceVersionDomainKey;
import org.n52.shetland.ogc.ows.service.OwsServiceKey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractJsonActivationDao extends AbstractJsonDao {

    protected boolean isActive(String path, Predicate<JsonNode> matcher, boolean defaultValue) {
        readLock().lock();
        try {
            JsonNode array = getConfiguration().path(JsonConstants.ACTIVATION).path(path);
            return createStream(array).filter(matcher)
                    .findAny().orElseGet(MissingNode::getInstance)
                    .path(JsonConstants.ACTIVE).asBoolean(defaultValue);
        } finally {
            readLock().unlock();
        }
    }

    protected void setStatus(String path, Predicate<JsonNode> matcher,
                             Function<Supplier<ObjectNode>, Supplier<ObjectNode>> encoder,
                             boolean active) {
        writeLock().lock();
        try {
            ArrayNode array = getConfiguration().with(JsonConstants.ACTIVATION).withArray(path);
            ObjectNode node = (ObjectNode) createStream(array).filter(matcher).findAny()
                    .orElseGet(encoder.apply(array::addObject));
            node.put(JsonConstants.ACTIVE, active);
        } finally {
            writeLock().unlock();
        }
        configuration().scheduleWrite();
    }

    protected <K> Set<K> getKeys(String path, Function<JsonNode, K> decoder) {
        readLock().lock();
        try {
            JsonNode array = getConfiguration().path(JsonConstants.ACTIVATION).path(path);
            return createStream(array).map(decoder).collect(toSet());
        } finally {
            readLock().unlock();
        }
    }

    protected Predicate<JsonNode> matches(AbstractComparableServiceVersionDomainKey<?> key) {
        OwsServiceKey sok = key == null ? null : key.getServiceOperatorKey();
        String domain = key == null ? null : key.getDomain();
        return matches(sok).and(matchesDomain(domain));
    }

    protected Predicate<JsonNode> matches(OwsServiceKey key) {
        String service = key == null ? null : key.getService();
        String version = key == null ? null : key.getVersion();
        return matchesService(service).and(matchesVersion(version));
    }

    protected Predicate<JsonNode> matchesDomain(String domain) {
        if (domain == null) {
            return isNullOrMissing(JsonConstants.DOMAIN);
        }
        return n -> n.path(JsonConstants.DOMAIN).asText().equals(domain);
    }

    protected Predicate<JsonNode> matchesService(String service) {
        if (service == null) {
            return isNullOrMissing(JsonConstants.SERVICE);
        }
        return n -> n.path(JsonConstants.SERVICE).asText().equals(service);
    }

    protected Predicate<JsonNode> matchesVersion(String version) {
        if (version == null) {
            return isNullOrMissing(JsonConstants.VERSION);
        }
        return n -> n.path(JsonConstants.VERSION).asText().equals(version);
    }

    protected Supplier<ObjectNode> encode(Supplier<ObjectNode> supplier,
                                          AbstractComparableServiceVersionDomainKey<?> key) {
        Objects.requireNonNull(supplier);
        return () -> {
            OwsServiceKey sok = key == null ? null : key
                    .getServiceOperatorKey();
            String domain = key == null ? null : key.getDomain();
            return encode(supplier, sok).get().put(JsonConstants.DOMAIN, domain);
        };
    }

    protected Supplier<ObjectNode> encode(Supplier<ObjectNode> supplier,
                                          OwsServiceKey key) {
        Objects.requireNonNull(supplier);
        return () -> {
            String service = key == null ? null : key.getService();
            String version = key == null ? null : key.getVersion();
            return supplier.get().put(JsonConstants.SERVICE, service)
                    .put(JsonConstants.VERSION, version);
        };
    }

    protected <K extends AbstractComparableServiceVersionDomainKey<K>> Function<JsonNode, K> createDomainDecoder(
            BiFunction<OwsServiceKey, String, K> fun) {
        Objects.requireNonNull(fun);
        return n -> fun.apply(decodeServiceOperatorKey(n), n.path(JsonConstants.DOMAIN).textValue());
    }

    protected OwsServiceKey decodeServiceOperatorKey(JsonNode node) {
        return new OwsServiceKey(
                node.path(JsonConstants.SERVICE).textValue(),
                node.path(JsonConstants.VERSION).textValue());
    }

}
