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
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.n52.janmayen.Comparables;
import org.n52.janmayen.function.Functions;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.swes.AbstractSWES;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.ReferencedEnvelope;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class which represents a ObservationOffering. Used in the SosCapabilities.
 *
 * @since 1.0.0
 */
public class SosObservationOffering extends AbstractSWES implements Comparable<SosObservationOffering> {

    /**
     * offering identifier for this contents sub section.
     */
    private SosOffering offering;

    /**
     * area observed by this offering.
     */
    private ReferencedEnvelope observedArea;

    /**
     * All observableProperties contained in the offering.
     */
    private final SortedSet<String> observableProperties = new TreeSet<>();

    /**
     * All compositePhenomenon contained in the offering.
     */
    private final SortedSet<String> compositePhenomena = new TreeSet<>();

    /**
     * All phenomenon for compositePhenomenon contained in the offering.
     */
    private final SortedMap<String, SortedSet<String>> phens4CompPhens = new TreeMap<>();

    /**
     * TimePeriod of data in the offering.
     */
    private Time phenomenonTime;

    /**
     * Result TimePeriod of data in the offering.
     */
    private Time resultTime;

    /**
     * All featuresOfinterest contained in the offering.
     */
    private final SortedSet<String> featureOfInterest = new TreeSet<>();

    /**
     * All related features contained in the offering.
     */
    private final SortedMap<String, SortedSet<String>> relatedFeatures = new TreeMap<>();

    /**
     * All procedures contained in the offering.
     */
    private final SortedSet<String> procedures = new TreeSet<>();

    /**
     * All resultModels contained in the offering.
     */
    private final SortedSet<QName> resultModels = new TreeSet<>(Comparables.qname());

    /**
     * All observation types contained in the offering.
     */
    private final SortedSet<String> observationTypes = new TreeSet<>();

    /**
     * All featureOfInterest types contained in the offering.
     */
    private final SortedSet<String> featureOfInterestTypes = new TreeSet<>();

    /**
     * All observation result types contained in the offering.
     */
    private final SortedMap<String, SortedSet<String>> observationResultTypes = new TreeMap<>();

    /**
     * All response formats contained in the offering.
     */
    private final SortedSet<String> responseFormats = new TreeSet<>();

    /**
     * All response modes contained in the offering.
     */
    private final SortedSet<String> responseModes = new TreeSet<>();

    /**
     * All procedure description formats contained in the offering.
     */
    private final SortedSet<String> procedureDescriptionFormats = new TreeSet<>();

    /**
     * @return Offering identifier
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public SosOffering getOffering() {
        return offering;
    }

    /**
     * @param offering
     *            Offering identifier
     */
    public void setOffering(String offering) {
        setOffering(new SosOffering(offering, ""));
    }

    /**
     * @param offering
     *            Offering identifier
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setOffering(SosOffering offering) {
        this.offering = offering;
        if (!isSetIdentifier() && offering.isSetIdentifier()) {
            this.setIdentifier(offering.getIdentifier());
        }
        if (!isSetName() && offering.isSetName()) {
            this.setName(offering.getName());
        }
        if (!isSetDescription() && offering.isSetDescription()) {
            this.setDescription(offering.getDescription());
        }
    }

    /**
     * @return Sorted observableProperties set
     */
    public SortedSet<String> getObservableProperties() {
        return Collections.unmodifiableSortedSet(observableProperties);
    }

    /**
     * @param observableProperties
     *            observableProperties to se
     */
    public void setObservableProperties(Collection<String> observableProperties) {
        set(this.observableProperties, observableProperties);
    }

    public void addObservatbleProperties(Collection<String> observableProperties) {
        add(this.observableProperties, observableProperties);
    }

    /**
     * @return Sorted composite phenomena set
     */
    public SortedSet<String> getCompositePhenomena() {
        return Collections.unmodifiableSortedSet(compositePhenomena);
    }

    /**
     * @param compositePhenomena
     *            compositePhenomena to set
     */
    public void setCompositePhenomena(Collection<String> compositePhenomena) {
        set(this.compositePhenomena, compositePhenomena);
    }

    /**
     * @return Sorted map containing the observableProperties and related composite phenomena
     */
    public SortedMap<String, SortedSet<String>> getPhens4CompPhens() {
        return Collections.unmodifiableSortedMap(phens4CompPhens);
    }

    /**
     * @param phens4CompPhens
     *            phens4CompPhens to set
     */
    public void setPhens4CompPhens(Map<String, ? extends Collection<String>> phens4CompPhens) {
        set(this.phens4CompPhens, phens4CompPhens);
    }

