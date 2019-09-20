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
package org.n52.shetland.w3c.xlink;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Reference {

    private Optional<URI> href = Optional.empty();
    private Optional<Type> type = Optional.empty();
    private Optional<String> role = Optional.empty();
    private Optional<String> arcrole = Optional.empty();
    private Optional<String> title = Optional.empty();
    private Optional<Show> show = Optional.empty();
    private Optional<Actuate> actuate = Optional.empty();
    private Optional<String> remoteSchema = Optional.empty();

    public Optional<Type> getType() {
        return type;
    }

    public Reference setType(String type) {
        return setType(Type.fromString(type));
    }

    public Reference setType(Type type) {
        this.type = Optional.ofNullable(type);
        return this;
    }

    public Optional<URI> getHref() {
        return href;
    }

    public Reference setHref(URI href) {
        this.href = Optional.ofNullable(href);
        return this;

    }

    public Optional<String> getRole() {
        return role;
    }

    public Reference setRole(String role) {
        this.role = Optional.ofNullable(Strings.emptyToNull(role));
        return this;
    }

    public Optional<String> getArcrole() {
        return arcrole;
    }

    public Reference setArcrole(String arcrole) {
        this.arcrole = Optional.ofNullable(Strings.emptyToNull(arcrole));
        return this;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Reference setTitle(String title) {
        this.title = Optional.ofNullable(Strings.emptyToNull(title));
        return this;
    }

    public Optional<Show> getShow() {
        return show;
    }

    public Reference setShow(String show) {
        return setShow(Show.fromString(show));
    }

    public Reference setShow(Show show) {
        this.show = Optional.ofNullable(show);
        return this;
    }

    public Optional<Actuate> getActuate() {
        return actuate;
    }

    public Reference setActuate(String actuate) {
        return setActuate(Actuate.fromString(actuate));
    }

    public Reference setActuate(Actuate actuate) {
        this.actuate = Optional.ofNullable(actuate);
        return this;
    }

    public Optional<String> getRemoteSchema() {
        return remoteSchema;
    }

    public Reference setRemoteSchema(String remoteSchema) {
        this.remoteSchema = Optional.ofNullable(Strings.emptyToNull(remoteSchema));
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActuate(), getArcrole(), getHref(),
                            getRemoteSchema(), getRole(), getShow(), getTitle(), getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reference) {
            Reference that = (Reference) obj;
            return Objects.equals(getActuate(), that.getActuate()) &&
                   Objects.equals(getArcrole(), that.getArcrole()) &&
                   Objects.equals(getHref(), that.getHref()) &&
                   Objects.equals(getRemoteSchema(), that.getRemoteSchema()) &&
                   Objects.equals(getRole(), that.getRole()) &&
                   Objects.equals(getShow(), that.getShow()) &&
                   Objects.equals(getTitle(), that.getTitle()) &&
                   Objects.equals(getType(), that.getType());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("actuate", getActuate().orElse(null))
                .add("arcrole", getArcrole().orElse(null))
                .add("href", getHref().orElse(null))
                .add("remoteSchema", getRemoteSchema().orElse(null))
                .add("role", getRole().orElse(null))
                .add("show", getShow().orElse(null))
                .add("title", getTitle().orElse(null))
                .add("type", getType().orElse(null))
                .toString();
    }

}
