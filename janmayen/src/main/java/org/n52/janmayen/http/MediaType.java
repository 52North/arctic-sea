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
package org.n52.janmayen.http;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.n52.janmayen.similar.Similar;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class MediaType implements Comparable<MediaType>, Similar<MediaType> {

    public static final String IMAGE_TYPE = "image";
    public static final String AUDIO_TYPE = "audio";
    public static final String VIDEO_TYPE = "video";
    public static final String MESSAGE_TYPE = "message";
    public static final String MODEL_TYPE = "model";
    public static final String MULTIPART_TYPE = "multipart";
    public static final String APPLICATION_TYPE = "application";
    public static final String TEXT_TYPE = "text";
    public static final String WILDCARD_TYPE = "*";
    private static final String QUALITY_PARAMETER = "q";

    private static final MediaType ANY = new MediaType(WILDCARD_TYPE, WILDCARD_TYPE);
    private static final MediaType ANY_APPLICATION = application(WILDCARD_TYPE);
    private static final MediaType ANY_TEXT = text(WILDCARD_TYPE);
    private static final MediaType ANY_IMAGE = image(WILDCARD_TYPE);
    private static final MediaType ANY_AUDIO = audio(WILDCARD_TYPE);
    private static final MediaType ANY_VIDEO = video(WILDCARD_TYPE);
    private static final MediaType ANY_MESSAGE = message(WILDCARD_TYPE);
    private static final MediaType ANY_MODEL = model(WILDCARD_TYPE);
    private static final MediaType ANY_MULTIPART = multipart(WILDCARD_TYPE);

    private final com.google.common.net.MediaType delegate;

    /**
     * Constructs a <code>*&#47;*</code> media type.
     */
    public MediaType() {
        this(WILDCARD_TYPE, WILDCARD_TYPE, Collections.emptyMap());
    }

    /**
     * Constructs a <code>type&#47;*</code> media type.
     *
     * @param type the type (may be <code>null</code> for a wild card)
     */
    public MediaType(String type) {
        this(type, WILDCARD_TYPE, Collections.emptyMap());
    }

    /**
     * Constructs a <code>type&#47;subtype</code> media type.
     *
     * @param type    the type (may be <code>null</code> for a wild card)
     * @param subtype the subtype (may be <code>null</code> for a wild card)
     */
    public MediaType(String type, String subtype) {
        this(type, subtype, Collections.emptyMap());
    }

    /**
     * Constructs a <code>type&#47;subtype;parameter="name"</code> media type.
     *
     * @param type           the type (may be <code>null</code> for a wild card)
     * @param subtype        the subtype (may be <code>null</code> for a wild card)
     * @param parameter      the parameter
     * @param parameterValue the parameter value
     */
    public MediaType(String type,
                     String subtype,
                     String parameter,
                     String parameterValue) {
        this(type, subtype, Collections.singletonMap(
             Objects.requireNonNull(parameter).toLowerCase(Locale.ROOT),
             Collections.singletonList(Objects.requireNonNull(parameterValue))));
    }

    /**
     * Constructs a media type using the supplied parameters.
     *
     * @param type       the type (may be <code>null</code> for a wild card)
     * @param subtype    the subtype (may be <code>null</code> for a wild card)
     * @param parameters the parameter map
     */
    public MediaType(String type, String subtype, Map<String, ? extends Collection<String>> parameters) {
        this(com.google.common.net.MediaType.create(type, subtype).withParameters(asMultiMap(parameters)));
    }

    private MediaType(com.google.common.net.MediaType mediaType) {
        this.delegate = Objects.requireNonNull(mediaType);
    }

    public String getType() {
        return getDelegate().type();
    }

    public String getSubtype() {
        return getDelegate().subtype();
    }

    public Map<String, Collection<String>> getParameters() {
        return getDelegate().parameters().asMap();
    }

    public boolean isWildcard() {
        return isWildcardType() && isWildcardSubtype();
    }

    public boolean isWildcardType() {
        return getType().equals(WILDCARD_TYPE);
    }

    public boolean isWildcardSubtype() {
        return getSubtype().equals(WILDCARD_TYPE);
    }

    public boolean isCompatible(MediaType other) {
        if (getDelegate().is(other.getDelegate())) {
            return true;
        }
        //check compatible types
        if (MediaTypes.COMPATIBLE_TYPES.containsKey(other)) {
            return MediaTypes.COMPATIBLE_TYPES.get(other).stream()
                    .map(MediaType::getDelegate)
                    .anyMatch(getDelegate()::is);
        }

        return false;
    }

    public Collection<String> getParameter(String parameter) {
        return getParameters().get(parameter.toLowerCase(Locale.ROOT));
    }

    public boolean hasParameter(String parameter) {
        return getParameters().containsKey(parameter.toLowerCase(Locale.ROOT));
    }

    public float getQuality() {
        if (hasParameter(QUALITY_PARAMETER)) {
            return Float.valueOf(getParameter(QUALITY_PARAMETER).iterator().next());
        } else {
            return 1;
        }
    }

    public MediaType withType(String type) {
        return new MediaType(type, getSubtype(), getParameters());

    }

    public MediaType withSubType(String subtype) {
        return new MediaType(getType(), subtype, getParameters());
    }

    public boolean hasParameters() {
        return !getParameters().isEmpty();
    }

    public MediaType withParameter(String parameter, String value) {
        return new MediaType(getDelegate().withParameter(value, value));
    }

    public MediaType withParameters(Map<String, ? extends Collection<String>> parameters) {
        return new MediaType(getType(), getSubtype(), parameters);
    }

    public MediaType withoutParameter(String parameter) {
        if (!hasParameter(parameter)) {
            return this;
        }
        Map<String, Collection<String>> parameters = new HashMap<>(getParameters());
        parameters.remove(parameter);
        return withParameters(parameters);
    }

    public MediaType withoutQuality() {
        return withoutParameter(QUALITY_PARAMETER);
    }

    public MediaType withoutParameters() {
        if (!hasParameters()) {
            return this;
        }
        return new MediaType(getDelegate().withoutParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getDelegate());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MediaType) {
            MediaType other = (MediaType) obj;
            return Objects.equals(getDelegate(), other.getDelegate());
        }
        return false;
    }

    @Override
    public int compareTo(MediaType o) {
        Objects.requireNonNull(o);
        return ComparisonChain.start().compare(getType(), o.getType()).compare(getSubtype(), o.getSubtype()).result();
    }

    @Override
    public String toString() {
        return getDelegate().toString();
    }

    @Override
    public int getSimilarity(MediaType other) {
        if (equals(other)) {
            return 0;
        } else if (other.isCompatible(this)) {
            if (isWildcard()) {
                return 1;
            } else if (isWildcardType()) {
                return 2;
            } else if (isWildcardSubtype()) {
                return 3;
            } else {
                return 0;
            }
        }
        return -1;
    }

    private com.google.common.net.MediaType getDelegate() {
        return delegate;
    }

    private static <K, V> Multimap<K, V> asMultiMap(Map<K, ? extends Collection<V>> map) {
        Objects.requireNonNull(map);
        ListMultimap<K, V> multiMap = ArrayListMultimap.create();
        map.forEach(multiMap::putAll);
        return multiMap;
    }

    public static MediaType parse(String string) {
        Preconditions.checkArgument(string != null);
        return new MediaType(com.google.common.net.MediaType.parse(string.trim()));
    }

    /**
     * Normalize mime type string by processing it through the MediaType parser. Handles differing spaces between type
     * and subtype, etc.
     *
     * @param string Mime type string to normalize
     *
     * @return Normalized mime type string
     */
    public static String normalizeString(String string) {
        return parse(string).toString();
    }

    public static MediaType application(String subtype) {
        return new MediaType(APPLICATION_TYPE, subtype);
    }

    public static MediaType application(String subtype, String parameterName, String parameterValue) {
        return application(subtype).withParameter(parameterName, parameterValue);
    }

    public static MediaType text(String subtype) {
        return new MediaType(TEXT_TYPE, subtype);
    }

    public static MediaType image(String subtype) {
        return new MediaType(IMAGE_TYPE, subtype);
    }

    public static MediaType audio(String subtype) {
        return new MediaType(AUDIO_TYPE, subtype);
    }

    public static MediaType video(String subtype) {
        return new MediaType(VIDEO_TYPE, subtype);
    }

    public static MediaType message(String subtype) {
        return new MediaType(MESSAGE_TYPE, subtype);
    }

    public static MediaType model(String subtype) {
        return new MediaType(MODEL_TYPE, subtype);
    }

    public static MediaType multipart(String subtype) {
        return new MediaType(MULTIPART_TYPE, subtype);
    }

    public static MediaType any() {
        return ANY;
    }

    public static MediaType anyApplication() {
        return ANY_APPLICATION;
    }

    public static MediaType anyText() {
        return ANY_TEXT;
    }

    public static MediaType anyImage() {
        return ANY_IMAGE;
    }

    public static MediaType anyAudio() {
        return ANY_AUDIO;
    }

    public static MediaType anyVideo() {
        return ANY_VIDEO;
    }

    public static MediaType anyMessage() {
        return ANY_MESSAGE;
    }

    public static MediaType anyModel() {
        return ANY_MODEL;
    }

    public static MediaType anyMultipart() {
        return ANY_MULTIPART;
    }
}
