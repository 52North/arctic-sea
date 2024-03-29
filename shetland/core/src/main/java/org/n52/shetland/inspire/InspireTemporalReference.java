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
package org.n52.shetland.inspire;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Service internal representation of INSPIRE temporal reference
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireTemporalReference {

    /* DateOfCreation 0..1 */
    private InspireDateOfCreation dateOfCreation;

    /* DateOfLastRevision 0..1 */
    private InspireDateOfLastRevision dateOfLastRevision;

    /* DateOfPublication 0..* */
    private List<InspireDateOfPublication> datesOfPublication = Lists.newArrayList();

    /*
     * TemporalExtent 0..*, IndividualDate == {@link org.n52.sos.ogc.gml.time.TimeInstant} or IntervalOfDates
     * == {@link org.n52.sos.ogc.gml.time.TimePeriod}
     */
    private List<Time> temporalExtents = Lists.newArrayList();

    /**
     * Get the date of creation
     *
     * @return the dateOfCreation
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public InspireDateOfCreation getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Set the date of creation
     *
     * @param dateOfCreation
     *            the dateOfCreation to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setDateOfCreation(InspireDateOfCreation dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * Check if the date of creation is set
     *
     * @return <code>true</code>, if the date of creation is set
     */
    public boolean isSetDateOfCreation() {
        return getDateOfCreation() != null && !getDateOfCreation().isEmpty();
    }

    /**
     * Get the date of last revision
     *
     * @return the dateOfLastRevision
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public InspireDateOfLastRevision getDateOfLastRevision() {
        return dateOfLastRevision;
    }

    /**
     * Set the date of last revision
     *
     * @param dateOfLastRevision
     *            the dateOfLastRevision to set
     */
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setDateOfLastRevision(InspireDateOfLastRevision dateOfLastRevision) {
        this.dateOfLastRevision = dateOfLastRevision;
    }

    /**
     * Check if the date of last revision is set
     *
     * @return <code>true</code>, if the date of last revision is set
     */
    public boolean isSetDateOfLastRevision() {
        return getDateOfLastRevision() != null;
    }

    /**
     * gethe dates of publication
     *
     * @return the datesOfPublication
     */
    public List<InspireDateOfPublication> getDatesOfPublication() {
        return Collections.unmodifiableList(datesOfPublication);
    }

    /**
     * Set the dates of publication, clears the existing collection
     *
     * @param datesOfPublication
     *            the datesOfPublication to set
     */
    public void setDatesOfPublication(Collection<InspireDateOfPublication> datesOfPublication) {
        this.datesOfPublication.clear();
        if (CollectionHelper.isNotEmpty(datesOfPublication)) {
            this.datesOfPublication.addAll(datesOfPublication);
        }
    }

    /**
     * Add a date of publication
     *
     * @param dateOfPublication
     *            the date of publication to add
     * @return this
     */
    public InspireTemporalReference addDateOfPublication(InspireDateOfPublication dateOfPublication) {
        if (dateOfPublication != null) {
            this.datesOfPublication.add(dateOfPublication);
        }
        return this;
    }

    /**
     * Check if date of publications are set
     *
     * @return <code>true</code>, if date of publications are set
     */
    public boolean isSetDatesOfPublication() {
        return CollectionHelper.isNotEmpty(getDatesOfPublication());
    }

    /**
     * Get the temporal extents
     *
     * @return the temporalExtents
     */
    public List<Time> getTemporalExtents() {
        return Collections.unmodifiableList(temporalExtents);
    }

    /**
     * Set the temporal extents, clears the existing collection
     *
     * @param temporalExtents
     *            the temporalExtents to set
     */
    public void setTemporalExtents(Collection<Time> temporalExtents) {
        this.temporalExtents.clear();
        if (CollectionHelper.isNotEmpty(temporalExtents)) {
            this.temporalExtents.addAll(temporalExtents);
        }
    }

    /**
     * Add a temporal extent
     *
     * @param temporalExtent
     *            the temporal extent to add
     * @return this
     */
    public InspireTemporalReference addTemporalExtent(Time temporalExtent) {
        if (temporalExtent != null) {
            this.temporalExtents.add(temporalExtent);
        }
        return this;
    }

    /**
     * Check if temporal extents are set
     *
     * @return <code>true</code>, if temporal extents are set
     */
    public boolean isSetTemporalExtents() {
        return CollectionHelper.isNotEmpty(getTemporalExtents());
    }

    @Override
    public String toString() {
        return String.format(
                "%s %n[%n dateOfCreation=%s,%n dateOfLastRevision=%s,"
                        + "%n dateOfPublication=%s,%n temporalReferences=%s%n]",
                this.getClass().getSimpleName(), getDateOfCreation(), getDateOfLastRevision(),
                CollectionHelper.collectionToString(getDatesOfPublication()),
                CollectionHelper.collectionToString(getTemporalExtents()));
    }
}
