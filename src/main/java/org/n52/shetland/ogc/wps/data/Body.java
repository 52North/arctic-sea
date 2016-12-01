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
package org.n52.shetland.ogc.wps.data;

import java.io.IOException;
import java.net.URI;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class Body {
    public abstract String getBody()
            throws IOException;

    public boolean isReferenced() {
        return false;
    }

    public ReferencedBody asReferenced() {
        throw new UnsupportedOperationException();
    }

    public boolean isInline() {
        return false;
    }

    public InlineBody asInline() {
        throw new UnsupportedOperationException();
    }

    public static Body inline(String body) {
        return new InlineBody(body);
    }

    public static Body reference(URI uri) {
        return new ReferencedBody(uri);
    }

    public static Body reference(String uri) {
        return reference(URI.create(uri));
    }

}
