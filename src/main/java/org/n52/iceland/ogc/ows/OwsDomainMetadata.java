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
package org.n52.iceland.ogc.ows;

import static com.google.common.base.Strings.emptyToNull;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.MoreObjects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsDomainMetadata {

    private final URI reference;
    private final String value;

    public OwsDomainMetadata(URI reference, String value) {
        this.reference = reference;
        this.value = Objects.requireNonNull(emptyToNull(value));
    }

    public OwsDomainMetadata(String value) {
        this(null, value);
    }

    public Optional<URI> getReference() {
        return Optional.ofNullable(this.reference);
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.reference, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsDomainMetadata that = (OwsDomainMetadata) obj;
        return Objects.equals(this.getValue(), that.getValue()) &&
               Objects.equals(this.getReference(), that.getReference());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("reference", getReference().orElse(null))
                .add("value", getValue())
                .toString();
    }
}
