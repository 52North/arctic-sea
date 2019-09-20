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
package org.n52.shetland.ogc.sos;

import static java.util.stream.Collectors.toSet;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.n52.janmayen.Comparables;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.swe.simpleType.SweAbstractSimpleType;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/**
 * class represents an offering in the SOS database
 *
 * @since 1.0.0
 */
public class SosOffering
        extends AbstractFeature
        implements Comparable<SosOffering> {
    private static final String OFFERING_NAME_PREFIX = "Offering for sensor ";
    /**
     * flag to identify offering as offering from a parent procedure, default =
     * false.
     */
    private boolean parentOffering;

    /**
     * constructor
     *
     * @param identifier
     *            offering identifier
     * @param name
     *            offering name
     */
    public SosOffering(final String identifier, final String name) {
        super(identifier);
        if (Strings.isNullOrEmpty(name)) {
            setName(new CodeType(OFFERING_NAME_PREFIX + identifier));
        } else {
            this.setName(new CodeType(name));
        }
    }

    /**
     * constructor
     *
     * @param identifier
     *            offering identifier
     * @param generateName
     *            Indicator whether the name should be generated.
     */
    public SosOffering(final String identifier, boolean generateName) {
        super(identifier);
        if (generateName) {
            setName(new CodeType(OFFERING_NAME_PREFIX + identifier));
        }
    }

    /**
     * constructor
     *
     * @param identifier
     *            offering identifier
     * @param name
     *            offering name
     */
    public SosOffering(final String identifier, final CodeType name) {
        super(identifier);
        if (name != null && !name.isSetValue()) {
            name.setValue(OFFERING_NAME_PREFIX + identifier);
        }
        this.setName(name);
    }

    /**
     * constructor with procedure identifier
     *
     * @param procedureIdentifier
     *            Procedure identifier
     */
    public SosOffering(String procedureIdentifier) {
        super(procedureIdentifier + "/observations");
        setName(new CodeType(OFFERING_NAME_PREFIX + procedureIdentifier));
    }

    /**
     * Get offering name
     *
     * @return Returns the name.
     */
    public String getOfferingName() {
        return getFirstName().getValue();
    }

    /**
     * Set if offering is from parent procedure or not
     *
     * @param parentOfferingFlag
     *            Offering is from parent procedure or not
     */
    public void setParentOfferingFlag(boolean parentOfferingFlag) {
        this.parentOffering = parentOfferingFlag;
    }

    /**
     *
     * @return offering is from parent procedure or not
     */
    public boolean isParentOffering() {
        return parentOffering;
    }

    @Override
    public int compareTo(SosOffering o) {
        Objects.requireNonNull(o);
        return Comparables.compare(getIdentifier(), o.getIdentifier());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("identifier", getIdentifier()).add("name", getName())
                .add("description", getDescription()).add("parentOfferingFlag", isParentOffering()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SosOffering) {
            SosOffering other = (SosOffering) o;
            return Objects.equals(getIdentifier(), other.getIdentifier()) && Objects.equals(getName(), other.getName())
                    && Objects.equals(isParentOffering(), other.isParentOffering());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getName(), isParentOffering());
    }

    /**
     * Creates a set of {@literal SosOffering}s from a map containing
     * identifiers as keys and names as values.
     *
     * @param map
     *            the map (may be {@literal null})
     *
     * @return the set (never {@literal null})
     */
    public static Set<SosOffering> fromMap(Map<String, String> map) {
        return Optional.ofNullable(map).map(Map::entrySet).map(Set::stream).orElseGet(Stream::empty)
                .map(e -> new SosOffering(e.getKey(), e.getValue())).collect(toSet());
    }

    /**
     * Creates a set of {@literal SosOffering}s from a set containing
     * identifiers as keys and names as values.
     *
     * @param set
     *            the set (may be {@literal null})
     *
     * @return the set (never {@literal null})
     */
    public static Set<SosOffering> fromSet(Set<SweAbstractSimpleType<?>> set) {
        return Optional.ofNullable(set).map(Set::stream).orElseGet(Stream::empty).map(SosOffering::from)
                .collect(toSet());
    }

    /**
     * Creates a set of {@literal SosOffering}s from SWE simple type.
     *
     * @param type
     *            the type (may be {@literal null})
     *
     * @return the set (never {@literal null})
     */
    public static SosOffering from(SweAbstractSimpleType<?> type) {
        if (type == null) {
            return null;
        }
        String identifer = type.getValue().toString();
        CodeType name = type.getName();
        SosOffering offering = new SosOffering(identifer, name);
        if (type.isSetDescription()) {
            offering.setDescription(type.getDescription());
        }
        return offering;
    }

}
