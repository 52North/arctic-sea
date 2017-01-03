/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;

import com.google.common.collect.Sets;

/**
 * @since 4.0.0
 *
 */
public class SosProcedureDescription<T extends AbstractFeature> extends AbstractFeature {

    private final T description;
    private Time validTime;
    private String descriptionFormat;

    private final Set<SosOffering> offerings = Sets.newLinkedHashSet();
    private Map<String, AbstractFeature> featuresOfInterestMap = new HashMap<>();
    private Set<String> featuresOfInterest = Sets.newLinkedHashSet();
    private Map<String, AbstractPhenomenon> phenomenonMap = new HashMap<>();
    private ReferenceType parentProcedure;
    private Set<AbstractSensorML> childProcedures = new LinkedHashSet<>();
    private boolean insitu = true;
    private boolean mobile = false;
    private ReferenceType typeOf;
    private boolean aggregation = false;

    public SosProcedureDescription(T description) {
        super(description.getIdentifier());
        this.description = description;
    }

    public T getProcedureDescription() {
        return this.description;
    }

    public Time getValidTime() {
        return validTime;
    }

    public SosProcedureDescription<T> setValidTime(Time validTime) {
        this.validTime = validTime;
        return this;
    }

    public boolean isSetValidTime() {
        return this.validTime != null && !this.validTime.isEmpty();
    }

    public String getDescriptionFormat() {
        return descriptionFormat;
    }

    public SosProcedureDescription<T> setDescriptionFormat(String descriptionFormat) {
        this.descriptionFormat = descriptionFormat;
        return this;
    }

    public Set<SosOffering> getOfferings() {
        return offerings;
    }

    public SosProcedureDescription<T> addOfferings(Collection<SosOffering> offerings) {
        this.offerings.addAll(offerings);
        return this;
    }

    public SosProcedureDescription<T> addOffering(SosOffering offering) {
        if (offering != null) {
            this.offerings.add(offering);
        }
        return this;
    }

    public boolean isSetOfferings() {
        return offerings != null && !offerings.isEmpty();
    }

    public ReferenceType getParentProcedure() {
        return parentProcedure;
    }

    @Deprecated
    public Collection<String> getParentProcedures() {
        if (isSetParentProcedure()) {
            return Sets.newHashSet(getParentProcedure().getHref());
        }
        return Sets.newHashSet();
    }

    public SosProcedureDescription<T> setParentProcedure(ReferenceType parentProcedure) {
        this.parentProcedure = parentProcedure;
        return this;
    }

    public boolean isSetParentProcedure() {
        return this.parentProcedure != null;
    }

    public SosProcedureDescription<T> addChildProcedure(AbstractSensorML process) {
        if (process != null) {
            this.childProcedures.add(process);
        }
        return this;
    }

    public Set<AbstractSensorML> getChildProcedures() {
        return childProcedures;
    }

    public SosProcedureDescription<T> setChildProcedures(Collection<AbstractSensorML> childProcedures) {
        this.childProcedures.clear();
        addChildProcedures(childProcedures);
        return this;
    }

    public SosProcedureDescription<T> addChildProcedures(Collection<AbstractSensorML> childProcedures) {
        if (childProcedures != null) {
            childProcedures.forEach(this.childProcedures::add);
        }
        return this;
    }

    public boolean isSetChildProcedures() {
        return this.childProcedures != null && !childProcedures.isEmpty();
    }

    public Map<String, AbstractFeature> getFeaturesOfInterestMap() {
        return featuresOfInterestMap;
    }

    public SosProcedureDescription<T> setFeaturesOfInterestMap(Map<String, AbstractFeature> featuresOfInterestMap) {
        this.featuresOfInterestMap.clear();
        addFeaturesOfInterestMap(featuresOfInterestMap);
        return this;
    }

    public SosProcedureDescription<T> addFeaturesOfInterestMap(Map<String, AbstractFeature> featureOfInterest) {
        featureOfInterest.forEach(this.featuresOfInterestMap::put);
        return this;
    }

    public SosProcedureDescription<T> addFeaturesOfInterest(Collection<String> featureOfInterest) {
        featureOfInterest.forEach(this.featuresOfInterest::add);
        return this;
    }

    public SosProcedureDescription<T> addFeatureOfInterest(AbstractFeature featureOfInterest) {
        this.featuresOfInterestMap.put(featureOfInterest.getIdentifier(), featureOfInterest);
        return this;
    }

    public SosProcedureDescription<T> addFeatureOfInterest(String featureOfInterest) {
        this.featuresOfInterest.add(featureOfInterest);
        return this;
    }

    public boolean isSetFeaturesOfInterestMap() {
        return this.featuresOfInterestMap != null && !this.featuresOfInterestMap.isEmpty();
    }

    public boolean isSetFeaturesOfInterest() {
        return this.featuresOfInterest != null && !this.featuresOfInterest.isEmpty();
    }

    public SosProcedureDescription<T> setFeaturesOfInterest(Collection<String> featuresOfInterest) {
        this.featuresOfInterest.clear();
        addFeaturesOfInterest(featuresOfInterest);
        return this;
    }

    public Set<String> getFeaturesOfInterest() {
        return featuresOfInterest;
    }

    public boolean isSetFeatures() {
        return isSetFeaturesOfInterest() || isSetFeaturesOfInterestMap();
    }

    public SosProcedureDescription<T> addPhenomenon(AbstractPhenomenon phenomenon) {
        getPhenomenon().put(phenomenon.getIdentifier(), phenomenon);
        return this;
    }

    public SosProcedureDescription<T> setPhenomenon(Map<String, AbstractPhenomenon> phenomenons) {
        this.phenomenonMap.clear();
        addPhenomenon(phenomenons);
        return this;
    }

    public SosProcedureDescription<T> addPhenomenon(Map<String, AbstractPhenomenon> phenomenons) {
        phenomenons.forEach(phenomenons::put);
        return this;
    }

    public Map<String, AbstractPhenomenon> getPhenomenon() {
        return phenomenonMap;
    }

    public boolean isSetPhenomenon() {
        return getPhenomenon() != null && !getPhenomenon().isEmpty();
    }

    public boolean hasPhenomenonFor(String identifier) {
        return isSetPhenomenon() && getPhenomenon().containsKey(identifier);
    }

    public AbstractPhenomenon getPhenomenonFor(String identifer) {
        return getPhenomenon().get(identifer);
    }

    public boolean isMobile() {
        return mobile;
    }

    public SosProcedureDescription<T> setMobile(boolean mobile) {
        this.mobile = mobile;
        return this;
    }

    public boolean isInsitu() {
        return insitu;
    }

    public SosProcedureDescription<T> setInsitu(boolean insitu) {
        this.insitu = insitu;
        return this;
    }

    /**
     * @return the typeOf
     */
    public ReferenceType getTypeOf() {
        return typeOf;
    }

    /**
     * @param typeOf
     *            the typeOf to set
     */
    public SosProcedureDescription<T> setTypeOf(ReferenceType typeOf) {
        this.typeOf = typeOf;
        return this;
    }

    /**
     * @return <code>true</code>, if typeOf is not null
     */
    public boolean isSetTypeOf() {
        return getTypeOf() != null;
    }

    public boolean isAggregation() {
        return aggregation;
    }

    public SosProcedureDescription<T> setIsAggregation(boolean aggregation) {
        this.aggregation = aggregation;
        return this;
    }
}
