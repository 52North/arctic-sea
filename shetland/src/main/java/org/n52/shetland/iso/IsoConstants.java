/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso;

import org.n52.shetland.w3c.SchemaLocation;

/**
 *
 * @author <a href="mailto:d.nuest@52north.org">Daniel Nüst</a>
 */
public interface IsoConstants {

    String NS_GMD_2005 = "http://www.isotc211.org/2005/gmd";

    String NS_GMD_2005_PREFIX = "gmd";

    String SCHEMA_LOCATION_URL_GMD_2005 = "http://www.isotc211.org/2005/gmd/gmd.xsd";

    SchemaLocation GMD_2005_20_SCHEMA_LOCATION = new SchemaLocation(NS_GMD_2005, SCHEMA_LOCATION_URL_GMD_2005);

    String NS_GCO_2005 = "http://www.isotc211.org/2005/gco";

    String NS_GCO_2005_PREFIX = "gco";

    String SCHEMA_LOCATION_URL_GCO_2005 = "http://www.isotc211.org/2005/gco/gco.xsd";

    SchemaLocation GCO_2005_20_SCHEMA_LOCATION = new SchemaLocation(NS_GCO_2005, SCHEMA_LOCATION_URL_GCO_2005);

    String NS_GMI_2005 = "http://www.isotc211.org/2005/gmi";

    String NS_GMI_2005_PREFIX = "gmi";

    String SCHEMA_LOCATION_URL_GMI_2005 = "http://www.isotc211.org/2005/gmi/gmi.xsd";

    SchemaLocation GMI_2005_20_SCHEMA_LOCATION = new SchemaLocation(NS_GMI_2005, SCHEMA_LOCATION_URL_GMI_2005);

}
