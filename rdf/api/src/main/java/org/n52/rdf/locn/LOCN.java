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
package org.n52.rdf.locn;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * Constants for the W3C Data Catalog Vocabulary.
 *
 * @see <a href="https://www.w3.org/ns/locn/">ISA Programme Location Core Vocabulary</a>
 */
public class LOCN {
    private static final Model m = ModelFactory.createDefaultModel();

    public static final String NS = "http://www.w3.org/ns/locn#";

    public static final Resource NAMESPACE = m.createResource(NS);

    /**
     * Returns the URI for this schema
     *
     * @return URI
     */
    public static String getURI() {
        return NS;
    }

    // Classes
    public static final Resource Catalog = m.createResource(NS + "Location");
    public static final Resource CatalogRecord = m.createResource(NS + "Address");
    public static final Resource Dataset = m.createResource(NS + "Geometry");

    // Properties
    public static final Property location = m.createProperty(NS + "location");
    public static final Property geographicName = m.createProperty(NS + "geographicName");
    public static final Property geometry = m.createProperty(NS + "geometry");
    public static final Property address = m.createProperty(NS + "address");
    public static final Property fullAddress = m.createProperty(NS + "fullAddress");
    public static final Property poBox = m.createProperty(NS + "poBox");
    public static final Property thoroughfare = m.createProperty(NS + "thoroughfare");
    public static final Property locatorDesignator = m.createProperty(NS + "locatorDesignator");
    public static final Property locatorName = m.createProperty(NS + "locatorName");
    public static final Property addressArea = m.createProperty(NS + "addressArea");
    public static final Property postName = m.createProperty(NS + "postName");
    public static final Property adminUnitL2 = m.createProperty(NS + "adminUnitL2");
    public static final Property adminUnitL1 = m.createProperty(NS + "adminUnitL1");
    public static final Property postCode = m.createProperty(NS + "postCode");
    public static final Property addressId = m.createProperty(NS + "addressId");

}