    /**
     * @param phenomenonTime
     *            the phenomenon time
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setPhenomenonTime(Time phenomenonTime) {
        this.phenomenonTime = phenomenonTime;
    }

    /**
     * @return the phenomenon time
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getPhenomenonTime() {
        return phenomenonTime;
    }

    /**
     * @param resultTime
     *            the result time
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setResultTime(Time resultTime) {
        this.resultTime = resultTime;
    }

    /**
     * @return the result time
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Time getResultTime() {
        return resultTime;
    }

    /**
     * @param featureOfInterest
     *            the fature of interest
     */
    public void setFeatureOfInterest(Collection<String> featureOfInterest) {
        set(this.featureOfInterest, featureOfInterest);
    }

    /**
     * @return Sorted featureOfInterest list
     */
    public SortedSet<String> getFeatureOfInterest() {
        return Collections.unmodifiableSortedSet(featureOfInterest);
    }

    /**
     * @param relatedFeatures
     *            the related features
     */
    public void setRelatedFeatures(Map<String, Set<String>> relatedFeatures) {
        set(this.relatedFeatures, relatedFeatures);
    }

    /**
     * @return Sorted map of related features
     */
    public SortedMap<String, SortedSet<String>> getRelatedFeatures() {
        return Collections.unmodifiableSortedMap(relatedFeatures);
    }

    /**
     * Add a related feature to this offering
     *
     * @param identifier
     *            Related feature identifier
     * @param role
     *            Related feature role
     */
    public void addRelatedFeature(String identifier, String role) {
        addToMap(this.relatedFeatures, identifier, role);
    }

    /**
     * Add a related feature to this offering
     *
     * @param identifier
     *            Related feature identifier
     * @param roles
     *            Related feature roles
     */
    public void addRelatedFeature(String identifier, Set<String> roles) {
        addToMap(this.relatedFeatures, identifier, roles);
    }

    /**
     * @return Sorted procedure set
     */
    public SortedSet<String> getProcedures() {
        return Collections.unmodifiableSortedSet(procedures);
    }

    /**
     * @param procedures
     *            procedures to set
     */
    public void setProcedures(Collection<String> procedures) {
        set(this.procedures, procedures);
    }

    /**
     * @return Sorted result models set
     */
    public SortedSet<QName> getResultModels() {
        return Collections.unmodifiableSortedSet(resultModels);
    }

    /**
     * @param resultModels
     *            resultModels to set
     */
    public void setResultModels(Collection<QName> resultModels) {
        set(this.resultModels, resultModels);
    }

    /**
     * @return Sorted observation types set
     */
    public SortedSet<String> getObservationTypes() {
        return Collections.unmodifiableSortedSet(observationTypes);
    }

    /**
     * @param observationTypes
     *            the observationTypes to set
     */
    public void setObservationTypes(Collection<String> observationTypes) {
        set(this.observationTypes, observationTypes);
    }

    /**
     * @return the observationResultTypes
     */
    public SortedMap<String, SortedSet<String>> getObservationResultTypes() {
        return Collections.unmodifiableSortedMap(observationResultTypes);
    }

    /**
     * @param observationResultTypes
     *            the observationResultTypes to set
     */
    public void setObservationResultTypes(Map<String, Collection<String>> observationResultTypes) {
        set(this.observationResultTypes, observationResultTypes);
    }

    /**
     * @return Sorted response formats set
     */
    public SortedSet<String> getResponseFormats() {
        return Collections.unmodifiableSortedSet(responseFormats);
    }

    /**
     * @param responseFormats
     *            responseFormats to set
     */
    public void setResponseFormats(Collection<String> responseFormats) {
        set(this.responseFormats, responseFormats);
    }

    /**
     * @return Sorted response mode set
     */
    public SortedSet<String> getResponseModes() {
        return Collections.unmodifiableSortedSet(responseModes);
    }

    /**
     * @param responseModes
     *            the response modes
     */
    public void setResponseModes(Collection<String> responseModes) {
        set(this.responseModes, responseModes);
    }

    public ReferencedEnvelope getObservedArea() {
        return observedArea;
    }

    public void setObservedArea(ReferencedEnvelope observedArea) {
        this.observedArea = observedArea;
    }

    public void setFeatureOfInterestTypes(Collection<String> featureOfInterestTypes) {
        set(this.featureOfInterestTypes, featureOfInterestTypes);
    }

    public SortedSet<String> getFeatureOfInterestTypes() {
        return Collections.unmodifiableSortedSet(featureOfInterestTypes);
    }

    public void setProcedureDescriptionFormat(Collection<String> procedureDescriptionFormats) {
        set(this.procedureDescriptionFormats, procedureDescriptionFormats);
    }

    public SortedSet<String> getProcedureDescriptionFormats() {
        return Collections.unmodifiableSortedSet(this.procedureDescriptionFormats);
    }

    @Override
    public int compareTo(SosObservationOffering o) {
        if (o == null) {
            throw new NullPointerException();
        }

        if (getOffering() == null ^ o.getOffering() == null) {
            return (getOffering() == null) ? -1 : 1;
        }

        if (getOffering() == null && o.getOffering() == null) {
            return 0;
        }

        return getOffering().compareTo(o.getOffering());
    }

