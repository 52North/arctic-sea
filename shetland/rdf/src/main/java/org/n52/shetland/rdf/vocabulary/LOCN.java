/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.rdf.vocabulary;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;

/**
 * Constants for the W3C Data Catalog Vocabulary.
 *
 * @see <a href="https://www.w3.org/ns/locn/">ISA Programme Location Core
 *         Vocabulary</a>
 */
@SuppressWarnings({"checkstyle:ConstantName", "checkstyle:LineLength", "checkstyle:DeclarationOrder"})
public class LOCN {
    private static final Model MODEL = ModelFactory.createDefaultModel();
    /**
     * The namespace of the vocabulary as a string.
     */

    public static final String NS = "http://www.w3.org/ns/locn#";

    /**
     * The namespace of the vocabulary as a resource.
     */
    public static final Resource NAMESPACE = MODEL.createResource(NS);

    // Classes
    public static final Resource Location = DCTerms.Location;
    public static final Resource Address = MODEL.createResource("http://www.w3.org/ns/locn#Address");
    public static final Resource Geometry = MODEL.createResource("http://www.w3.org/ns/locn#Geometry");

    // Properties
    public static final Property location = MODEL.createProperty("http://www.w3.org/ns/locn#location");
    public static final Property geographicName = MODEL.createProperty("http://www.w3.org/ns/locn#geographicName");
    public static final Property geometry = MODEL.createProperty("http://www.w3.org/ns/locn#geometry");
    public static final Property address = MODEL.createProperty("http://www.w3.org/ns/locn#address");
    public static final Property fullAddress = MODEL.createProperty("http://www.w3.org/ns/locn#fullAddress");
    public static final Property poBox = MODEL.createProperty("http://www.w3.org/ns/locn#poBox");
    public static final Property thoroughfare = MODEL.createProperty("http://www.w3.org/ns/locn#thoroughfare");
    public static final Property locatorDesignator = MODEL.createProperty("http://www.w3.org/ns/locn#locatorDesignator");
    public static final Property locatorName = MODEL.createProperty("http://www.w3.org/ns/locn#locatorName");
    public static final Property addressArea = MODEL.createProperty("http://www.w3.org/ns/locn#addressArea");
    public static final Property postName = MODEL.createProperty("http://www.w3.org/ns/locn#postName");
    public static final Property adminUnitL2 = MODEL.createProperty("http://www.w3.org/ns/locn#adminUnitL2");
    public static final Property adminUnitL1 = MODEL.createProperty("http://www.w3.org/ns/locn#adminUnitL1");
    public static final Property postCode = MODEL.createProperty("http://www.w3.org/ns/locn#postCode");
    public static final Property addressId = MODEL.createProperty("http://www.w3.org/ns/locn#addressId");

    /**
     * Returns the URI for this schema
     *
     * @return URI
     */
    public static String getURI() {
        return NS;
    }
}
