/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.n52.janmayen.Streams;
import org.n52.janmayen.function.Functions;

import com.google.common.base.Joiner;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class QueryBuilder {
    private static final Joiner COMMA_JOINER = Joiner.on(",");
    private final URL url;
    private final Charset charset;
    private final Map<String, List<String>> query = new LinkedHashMap<>();

    public QueryBuilder(URL url) {
        this(url, StandardCharsets.UTF_8);
    }

    public QueryBuilder(URL url, Charset charset) {
        this.charset = Objects.requireNonNull(charset);
        this.url = Objects.requireNonNull(url);
    }

    public QueryBuilder(String url) throws MalformedURLException {
        this(new URL(url));
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
                builder.append(name).append('=');

                COMMA_JOINER.appendTo(builder, new MappingIterator<>(values.iterator(), this::encodeValue));
            });
        }

        try {
            return new URL(this.url, builder.toString());
        } catch (MalformedURLException ex) {
            throw new Error(ex);
        }
    }

    private String encodeValue(Object s)  {
        try {
            if (s == null) {
                return "";
            }
            return URLEncoder.encode(s.toString(), charset.name());
        } catch (UnsupportedEncodingException ex) {
            throw new Error(ex);
        }
    }

    public static void main(String[] args) throws MalformedURLException {
        String s = "http://abc:asdf@www.52north.org?request=GetCapabilities&service=SOS&version=2.0.0";

        URL url = new URL(s);
        url = new URL(url, url.getPath() + "?");

        System.out.println(url);
        System.out.println("authority:" + url.getAuthority());
        System.out.println("defaultport:" + url.getDefaultPort());
        System.out.println("file:" + url.getFile());
        System.out.println("host:" + url.getHost());
        System.out.println("path:" + url.getPath());
        System.out.println("port:" + url.getPort());
        System.out.println("protocol:" + url.getProtocol());
        System.out.println("query:" + url.getQuery());
        System.out.println("ref:" + url.getRef());
        System.out.println("userInfo:" + url.getUserInfo());

    }
    private static class MappingIterator<S, T> implements Iterator<T> {
        private final Iterator<S> iter;
        private final Function<? super S, ? extends T> mapper;

        public MappingIterator(Iterator<S> iter, Function<? super S, ? extends T> mapper) {
            this.iter = Objects.requireNonNull(iter);
            this.mapper = Objects.requireNonNull(mapper);
        }

        @Override
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override
        public T next() {
            return this.mapper.apply(this.iter.next());
        }
    }
}
