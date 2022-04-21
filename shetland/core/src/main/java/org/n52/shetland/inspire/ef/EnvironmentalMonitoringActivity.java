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
package org.n52.shetland.inspire.ef;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class EnvironmentalMonitoringActivity extends AbstractFeature {

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

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public EnvironmentalMonitoringActivity(Time activityTime, String activityConditions, RelatedParty responsibleParty,
            Identifier inspireId) {
        super(inspireId);
        this.activityTime = activityTime;
        this.activityConditions = activityConditions;
        this.responsibleParty = responsibleParty;
        this.inspireId = inspireId;
    }

    /**
     * @return the activityTime
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
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
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public RelatedParty getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * @return the inspireId
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Identifier getInspireId() {
        return inspireId;
    }

    /**
     * @return the onlineResource
     */
    public Set<URI> getOnlineResource() {
        return Collections.unmodifiableSet(onlineResource);
    }

    /**
     * @param onlineResource
     *            the onlineResource to set
     * @return this
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public EnvironmentalMonitoringActivity setOnlineResource(Collection<URI> onlineResource) {
        this.onlineResource.clear();
        if (onlineResource != null) {
            this.onlineResource.addAll(onlineResource);
        }
        return this;
    }

    public boolean isSetOnlineResource() {
        return CollectionHelper.isNotEmpty(getOnlineResource());
    }

    /**
     * @return the setUpFor
     */
    public Set<EnvironmentalMonitoringProgramme> getSetUpFor() {
        return Collections.unmodifiableSet(setUpFor);
    }

    /**
     * @param setUpFor
     *            the setUpFor to set
     * @return this
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public EnvironmentalMonitoringActivity setSetUpFor(Collection<EnvironmentalMonitoringProgramme> setUpFor) {
        this.setUpFor.clear();
        if (setUpFor != null) {
            this.setUpFor.addAll(setUpFor);
        }
        return this;
    }

    public boolean isSetUpFor() {
        return CollectionHelper.isNotEmpty(getSetUpFor());
    }

    /**
     * @return the uses
     */
    public Set<AbstractMonitoringFeature> getUses() {
        return Collections.unmodifiableSet(uses);
    }

    /**
     * @param uses
     *            the uses to set
     * @return this
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public EnvironmentalMonitoringActivity setUses(Collection<AbstractMonitoringFeature> uses) {
        this.uses.clear();
        if (uses != null) {
            this.uses.addAll(uses);
        }
        return this;
    }

    public boolean isSetUses() {
        return CollectionHelper.isNotEmpty(getUses());
    }

}
