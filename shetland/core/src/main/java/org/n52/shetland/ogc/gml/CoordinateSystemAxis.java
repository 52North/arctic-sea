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
package org.n52.shetland.ogc.gml;

/**
 * Internal representation of the OGC GML CoordinateSystemAxis.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class CoordinateSystemAxis
        extends IdentifiedObject {

    /* 1..1 */
    private CodeType axisAbbrev;
    /* 1..1 */
    private CodeWithAuthority axisDirection;
    /* 0..1 */
    private Double minimumValue;
    /* 0..1 */
    private Double maximumValue;
    /* 0..1 */
    private CodeWithAuthority rangeMeaning;
    /* 1..1 */
    private String uom;

    public CoordinateSystemAxis(
            CodeWithAuthority identifier, CodeType axisAbbrev, CodeWithAuthority axisDirection, String uom) {
        super(identifier);
        this.axisAbbrev = axisAbbrev;
        this.axisDirection = axisDirection;
        this.uom = uom;
    }

    /**
     * @return the axisAbbrev
     */
    public CodeType getAxisAbbrev() {
        return axisAbbrev;
    }

    /**
     * @return the axisDirection
     */
    public CodeWithAuthority getAxisDirection() {
        return axisDirection;
    }

    /**
     * @return the minimumValue
     */
    public Double getMinimumValue() {
        return minimumValue;
    }

    /**
     * @param minimumValue
     *            the minimumValue to set
     */
    public CoordinateSystemAxis setMinimumValue(Double minimumValue) {
        this.minimumValue = minimumValue;
        return this;
    }

    public boolean isSetMinimumValue() {
        return getMinimumValue() != null;
    }

    /**
     * @return the maximumValue
     */
    public Double getMaximumValue() {
        return maximumValue;
    }

    /**
     * @param maximumValue
     *            the maximumValue to set
     */
    public CoordinateSystemAxis setMaximumValue(Double maximumValue) {
        this.maximumValue = maximumValue;
        return this;
    }

    public boolean isSetMaximumValue() {
        return getMaximumValue() != null;
    }

    /**
     * @return the rangeMeaning
     */
    public CodeWithAuthority getRangeMeaning() {
        return rangeMeaning;
    }

    /**
     * @param rangeMeaning
     *            the rangeMeaning to set
     */
    public CoordinateSystemAxis setRangeMeaning(CodeWithAuthority rangeMeaning) {
        this.rangeMeaning = rangeMeaning;
        return this;
    }

    public boolean isSetRangeMeaning() {
        return getRangeMeaning() != null;
    }

    /**
     * @return the uom
     */
    public String getUom() {
        return uom;
    }

}
