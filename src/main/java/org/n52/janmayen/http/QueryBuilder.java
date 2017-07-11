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
package org.n52.janmayen.http;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.n52.janmayen.function.Functions;
import org.n52.janmayen.stream.Streams;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class QueryBuilder {
    private final URL url;
    private final Charset charset;
    private final Map<String, List<String>> query = new LinkedHashMap<>();
    private String listSeperator = ",";

    public QueryBuilder(URL url) {
        this(url, StandardCharsets.UTF_8);
    }

    public QueryBuilder(URL url, Charset charset) {
        this.charset = Objects.requireNonNull(charset);
        this.url = Objects.requireNonNull(url);
    }

    public QueryBuilder(QueryBuilder builder) {
        this.url = builder.getURL();
        this.charset = builder.getCharset();
        this.listSeperator = builder.getListSeperator();
        builder.getQuery().forEach((name, list) -> add(name, list));
    }

    public QueryBuilder(String url) throws MalformedURLException {
        this(new URL(url));
    }

    private String getListSeperator() {
        return this.listSeperator;
    }

    private Charset getCharset() {
        return this.charset;
    }

    private Map<String, List<String>> getQuery() {
        return Collections.unmodifiableMap(this.query);
    }

    private URL getURL() {
        return this.url;
    }

    public QueryBuilder setListSeperator(String seperator) {
        this.listSeperator = Objects.requireNonNull(Strings.emptyToNull(seperator));
        return this;
    }

    public QueryBuilder add(String name, Object... value) {
        return add(name, Arrays.asList(value));
    }

    public QueryBuilder add(Enum<?> name, Object... value) {
        return add(name, Arrays.asList(value));
    }

    public QueryBuilder add(Enum<?> name, Iterable<Object> value) {
        return add(name.name(), value);
    }

    public QueryBuilder add(String name, Iterable<Object> value) {
        List<String> list = query.computeIfAbsent(name, Functions.forSupplier(LinkedList::new));
        Streams.stream(value).map(o -> o == null ? "" : o.toString()).forEach(list::add);
        return this;
    }

    public URL build() {
        StringBuilder builder = new StringBuilder();
        if (!this.query.isEmpty()) {
            builder.append(this.url.getPath()).append('?');
            this.query.forEach((name, values) -> {
                if (!(builder.lastIndexOf("?") == builder.length() - 1)) {
                    builder.append('&');
                }
                builder.append(name).append('=');
                Iterator<String> iter = values.iterator();
                if (iter.hasNext()) {
                    builder.append(encodeValue(iter.next()));
                    while (iter.hasNext()) {
                        builder.append(this.listSeperator).append(encodeValue(iter.next()));
                    }
                }
            });
        }

        try {
            return new URL(this.url, builder.toString());
        } catch (MalformedURLException ex) {
            throw new Error(ex);
        }
    }

    private String encodeValue(Object s) {
        try {
            if (s == null) {
                return "";
            }
            return URLEncoder.encode(s.toString(), charset.name());
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }
}
