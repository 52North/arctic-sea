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
package org.n52.shetland.ogc.ows;

import java.net.URI;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import org.n52.janmayen.Optionals;
import org.n52.shetland.w3c.xlink.Actuate;
import org.n52.shetland.w3c.xlink.Link;
import org.n52.shetland.w3c.xlink.Show;

/**
 * @author Christian Autermann
 */
public class OwsMetadata extends Link implements Comparable<OwsMetadata> {

    private static final Comparator<OwsMetadata> COMPARATOR
            = Comparator.nullsLast(Comparator
                    .comparing(OwsMetadata::getTitle, Optionals.nullsLast())
                    .thenComparing(OwsMetadata::getHref, Optionals.nullsLast()));

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

    public OwsMetadata(URI href, URI role, URI arcrole, String title, Show show,
                       Actuate actuate, URI about) {
        super(href, role, arcrole, title, show, actuate);
        this.about = Optional.ofNullable(about);
    }

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
