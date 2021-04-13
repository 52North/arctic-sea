/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding;

import java.util.Objects;

import org.n52.janmayen.http.MediaType;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class MediaTypeBindingKey implements BindingKey {

    private final MediaType mediaType;

    public MediaTypeBindingKey(MediaType mediaType) {
        this.mediaType = Objects.requireNonNull(mediaType);
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    @Override
    public String toString() {
        return "MediaTypeBindingKey{" + "mediaType=" + mediaType + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.mediaType);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MediaTypeBindingKey other = (MediaTypeBindingKey) obj;
        if (!Objects.equals(this.getMediaType(), other.getMediaType())) {
            return false;
        }
        return true;
    }

    @Override
    public String getKeyAsString() {
        return getMediaType().toString();
    }
}
