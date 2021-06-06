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
package org.n52.shetland.ogc.ows;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.n52.janmayen.Optionals;

import java.io.Serializable;
import java.net.URI;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsCode implements Comparable<OwsCode>, Serializable {
    private static final long serialVersionUID = -1884521299174349193L;

    private static final Comparator<OwsCode> COMPARATOR
            = Comparator.nullsLast(Comparator.comparing(OwsCode::getCodeSpace, Optionals.nullsLast())
                                             .thenComparing(OwsCode::getValue));

    private final String value;
    private final URI codeSpace;

    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public OwsCode(String value, URI codeSpace) {
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
        this.codeSpace = codeSpace;
    }

    public OwsCode(String value, String codeSpace) {
        this(value, Optional.ofNullable(codeSpace).map(URI::create).orElse(null));
    }

    public OwsCode(String value) {
        this(value, (URI) null);
    }

    public String getValue() {
        return this.value;
    }

    public Optional<URI> getCodeSpace() {
        return Optional.ofNullable(this.codeSpace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getCodeSpace());
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
        final OwsCode other = (OwsCode) obj;
        return Objects.equals(this.getValue(), other.getValue()) &&
               Objects.equals(this.getCodeSpace(), other.getCodeSpace());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .omitNullValues()
                          .add("value", getValue())
                          .add("codeSpace", getCodeSpace().orElse(null))
                          .toString();
    }

    @Override
    public int compareTo(OwsCode o) {
        return COMPARATOR.compare(this, o);
    }

}
