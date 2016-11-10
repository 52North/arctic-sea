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
package org.n52.shetland.w3c.xlink;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Link {
    private final Optional<URI> href;
    private final Optional<URI> role;
    private final Optional<URI> arcrole;
    private final Optional<String> title;
    private final Optional<Show> show;
    private final Optional<Actuate> actuate;

    public Link(URI href) {
        this(href, null, null, null, null, null);
    }

    public Link(URI href, String title) {
        this(href, null, null, title, null, null);
    }

    public Link(URI href, URI role, URI arcrole, String title, Show show,
                Actuate actuate) {
        this.href = Optional.ofNullable(href);
        this.role = Optional.ofNullable(role);
        this.arcrole = Optional.ofNullable(arcrole);
        this.title = Optional.ofNullable(Strings.emptyToNull(title));
        this.show = Optional.ofNullable(show);
        this.actuate = Optional.ofNullable(actuate);
    }

    public Optional<URI> getHref() {
        return this.href;
    }

    public Optional<URI> getRole() {
        return role;
    }

    public Optional<URI> getArcrole() {
        return arcrole;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<Show> getShow() {
        return show;
    }

    public Optional<Actuate> getActuate() {
        return actuate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHref(), getRole(), getArcrole(),
                            getTitle(), getShow(), getActuate());
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
        final Link other = (Link) obj;
        return Objects.equals(this.getHref(), other.getHref()) &&
               Objects.equals(this.getRole(), other.getRole()) &&
               Objects.equals(this.getArcrole(), other.getArcrole()) &&
               Objects.equals(this.getTitle(), other.getTitle()) &&
               Objects.equals(this.getShow(), other.getShow()) &&
               Objects.equals(this.getActuate(), other.getActuate());
    }



}
