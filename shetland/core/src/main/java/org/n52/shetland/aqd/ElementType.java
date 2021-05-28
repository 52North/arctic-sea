/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import org.n52.shetland.aqd.AqdConstants.PrimaryObservation;
import org.n52.shetland.ogc.om.OmConstants;

import com.google.common.base.Strings;

public class ElementType {

    public static final ElementType START_TIME =
            new ElementType("StartTime", OmConstants.PHEN_SAMPLING_TIME, OmConstants.PHEN_UOM_ISO8601);

    public static final ElementType END_TIME =
            new ElementType("EndTime", OmConstants.PHEN_SAMPLING_TIME, OmConstants.PHEN_UOM_ISO8601);

    public static final ElementType VERIFICATION =
            new ElementType("Verification", AqdConstants.DEFINITION_VERIFICATION);

    public static final ElementType VALIDITY = new ElementType("Validity", AqdConstants.DEFINITION_VALIDITY);

    public static final ElementType DATA_CAPTURE = new ElementType("DataCapture", AqdConstants.DEFINITION_DATA_CAPTURE,
            AqdConstants.DEFINITION_UOM_STATISTICS_PERCENTAGE);

    private final String name;

    private final String definition;

    private final String uom;

    public ElementType(String name, String definition) {
        this(name, definition, null);
    }

    public ElementType(String name, String definition, String uom) {
        this.name = name;
        this.definition = definition;
        this.uom = uom;
    }

    private ElementType(String name, PrimaryObservation primaryObs, String uom) {
        this(name, primaryObs.getConceptURI(), uom);
    }

    public String getName() {
        return name;
    }

    public String getDefinition() {
        return definition;
    }

    public String getUOM() {
        return uom;
    }

    public boolean isSetUOM() {
        return !Strings.isNullOrEmpty(getUOM());
    }

    public static ElementType getValueElementType(PrimaryObservation primaryObs, String uom) {
        return new ElementType("Value", primaryObs, uom);
    }
}
