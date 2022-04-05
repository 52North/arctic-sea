/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.n52.janmayen.Comparables;
import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.ogc.filter.FilterConstants.TimeOperator;
import org.n52.shetland.ogc.ows.OwsDomain;
import org.n52.shetland.util.CollectionHelper;

/**
 * SOS filter capabilities
 *
 * @since 1.0.0
 */
public class FilterCapabilities {
    private final SortedSet<QName> spatialOperands = new TreeSet<>(Comparables.qname());
    private final SortedSet<QName> temporalOperands = new TreeSet<>(Comparables.qname());
    private final SortedMap<SpatialOperator, SortedSet<QName>> spatialOperators = new TreeMap<>();
    private final SortedMap<TimeOperator, SortedSet<QName>> temporalOperators = new TreeMap<>();
    private final SortedSet<ComparisonOperator> comparisonOperators = new TreeSet<>();
    private final Collection<OwsDomain> conformance = new TreeSet<>();

    /**
     * Get spatial operands
     *
     * @return spatial operands
     */
    public SortedSet<QName> getSpatialOperands() {
        return Collections.unmodifiableSortedSet(spatialOperands);
    }

    /**
     * Set spatial operands
     *
     * @param spatialOperands
     *                        spatial operands
     */
    public void setSpatialOperands(Collection<QName> spatialOperands) {
        this.spatialOperands.clear();
        Optional.ofNullable(spatialOperands).ifPresent(this.spatialOperands::addAll);
    }

    /**
     * Get spatial operators
     *
     * @return spatial operators
     */
    public SortedMap<SpatialOperator, SortedSet<QName>> getSpatialOperators() {
        return Collections.unmodifiableSortedMap(spatialOperators);
    }

    /**
     * Set spatial operators
     *
     * @param spatialOperators
     *                         spatial operators
     */
    public void setSpatialOperators(Map<SpatialOperator, ? extends Collection<QName>> spatialOperators) {
        setOperators(this.spatialOperators, spatialOperators);
    }

    /**
     * Get temporal operands
     *
     * @return temporal operands
     */
    public SortedSet<QName> getTemporalOperands() {
        return Collections.unmodifiableSortedSet(temporalOperands);
    }

    /**
     * Set temporal operands
     *
     * @param temporalOperands
     *                         temporal operands
     */
    public void setTemporalOperands(Collection<QName> temporalOperands) {
        this.temporalOperands.clear();
        Optional.ofNullable(temporalOperands).ifPresent(this.temporalOperands::addAll);
    }

    /**
     * Get temporal operators
     *
     * @return temporal operators
     */
    public SortedMap<TimeOperator, SortedSet<QName>> getTemporalOperators() {
        return Collections.unmodifiableSortedMap(temporalOperators);
    }

    /**
     * Set temporal operators
     *
     * @param temporalOperators
     *                          temporal operators
     */
    public void setTemporalOperators(Map<TimeOperator, ? extends Collection<QName>> temporalOperators) {
        setOperators(this.temporalOperators, temporalOperators);
    }

    private <T> void setOperators(SortedMap<T, SortedSet<QName>> operators,
                                  Map<T, ? extends Collection<QName>> newOperators) {
        operators.clear();
        Optional.ofNullable(newOperators).ifPresent(so -> {
            so.forEach((operator, qnames) -> {
                TreeSet<QName> set = new TreeSet<>(Comparables.qname());
                Optional.ofNullable(qnames).ifPresent(set::addAll);
                operators.put(operator, set);
            });
        });

    }

    /**
     * Get comparison operators
     *
     * @return comparison operators
     */
    public SortedSet<ComparisonOperator> getComparisonOperators() {
        return Collections.unmodifiableSortedSet(comparisonOperators);
    }

    /**
     * Set comparison operators
     *
     * @param comparisonOperators
     *                            comparison operators
     */
    public void setComparisonOperators(Collection<ComparisonOperator> comparisonOperators) {
        this.comparisonOperators.clear();
        Optional.ofNullable(comparisonOperators).ifPresent(this.comparisonOperators::addAll);
    }

    public void addConformance(OwsDomain domainType) {
        this.conformance.add(Objects.requireNonNull(domainType));
    }

    public void addConformance(Collection<OwsDomain> domainTypes) {
        domainTypes.stream().forEach(this::addConformance);
    }

    public void setConformance(Collection<OwsDomain> domainTypes) {
        this.conformance.clear();
        this.conformance.addAll(domainTypes);
    }

    public Collection<OwsDomain> getConformance() {
        return Collections.unmodifiableCollection(conformance);
    }

    public boolean isSetCoinformance() {
        return CollectionHelper.isNotEmpty(getConformance());
    }
}
