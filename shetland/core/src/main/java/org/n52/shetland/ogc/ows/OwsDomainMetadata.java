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
package org.n52.shetland.ogc.ows;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsDomainMetadata {

    private final URI reference;
    private final String value;

    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")
    public OwsDomainMetadata(@Nullable URI reference, @Nullable String value) {
        this.reference = reference;
        this.value = Strings.emptyToNull(value);
    }

    public OwsDomainMetadata(@Nullable String value) {
        this(null, value);
    }

    public OwsDomainMetadata(@Nullable URI reference) {
        this(reference, null);
    }

    public Optional<URI> getReference() {
        return Optional.ofNullable(this.reference);
    }

    public Optional<String> getValue() {
        return Optional.ofNullable(this.value);
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
        return Objects.equals(this.getValue(), that.getValue())
                && Objects.equals(this.getReference(), that.getReference());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("reference", getReference().orElse(null))
                .add("value", getValue().orElse(null)).toString();
    }
}
