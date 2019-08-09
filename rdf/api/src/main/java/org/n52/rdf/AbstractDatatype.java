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
package org.n52.rdf;

import org.apache.jena.datatypes.BaseDatatype;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.impl.XSDDateType;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

public abstract class AbstractDatatype extends AbstractValue {

    private final DataType dataType;

    public AbstractDatatype(DataType dataType, String value) {
        super(value);
        this.dataType = dataType;
    }

    @Override
    public Resource addToResource(Model model, Resource parent) {
        addNsPrefix(model);
        parent.addProperty(getProperty(), getValue(), getDataType());
        return parent;
    }

    private RDFDatatype getDataType() {
        if (dataType.equals(DataType.Date)) {
            return XSDDateType.XSDdate;
        } else if (dataType.equals(DataType.DateTime)) {
            return XSDDateType.XSDdateTime;
        }
        return new DynamicDatatype(dataType.getType());
    }

    public enum DataType {
        Date("http://www.w3.org/2001/XMLSchema#date"),
        DateTime("http://www.w3.org/2001/XMLSchema#dateTime"),
        WKT_LITERAL("http://www.opengis.net/ont/geosparql#wktLiteral"),
        GEO_JSON("https://www.iana.org/assignments/media-types/application/vnd.geo+json");


        private final String type;

        DataType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

    public static class DynamicDatatype extends BaseDatatype implements RDFDatatype {

        private DynamicDatatype(String uri) {
            super(uri);
        }

    }
}
