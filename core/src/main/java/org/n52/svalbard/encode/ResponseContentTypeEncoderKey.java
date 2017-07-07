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
package org.n52.svalbard.encode;

import org.n52.janmayen.http.MediaType;

import com.google.common.base.Objects;

public class ResponseContentTypeEncoderKey implements EncoderKey {

    private final Class<?> clazz;
    private final MediaType contentType;

    /**
     * @param clazz The class
     * @param contentType The response content type
     */
    public ResponseContentTypeEncoderKey(Class<?> clazz, MediaType contentType) {
        super();
        this.clazz = clazz;
        this.contentType = contentType;
    }

    public Class<?> getObjectClass() {
        return clazz;
    }

    public MediaType getContentType() {
        return contentType;
    }

    @Override
    public int getSimilarity(EncoderKey key) {
        return equals(key) ? 0 : -1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.clazz, this.contentType);
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);
        if (obj != null && getClass() == obj.getClass()) {
            final ResponseContentTypeEncoderKey other = (ResponseContentTypeEncoderKey) obj;
            return getObjectClass().equals(other.getObjectClass()) && getContentType() != null
                    && getContentType().isCompatible(other.getContentType());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[object=%s, contentType=%s]", getClass().getSimpleName(),
                getObjectClass().getSimpleName(), getContentType());
    }

}