    public boolean isEmpty() {
        return !isSetOffering() && !isSetObservedArea() && !isSetObservableProperties() && !isSetCompositePhenomena()
                && !isSetPhens4CompPhens() && !isSetPhenomenonTime() && !isSetResultTime() && !isSetFeatureOfInterest()
                && !isSetRelatedFeature() && !isSetProcedures() && !isSetresultModels() && !isSetObservationTypes()
                && !isSetFeatureOfInterestTypes() && !isSetObservationResultTypes() && !isSetResponseFormats()
                && !isSetResponseModes() && !isSetProcedureDescriptionFormats();
    }

    public boolean isValidObservationOffering() {
        return isSetOffering() && isSetProcedures();
    }

    public boolean isSetOffering() {
        return getOffering() != null;
    }

    public boolean isSetObservedArea() {
        return getObservedArea() != null && getObservedArea().isSetEnvelope();
    }

    public boolean isSetObservableProperties() {
        return CollectionHelper.isNotEmpty(getObservableProperties());
    }

    public boolean isSetCompositePhenomena() {
        return CollectionHelper.isNotEmpty(getCompositePhenomena());
    }

    public boolean isSetPhens4CompPhens() {
        return CollectionHelper.isNotEmpty(getPhens4CompPhens());
    }

    public boolean isSetPhenomenonTime() {
        return getPhenomenonTime() != null;
    }

    public boolean isSetResultTime() {
        return getResultTime() != null;
    }

    public boolean isSetFeatureOfInterest() {
        return CollectionHelper.isNotEmpty(getFeatureOfInterest());
    }

    public boolean isSetRelatedFeature() {
        return CollectionHelper.isNotEmpty(getRelatedFeatures());
    }

    public boolean isSetProcedures() {
        return CollectionHelper.isNotEmpty(getProcedures());
    }

    public boolean isSetresultModels() {
        return CollectionHelper.isNotEmpty(getResultModels());
    }

    public boolean isSetObservationTypes() {
        return CollectionHelper.isNotEmpty(getObservationTypes());
    }

    public boolean isSetFeatureOfInterestTypes() {
        return CollectionHelper.isNotEmpty(getFeatureOfInterestTypes());
    }

    private boolean isSetObservationResultTypes() {
        return CollectionHelper.isNotEmpty(getObservationResultTypes());
    }

    public boolean isSetResponseFormats() {
        return CollectionHelper.isNotEmpty(getResponseFormats());
    }

    private boolean isSetResponseModes() {
        return CollectionHelper.isNotEmpty(getResponseModes());
    }

    public boolean isSetProcedureDescriptionFormats() {
        return CollectionHelper.isNotEmpty(getProcedureDescriptionFormats());
    }

    @Override
    public String toString() {
        return "SosObservationOffering [offering=" + offering + "]";
    }

    /**
     * clear and add collection to sorted set.
     *
     * @param <T>
     *            the element type
     * @param set
     *            the set
     * @param coll
     *            the collection
     */
    private static <
            T> void set(SortedSet<T> set, Collection<? extends T> coll) {
        if (set != null) {
            set.clear();
            add(set, coll);
        }
    }

    /**
     * Add map to sorted map.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param sortedMap
     *            the target map
     * @param map
     *            the source map
     */
    private static <
            K,
            V extends Comparable<? super V>> void set(SortedMap<K, SortedSet<V>> sortedMap,
                    Map<K, ? extends Collection<V>> map) {
        if (sortedMap != null) {
            sortedMap.clear();
            if (map != null) {
                map.forEach(
                        (key, value) -> sortedMap.put(key, value != null ? new TreeSet<>(value) : new TreeSet<>()));
            }
        }
    }

    /**
     * add collection to sorted set.
     *
     * @param <T>
     *            the element type
     * @param set
     *            the set
     * @param coll
     *            the collection
     */
    private static <
            T> void add(SortedSet<T> set, Collection<? extends T> coll) {
        if (set != null && coll != null) {
            set.addAll(coll);
        }
    }

    /**
     * Add key and value to map.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map
     * @param key
     *            the key
     * @param value
     *            the value
     */
    private static <
            K,
            V> void addToMap(SortedMap<K, SortedSet<V>> map, K key, V value) {
        if (map != null && key != null && value != null) {
            map.computeIfAbsent(key, Functions.forSupplier(TreeSet::new)).add(value);
        }
    }

    /**
     * Add key and values to map.
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map
     * @param key
     *            the key
     * @param value
     *            the values
     */
    private static <
            K,
            V> void addToMap(SortedMap<K, SortedSet<V>> map, K key, Collection<V> value) {
        if (map != null && key != null && value != null) {
            map.computeIfAbsent(key, Functions.forSupplier(TreeSet::new)).addAll(value);
        }
    }

}
