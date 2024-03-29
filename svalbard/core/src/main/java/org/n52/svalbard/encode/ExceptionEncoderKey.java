/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ExceptionEncoderKey implements EncoderKey {
    private final MediaType mediaType;

    public ExceptionEncoderKey(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public int getSimilarity(EncoderKey key) {
        return equals(key) ? 0 : -1;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getMediaType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && getClass().equals(obj.getClass())) {
            ExceptionEncoderKey key = (ExceptionEncoderKey) obj;
            return key.getMediaType().equals(getMediaType());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("ExceptionEncoderKey[mediaType=%s]", getMediaType());
    }
}
