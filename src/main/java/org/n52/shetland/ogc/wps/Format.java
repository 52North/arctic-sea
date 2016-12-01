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
package org.n52.shetland.ogc.wps;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;
import static java.util.stream.Collectors.toSet;

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

import org.n52.janmayen.Optionals;
import org.n52.janmayen.http.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Format implements Comparable<Format> {

    private static final Comparator<Format> COMPARATOR = Comparator.nullsLast(Comparator
            .comparing(Format::getMimeType, Optionals.nullsFirst()))
            .thenComparing(Format::getSchema, Optionals.nullsFirst())
            .thenComparing(Format::getEncoding, Optionals.nullsFirst());

    private static final Set<String> CHARSETS = new HashSet<>(Charset.availableCharsets().keySet());

    public static final String BASE64_ENCODING = "base64";
    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final Format TEXT_PLAIN = new Format("text/plain");
    public static final Format APPLICATION_XML = new Format("application/xml");
    public static final Format TEXT_XML = new Format("text/xml");

    private final Optional<String> mimeType;
    private final Optional<String> encoding;
    private final Optional<String> schema;

    public Format(String mimeType) {
        this(mimeType, (String) null, null);
    }

    public Format(String mimeType, String encoding) {
        this(mimeType, encoding, null);
    }

    public Format(String mimeType, String encoding, String schema) {
        this(Optional.ofNullable(emptyToNull(mimeType)),
             Optional.ofNullable(emptyToNull(encoding)),
             Optional.ofNullable(emptyToNull(schema)));
    }

    private Format(Optional<String> mimeType, Optional<String> encoding, Optional<String> schema) {
        this.mimeType = Objects.requireNonNull(mimeType);
        this.encoding = Objects.requireNonNull(encoding);
        this.schema = Objects.requireNonNull(schema);
    }

    public Format(String mimeType, Charset encoding) {
        this(mimeType, encoding, null);
    }

    public Format(String mimeType, Charset encoding, String schema) {
        this(mimeType, encoding == null ? null : encoding.name(), schema);
    }

    public Format() {
        this(null, (String) null, null);
    }

    public Optional<String> getMimeType() {
        return mimeType;
    }

    public Optional<String> getEncoding() {
        return encoding;
    }

    public Optional<String> getSchema() {
        return schema;
    }

    public boolean isEmpty() {
        return !hasMimeType() && !hasEncoding() && !hasSchema();
    }

    public boolean hasSchema() {
        return getSchema().isPresent();
    }

    public boolean hasEncoding() {
        return getEncoding().isPresent();
    }

    public boolean hasMimeType() {
        return getMimeType().isPresent();
    }

    public boolean hasMimeType(String mimeType) {
        return getMimeType().orElse("").equalsIgnoreCase(nullToEmpty(mimeType));
    }

    public boolean hasEncoding(String encoding) {
        return getEncoding().orElse("").equalsIgnoreCase(nullToEmpty(encoding));
    }

    public boolean hasSchema(String schema) {
        return getSchema().orElse("").equalsIgnoreCase(nullToEmpty(schema));
    }

    public boolean hasMimeType(Format other) {
        return hasMimeType(other.getMimeType().orElse(null));
    }

    public boolean hasEncoding(Format other) {
        return hasEncoding(other.getEncoding().orElse(null));
    }

    public boolean hasSchema(Format other) {
        return hasSchema(other.getSchema().orElse(null));
    }

    public boolean matchesMimeType(String mimeType) {
        return !hasMimeType() || hasMimeType(mimeType);
    }

    public boolean matchesEncoding(String encoding) {
        return !hasEncoding() || hasEncoding(encoding);
    }

    public boolean matchesSchema(String schema) {
        return !hasSchema() || hasSchema(schema);
    }

    public boolean matchesMimeType(Format other) {
        return !hasMimeType() || hasMimeType(other);
    }

    public boolean matchesEncoding(Format other) {
        return !hasEncoding() || hasEncoding(other);
    }

    public boolean matchesSchema(Format other) {
        return !hasSchema() || hasSchema(other);
    }

    public Format withEncoding(Charset encoding) {
        return withEncoding(encoding.name());
    }

    public Format withEncoding(String encoding) {
        return new Format(getMimeType(), Optional.ofNullable(encoding), getSchema());
    }

    public Format withBase64Encoding() {
        return withEncoding(BASE64_ENCODING);
    }

    public Format withUTF8Encoding() {
        return withEncoding(DEFAULT_ENCODING);
    }

    public Format withSchema(String schema) {
        return new Format(getMimeType(), getEncoding(), Optional.ofNullable(schema));
    }

    public Format withMimeType(String mimeType) {
        return new Format(Optional.ofNullable(mimeType), getEncoding(), getSchema());
    }

    public Format withoutMimeType() {
        return new Format(Optional.empty(), getEncoding(), getSchema());
    }

    public Format withoutEncoding() {
        return new Format(getMimeType(), Optional.empty(), getSchema());
    }

    public Format withoutSchema() {
        return new Format(getMimeType(), getEncoding(), Optional.empty());
    }

    @Override
    public String toString() {
        return "Format{" + "mimeType=" + mimeType.orElse(null) + ", encoding=" + encoding.orElse(null) + ", schema=" +
               schema.orElse(null) + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.mimeType, this.encoding, this.schema);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Format) {
            final Format that = (Format) obj;
            return Objects.equals(this.mimeType, that.getMimeType()) &&
                   Objects.equals(this.encoding, that.getEncoding()) &&
                   Objects.equals(this.schema, that.getSchema());
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
        if (!((!this.hasEncoding() && (!that.hasEncoding() || that.isCharacterEncoding())) || this.hasEncoding(that))) {
            return false;
        }


        if (!MediaType.parse(that.getMimeType().orElse("*/*")).isCompatible(MediaType.parse(this.getMimeType().orElse("*/*")))) {
            return false;
        }

        return (!this.hasSchema() || this.hasSchema(that));
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

    public boolean isXML() {
        return getMimeType().map(String::toLowerCase).filter(x -> x.endsWith("xml")).isPresent();
    }

    public boolean isCharacterEncoding() {
        return getEncoding().filter(getAvailableCharsets()::contains).isPresent();
    }

    public Optional<Charset> getEncodingAsCharset() {
        return getEncoding().filter(getAvailableCharsets()::contains).map(Charset::forName);
    }

    public Charset getEncodingAsCharsetOrDefault() {
        return getEncodingAsCharset().orElse(StandardCharsets.UTF_8);
    }

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
        return getAvailableCharsets().stream().map(this::withEncoding).collect(toSet());
    }
}
