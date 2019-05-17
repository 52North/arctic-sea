/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class Json {
    private static JsonNodeFactory FACTORY = JsonNodeFactory.withExactBigDecimals(false);
    private static ObjectReader READER;
    private static ObjectWriter WRITER;

    static {
        ObjectMapper mapper = new ObjectMapper()
                .setNodeFactory(FACTORY)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        READER = mapper.reader();
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        pp.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        WRITER = mapper.writer(pp);
    }

    protected Json() {
    }

    private static ObjectWriter writer() {
        return WRITER;
    }

    private static ObjectReader reader() {
        return READER;
    }

    public static JsonNodeFactory nodeFactory() {
        return FACTORY;
    }

    public static String print(JsonNode node) {
        try (StringWriter writer = new StringWriter()) {
            print(writer, node);
            writer.flush();
            return writer.toString();
        } catch (IOException ex) {
            // can not happen
            throw new Error(ex);
        }
    }

    public static void print(Writer writer, JsonNode node) throws IOException {
        writer().writeValue(writer, node);
    }

    public static void print(OutputStream writer, JsonNode node) throws IOException {
        writer().writeValue(writer, node);
    }

    public static JsonNode loadURL(URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            return loadStream(stream);
        }
    }

    public static JsonNode loadPath(String path) throws IOException {
        try (InputStream in = new FileInputStream(path)) {
            return loadStream(in);
        }
    }

    public static JsonNode loadPath(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            return loadStream(in);
        }
    }

    public static JsonNode loadFile(File file) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            return loadStream(in);
        }
    }

    public static JsonNode loadStream(InputStream in) throws IOException {
        return reader().readTree(in);
    }

    public static JsonNode loadReader(Reader reader) throws IOException {
        return reader().readTree(reader);
    }

    public static JsonNode loadString(String json) {
        try {
            return loadReader(new StringReader(json));
        } catch (IOException ex) {
            // cannot happen
            throw new Error(ex);
        }
    }

    public static ObjectNode toJSON(Map<String, ?> map) {
        ObjectNode node = nodeFactory().objectNode();
        Optional.ofNullable(map).orElseGet(Collections::emptyMap)
                .forEach((key, value) -> node.set(key, toJSONString(value)));
        return node;
    }

    public static ArrayNode toJSON(Collection<?> coll) {
        return Optional.ofNullable(coll)
                .map(Collection::stream)
                .orElseGet(Stream::empty).map(Json::toJSONString)
                .collect(Collector.of(nodeFactory()::arrayNode, ArrayNode::add, ArrayNode::addAll));
    }

    private static JsonNode toJSONString(Object object) {
        if (object == null) {
            return nodeFactory().nullNode();
        } else if (object instanceof JsonNode) {
            return (JsonNode) object;
        } else {
            return nodeFactory().textNode(String.valueOf(object));
        }
    }

}
