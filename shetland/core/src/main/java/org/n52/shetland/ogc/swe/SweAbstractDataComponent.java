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
package org.n52.shetland.ogc.swe;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n52.janmayen.Copyable;
import org.n52.shetland.ogc.HasDefaultEncoding;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * @since 1.0.0
 *
 */
public abstract class SweAbstractDataComponent
        implements HasDefaultEncoding<SweAbstractDataComponent>, Copyable<SweAbstractDataComponent> {

    private String definition;

    /**
     * optional: swe:description[0..1]
     */
    private String description;

    /**
     * optional: swe:label [0..1]
     */
    private String label;

    /**
     * optional: gml:name [0..*] (SweCommon 1.0.1)
     */
    private final List<CodeType> names = Lists.newArrayList();

    /**
     * optional: swe:identifier [0..1]
     */
    private String identifier;

    /**
     * pre-set XML representation
     */
    private String xml;

    private String defaultEncoding = SweConstants.NS_SWE_20;

    public String getDefinition() {
        return definition;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        if (label != null && !label.isEmpty()) {
            return label;
        } else if (isSetNames()) {
            return getName().getValue();
        }
        return null;
    }

    public CodeType getName() {
        if (isSetNames()) {
            return getNames().iterator().next();
        } else if (label != null && !label.isEmpty()) {
            return new CodeType(getLabel());
        }
        return null;
    }

    public List<CodeType> getNames() {
        return Collections.unmodifiableList(names);
    }

    public String getIdentifier() {
        return identifier;
    }

    public SweAbstractDataComponent setDefinition(final String definition) {
        this.definition = definition;
        return this;
    }

    public SweAbstractDataComponent setDescription(final String description) {
        this.description = description;
        return this;
    }

    public SweAbstractDataComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    public SweAbstractDataComponent addName(final String name) {
        this.names.add(new CodeType(name));
        return this;
    }

    public SweAbstractDataComponent addName(final CodeType name) {
        this.names.add(name);
        return this;
    }

    public SweAbstractDataComponent addName(final Collection<CodeType> names) {
        this.names.addAll(names);
        return this;
    }

    public SweAbstractDataComponent setName(final String name) {
        names.clear();
        this.names.add(new CodeType(name));
        return this;
    }

    public SweAbstractDataComponent setName(final CodeType name) {
        this.names.clear();
        this.names.add(name);
        return this;
    }

    public SweAbstractDataComponent setName(final Collection<CodeType> names) {
        this.names.clear();
        this.names.addAll(names);
        return this;
    }

    public SweAbstractDataComponent setIdentifier(final String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String getXml() {
        return xml;
    }

    public SweAbstractDataComponent setXml(final String xml) {
        this.xml = xml;
        return this;
    }

    public boolean isSetDefinition() {
        return definition != null && !definition.isEmpty();
    }

    public boolean isSetDescription() {
        return description != null && !description.isEmpty();
    }

    public boolean isSetLabel() {
        return getLabel() != null && !getLabel().isEmpty();
    }

    public boolean isSetName() {
        return getName() != null && getName().isSetValue();
    }

    public boolean isSetNames() {
        return CollectionHelper.isNotEmpty(getNames());
    }

    public boolean isSetIdentifier() {
        return identifier != null && !identifier.isEmpty();
    }

    public boolean isSetXml() {
        return xml != null && !xml.isEmpty();
    }

    @Override
    public String getDefaultElementEncoding() {
        return defaultEncoding;
    }

    @Override
    public SweAbstractDataComponent setDefaultElementEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(31, 7, getDefinition(), getDescription(), getIdentifier());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SweAbstractDataComponent other = (SweAbstractDataComponent) obj;
        if ((getDefinition() == null) ? (other.getDefinition() != null)
                : !getDefinition().equals(other.getDefinition())) {
            return false;
        }
        if ((getDescription() == null) ? (other.getDescription() != null)
                : !getDescription().equals(other.getDescription())) {
            return false;
        }
        if ((getIdentifier() == null) ? (other.getIdentifier() != null)
                : !getIdentifier().equals(other.getIdentifier())) {
            return false;
        }
        return true;
    }

    public abstract SweDataComponentType getDataComponentType();

    public abstract <
            T,
            X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X;

    public abstract <
            X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X;

    /**
     * Copies all values from this {@link SweAbstractDataComponent} to the passed
     *
     * @param copy
     *            {@link SweAbstractDataComponent} to copy values to
     *
     * @return Copy of this
     */
    public SweAbstractDataComponent copyValueTo(SweAbstractDataComponent copy) {
        copy.setDefinition(definition);
        copy.setDescription(description);
        copy.setIdentifier(identifier);
        copy.setLabel(label);
        copy.setName(names);
        return copy;
    }

}
