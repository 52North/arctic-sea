/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

import org.n52.shetland.ogc.gml.GmlConstants.NilReason;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GmdQuantitativeResult extends GmdDomainConsistency {

    private final GmlBaseUnit unit;
    private final String value;
    private final NilReason valueNilReason;

    public GmdQuantitativeResult(GmlBaseUnit unit, String value) {
        this.unit = unit;
        this.value = value;
        this.valueNilReason = null;
    }

    public GmdQuantitativeResult(GmlBaseUnit unit, NilReason valueNilReason) {
        this.unit = unit;
        this.value = null;
        this.valueNilReason = valueNilReason;
    }

    public GmlBaseUnit getUnit() {
        return unit;
    }

    public String getValue() {
        return value;
    }

    public boolean isSetValueNilReason() {
        return getValueNilReason() != null;
    }

    public NilReason getValueNilReason() {
        return valueNilReason;
    }

}
