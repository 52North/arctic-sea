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
package org.n52.shetland.ogc.sensorML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.sensorML.elements.AbstractSmlDocumentation;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCharacteristics;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

/**
 * @since 4.0.0
 *
 */
public abstract class AbstractSensorML extends AbstractFeature {

    private List<String> keywords = new ArrayList<>(0);
    private List<SmlIdentifier> identifications = new ArrayList<>(0);
    private List<SmlClassifier> classifications = new ArrayList<>(0);
    private List<SmlCharacteristics> characteristics = new ArrayList<>(0);
    private List<SmlCapabilities> capabilities = new ArrayList<>(0);
    private List<SmlContact> contacts = new ArrayList<>(0);
    private List<AbstractSmlDocumentation> documentations = new ArrayList<>(0);
    private Map<String, AbstractFeature> featuresOfInterestMap = new HashMap<>();
    private Set<String> featuresOfInterest = Sets.newLinkedHashSet();
    private Map<String, AbstractPhenomenon> phenomenonMap = new HashMap<>();
    private ReferenceType parentProcedure;
    private Set<AbstractSensorML> childProcedures = new LinkedHashSet<>();
    private ReferenceType typeOf;
    private String history;
    private Time validTime;

    /**
     * constructor
     *
     * @param identifier
     *                   Feature identifier
     */
    public AbstractSensorML(String identifier) {
        super(identifier);
    }

    /**
     * constructor
     *
     * @param identifier
     *                   Feature identifier
     */
    public AbstractSensorML(CodeWithAuthority identifier) {
        super(identifier);
    }

    /**
     * constructor
     *
     * @param identifier
     *                   Feature identifier
     * @param gmlId
     *                   GML id
     */
    public AbstractSensorML(CodeWithAuthority identifier, String gmlId) {
        super(identifier, gmlId);
    }

    public AbstractSensorML() {
        this("");
    }

    public boolean isSetParentProcedure() {
        return this.parentProcedure != null;
    }

    public boolean isAggragation() {
        return false;
    }

