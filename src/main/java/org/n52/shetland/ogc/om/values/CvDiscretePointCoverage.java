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
package org.n52.shetland.ogc.om.values;



import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.PointValuePair;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.ogc.om.values.visitor.VoidValueVisitor;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.JavaHelper;

import com.google.common.base.Strings;

/**
 * Class that represents a CV_DiscretePointCoverage
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.4.0
 *
 */
public class CvDiscretePointCoverage implements Value<PointValuePair> {

    private String gmlId;

    private String domainExtent;

    private ReferenceType rangeType;

    private PointValuePair value;

    private UoM unit;

    public CvDiscretePointCoverage(String gmlId) {
        if (Strings.isNullOrEmpty(gmlId)) {
            gmlId = JavaHelper.generateID(toString());
        } else if (!gmlId.startsWith("dpc_")) {
            gmlId = "dpc_" + gmlId;
        }
        this.gmlId = gmlId;
    }

    public String getGmlId() {
        return gmlId;
    }

    /**
     * @return the domainExtent
     */
    public String getDomainExtent() {
        return domainExtent;
    }

    /**
     * @param domainExtent
     *            the domainExtent to set
     */
    public void setDomainExtent(String domainExtent) {
        this.domainExtent = domainExtent;
    }

    public boolean isSetDomainExtent() {
        return !Strings.isNullOrEmpty(domainExtent);
    }

    /**
     * @return the rangeType
     */
    public ReferenceType getRangeType() {
        return rangeType;
    }

    /**
     * @param rangeType
     *            the rangeType to set
     */
    public void setRangeType(ReferenceType rangeType) {
        this.rangeType = rangeType;
    }

    @Override
    public CvDiscretePointCoverage setValue(PointValuePair value) {
        this.value = value;
        return this;
    }

    @Override
    public PointValuePair getValue() {
        return value;
    }

    @Override
    public boolean isSetValue() {
        return getValue() != null && !getValue().isEmpty();
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public String getUnit() {
        if (isSetUnit()) {
            return unit.getUom();
        }
        return null;
    }

    @Override
    public UoM getUnitObject() {
        return this.unit;
    }

    @Override
    public CvDiscretePointCoverage setUnit(UoM unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor) throws E {
        return visitor.visit(this);
    }

}
