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
package org.n52.shetland.iso;

import javax.xml.namespace.QName;

import org.n52.shetland.w3c.SchemaLocation;

public interface GcoConstants {
    String NS_GCO = "http://www.isotc211.org/2005/gco";

    String NS_GCO_PREFIX = "gco";

    String SCHEMA_LOCATION_URL_GCO = "http://schemas.opengis.net/iso/19139/20070417/gco/gco.xsd";

    SchemaLocation GCO_SCHEMA_LOCATION = new SchemaLocation(NS_GCO, SCHEMA_LOCATION_URL_GCO);

    String EN_CHARACTER_STRING = "CharacterString";

    String AN_NIL_REASON = "nilReason";

    QName QN_GCO_DATE = new QName(GcoConstants.NS_GCO, "Date", GcoConstants.NS_GCO_PREFIX);

    QName QN_GCO_CHARACTER_STRING = new QName(NS_GCO, EN_CHARACTER_STRING, NS_GCO_PREFIX);

    QName QN_GCO_NIL_REASON = new QName(NS_GCO, AN_NIL_REASON, NS_GCO_PREFIX);

}
