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
package org.n52.shetland.ogc.wps.data;

import com.google.common.base.MoreObjects;
import org.n52.shetland.util.HTTP;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReferencedBody extends Body {

    private final URI href;
    private String body;

    public ReferencedBody(URI href) {
        this.href = Objects.requireNonNull(href);
    }

    public URI getHref() {
        return href;
    }

    @Override
    public synchronized String getBody() throws IOException {
        if (this.body == null) {
            this.body = HTTP.getAsString(this.href);
        }
        return this.body;
    }

    @Override
    public boolean isReferenced() {
        return true;
    }

    @Override
    public ReferencedBody asReferenced() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        ReferencedBody other = (ReferencedBody) obj;

        return Objects.equals(getHref(), other.getHref());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHref());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("href", getHref())
                          .toString();
    }
}
