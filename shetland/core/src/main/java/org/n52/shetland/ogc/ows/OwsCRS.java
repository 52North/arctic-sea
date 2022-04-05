/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import static java.util.stream.Collectors.toSet;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsCRS {
    private final URI value;

    public OwsCRS(URI value) {
        this.value = Objects.requireNonNull(value);
    }

    public URI getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsCRS other = (OwsCRS) obj;
        return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return "OwsCRS{" + "value=" + value + '}';
    }

    public static OwsCRS of(URI value) {
        return new OwsCRS(value);
    }

    public static Set<OwsCRS> of(Collection<URI> values) {
        if (values == null || values.isEmpty()) {
            return Collections.emptySet();
        } else {
            return values.stream().map(OwsCRS::new).collect(toSet());
        }
    }

}
