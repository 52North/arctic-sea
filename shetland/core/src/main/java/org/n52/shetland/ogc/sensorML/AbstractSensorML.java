/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import org.n52.shetland.ogc.PhenomenonNameDescriptionProvider;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.sensorML.elements.AbstractSmlDocumentation;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCapability;
import org.n52.shetland.ogc.sensorML.elements.SmlCharacteristics;
import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifier;
import org.n52.shetland.ogc.sensorML.elements.SmlIdentifierPredicates;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;

import com.google.common.collect.Sets;

/**
 * @since 1.0.0
 *
 */
public abstract class AbstractSensorML
        extends AbstractFeature
        implements PhenomenonNameDescriptionProvider {

    private List<String> keywords = new ArrayList<>(0);
    private List<SmlIdentifier> identifications = new ArrayList<>(0);
    private List<SmlClassifier> classifications = new ArrayList<>(0);
    private List<SmlCharacteristics> characteristics = new ArrayList<>(0);
    private List<SmlCapabilities> capabilities = new ArrayList<>(0);
    private List<SmlContact> contacts = new ArrayList<>(0);
    private List<AbstractSmlDocumentation> documentations = new ArrayList<>(0);
    // private Map<String, AbstractFeature> featuresOfInterestMap = new
    // HashMap<>();
    // private Set<String> featuresOfInterest = Sets.newLinkedHashSet();
    // private Map<String, AbstractPhenomenon> phenomenonMap = new HashMap<>();
    // private ReferenceType parentProcedure;
    // private Set<AbstractSensorML> childProcedures = new LinkedHashSet<>();
    private String history;

    /**
     * constructor
     *
     * @param identifier
     *            Feature identifier
     */
    public AbstractSensorML(String identifier) {
        super(identifier);
    }

    /**
     * constructor
     *
     * @param identifier
     *            Feature identifier
     */
    public AbstractSensorML(CodeWithAuthority identifier) {
        super(identifier);
    }

    /**
     * constructor
     *
     * @param identifier
     *            Feature identifier
     * @param gmlId
     *            GML id
     */
    public AbstractSensorML(CodeWithAuthority identifier, String gmlId) {
        super(identifier, gmlId);
    }

    public AbstractSensorML() {
        this("");
    }

    public boolean isAggragation() {
        return false;
    }

    @Override
    public AbstractSensorML setIdentifier(String identifier) {
        super.setIdentifier(identifier);
        return this;
    }

    protected Predicate<SmlIdentifier> createSmlIdentifierPredicate(String name) {
        return createSmlIdentifierPredicate(name, name);
    }

    protected Predicate<SmlIdentifier> createSmlIdentifierPredicate(String name, String definition) {
        return SmlIdentifierPredicates.nameOrDefinition(name, definition);
    }

    private boolean isSetShortName() {
        return isIdentificationSet(createSmlIdentifierPredicate(SensorMLConstants.ELEMENT_NAME_SHORT_NAME,
                SensorMLConstants.ELEMENT_NAME_SHORT_DEFINITION));
    }

    private String getShortName() {
        if (isSetShortName()) {
            return findIdentification(createSmlIdentifierPredicate(SensorMLConstants.ELEMENT_NAME_SHORT_NAME,
                    SensorMLConstants.ELEMENT_NAME_SHORT_DEFINITION)).get().getValue();
        }
        return null;
    }

    @Override
    public boolean isSetName() {
        return super.isSetName() || isSetShortName();
    }

    @Override
    public CodeType getFirstName() {
        return isSetName() ? super.isSetName() ? super.getFirstName() : new CodeType(getShortName()) : null;
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
            return getIdentifications().stream().filter(predicate).findFirst();
        }
        return Optional.empty();
    }

    public boolean isIdentificationSet(Predicate<SmlIdentifier> predicate) {
        return findIdentification(predicate).isPresent();
    }

    public List<SmlClassifier> getClassifications() {
        return classifications;
    }

    public AbstractSensorML setClassifications(final List<SmlClassifier> classifications) {
        this.classifications = classifications;
        return this;
    }

    public AbstractSensorML addClassifications(List<SmlClassifier> classifications) {
        if (isSetClassifications()) {
            this.classifications.addAll(classifications);
        }
        return this;
    }

    public Optional<SmlClassifier> findClassifier(Predicate<SmlClassifier> predicate) {
        if (isSetClassifications()) {
            return getClassifications().stream().filter(predicate).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public AbstractSensorML addClassification(final SmlClassifier classifier) {
        classifications.add(classifier);
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
            return getCharacteristics().stream().filter(predicate).findFirst();
        } else {
            return Optional.empty();
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

    public AbstractSensorML addCapabilities(final SmlCapabilities capabilities) {
        return addCapabilities(Collections.singletonList(capabilities));
    }

    public Optional<SmlCapabilities> findCapabilities(Predicate<SmlCapabilities> predicate) {
        if (this.capabilities != null) {
            return getCapabilities().stream().filter(predicate).findFirst();
        } else {
            return Optional.empty();
        }
    }

    public void removeCapabilities(SmlCapabilities caps) {
        if (this.capabilities != null) {
            this.capabilities.remove(caps);
        }
    }

    public List<SmlContact> getContact() {
        return contacts;
    }

    /**
     * Get {@link SmlContact} for a specific role
     *
     * @param contactRole
     *            Role to get {@link SmlContact} for
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
            } else if (contact.getRole() != null && contact.getRole().equals(contactRole)
                    && contact instanceof SmlResponsibleParty) {
                return (SmlResponsibleParty) contact;
            }
        }
        return null;
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

    public boolean isSetMobile() {
        return getSweBooleanFromCapabilitiesFor(
                Sets.newHashSet(SensorMLConstants.STATIONARY, SensorMLConstants.MOBILE)) == null ? false : true;
    }

    public boolean getMobile() {
        SweBoolean sweBoolean = getSweBooleanFromCapabilitiesFor(
                Sets.newHashSet(SensorMLConstants.STATIONARY, SensorMLConstants.MOBILE, SensorMLConstants.FIXED));
        if (SensorMLConstants.MOBILE.equalsIgnoreCase(sweBoolean.getDefinition())) {
            return sweBoolean.getValue();
        } else if (SensorMLConstants.STATIONARY.equalsIgnoreCase(sweBoolean.getDefinition())) {
            return !sweBoolean.getValue();
        }
        return false;
    }

    public boolean isSetInsitu() {
        return getSweBooleanFromCapabilitiesFor(
                Sets.newHashSet(Sets.newHashSet(SensorMLConstants.INSITU, SensorMLConstants.REMOTE))) == null ? false
                        : true;
    }

    public boolean getInsitu() {
        SweBoolean sweBoolean = getSweBooleanFromCapabilitiesFor(
                Sets.newHashSet(Sets.newHashSet(SensorMLConstants.INSITU, SensorMLConstants.REMOTE)));
        if (SensorMLConstants.INSITU.equalsIgnoreCase(sweBoolean.getDefinition())) {
            return sweBoolean.getValue();
        } else if (SensorMLConstants.REMOTE.equalsIgnoreCase(sweBoolean.getDefinition())) {
            return !sweBoolean.getValue();
        }
        return true;
    }

    private SweBoolean getSweBooleanFromCapabilitiesFor(Collection<String> definitions) {
        if (this instanceof SensorML && ((SensorML) this).isWrapper()) {
            for (AbstractProcess absProcess : ((SensorML) this).getMembers()) {
                return getSweBooleanFromCapabilitiesFor(absProcess, definitions);
            }
        } else {
            return getSweBooleanFromCapabilitiesFor(this, definitions);
        }
        return null;
    }

    private SweBoolean getSweBooleanFromCapabilitiesFor(AbstractSensorML sml, Collection<String> definitions) {
        if (sml.isSetCapabilities()) {
            for (SmlCapabilities caps : sml.getCapabilities()) {
                for (SmlCapability cap : caps.getCapabilities()) {
                    if (cap.getAbstractDataComponent() instanceof SweDataRecord) {
                        for (SweField field : ((SweDataRecord) cap.getAbstractDataComponent()).getFields()) {
                            if (field.getElement() instanceof SweBoolean) {
                                if (field.getElement().isSetDefinition() && definitions
                                        .contains(field.getElement().getDefinition().toLowerCase(Locale.ROOT))) {
                                    return (SweBoolean) field.getElement();
                                } else if (cap.isSetName()
                                        && definitions.contains(cap.getName().toLowerCase(Locale.ROOT))) {
                                    return (SweBoolean) field.getElement();
                                }
                            }
                        }
                    } else if (cap.getAbstractDataComponent() instanceof SweBoolean) {
                        if (cap.getAbstractDataComponent().isSetDefinition() && definitions
                                .contains(cap.getAbstractDataComponent().getDefinition().toLowerCase(Locale.ROOT))) {
                            return (SweBoolean) cap.getAbstractDataComponent();
                        } else if (cap.isSetName() && definitions.contains(cap.getName().toLowerCase(Locale.ROOT))) {
                            return (SweBoolean) cap.getAbstractDataComponent();
                        }
                    }
                }
            }
        }
        return null;
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
    // public ReferenceType getParentProcedure() {
    // return parentProcedure;
    // }
    //
    // public void setParentProcedure(ReferenceType parentProcedure) {
    // this.parentProcedure = parentProcedure;
    // }
    //
    // public boolean isSetParentProcedure() {
    // return this.parentProcedure != null;
    // }
    //
    // public void addChildProcedure(AbstractSensorML process) {
    // this.childProcedures.add(process);
    // }
    //
    // public Set<AbstractSensorML> getChildProcedures() {
    // return childProcedures;
    // }
    //
    // public void setChildProcedures(Set<AbstractSensorML> childProcedures) {
    // this.childProcedures = childProcedures;
    // }
    //
    // public Map<String, AbstractFeature> getFeaturesOfInterestMap() {
    // return featuresOfInterestMap;
    // }
    //
    // public void setFeaturesOfInterestMap(Map<String, AbstractFeature>
    // featuresOfInterestMap) {
    // this.featuresOfInterestMap = featuresOfInterestMap;
    // }
    //
    // public void addFeaturesOfInterest(Map<String, AbstractFeature>
    // featureOfInterest) {
    // featureOfInterest.forEach(this.featuresOfInterestMap::put);
    // }
    //
    // public void addFeaturesOfInterest(Set<String> featureOfInterest) {
    // featureOfInterest.forEach(this.featuresOfInterest::add);
    // }
    //
    // public void addFeatureOfInterest(AbstractFeature featureOfInterest) {
    // this.featuresOfInterestMap.put(featureOfInterest.getIdentifier(),
    // featureOfInterest);
    // }
    //
    // public void addFeatureOfInterest(String featureOfInterest) {
    // this.featuresOfInterest.add(featureOfInterest);
    // }
    //
    // public boolean isSetFeaturesOfInterestMap() {
    // return this.featuresOfInterestMap != null &&
    // !this.featuresOfInterestMap.isEmpty();
    // }
    //
    // public boolean isSetFeaturesOfInterest() {
    // return this.featuresOfInterest != null &&
    // !this.featuresOfInterest.isEmpty();
    // }
    //
    // public void setFeaturesOfInterest(Set<String> featuresOfInterest) {
    // this.featuresOfInterest = featuresOfInterest;
    // }
    //
    // public Set<String> getFeaturesOfInterest() {
    // return featuresOfInterest;
    // }
    //
    // public boolean isSetFeatures() {
    // return isSetFeaturesOfInterest() || isSetFeaturesOfInterestMap();
    // }
}
