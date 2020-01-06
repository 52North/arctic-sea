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
package org.n52.shetland.rdf.locn;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

/**
 * Constants for the W3C Data Catalog Vocabulary.
 *
 * @see <a href="https://www.w3.org/ns/locn/">ISA Programme Location Core
 *      Vocabulary</a>
 */
public class LOCN {

    public static final Model MODEL = ModelFactory.createDefaultModel();

    public static final String NS = "http://www.w3.org/ns/locn#";

    public static final Resource NAMESPACE = MODEL.createResource(NS);

    // Classes
    public static final Resource CATALOG_RESOURCE = MODEL.createResource(NS + "Location");

    public static final Resource CATALOG_RECORD_RESOURCE = MODEL.createResource(NS + "Address");

    public static final Resource DATASET_RESOURCE = MODEL.createResource(NS + "Geometry");

    // Properties
    public static final Property LOCATION_PROPERTY = MODEL.createProperty(NS + "location");

    public static final Property GEOGRAPHIC_NAME_PROPERTY = MODEL.createProperty(NS + "geographicName");

    public static final Property GEOMETRY_PROPERTY = MODEL.createProperty(NS + "geometry");

    public static final Property ADDRESS_PROPERTY = MODEL.createProperty(NS + "address");

    public static final Property FULL_ADDRESS_PROPERTY = MODEL.createProperty(NS + "fullAddress");

    public static final Property PO_BOX_PROPERTY = MODEL.createProperty(NS + "poBox");

    public static final Property THOROUFH_FARE_PROPERTY = MODEL.createProperty(NS + "thoroughfare");

    public static final Property LOCATOR_DESIGNATOR_PROPERTY = MODEL.createProperty(NS + "locatorDesignator");

    public static final Property LOCATOR_NAME_PROPERTY = MODEL.createProperty(NS + "locatorName");

    public static final Property ADDRESS_AREA_PROPERTY = MODEL.createProperty(NS + "addressArea");

    public static final Property POST_NAME_PROPERTY = MODEL.createProperty(NS + "postName");

    public static final Property ADMIN_UNIT_L2_PROPERTY = MODEL.createProperty(NS + "adminUnitL2");

    public static final Property ADMIN_UNIT_L1_PROPERTY = MODEL.createProperty(NS + "adminUnitL1");

    public static final Property POST_CODE_PROPERTY = MODEL.createProperty(NS + "postCode");

    public static final Property ADDRESS__PROPERTY = MODEL.createProperty(NS + "addressId");

    /**
     * Returns the URI for this schema
     *
     * @return URI
     */
    public static String getURI() {
        return NS;
    }

}