    @Override
    public AbstractSensorML setIdentifier(String identifier) {
        super.setIdentifier(identifier);
        return this;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public AbstractSensorML setKeywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    public AbstractSensorML addKeywords(List<String> keywords) {
        if (isSetKeywords()) {
            this.keywords.addAll(keywords);
        } else {
            this.keywords = keywords;
        }
        return this;
    }

    public List<SmlIdentifier> getIdentifications() {
        return identifications;
    }

    public AbstractSensorML setIdentifications(final List<SmlIdentifier> identifications) {
        if (this.identifications.isEmpty()) {
            this.identifications = identifications;
        } else {
            this.identifications.addAll(identifications);
        }
        return this;
    }

    public Optional<SmlIdentifier> findIdentification(Predicate<SmlIdentifier> predicate) {
        if (isSetIdentifications()) {
            return Iterables.tryFind(getIdentifications(), predicate);
        }
        return Optional.absent();
    }

    public boolean isIdentificationSet(Predicate<SmlIdentifier> predicate) {
        return findIdentification(predicate).isPresent();
    }

    public List<SmlClassifier> getClassifications() {
        return classifications;
    }

    public AbstractSensorML setClassifications(
            final List<SmlClassifier> classifications) {
        this.classifications = classifications;
        return this;
    }

    public AbstractSensorML addClassifications(
            List<SmlClassifier> classifications) {
        if (isSetClassifications()) {
            this.classifications.addAll(classifications);
        }
        return this;
    }

    public Optional<SmlClassifier> findClassifier(Predicate<SmlClassifier> predicate) {
        if (isSetClassifications()) {
            return Iterables.tryFind(this.classifications, predicate);
        } else {
            return Optional.absent();
        }
    }

    public AbstractSensorML addClassification(final SmlClassifier classifier) {
        classifications.add(classifier);
        return this;
    }

    public AbstractSensorML setValidTime(final Time validTime) {
        this.validTime = validTime;
        return this;
    }

    public List<SmlCharacteristics> getCharacteristics() {
        return characteristics;
    }

    public AbstractSensorML setCharacteristics(final List<SmlCharacteristics> characteristics) {
        if (isSetCharacteristics()) {
            this.characteristics.addAll(characteristics);
        } else {
            this.characteristics = characteristics;
        }
        return this;
    }

    public Optional<SmlCharacteristics> findCharacteristics(Predicate<SmlCharacteristics> predicate) {
        if (isSetCharacteristics()) {
            return Iterables.tryFind(this.characteristics, predicate);
        } else {
            return Optional.absent();
        }
    }

    public AbstractSensorML addCharacteristic(final SmlCharacteristics characteristic) {
        characteristics.add(characteristic);
        return this;
    }

    public List<SmlCapabilities> getCapabilities() {
        return capabilities;
    }

    public AbstractSensorML addCapabilities(final List<SmlCapabilities> capabilities) {
        if (capabilities != null) {
            this.capabilities.addAll(capabilities);
        }
        return this;
    }

    public Optional<SmlCapabilities> findCapabilities(Predicate<SmlCapabilities> predicate) {
        if (this.capabilities != null) {
            return Iterables.tryFind(this.capabilities, predicate);
        } else {
            return Optional.absent();
        }
    }

    public void removeCapabilities(SmlCapabilities caps) {
        if (this.capabilities != null) {
            this.capabilities.remove(caps);
        }
    }

    public AbstractSensorML addCapabilities(final SmlCapabilities capabilities) {
        return addCapabilities(Collections.singletonList(capabilities));
    }

    public List<SmlContact> getContact() {
        return contacts;
    }

    public AbstractSensorML setContact(List<SmlContact> contacts) {
        if (isSetContact()) {
            this.contacts.addAll(contacts);
        } else {
            this.contacts = contacts;
        }
        return this;
    }

    public AbstractSensorML addContact(final SmlContact contact) {
        if (this.contacts == null) {
            this.contacts = new LinkedList<>();
        }
        this.contacts.add(contact);
        return this;
    }

    /**
     * Get {@link SmlContact} for a specific role
     *
     * @param contactRole
     *                    Role to get {@link SmlContact} for
     *
     * @return The {@link SmlContact} or null if not defined
     */
    public SmlContact getContact(String contactRole) {
        if (isSetContact()) {
            return getContact(getContact(), contactRole);
        }
        return null;
    }

    private SmlContact getContact(List<SmlContact> contacts, String contactRole) {
        for (SmlContact contact : contacts) {
            if (contact instanceof SmlContactList) {
                SmlContact cont = getContact(((SmlContactList) contact).getMembers(), contactRole);
                if (cont != null) {
                    return cont;
                }
            } else if (contact.getRole() != null && contact.getRole().equals(contactRole) &&
                       contact instanceof SmlResponsibleParty) {
                return (SmlResponsibleParty) contact;
            }
        }
        return null;
    }

    public List<AbstractSmlDocumentation> getDocumentation() {
        return documentations;
    }

    public AbstractSensorML setDocumentation(final List<AbstractSmlDocumentation> documentations) {
        this.documentations.addAll(documentations);
        return this;
    }

    public AbstractSensorML addDocumentation(final AbstractSmlDocumentation documentation) {
        documentations.add(documentation);
        return this;
    }

    public String getHistory() {
        return history;
    }

    public AbstractSensorML setHistory(final String history) {
        this.history = history;
        return this;
    }

    public AbstractSensorML addIdentifier(final SmlIdentifier identifier) {
        identifications.add(identifier);
        return this;
    }

    public boolean isSetKeywords() {
        return keywords != null && !keywords.isEmpty();
    }

    public boolean isSetIdentifications() {
        return identifications != null && !identifications.isEmpty();
    }

    public boolean isSetClassifications() {
        return classifications != null && !classifications.isEmpty();
    }

    public boolean isSetCharacteristics() {
        return characteristics != null && !characteristics.isEmpty();
    }

    public boolean isSetCapabilities() {
        return capabilities != null && !capabilities.isEmpty();
    }

    public boolean isSetDocumentation() {
        return documentations != null && !documentations.isEmpty();
    }

    public boolean isSetContact() {
        return contacts != null && !contacts.isEmpty();
    }

    public boolean isSetHistory() {
        return history != null && !history.isEmpty();
    }

    public void copyTo(AbstractSensorML copyOf) {
        super.copyTo(copyOf);
        copyOf.setCharacteristics(getCharacteristics());
        copyOf.setClassifications(getClassifications());
        copyOf.setContact(getContact());
        copyOf.setDocumentation(getDocumentation());
        copyOf.setHistory(getHistory());
        copyOf.setIdentifications(getIdentifications());
        copyOf.setKeywords(getKeywords());
    }

    public Time getValidTime() {
        return validTime;
    }

    public ReferenceType getParentProcedure() {
        return parentProcedure;
    }

    public void setParentProcedure(ReferenceType parentProcedure) {
        this.parentProcedure = parentProcedure;
    }

    public void addChildProcedure(AbstractSensorML process) {
        this.childProcedures.add(process);
    }

    public Set<AbstractSensorML> getChildProcedures() {
        return childProcedures;
    }

    public void setChildProcedures(Set<AbstractSensorML> childProcedures) {
        this.childProcedures = childProcedures;
    }

    public ReferenceType getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(ReferenceType typeOf) {
        this.typeOf = typeOf;
    }

    public Map<String, AbstractFeature> getFeaturesOfInterestMap() {
        return featuresOfInterestMap;
    }

    public void setFeaturesOfInterestMap(Map<String, AbstractFeature> featuresOfInterestMap) {
        this.featuresOfInterestMap = featuresOfInterestMap;
    }

    public void addFeaturesOfInterest(Map<String, AbstractFeature> featureOfInterest) {
        featureOfInterest.forEach(this.featuresOfInterestMap::put);
    }

    public void addFeaturesOfInterest(Set<String> featureOfInterest) {
        featureOfInterest.forEach(this.featuresOfInterest::add);
    }

    public void addFeatureOfInterest(AbstractFeature featureOfInterest) {
        this.featuresOfInterestMap.put(featureOfInterest.getIdentifier(), featureOfInterest);
    }

    public void addFeatureOfInterest(String featureOfInterest) {
        this.featuresOfInterest.add(featureOfInterest);
    }

    public boolean isSetFeaturesOfInterestMap() {
        return this.featuresOfInterestMap != null && !this.featuresOfInterestMap.isEmpty();
    }

    public boolean isSetFeaturesOfInterest() {
        return this.featuresOfInterest != null && !this.featuresOfInterest.isEmpty();
    }

    public void setFeaturesOfInterest(Set<String> featuresOfInterest) {
        this.featuresOfInterest = featuresOfInterest;
    }

    public Set<String> getFeaturesOfInterest() {
        return featuresOfInterest;
    }

    public boolean isSetFeatures() {
        return isSetFeaturesOfInterest() || isSetFeaturesOfInterestMap();
    }
}
