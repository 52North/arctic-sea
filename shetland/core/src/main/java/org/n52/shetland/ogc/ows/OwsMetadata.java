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
package org.n52.shetland.ogc.ows;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.n52.janmayen.Optionals;
import org.n52.shetland.w3c.xlink.Actuate;
import org.n52.shetland.w3c.xlink.Link;
import org.n52.shetland.w3c.xlink.Show;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Christian Autermann
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OwsMetadata extends Link implements Comparable<OwsMetadata> {

    private static final Comparator<OwsMetadata> COMPARATOR
            = Comparator.nullsLast(Comparator.comparing(OwsMetadata::getTitle, Optionals.nullsLast())
                                             .thenComparing(OwsMetadata::getHref, Optionals.nullsLast()));
    private static final String ABOUT = "about";

    private final Optional<URI> about;

    public OwsMetadata(URI href) {
        this(href, null, null, null, null, null, null);
    }

    public OwsMetadata(URI href, URI about) {
        this(href, null, null, null, null, null, about);
    }

    public OwsMetadata(URI href, String title) {
        this(href, null, null, title, null, null, null);
    }

    public OwsMetadata(URI href, String title, URI about) {
        this(href, null, null, title, null, null, about);
    }

    @JsonCreator
    public OwsMetadata(@JsonProperty(HREF) @Nullable URI href,
                       @JsonProperty(ROLE) @Nullable URI role,
                       @JsonProperty(ARCROLE) @Nullable URI arcrole,
                       @JsonProperty(TITLE) @Nullable String title,
                       @JsonProperty(SHOW) @Nullable Show show,
                       @JsonProperty(ACTUATE) @Nullable Actuate actuate,
                       @JsonProperty(ABOUT) @Nullable URI about) {
        super(href, role, arcrole, title, show, actuate);
        this.about = Optional.ofNullable(about);
    }

    @JsonProperty(ABOUT)
    public Optional<URI> getAbout() {
        return about;
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
        final OwsMetadata other = (OwsMetadata) obj;

        return super.equals(obj) && Objects.equals(getAbout(), other.getAbout());
    }

    @Override
    public int hashCode() {
        return 53 * super.hashCode() + Objects.hashCode(getAbout());
    }

    @Override
    public int compareTo(OwsMetadata o) {
        return COMPARATOR.compare(this, o);
    }

}
