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
package org.n52.shetland.rdf.foaf;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.n52.shetland.rdf.RDFElement;

public class Organization implements RDFElement, FoafRdfPrefix {

    private Name name;

    private MBox mBox;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public MBox getmBox() {
        return mBox;
    }

    public void setmBox(MBox mBox) {
        this.mBox = mBox;
    }

    @Override
    public Model addToModel(Model model) {
        addNsPrefix(model);
        Resource organization = model.createResource(FOAF.NS);
        organization.addProperty(RDF.type, FOAF.Organization);

        if (getmBox() != null) {
            getmBox().addToResource(model, organization);
        }

        if (getName() != null) {
            getName().addToResource(model, organization);
        }
        return model;
    }

}
