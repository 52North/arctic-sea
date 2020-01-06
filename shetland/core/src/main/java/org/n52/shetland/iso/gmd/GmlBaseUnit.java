/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import java.net.URI;

import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.util.IdGenerator;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GmlBaseUnit extends AbtractGmd {

    private static final String UCUM_URL = "http://www.opengis.net/def/uom/UCUM/";
    private String id;
    private final String identifier;
    private final CodeType catalogSymbol;
    private final String unitSystem;

    public GmlBaseUnit(String id, String identifier, CodeType catalogSymbol, String unitSystem) {
        this.id = id;
        this.identifier = identifier;
        this.catalogSymbol = catalogSymbol;
        this.unitSystem = unitSystem;
    }

    public String getId() {
        return id;
    }

    public GmlBaseUnit unifyId(Object object) {
        id = id + "_" + IdGenerator.generate(object.toString());
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public CodeType getCatalogSymbol() {
        return catalogSymbol;
    }

    public String getUnitSystem() {
        return unitSystem;
    }

    public static GmlBaseUnit uncertaintyEstimation() {
        return new GmlBaseUnit("PercentageUnit",
                "http://dd.eionet.europa.eu/vocabularies/aq/resultquality/uncertaintyestimation/",
                new CodeType("%", URI.create(UCUM_URL)), UCUM_URL);
    }

}
