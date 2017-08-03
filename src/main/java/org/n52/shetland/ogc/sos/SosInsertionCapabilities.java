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
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;
import org.n52.shetland.ogc.ows.extension.MergableExtension;
import org.n52.shetland.util.CollectionHelper;

/**
 * @since 1.0.0
 *
 */
public class SosInsertionCapabilities implements OwsCapabilitiesExtension, MergableExtension<SosInsertionCapabilities> {
    private static final String SECTION_NAME = Sos2Constants.CapabilitiesSections.InsertionCapabilities.name();

    private final SortedSet<String> featureOfInterestTypes = new TreeSet<>();
    private final SortedSet<String> observationTypes = new TreeSet<>();
    private final SortedSet<String> procedureDescriptionFormats = new TreeSet<>();
    private final SortedSet<String> supportedEncodings = new TreeSet<>();

    public SortedSet<String> getFeatureOfInterestTypes() {
        return Collections.unmodifiableSortedSet(featureOfInterestTypes);
    }

    public SortedSet<String> getObservationTypes() {
        return Collections.unmodifiableSortedSet(observationTypes);
    }

    public SortedSet<String> getProcedureDescriptionFormats() {
        return Collections.unmodifiableSortedSet(procedureDescriptionFormats);
    }

    public SortedSet<String> getSupportedEncodings() {
        return Collections.unmodifiableSortedSet(supportedEncodings);
    }

    public void addFeatureOfInterestTypes(Collection<String> featureOfInterestTypes) {
        this.featureOfInterestTypes.addAll(featureOfInterestTypes);
    }

    public void addObservationTypes(Collection<String> observationTypes) {
        this.observationTypes.addAll(observationTypes);
    }

    public void addProcedureDescriptionFormats(Collection<String> procedureDescriptionFormats) {
        this.procedureDescriptionFormats.addAll(procedureDescriptionFormats);
    }

    public void addSupportedEncodings(Collection<String> supportedEncodings) {
        this.supportedEncodings.addAll(supportedEncodings);
    }

    public void addFeatureOfInterestType(String featureOfInterestType) {
        this.featureOfInterestTypes.add(featureOfInterestType);
    }

    public void addObservationType(String observationType) {
        this.observationTypes.add(observationType);
    }

    public void addProcedureDescriptionFormat(String procedureDescriptionFormat) {
        this.procedureDescriptionFormats.add(procedureDescriptionFormat);
    }

    public void addSupportedEncoding(String supportedEncoding) {
        this.supportedEncodings.add(supportedEncoding);
    }

    public boolean isSetFeatureOfInterestTypes() {
        return CollectionHelper.isNotEmpty(featureOfInterestTypes);
    }

    public boolean isSetObservationTypes() {
        return CollectionHelper.isNotEmpty(observationTypes);
    }

    public boolean isSetProcedureDescriptionFormats() {
        return CollectionHelper.isNotEmpty(procedureDescriptionFormats);
    }

    public boolean isSetSupportedEncodings() {
        return CollectionHelper.isNotEmpty(supportedEncodings);
    }

    @Override
    public String getSectionName() {
        return SECTION_NAME;
    }

    @Override
    public void merge(SosInsertionCapabilities insertionCapabilities) {
        addFeatureOfInterestTypes(insertionCapabilities.getFeatureOfInterestTypes());
        addObservationTypes(insertionCapabilities.getObservationTypes());
        addProcedureDescriptionFormats(insertionCapabilities.getProcedureDescriptionFormats());
        addSupportedEncodings(insertionCapabilities.getSupportedEncodings());
    }
}
