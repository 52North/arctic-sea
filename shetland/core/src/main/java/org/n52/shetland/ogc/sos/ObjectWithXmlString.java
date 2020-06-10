/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nullable;

import org.n52.janmayen.Optionals;

import com.google.common.base.Strings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ObjectWithXmlString<T> {

    private Optional<T> object;
    private Optional<String> xml;

    public ObjectWithXmlString(@Nullable T object) {
        this(object, null);
    }

    public ObjectWithXmlString(@Nullable String xml) {
        this(null, xml);
    }

    @SuppressFBWarnings(value = "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE")
    public ObjectWithXmlString(@Nullable T object, @Nullable String xml) {
        this.object = Optional.ofNullable(object);
        this.xml = Optional.ofNullable(Strings.emptyToNull(xml));
        if (!Optionals.any(this.object, this.xml)) {
            throw new NullPointerException();
        }
    }

    public Optional<T> get() {
        return object;
    }

    public void set(T object) {
        this.object = Optional.of(object);
    }

    public Optional<String> getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = Optional.of(Strings.emptyToNull(xml));
    }

    public boolean isDecoded() {
        return this.object.isPresent();
    }

    public boolean isEncoded() {
        return this.xml.isPresent();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.object, this.xml);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ObjectWithXmlString<?> other = (ObjectWithXmlString<?>) obj;
        return ((this.isDecoded() && other.isDecoded()) || (this.isEncoded() && other.isEncoded()))
                && (this.isDecoded() && other.isDecoded() ? Objects.equals(this.object, other.object) : true)
                && (this.isEncoded() && other.isEncoded() ? Objects.equals(this.xml, other.xml) : true);
    }
    
   
}
