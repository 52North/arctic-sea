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
package org.n52.iceland.config.json;



import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.MediaTypeBindingKey;
import org.n52.iceland.binding.PathBindingKey;
import org.n52.iceland.config.ActivationDao;
import org.n52.iceland.ogc.ows.extension.OwsExtendedCapabilitiesProviderKey;
import org.n52.iceland.ogc.swes.OfferingExtensionKey;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.util.http.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class JsonActivationDao extends AbstractJsonActivationDao
        implements ActivationDao {

    @Deprecated // SOS-specific
    protected static final String OFFERING_EXTENSIONS = "offeringExtensions";

  @Override
    public boolean isRequestOperatorActive(RequestOperatorKey key) {
        return isActive(JsonConstants.OPERATIONS, matches(key), key.isDefaultActive());
    }

    @Override
    public void setOperationStatus(RequestOperatorKey key, boolean active) {
        setStatus(JsonConstants.OPERATIONS, matches(key), s -> encode(s, key), active);
    }

    @Override
    public Set<RequestOperatorKey> getRequestOperatorKeys() {
        return getKeys(JsonConstants.OPERATIONS, this::decodeRequestOperatorKey);
    }

    @Override
    public boolean isBindingActive(BindingKey key) {
        readLock().lock();
        try {
            return getConfiguration()
                    .path(JsonConstants.ACTIVATION)
                    .path(JsonConstants.BINDINGS)
                    .path(key.getKeyAsString())
                    .asBoolean(true);
        } finally {
            readLock().unlock();
        }
    }

    @Override
    public void setBindingStatus(BindingKey key, boolean active) {
        writeLock().lock();
        try {
            ObjectNode node = getConfiguration()
                    .with(JsonConstants.ACTIVATION)
                    .with(JsonConstants.BINDINGS);
            if (key instanceof PathBindingKey) {
                node = node.with(JsonConstants.BY_PATH);
            } else if (key instanceof MediaTypeBindingKey) {
                node = node.with(JsonConstants.BY_MEDIA_TYPE);
            }
            node.put(key.getKeyAsString(), active);
        } finally {
            writeLock().unlock();
        }
        configuration().scheduleWrite();
    }

    @Override
    public Set<BindingKey> getBindingKeys() {
        readLock().lock();
        try {
            JsonNode node = getConfiguration().path(JsonConstants.ACTIVATION).path(JsonConstants.BINDINGS);
            Set<BindingKey> keys = new HashSet<>(node.size());
            node.path(JsonConstants.BY_PATH).fieldNames()
                    .forEachRemaining(k -> keys.add(new PathBindingKey(k)));
            node.path(JsonConstants.BY_MEDIA_TYPE).fieldNames()
                    .forEachRemaining(k -> keys
                            .add(new MediaTypeBindingKey(MediaType.parse(k))));
            return keys;
        } finally {
            readLock().unlock();
        }
    }

     @Override
    public boolean isOfferingExtensionActive(OfferingExtensionKey key) {
        return isActive(OFFERING_EXTENSIONS, matches(key), true);
    }

    @Override
    public void setOfferingExtensionStatus(OfferingExtensionKey key, boolean active) {
        setStatus(OFFERING_EXTENSIONS, matches(key), s -> encode(s, key), active);
    }

    @Override
    public Set<OfferingExtensionKey> getOfferingExtensionKeys() {
        Function<JsonNode, OfferingExtensionKey> fun
                = createDomainDecoder(OfferingExtensionKey::new);
        return getKeys(OFFERING_EXTENSIONS, fun);
    }


    @Override
    public boolean isOwsExtendedCapabilitiesProviderActive(OwsExtendedCapabilitiesProviderKey key) {
        return isActive(JsonConstants.OWS_EXTENDED_CAPABILITIES_PROVIDERS, matches(key), true);
    }

    @Override
    public void setOwsExtendedCapabilitiesStatus(OwsExtendedCapabilitiesProviderKey key, boolean active) {
        setStatus(JsonConstants.OWS_EXTENDED_CAPABILITIES_PROVIDERS, matches(key), s -> encode(s, key), active);
    }

    @Override
    public Set<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabilitiesProviderKeys() {
        return getKeys(JsonConstants.OWS_EXTENDED_CAPABILITIES_PROVIDERS, decodeOwsExtendedCapabilitiesProviderKey());
    }


    protected RequestOperatorKey decodeRequestOperatorKey(JsonNode node) {
        return new RequestOperatorKey(decodeServiceOperatorKey(node),
                node.path(JsonConstants.OPERATION_NAME).textValue());
    }

    protected Function<JsonNode, OwsExtendedCapabilitiesProviderKey> decodeOwsExtendedCapabilitiesProviderKey() {
        return createDomainDecoder(OwsExtendedCapabilitiesProviderKey::new);
    }

    protected Supplier<ObjectNode> encode(Supplier<ObjectNode> supplier, RequestOperatorKey key) {
        Objects.requireNonNull(supplier);
        return () -> {
            ServiceOperatorKey sok = key == null ? null : key.getServiceOperatorKey();
            String operationName = key == null ? null : key.getOperationName();
            return encode(supplier, sok).get()
                    .put(JsonConstants.OPERATION_NAME, operationName);
        };
    }

    protected Predicate<JsonNode> matches(RequestOperatorKey key) {
        ServiceOperatorKey sok = key == null ? null : key.getServiceOperatorKey();
        String operationName = key == null ? null : key.getOperationName();
        return matches(sok).and(matchesOperationName(operationName));
    }

    protected Predicate<JsonNode> matchesOperationName(String operationName) {
        if (operationName == null) {
            return isNullOrMissing(JsonConstants.OPERATION_NAME);
        }
        return n -> n.path(JsonConstants.OPERATION_NAME).asText().equals(operationName);
    }

}
