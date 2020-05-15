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
package org.n52.shetland.rdf;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public abstract class AbstractDatatype extends AbstractValue {

    private final RDFDatatype dataType;

    @Deprecated
    public AbstractDatatype(DataType dataType, String value) {
        this(getDataType(dataType), value);
    }

    public AbstractDatatype(RDFDatatype dataType, String value) {
        super(value);
        this.dataType = dataType;
    }

    public AbstractDatatype(String dataType, String value) {
        this(new BaseDatatype(dataType), value);
    }

    @Override
    public Resource addToResource(Model model, Resource parent) {
        addNsPrefix(model);
        parent.addProperty(getProperty(), getValue(), getDataType());
        return parent;
    }

    public RDFDatatype getDataType() {
        return dataType;
    }

    @Deprecated
    private static RDFDatatype getDataType(DataType dataType) {
        switch (dataType) {
            case Date:
                return XSDDatatype.XSDdate;
            case DateTime:
                return XSDDatatype.XSDdateTime;
            case GEO_JSON:
                return RDFDataTypes.GEO_JSON;
            case WKT_LITERAL:
                return RDFDataTypes.WKT_LITERAL;
            default:
                return new BaseDatatype(dataType.getType());
        }
    }

    @Deprecated
    public enum DataType {
        Date("http://www.w3.org/2001/XMLSchema#date"),
        DateTime("http://www.w3.org/2001/XMLSchema#dateTime"),
        WKT_LITERAL("http://www.opengis.net/ont/geosparql#wktLiteral"),
        GEO_JSON("https://www.iana.org/assignments/media-types/application/geo+json");

        private final String type;

        DataType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }
}
