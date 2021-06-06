/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.faroe.json;

import java.util.Map.Entry;
import java.util.Spliterators;
import java.util.concurrent.locks.Lock;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.n52.janmayen.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractJsonDao {

    private final JsonNodeFactory nodeFactory = Json.nodeFactory();
    private JsonConfiguration configuration;

    @Inject
    public void setConfiguration(JsonConfiguration configuration) {
        this.configuration = configuration;
    }

    protected JsonNodeFactory nodeFactory() {
        return nodeFactory;
    }

    protected Lock writeLock() {
        return configuration().writeLock();
    }

    protected Lock readLock() {
        return configuration().readLock();
    }

    protected JsonConfiguration configuration() {
        return this.configuration;
    }

    protected ObjectNode getConfiguration() {
        return configuration().get();
    }

    protected Stream<JsonNode> createStream(JsonNode node) {
        return StreamSupport.stream(node.spliterator(), false);
    }

    protected Stream<Entry<String, JsonNode>> createEntryStream(JsonNode node) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(node.fields(), 0), true);
    }

    protected Predicate<JsonNode> isNullOrMissing(String key) {
        return n -> n.path(key).isNull() || n.path(key).isMissingNode();
    }

}
