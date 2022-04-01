/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.rdf;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.RDFDatatype;

public interface RDFDataTypes {
    RDFDatatype WKT_LITERAL = new BaseDatatype("http://www.opengis.net/ont/geosparql#wktLiteral");
    RDFDatatype GML_LITERAL = new BaseDatatype("http://www.opengis.net/ont/geosparql#gmlLiteral");
    RDFDatatype GEO_JSON = new BaseDatatype("https://www.iana.org/assignments/media-types/application/geo+json");
}
