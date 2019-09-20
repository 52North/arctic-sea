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
package org.n52.shetland.inspire.ef;

import java.net.URI;
import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

public class EnvironmentalMonitoringActivity
        extends AbstractFeature {

    /**
     * 1..1
     */
    private Time activityTime;

    /**
     * 1..1
     */
    private String activityConditions;

    /**
     * 0..1
     */
    private Object boundingBox;

    /**
     * 1..1
     */
    private RelatedParty responsibleParty;

    /**
     * 1..1
     */
    private Identifier inspireId;

    /**
     * 0..*
     */
    private Set<URI> onlineResource = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<EnvironmentalMonitoringProgramme> setUpFor = Sets.newHashSet();

    /**
     * 0..*
     */
    private Set<AbstractMonitoringFeature> uses = Sets.newHashSet();

    public EnvironmentalMonitoringActivity(
            Time activityTime, String activityConditions, RelatedParty responsibleParty, Identifier inspireId) {
        super(inspireId);
        this.activityTime = activityTime;
        this.activityConditions = activityConditions;
        this.responsibleParty = responsibleParty;
        this.inspireId = inspireId;
    }

    /**
     * @return the activityTime
     */
    public Time getActivityTime() {
        return activityTime;
    }

    /**
     * @return the activityConditions
     */
    public String getActivityConditions() {
        return activityConditions;
    }

    /**
     * @return the boundingBox
     */
    public Object getBoundingBox() {
        return boundingBox;
    }

    /**
     * @param boundingBox
     *            the boundingBox to set
     */
    public void setBoundingBox(Object boundingBox) {
        this.boundingBox = boundingBox;
    }

    public boolean isSetBoundingBox() {
        return getBoundingBox() != null;
    }

    /**
     * @return the responsibleParty
     */
    public RelatedParty getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * @return the inspireId
     */
    public Identifier getInspireId() {
        return inspireId;
    }

    /**
     * @return the onlineResource
     */
    public Set<URI> getOnlineResource() {
        return onlineResource;
    }

    /**
     * @param onlineResource
     *            the onlineResource to set
     */
    public void setOnlineResource(Set<URI> onlineResource) {
        this.onlineResource.clear();
        this.onlineResource = onlineResource;
    }

    public boolean isSetOnlineResource() {
        return CollectionHelper.isNotEmpty(getOnlineResource());
    }

    /**
     * @return the setUpFor
     */
    public Set<EnvironmentalMonitoringProgramme> getSetUpFor() {
        return setUpFor;
    }

    /**
     * @param setUpFor
     *            the setUpFor to set
     */
    public void setSetUpFor(Set<EnvironmentalMonitoringProgramme> setUpFor) {
        this.setUpFor.clear();
        this.setUpFor = setUpFor;
    }

    public boolean isSetUpFor() {
        return CollectionHelper.isNotEmpty(getSetUpFor());
    }

    /**
     * @return the uses
     */
    public Set<AbstractMonitoringFeature> getUses() {
        return uses;
    }

    /**
     * @param uses
     *            the uses to set
     */
    public void setUses(Set<AbstractMonitoringFeature> uses) {
        this.uses.clear();
        this.uses = uses;
    }

    public boolean isSetUses() {
        return CollectionHelper.isNotEmpty(getUses());
    }

}
