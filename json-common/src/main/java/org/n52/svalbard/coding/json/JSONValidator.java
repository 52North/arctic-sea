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
package org.n52.svalbard.coding.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URL;

import org.n52.janmayen.Json;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.JSONDecodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.load.configuration.LoadingConfiguration;
import com.github.fge.jsonschema.core.load.download.ResourceURIDownloader;
import com.github.fge.jsonschema.core.load.download.URIDownloader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public final class JSONValidator {
    private static final Logger LOG = LoggerFactory.getLogger(JSONValidator.class);

    private final JsonSchemaFactory jsonSchemaFactory =
            JsonSchemaFactory.newBuilder()
                    .setLoadingConfiguration(
                            LoadingConfiguration.newBuilder().addScheme("http", new ResourceRedirect()).freeze())
                    .freeze();

    private JSONValidator() {
    }

    public JsonSchemaFactory getJsonSchemaFactory() {
        return jsonSchemaFactory;
    }

    public ProcessingReport validate(String json, String schema)
            throws IOException {
        return validate(Json.loadString(json), schema);
    }

    public ProcessingReport validate(URL url, String schema)
            throws IOException {
        return validate(Json.loadURL(url), schema);
    }

    public ProcessingReport validate(File file, String schema)
            throws IOException {
        return validate(Json.loadFile(file), schema);
    }

    public ProcessingReport validate(InputStream is, String schema)
            throws IOException {
        return validate(Json.loadStream(is), schema);
    }

    public ProcessingReport validate(Reader reader, String schema)
            throws IOException {
        return validate(Json.loadReader(reader), schema);
    }

    public ProcessingReport validate(JsonNode node, String schema) {
        JsonSchema jsonSchema;
        try {
            jsonSchema = getJsonSchemaFactory().getJsonSchema(schema);
        } catch (ProcessingException ex) {
            throw new IllegalArgumentException("Unknown schema: " + schema, ex);
        }
        return jsonSchema.validateUnchecked(node);
    }

    public boolean isValid(String json, String schema)
            throws IOException {
        return isValid(Json.loadString(json), schema);
    }

    public boolean isValid(URL url, String schema)
            throws IOException {
        return isValid(Json.loadURL(url), schema);
    }

    public boolean isValid(File file, String schema)
            throws IOException {
        return isValid(Json.loadFile(file), schema);
    }

    public boolean isValid(InputStream is, String schema)
            throws IOException {
        return isValid(Json.loadStream(is), schema);
    }

    public boolean isValid(Reader reader, String schema)
            throws IOException {
        return isValid(Json.loadReader(reader), schema);
    }

    public boolean isValid(JsonNode node, String schema) {
        return validate(node, schema).isSuccess();
    }

    public String encode(ProcessingReport report, JsonNode instance) {
        ObjectNode objectNode = Json.nodeFactory().objectNode();
        objectNode.set(JSONConstants.INSTANCE, instance);
        ArrayNode errors = objectNode.putArray(JSONConstants.ERRORS);
        for (ProcessingMessage m : report) {
            errors.add(m.asJson());
        }
        return Json.print(objectNode);
    }

    public void validateAndThrow(JsonNode instance, String schema)
            throws DecodingException {
        ProcessingReport report = JSONValidator.getInstance().validate(instance, schema);
        if (!report.isSuccess()) {
            String message = encode(report, instance);
            LOG.info("Invalid JSON instance:\n{}", message);
            throw new JSONDecodingException(message);
        }
    }

    public static JSONValidator getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static final class LazyHolder {
        private static JSONValidator INSTANCE = new JSONValidator();

        private LazyHolder() {
        }
    }

    private static class ResourceRedirect
            implements URIDownloader {
        private final URIDownloader resource = ResourceURIDownloader.getInstance();

        @Override
        public InputStream fetch(URI source)
                throws IOException {
            return resource.fetch(URI.create(toResource(source)));
        }

        protected String toResource(URI source) {
            return String.format("resource://%s.json", source.getPath().replace("/json", ""));
        }
    }
}
