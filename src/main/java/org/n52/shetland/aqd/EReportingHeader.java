/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.aqd;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.inspire.base2.RelatedParty;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class EReportingHeader extends AbstractEReportingHeader {
    private Identifier inspireID;
    private RelatedParty reportingAuthority;
    private EReportingChange change;
    private Referenceable<Time> reportingPeriod = Referenceable.of(Nillable.<Time>missing());
    private final List<Referenceable<AbstractFeature>> delete = new LinkedList<>();
    private final List<Referenceable<AbstractFeature>> content = new LinkedList<>();

    public EReportingHeader() {
        setDefaultElementEncoding(AqdConstants.NS_AQD);
    }

    public EReportingChange getChange() {
        return change;
    }

    public EReportingHeader setChange(EReportingChange change) {
        this.change = Preconditions.checkNotNull(change);
        return this;
    }

    public Identifier getInspireID() {
        return inspireID;
    }

    public EReportingHeader setInspireID(Identifier inspireID) {
        this.inspireID = Preconditions.checkNotNull(inspireID);
        return this;
    }

    public Referenceable<Time> getReportingPeriod() {
        return reportingPeriod;
    }

    public EReportingHeader setReportingPeriod(
            Referenceable<Time> reportingPeriod) {
        this.reportingPeriod = Preconditions.checkNotNull(reportingPeriod);
        return this;
    }

    public RelatedParty getReportingAuthority() {
        return reportingAuthority;
    }

    public EReportingHeader setReportingAuthority(
            RelatedParty reportingAuthority) {
        this.reportingAuthority = Preconditions.checkNotNull(reportingAuthority);
        return this;
    }

    public List<Referenceable<AbstractFeature>> getDelete() {
        return Collections.unmodifiableList(delete);
    }

    public EReportingHeader addDelete(Referenceable<AbstractFeature> delete) {
        this.delete.add(Preconditions.checkNotNull(delete));
        return this;
    }

    public EReportingHeader addDelete(AbstractFeature delete) {
        return addDelete(Referenceable.of(delete));
    }

    public List<Referenceable<AbstractFeature>> getContent() {
        return Collections.unmodifiableList(content);
    }

    public boolean isSetContent() {
        return CollectionHelper.isNotEmpty(content);
    }

    public EReportingHeader addContent(Referenceable<AbstractFeature> content) {
        this.content.add(Preconditions.checkNotNull(content));
        return this;
    }

    @Override
    public EReportingHeader addContent(AbstractFeature content) {
        return addContent(Referenceable.of(content));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getInspireID(), getReportingAuthority(),
                                getChange(), getReportingPeriod(), getDelete(),
                                getContent());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EReportingHeader) {
            EReportingHeader that = (EReportingHeader) obj;
            return Objects.equal(getInspireID(), that.getInspireID()) &&
                   Objects.equal(getReportingAuthority(), that.getReportingAuthority()) &&
                   Objects.equal(getChange(), that.getChange()) &&
                   Objects.equal(getReportingPeriod(), that.getReportingPeriod()) &&
                   Objects.equal(getDelete(), that.getDelete()) &&
                   Objects.equal(getContent(), that.getContent());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("inspireID", getInspireID())
                .add("reportingAuthority", getReportingAuthority())
                .add("change", getChange())
                .add("reportingPeriod", getReportingPeriod())
                .add("delete", getDelete())
                .add("content", getContent())
                .toString();
    }



}
