/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import javax.xml.namespace.QName;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.w3c.SchemaLocation;

public interface GmdConstants {

    String NS_GMD = "http://www.isotc211.org/2005/gmd";

    String NS_GMD_PREFIX = "gmd";

    String SCHEMA_LOCATION_URL_GMD = "http://schemas.opengis.net/iso/19139/20070417/gmd/gmd.xsd";

    SchemaLocation GMD_SCHEMA_LOCATION = new SchemaLocation(NS_GMD, SCHEMA_LOCATION_URL_GMD);

    QName QN_GMD_CONFORMANCE_RESULT = new QName(GmdConstants.NS_GMD, "DQ_ConformanceResult",
            GmdConstants.NS_GMD_PREFIX);

    QName QN_GMD_QUANTITATIVE_RESULT = new QName(GmdConstants.NS_GMD, "DQ_QuantitativeResult",
            GmdConstants.NS_GMD_PREFIX);

    QName QN_GML_BASE_UNIT = new QName(GmlConstants.NS_GML_32, "BaseUnit", GmlConstants.NS_GML_PREFIX);
}
