/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;

/**
 * Implementation of {@link OwsServiceRequest} for OWS GetCapabilities
 *
 * @since 1.0.0
 */
public class GetCapabilitiesRequest
        extends OwsServiceRequest {

    private final List<String> acceptVersions = new LinkedList<>();
    private final List<String> sections = new LinkedList<>();
    private final List<String> acceptFormats = new LinkedList<>();
    private final List<String> acceptLanguages = new LinkedList<>();

    private String capabilitiesId;
    private String updateSequence;

    public GetCapabilitiesRequest() {
        this(null);
    }

    public GetCapabilitiesRequest(String service) {
        super(service, null, OWSConstants.Operations.GetCapabilities.name());
    }

    /**
     * Get accept Formats
     *
     * @return accept Formats
     */
    public List<String> getAcceptFormats() {
        return acceptFormats == null ? null : Collections.unmodifiableList(acceptFormats);
    }

    /**
     * Set accept Formats
     *
     * @param acceptFormats
     *            accept Formats
     */
    public void setAcceptFormats(List<String> acceptFormats) {
        this.acceptFormats.clear();
        if (acceptFormats != null) {
            this.acceptFormats.addAll(acceptFormats);
        }
    }

    /**
     * Get accept versions
     *
     * @return accept versions
     */
    public List<String> getAcceptVersions() {
        return acceptVersions == null ? null : Collections.unmodifiableList(acceptVersions);
    }

    public void setAcceptVersions(List<String> acceptVersions) {
        this.acceptVersions.clear();
        if (acceptVersions != null) {
            this.acceptVersions.addAll(acceptVersions);
        }
    }

    public void addAcceptVersion(String acceptVersion) {
        if (acceptVersion != null) {
            acceptVersions.add(acceptVersion);
        }
    }

    /**
     * Get sections
     *
     * @return sections
     */
    public List<String> getSections() {
        return sections == null ? null : Collections.unmodifiableList(sections);
    }

    /**
     * Set sections
     *
     * @param sections
     *            sections
     */
    public void setSections(List<String> sections) {
        this.sections.clear();
        if (sections != null) {
            this.sections.addAll(sections);
        }
    }

    /**
     * Get update sequence
     *
     * @return update sequence
     */
    public String getUpdateSequence() {
        return updateSequence;
    }

    /**
     * Set update sequence
     *
     * @param updateSequence
     *            update sequence
     */
    public void setUpdateSequence(String updateSequence) {
        this.updateSequence = updateSequence;
    }

    public String getCapabilitiesId() {
        return this.capabilitiesId;
    }

    public void setCapabilitiesId(String id) {
        this.capabilitiesId = id;
    }

    public boolean isCapabilitiesIdSet() {
        return getCapabilitiesId() != null && !getCapabilitiesId().isEmpty();
    }

    public boolean isSetAcceptFormats() {
        return CollectionHelper.isNotEmpty(getAcceptFormats());
    }

    public boolean isSetAcceptVersions() {
        return CollectionHelper.isNotEmpty(getAcceptVersions());
    }

    public boolean isSetSections() {
        return CollectionHelper.isNotEmpty(getSections());
    }

    public boolean isSetUpdateSequence() {
        return !Strings.isNullOrEmpty(getUpdateSequence());
    }

    public void setAcceptLanguages(List<String> acceptLanguages) {
        this.acceptLanguages.clear();
        if (acceptLanguages != null) {
            this.acceptLanguages.addAll(acceptLanguages);
        }
    }

    public List<String> getAcceptLanguages() {
        return Collections.unmodifiableList(acceptLanguages);
    }

    @Override
    public String getRequestedLanguage() {
        if (acceptLanguages.isEmpty()) {
            return super.getRequestedLanguage();
        } else {
            return acceptLanguages.iterator().next();
        }
    }
}
