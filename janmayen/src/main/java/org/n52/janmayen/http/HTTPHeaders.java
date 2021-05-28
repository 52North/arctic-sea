/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.janmayen.stream.Streams;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public abstract class HTTPHeaders {
    public static final String AUTHORIZATION = "Authorization";

    public static final String ACCEPT_ENCODING = "Accept-Encoding";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    public static final String ALLOW = "Allow";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String ACCEPT = "Accept";

    public static final String LOCATION = "Location";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private static final Logger LOGGER = LoggerFactory.getLogger(HTTPHeaders.class);

    private HTTPHeaders() {
    }

    public static boolean supportsGzipEncoding(HttpServletRequest req) {
        return checkHeader(req, HTTPHeaders.ACCEPT_ENCODING, HTTPConstants.GZIP_ENCODING);
    }

    public static boolean isGzipEncoded(HttpServletRequest req) {
        return checkHeader(req, HTTPHeaders.CONTENT_ENCODING, HTTPConstants.GZIP_ENCODING);
    }

    private static boolean checkHeader(HttpServletRequest req, String headerName, String value) {
        return Streams.stream(Optional.ofNullable(req.getHeaders(headerName))
                .orElseGet(Collections::emptyEnumeration))
                .map(Strings::emptyToNull)
                .filter(Objects::nonNull)
                .map(h -> h.split(","))
                .flatMap(Arrays::stream)
                .anyMatch(value::equalsIgnoreCase);
    }

    public static List<MediaType> getAcceptHeader(HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader(HTTPHeaders.ACCEPT))
                .map(Strings::emptyToNull)
                .map(h -> h.split(","))
                .map(Arrays::stream)
                .map(s -> s.map(HTTPHeaders::parseMediaType).filter(Objects::nonNull).collect(toList()))
                .filter(l -> !l.isEmpty())
                .orElseGet(() -> Collections.singletonList(MediaType.any()));
    }

    private static MediaType parseMediaType(String mt) {
        try {
            return MediaType.parse(mt);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("The HTTP-Accept header contains an invalid value: " + mt, e);
            return null;
        }
    }

}
