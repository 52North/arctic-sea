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
package org.n52.shetland.w3c.xlink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Link {
    protected static final String HREF = "href";
    protected static final String ROLE = "role";
    protected static final String ARCROLE = "arcrole";
    protected static final String TITLE = "title";
    protected static final String SHOW = "show";
    protected static final String ACTUATE = "actuate";
    private final Optional<URI> href;
    private final Optional<URI> role;
    private final Optional<URI> arcrole;
    private final Optional<String> title;
    private final Optional<Show> show;
    private final Optional<Actuate> actuate;

    public Link(@Nullable URI href) {
        this(href, null, null, null, null, null);
    }

    public Link(@Nullable URI href, @Nullable String title) {
        this(href, null, null, title, null, null);
    }

    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")
    @JsonCreator
    public Link(@JsonProperty(HREF) @Nullable URI href,
                @JsonProperty(ROLE) @Nullable URI role,
                @JsonProperty(ARCROLE) @Nullable URI arcrole,
                @JsonProperty(TITLE) @Nullable String title,
                @JsonProperty(SHOW) @Nullable Show show,
                @JsonProperty(ACTUATE) @Nullable Actuate actuate) {
        this.href = Optional.ofNullable(href);
        this.role = Optional.ofNullable(role);
        this.arcrole = Optional.ofNullable(arcrole);
        this.title = Optional.ofNullable(Strings.emptyToNull(title));
        this.show = Optional.ofNullable(show);
        this.actuate = Optional.ofNullable(actuate);
    }

    @JsonProperty(HREF)
    public Optional<URI> getHref() {
        return this.href;
    }

    @JsonProperty(ROLE)
    public Optional<URI> getRole() {
        return role;
    }

    @JsonProperty(ARCROLE)
    public Optional<URI> getArcrole() {
        return arcrole;
    }

    @JsonProperty(TITLE)
    public Optional<String> getTitle() {
        return title;
    }

    @JsonProperty(SHOW)
    public Optional<Show> getShow() {
        return show;
    }

    @JsonProperty(ACTUATE)
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
