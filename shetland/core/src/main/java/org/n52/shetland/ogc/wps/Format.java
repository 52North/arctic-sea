/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.wps;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import org.n52.janmayen.Optionals;
import org.n52.janmayen.http.MediaType;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Format implements Comparable<Format> {

    public static final String BASE64_ENCODING = "base64";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final Format TEXT_PLAIN = new Format("text/plain");
    public static final Format APPLICATION_XML = new Format("application/xml");
    public static final Format TEXT_XML = new Format("text/xml");
    private static final Comparator<Format> COMPARATOR =
            Comparator.nullsLast(Comparator.comparing(Format::getMimeType, Optionals.nullsFirst()))
                      .thenComparing(Format::getSchema, Optionals.nullsFirst())
                      .thenComparing(Format::getEncoding, Optionals.nullsFirst());
    private static final String STAR_SLASH_STAR = "*/*";
    private static final Set<String> CHARSETS = new HashSet<>(Charset.availableCharsets().keySet());
    private static final String SCHEMA = "schema";
    private static final String ENCODING = "encoding";
    private static final String MIME_TYPE = "mimeType";

    private final String mimeType;
    private final String encoding;
    private final String schema;

    @JsonCreator
    public Format(@JsonProperty(MIME_TYPE) String mimeType,
                  @JsonProperty(ENCODING) String encoding,
                  @JsonProperty(SCHEMA) String schema) {
        this.mimeType = Strings.emptyToNull(mimeType);
        this.encoding = Strings.emptyToNull(encoding);
        this.schema = Strings.emptyToNull(schema);
    }

    public Format(String mimeType) {
        this(mimeType, (String) null, null);
    }

    public Format(String mimeType, String encoding) {
        this(mimeType, encoding, null);
    }

    public Format(String mimeType, Charset encoding) {
        this(mimeType, encoding == null ? null : encoding.name(), null);
    }

    public Format(String mimeType, Charset encoding, String schema) {
        this(mimeType, encoding == null ? null : encoding.name(), schema);
    }

    public Format() {
        this(null, (String) null, null);
    }

    @JsonProperty(MIME_TYPE)
    public Optional<String> getMimeType() {
        return Optional.ofNullable(mimeType);
    }

    @JsonProperty(ENCODING)
    public Optional<String> getEncoding() {
        return Optional.ofNullable(encoding);
    }

    @JsonProperty(SCHEMA)
    public Optional<String> getSchema() {
        return Optional.ofNullable(schema);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return !hasMimeType() && !hasEncoding() && !hasSchema();
    }

    public boolean hasSchema() {
        return this.schema != null;
    }

    public boolean hasSchema(String schema) {
        return getSchema().orElse("").equalsIgnoreCase(Strings.nullToEmpty(schema));
    }

    public boolean hasSchema(Format other) {
        return hasSchema(other.getSchema().orElse(null));
    }

    public boolean hasEncoding() {
        return this.encoding != null;
    }

    public boolean hasEncoding(String encoding) {
        return getEncoding().orElse("").equalsIgnoreCase(Strings.nullToEmpty(encoding));
    }

    public boolean hasEncoding(Format other) {
        return hasEncoding(other.getEncoding().orElse(null));
    }

    public boolean hasMimeType() {
        return this.mimeType != null;
    }

    public boolean hasMimeType(String mimeType) {
        return getMimeType().orElse("").equalsIgnoreCase(Strings.nullToEmpty(mimeType));
    }

    public boolean hasMimeType(Format other) {
        return hasMimeType(other.getMimeType().orElse(null));
    }

    public boolean matchesMimeType(String mimeType) {
        return !hasMimeType() || hasMimeType(mimeType);
    }

    public boolean matchesMimeType(Format other) {
        return !hasMimeType() || hasMimeType(other);
    }

    public boolean matchesEncoding(String encoding) {
        return !hasEncoding() || hasEncoding(encoding);
    }

    public boolean matchesEncoding(Format other) {
        return !hasEncoding() || hasEncoding(other);
    }

    public boolean matchesSchema(String schema) {
        return !hasSchema() || hasSchema(schema);
    }

    public boolean matchesSchema(Format other) {
        return !hasSchema() || hasSchema(other);
    }

    public Format withEncoding(Charset encoding) {
        return withEncoding(encoding.name());
    }

    public Format withEncoding(String encoding) {
        return new Format(this.mimeType, encoding, this.schema);
    }

    public Format withBase64Encoding() {
        return withEncoding(BASE64_ENCODING);
    }

    public Format withUTF8Encoding() {
        return withEncoding(DEFAULT_ENCODING);
    }

    public Format withSchema(String schema) {
        return new Format(this.mimeType, this.encoding, schema);
    }

    public Format withMimeType(String mimeType) {
        return new Format(mimeType, this.encoding, this.schema);
    }

    public Format withoutMimeType() {
        return new Format(null, this.encoding, this.schema);
    }

    public Format withoutEncoding() {
        return new Format(this.mimeType, (String) null, this.schema);
    }

    public Format withoutSchema() {
        return new Format(this.mimeType, this.encoding, null);
    }

    @Override
    public String toString() {
        return String.format("Format{mimeType=%s, encoding=%s, schema=%s}", mimeType, encoding, schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mimeType, this.encoding, this.schema);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Format) {
            final Format that = (Format) obj;
            return Objects.equals(this.mimeType, that.mimeType) &&
                   Objects.equals(this.encoding, that.encoding) &&
                   Objects.equals(this.schema, that.schema);
        }
        return false;
    }

    public Predicate<Format> matchingEncoding() {
        return this::hasEncoding;
    }

    public Predicate<Format> matchingSchema() {
        return this::hasSchema;
    }

    public Predicate<Format> matchingMimeType() {
        return this::hasMimeType;
    }

    public boolean isCompatible(Format that) {
        if (!((!this.hasEncoding() && (!that.hasEncoding() || that.isCharacterEncoding())) ||
              this.hasEncoding(that))) {
            return false;
        }

        if (!MediaType.parse(that.getMimeType().orElse(STAR_SLASH_STAR))
                      .isCompatible(MediaType.parse(this.getMimeType().orElse(STAR_SLASH_STAR)))) {
            return false;
        }

        return !this.hasSchema() || this.hasSchema(that);
    }

    public void setTo(Consumer<String> encoding, Consumer<String> mimeType, Consumer<String> schema) {
        getEncoding().ifPresent(encoding);
        getMimeType().ifPresent(mimeType);
        getSchema().ifPresent(schema);
    }

    @Override
    public int compareTo(Format that) {
        return comparator().compare(this, that);
    }

    @JsonIgnore
    public boolean isXML() {
        return getMimeType().map(String::toLowerCase).filter(x -> x.endsWith("xml")).isPresent();
    }

    @JsonIgnore
    public boolean isCharacterEncoding() {
        return getEncoding().filter(getAvailableCharsets()::contains).isPresent();
    }

    @JsonIgnore
    public Optional<Charset> getEncodingAsCharset() {
        return getEncoding().filter(getAvailableCharsets()::contains).map(Charset::forName);
    }

    @JsonIgnore
    public Charset getEncodingAsCharsetOrDefault() {
        return getEncodingAsCharset().orElse(StandardCharsets.UTF_8);
    }

    @JsonIgnore
    public boolean isBase64() {
        return getEncoding().filter(BASE64_ENCODING::equalsIgnoreCase).isPresent();
    }

    public static Format empty() {
        return new Format();
    }

    public static Set<String> getAvailableCharsets() {
        return Collections.unmodifiableSet(CHARSETS);
    }

    public static Comparator<Format> comparator() {
        return COMPARATOR;
    }

    public Set<Format> withAnyCharset() {
        return getAvailableCharsets().stream().map(this::withEncoding).collect(Collectors.toSet());
    }
}
