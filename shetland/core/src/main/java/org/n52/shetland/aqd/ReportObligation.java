/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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

import org.n52.shetland.inspire.base.Identifier;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ReportObligation {
    private Identifier inspireID;
    private EReportingChange change;
    private Referenceable<Time> reportingPeriod
            = Referenceable.of(Nillable.<Time>missing());

    public EReportingChange getChange() {
        return change;
    }

    public ReportObligation setChange(EReportingChange change) {
        this.change = Preconditions.checkNotNull(change);
        return this;
    }

    public boolean isSetChange() {
        return getChange() != null;
    }

    public Identifier getInspireID() {
        return inspireID;
    }

    public ReportObligation setInspireID(Identifier inspireID) {
        this.inspireID = Preconditions.checkNotNull(inspireID);
        return this;
    }

    public boolean isSetInspireID() {
        return getInspireID() != null;
    }

    public Referenceable<Time> getReportingPeriod() {
        return reportingPeriod;
    }

    public ReportObligation setReportingPeriod(
            Referenceable<Time> reportingPeriod) {
        this.reportingPeriod = Preconditions.checkNotNull(reportingPeriod);
        return this;
    }

    public boolean isValid() {
        return isSetInspireID() && isSetChange();
    }

    @Override
    public int hashCode() {
        return Objects
                .hashCode(getInspireID(), getChange(), getReportingPeriod());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReportObligation) {
            ReportObligation that = (ReportObligation) obj;
            return Objects.equal(getInspireID(), that.getInspireID()) &&
                   Objects.equal(getChange(), that.getChange()) &&
                   Objects.equal(getReportingPeriod(), that.getReportingPeriod());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("inspireID", getInspireID())
                .add("change", getChange())
                .add("reportingPeriod", getReportingPeriod())
                .toString();
    }
}
