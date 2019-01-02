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
package org.n52.shetland.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", justification = "spotbugs false positive")
public final class HTTP {
    private static final CloseableHttpClient CLIENT = HttpClientBuilder.create()
            .useSystemProperties()
            .setUserAgent("shetland-1.0.0")
            .build();

    private HTTP() {
    }

    public static String getAsString(URI uri)
            throws IOException {
        try (CloseableHttpResponse response = CLIENT.execute(new HttpGet(uri))) {
            HttpEntity entity = response.getEntity();
            String encoding = Optional.ofNullable(entity.getContentEncoding())
                    .map(Header::getValue).orElse(StandardCharsets.UTF_8.name());
            Charset charset = Charset.forName(encoding);
            try (InputStream is = entity.getContent();
                 Reader reader = new InputStreamReader(is, charset)) {
                return CharStreams.toString(reader);
            }
        }
    }

    public static byte[] get(URI uri)
            throws IOException {
        return execute(new HttpGet(uri));
    }

    public static void get(URI uri, OutputStream out)
            throws IOException {
        execute(new HttpGet(uri), out);
    }

    public static byte[] post(URI uri, byte[] bytes)
            throws IOException {
        HttpPost request = new HttpPost(uri);
        request.setEntity(new ByteArrayEntity(bytes));
        return execute(request);
    }

    public static void post(URI uri, byte[] bytes, OutputStream out)
            throws IOException {
        HttpPost request = new HttpPost(uri);
        request.setEntity(new ByteArrayEntity(bytes));
        execute(request, out);
    }

    private static byte[] execute(HttpUriRequest request)
            throws IOException {
        try (CloseableHttpResponse response = CLIENT.execute(request)) {
            try (InputStream is = response.getEntity().getContent()) {
                return ByteStreams.toByteArray(is);
            }
        }
    }

    private static void execute(HttpUriRequest request, OutputStream out)
            throws IOException {
        try (CloseableHttpResponse response = CLIENT.execute(request)) {
            response.getEntity().writeTo(out);
        }
    }

}
