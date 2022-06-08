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
package org.n52.shetland.ogc.gml;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

/**
 * Class represents an AbstractGML object
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractGML {

    /**
     * Feature identifier
     */
    private CodeWithAuthority identifier;

    /**
     * Feature identifier as human readable name
     */
    private CodeWithAuthority humanReadableIdentifier;

    /**
     * Original Feature identifier, set if {@link AbstractGML#setHumanReadableIdentifierAsIdentifier()} is
     * called.
     */
    private CodeWithAuthority originalIdentifier;

    /**
     * List of feature names
     */
    private List<CodeType> names = new LinkedList<>();

    /**
     * Feature description
     */
    private String description;

    private List<AbstractMetaData> metaDataProperty = new LinkedList<>();

    /**
     * GML id
     */
    private String gmlId;

    public AbstractGML() {
        this(null, null);
    }

    public AbstractGML(String identifier) {
        this(new CodeWithAuthority(identifier), null);
    }

    public AbstractGML(CodeWithAuthority identifier) {
        this(identifier, null);
    }

    /**
     * constructor
     *
     * @param identifier
     *            identifier
     * @param gmlId
     *            GML id
     */
    public AbstractGML(CodeWithAuthority identifier, String gmlId) {
        this.identifier = identifier;
        this.gmlId = gmlId;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractGML) {
            AbstractGML feature = (AbstractGML) o;
            if (feature.isSetIdentifier() && this.isSetIdentifier() && feature.isSetGmlID() && this.isSetGmlID()) {
                return feature.getIdentifierCodeWithAuthority().equals(this.getIdentifierCodeWithAuthority())
                        && feature.getGmlId().equals(this.getGmlId());
            } else if (feature.isSetIdentifier() && this.isSetIdentifier()) {
                return feature.getIdentifierCodeWithAuthority().equals(this.getIdentifierCodeWithAuthority());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdentifierCodeWithAuthority(), getGmlId());
    }

    /**
     * Get the string identifier of this abstract feature
     *
     * @return Identifier of this abstract feature
     */
    public String getIdentifier() {
        if (isSetIdentifier()) {
            return getIdentifierCodeWithAuthority().getValue();
        }
        return null;
    }

    /**
     * Get {@link CodeWithAuthority} identifier of this abstract feature
     *
     * @return Returns the identifier of this abstract feature .
     */
    public CodeWithAuthority getIdentifierCodeWithAuthority() {
        return identifier;
    }

    /**
     * Set identifier
     *
     * @param identifier
     *            the identifier to set
     *
     * @return this
     */
    public AbstractGML setIdentifier(CodeWithAuthority identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Set identifier
     *
     * @param identifier
     *            the identifier to set
     *
     * @return this
     */
    public AbstractGML setIdentifier(String identifier) {
        setIdentifier(new CodeWithAuthority(identifier));
        return this;
    }

    /**
     * @return {@code true}, if identifier is set and value is not an empty string, else {@code false}
     */
    public boolean isSetIdentifier() {
        return getIdentifierCodeWithAuthority() != null && getIdentifierCodeWithAuthority().isSetValue();
    }

    /**
     * Get the string human readable identifier of this abstract feature
     *
     * @return Human readable identifier of this abstract feature
     */
    public String getHumanReadableIdentifier() {
        return getHumanReadableIdentifierCodeWithAuthority().getValue();
    }

    /**
     * Get {@link CodeWithAuthority} human readable identifier of this abstract feature
     *
     * @return Returns the human readable identifier of this abstract feature .
     */
    public CodeWithAuthority getHumanReadableIdentifierCodeWithAuthority() {
        return humanReadableIdentifier;
    }

    /**
     * Set human readable identifier
     *
     * @param humanReadableIdentifier
     *            the human readable identifier to set
     *
     * @return this
     */
    public AbstractGML setHumanReadableIdentifier(CodeWithAuthority humanReadableIdentifier) {
        this.humanReadableIdentifier = humanReadableIdentifier;
        return this;
    }

    /**
     * Set human readable identifier
     *
     * @param humanReadableIdentifier
     *            the human readable identifier to set
     *
     * @return this
     */
    public AbstractGML setHumanReadableIdentifier(String humanReadableIdentifier) {
        setHumanReadableIdentifier(new CodeWithAuthority(humanReadableIdentifier));
        return this;
    }

    /**
     * @return <tt>true</tt>, if human readable identifier is set and value is not an empty string,<br>
     *         else <tt>false</tt>
     */
    public boolean isSetHumanReadableIdentifier() {
        return getHumanReadableIdentifierCodeWithAuthority() != null
                && getHumanReadableIdentifierCodeWithAuthority().isSetValue();
    }

    /**
     * Set the human readable identifier as identifier and saves the identifier as original identifier
     *
     * @return this
     */
    public AbstractGML setHumanReadableIdentifierAsIdentifier() {
        if (isSetHumanReadableIdentifier()) {
            originalIdentifier = getIdentifierCodeWithAuthority();
            setIdentifier(getHumanReadableIdentifierCodeWithAuthority());
        }
        return this;
    }

    /**
     * @return <code>true</code>, if the original identifier is set
     */
    public boolean isSetOriginalIdentifier() {
        return getOriginalIdentifierCodeWithAuthority() != null
                && getOriginalIdentifierCodeWithAuthority().isSetValue();
    }

    /**
     * Get the original string identifier of this abstract feature
     *
     * @return Original identifier of this abstract feature
     */
    public String getOriginalIdentifier() {
        if (isSetIdentifier()) {
            return getOriginalIdentifierCodeWithAuthority().getValue();
        }
        return null;
    }

    /**
     * Get {@link CodeWithAuthority} original identifier of this abstract feature
     *
     * @return Returns the original identifier of this abstract feature .
     */
    public CodeWithAuthority getOriginalIdentifierCodeWithAuthority() {
        return originalIdentifier;
    }

    /**
     * Get feature names
     *
     * @return Feature names
     */
    public List<CodeType> getName() {
        return Collections.unmodifiableList(names);
    }

    /**
     * Add feature names, clears the name list
     *
     * @param name
     *            Feature names to ad
     *
     * @return {@code this}
     */
    public AbstractGML setName(final Collection<CodeType> name) {
        this.names.clear();
        if (name != null) {
            this.names.addAll(name);
        }
        return this;
    }

    /**
     * Set the name, clears the name list
     *
     * @param name
     *            Name to set
     *
     * @return {@code this}
     */
    public AbstractGML setName(CodeType name) {
        this.names.clear();
        if (name != null) {
            this.names.add(name);
        }
        return this;
    }

    /**
     * Adds the name to the name list
     *
     * @param name
     *            Name to add
     *
     * @return {@code this}
     */
    public AbstractGML addName(final CodeType name) {
        if (name != null && name.isSetValue()) {
            this.names.add(name);
        }
        return this;
    }

    /**
     * Add a feature name
     *
     * @param name
     *            Feature name to add
     *
     * @return {@code this}
     */
    public AbstractGML addName(final String name) {
        if (!Strings.isNullOrEmpty(name)) {
            addName(new CodeType(name));
        }
        return this;
    }

    /**
     * Add a feature name
     *
     * @param name
     *            Feature name to add
     * @param codespace
     *            Codespace of the feature name
     *
     * @return {@code this}
     *
     * @throws java.net.URISyntaxException
     *             if the code space is not a valid URI
     */
    public AbstractGML addName(final String name, final URI codespace) throws URISyntaxException {
        if (!Strings.isNullOrEmpty(name)) {
            addName(new CodeType(name, codespace));
        }
        return this;
    }

    /**
     * Check whether feature has a names
     *
     * @return <code>true</code> if feature has names
     */
    public boolean isSetName() {
        return CollectionHelper.isNotEmpty(names);
    }

    /**
     * Get first feature name or null if feature has no names
     *
     * @return First feature name or null if feature has no names
     */
    public CodeType getFirstName() {
        if (isSetName()) {
            return getName().iterator().next();
        }
        return null;
    }

    /**
     * Get feature description
     *
     * @return Feature description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set feature description
     *
     * @param description
     *            Feature description to set
     *
     * @return this
     */
    public AbstractGML setDescription(final String description) {
        this.description = description;
        return this;
    }

    /**
     * Check whether feature has a description
     *
     * @return <code>true</code> if feature a description
     */
    public boolean isSetDescription() {
        return Strings.emptyToNull(getDescription()) != null;
    }

    /**
     * Get GML id
     *
     * @return GML id
     */
    public String getGmlId() {
        return gmlId == null ? null : gmlId.replaceFirst("#", "");
    }

    /**
     * Set GML id
     *
     * @param gmlId
     *            GML id to set
     */
    public void setGmlId(String gmlId) {
        this.gmlId = gmlId;
    }

    /**
     * Check whether GML id is set
     *
     * @return <code>true</code> if GML id is set
     */
    public boolean isSetGmlID() {
        return !Strings.isNullOrEmpty(gmlId);
    }

    /**
     * Check whether feature is still contained in XML document by sign {@code #}.
     *
     * @return <code>true</code> if feature is still contained in XML document
     */
    public boolean isReferenced() {
        return isSetGmlID() && gmlId.startsWith("#");
    }

    /**
     * @return the metaDataProperty
     */
    public List<AbstractMetaData> getMetaDataProperty() {
        return Collections.unmodifiableList(metaDataProperty);
    }

    /**
     * @param metaDataProperty
     *            the metaDataProperty to set
     * @return this
     */
    public AbstractGML setMetaDataProperty(List<AbstractMetaData> metaDataProperty) {
        this.metaDataProperty.clear();
        if (metaDataProperty != null) {
            this.metaDataProperty.addAll(metaDataProperty);
        }
        return this;
    }

    /**
     * @param metaDataProperty
     *            the metaDataProperty to add
     * @return this
     */
    public AbstractGML addMetaDataProperty(List<AbstractMetaData> metaDataProperty) {
        if (metaDataProperty != null) {
            this.metaDataProperty.addAll(metaDataProperty);
        }
        return this;
    }

    /**
     * @param metaDataProperty
     *            the metaDataProperty to add
     * @return this
     */
    public AbstractGML addMetaDataProperty(AbstractMetaData metaDataProperty) {
        if (metaDataProperty != null) {
            this.metaDataProperty.add(metaDataProperty);
        }
        return this;
    }

    /**
     * @return <code>true</code>, if metaData property is set, else <code>false</code>
     */
    public boolean isSetMetaDataProperty() {
        return CollectionHelper.isNotEmpty(getMetaDataProperty());
    }

    /**
     * Copies values of this {@link AbstractGML} to the committed {@link AbstractGML}
     *
     * @param copyOf
     *            {@link AbstractGML} to set values
     */
    public void copyTo(AbstractGML copyOf) {
        copyOf.setDescription(getDescription());
        copyOf.setGmlId(getGmlId());
        copyOf.setIdentifier(getIdentifierCodeWithAuthority());
        copyOf.setName(getName());
        copyOf.setMetaDataProperty(getMetaDataProperty());
    }
}
