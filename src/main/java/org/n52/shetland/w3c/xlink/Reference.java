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

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Reference {

    private Optional<URI> href = Optional.absent();
    private Optional<String> type = Optional.absent();
    private Optional<String> role = Optional.absent();
    private Optional<String> arcrole = Optional.absent();
    private Optional<String> title = Optional.absent();
    private Optional<String> show = Optional.absent();
    private Optional<String> actuate = Optional.absent();
    private Optional<String> remoteSchema = Optional.absent();

    public Optional<String> getType() {
        return type;
    }

    public Reference setType(String type) {
        this.type = Optional.fromNullable(Strings.emptyToNull(type));
        return this;
    }

    public Optional<URI> getHref() {
        return href;
    }

    public Reference setHref(URI href) {
        this.href = Optional.fromNullable(href);
        return this;

    }

    public Optional<String> getRole() {
        return role;
    }

    public Reference setRole(String role) {
        this.role = Optional.fromNullable(Strings.emptyToNull(role));
        return this;
    }

    public Optional<String> getArcrole() {
        return arcrole;
    }

    public Reference setArcrole(String arcrole) {
        this.arcrole = Optional.fromNullable(Strings.emptyToNull(arcrole));
        return this;
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Reference setTitle(String title) {
        this.title = Optional.fromNullable(Strings.emptyToNull(title));
        return this;
    }

    public Optional<String> getShow() {
        return show;
    }

    public Reference setShow(String show) {
        this.show = Optional.fromNullable(Strings.emptyToNull(show));
        return this;
    }

    public Optional<String> getActuate() {
        return actuate;
    }

    public Reference setActuate(String actuate) {
        this.actuate = Optional.fromNullable(Strings.emptyToNull(actuate));
        return this;
    }

    public Optional<String> getRemoteSchema() {
        return remoteSchema;
    }

    public Reference setRemoteSchema(String remoteSchema) {
        this.remoteSchema = Optional.fromNullable(Strings.emptyToNull(remoteSchema));
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getActuate(), getArcrole(), getHref(),
            getRemoteSchema(), getRole(), getShow(), getTitle(), getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Reference) {
            Reference that = (Reference) obj;
            return Objects.equal(getActuate(), that.getActuate())&&
                   Objects.equal(getArcrole(), that.getArcrole()) &&
                   Objects.equal(getHref(), that.getHref()) &&
                   Objects.equal(getRemoteSchema(), that.getRemoteSchema()) &&
                   Objects.equal(getRole(), that.getRole()) &&
                   Objects.equal(getShow(), that.getShow()) &&
                   Objects.equal(getTitle(), that.getTitle()) &&
                   Objects.equal(getType(), that.getType());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("actuate", getActuate().orNull())
                .add("arcrole", getArcrole().orNull())
                .add("href", getHref().orNull())
                .add("remoteSchema", getRemoteSchema().orNull())
                .add("role", getRole().orNull())
                .add("show", getShow().orNull())
                .add("title", getTitle().orNull())
                .add("type", getType().orNull())
                .toString();
    }



}
