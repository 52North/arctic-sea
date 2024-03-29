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
package org.n52.shetland.ogc.sos;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @since 1.0.0
 *
 */
public class SosProcedureDescription<
        T extends AbstractFeature> extends AbstractFeature {

    private final T procedureDescription;
    private Time validTime;
    private String descriptionFormat;

    private final Set<SosOffering> offerings = Sets.newLinkedHashSet();
    private Map<String, AbstractFeature> featuresOfInterestMap = new HashMap<>();
    private Set<String> featuresOfInterest = Sets.newLinkedHashSet();
    private Map<String, AbstractPhenomenon> phenomenonMap = new HashMap<>();
    private ReferenceType parentProcedure;
    private Set<AbstractSensorML> childProcedures = new LinkedHashSet<>();
    private boolean insitu = true;
    private boolean mobile;
    private ReferenceType typeOf;
    private boolean aggregation;
    private boolean reference;

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SosProcedureDescription(T procedureDescription) {
        super(procedureDescription.getIdentifier());
        this.procedureDescription = procedureDescription;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public T getProcedureDescription() {
        return procedureDescription;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getValidTime() {
        return validTime;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SosProcedureDescription<T> setValidTime(Time validTime) {
        this.validTime = validTime;
        return this;
    }

    public boolean isSetValidTime() {
        return validTime != null && !validTime.isEmpty();
    }

    public String getDescriptionFormat() {
        return descriptionFormat;
    }

    public SosProcedureDescription<T> setDescriptionFormat(String descriptionFormat) {
        this.descriptionFormat = descriptionFormat;
        return this;
    }

    public Set<SosOffering> getOfferings() {
        return Collections.unmodifiableSet(offerings);
    }

    public SosProcedureDescription<T> addOfferings(Collection<SosOffering> offerings) {
        if (offerings != null) {
            this.offerings.addAll(offerings);
        }
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

    public SosProcedureDescription<T> setParentProcedure(ReferenceType parentProcedure) {
        this.parentProcedure = parentProcedure;
        return this;
    }

    public boolean isSetParentProcedure() {
        return parentProcedure != null;
    }

    public SosProcedureDescription<T> addChildProcedure(AbstractSensorML process) {
        if (process != null) {
            childProcedures.add(process);
        }
        return this;
    }

    public Set<AbstractSensorML> getChildProcedures() {
        return Collections.unmodifiableSet(childProcedures);
    }

    public SosProcedureDescription<T> setChildProcedures(Collection<AbstractSensorML> childProcedures) {
        this.childProcedures.clear();
        if (childProcedures != null) {
            addChildProcedures(childProcedures);
        }
        return this;
    }

    public SosProcedureDescription<T> addChildProcedures(Collection<AbstractSensorML> childProcedures) {
        if (childProcedures != null) {
            childProcedures.forEach(this.childProcedures::add);
        }
        return this;
    }

    public boolean isSetChildProcedures() {
        return childProcedures != null && !childProcedures.isEmpty();
    }

    public Map<String, AbstractFeature> getFeaturesOfInterestMap() {
        return Collections.unmodifiableMap(featuresOfInterestMap);
    }

    public SosProcedureDescription<T> setFeaturesOfInterestMap(Map<String, AbstractFeature> featuresOfInterestMap) {
        this.featuresOfInterestMap.clear();
        if (featuresOfInterestMap != null) {
            addFeaturesOfInterestMap(featuresOfInterestMap);
        }
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
        if (featureOfInterest != null) {
            this.featuresOfInterestMap.put(featureOfInterest.getIdentifier(), featureOfInterest);
        }
        return this;
    }

    public SosProcedureDescription<T> addFeatureOfInterest(String featureOfInterest) {
        if (featureOfInterest != null) {
            this.featuresOfInterest.add(featureOfInterest);
        }
        return this;
    }

    public boolean isSetFeaturesOfInterestMap() {
        return featuresOfInterestMap != null && !featuresOfInterestMap.isEmpty();
    }

    public boolean isSetFeaturesOfInterest() {
        return featuresOfInterest != null && !featuresOfInterest.isEmpty();
    }

    public SosProcedureDescription<T> setFeaturesOfInterest(Collection<String> featuresOfInterest) {
        this.featuresOfInterest.clear();
        addFeaturesOfInterest(featuresOfInterest);
        return this;
    }

    public Set<String> getFeaturesOfInterest() {
        return Collections.unmodifiableSet(featuresOfInterest);
    }

    public boolean isSetFeatures() {
        return isSetFeaturesOfInterest() || isSetFeaturesOfInterestMap();
    }

    public SosProcedureDescription<T> addPhenomenon(AbstractPhenomenon phenomenon) {
        phenomenonMap.put(phenomenon.getIdentifier(), phenomenon);
        return this;
    }

    public SosProcedureDescription<T> addPhenomenon(Map<String, AbstractPhenomenon> phenomenons) {
        phenomenons.forEach(phenomenonMap::put);
        return this;
    }

    public SosProcedureDescription<T> setPhenomenon(Map<String, AbstractPhenomenon> phenomenons) {
        this.phenomenonMap.clear();
        addPhenomenon(phenomenons);
        return this;
    }

    public Map<String, AbstractPhenomenon> getPhenomenon() {
        return Collections.unmodifiableMap(phenomenonMap);
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

    /**
     * Flag used by the REST API for displaying reference values. Not used by OGC SOS interface
     *
     * @return the reference flag state
     */
    public boolean isReference() {
        return reference;
    }

    public void setReference(boolean isReference) {
        reference = isReference;
    }

    public SosProcedureDescription<T> add(SosProcedureDescription<?> desc) {
        setChildProcedures(desc.getChildProcedures());
        setFeaturesOfInterest(desc.getFeaturesOfInterest());
        setFeaturesOfInterestMap(desc.getFeaturesOfInterestMap());
        setInsitu(desc.isInsitu());
        setIsAggregation(desc.isAggregation());
        setReference(desc.isReference());
        setMobile(desc.isMobile());
        setParentProcedure(desc.getParentProcedure());
        setPhenomenon(desc.getPhenomenon());
        setTypeOf(desc.getTypeOf());
        setValidTime(desc.getValidTime());
        addOfferings(desc.getOfferings());
        addPhenomenon(desc.getPhenomenon());
        return this;
    }

    @Override
    public String getDefaultElementEncoding() {
        if (getProcedureDescription() != null
                && !Strings.isNullOrEmpty(getProcedureDescription().getDefaultElementEncoding())) {
            return getProcedureDescription().getDefaultElementEncoding();
        }
        return super.getDefaultElementEncoding();
    }

    @Override
    public boolean isSetDefaultElementEncoding() {
        return super.isSetDefaultElementEncoding() || getProcedureDescription() != null
                && !Strings.isNullOrEmpty(getProcedureDescription().getDefaultElementEncoding());
    }
}
