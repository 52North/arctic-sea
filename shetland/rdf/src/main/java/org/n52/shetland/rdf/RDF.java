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

import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;

public class RDF implements ModelAdder {

    private List<RDFElement> elements = new LinkedList<RDFElement>();

    public List<RDFElement> getElements() {
        return elements;
    }

    public RDF setElements(List<RDFElement> elements) {
        this.getElements().clear();
        if (elements != null) {
            this.getElements().addAll(elements);
        }
        return this;
    }

    public RDF addElements(RDFElement element) {
        if (element != null) {
            this.getElements().add(element);
        }
        return this;
    }

    @Override
    public Model addToModel(Model model) {
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        for (RDFElement element : getElements()) {
            element.addToModel(model);
        }
        return model;
    }

}
