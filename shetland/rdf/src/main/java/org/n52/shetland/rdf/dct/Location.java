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
package org.n52.shetland.rdf.dct;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.DCTerms;
import org.n52.shetland.rdf.ResourceCreator;
import org.n52.shetland.rdf.locn.Geometry;

public class Location implements ResourceCreator, DctRdfPrefix {

    private List<Geometry> geometries = new LinkedList<>();

    public Location(Geometry geometry) {
        addGeometry(geometry);
    }

    public Location(Collection<Geometry> geometries) {
        setGeometries(geometries);
    }

    public List<Geometry> getGeometries() {
        return geometries;
    }

    public Location setGeometries(Collection<Geometry> geometries) {
        this.getGeometries().clear();
        if (geometries != null) {
            this.getGeometries().addAll(geometries);
        }
        return this;
    }

    public Location addGeometry(Geometry geometry) {
        if (geometry != null) {
            this.getGeometries().add(geometry);
        }
        return this;
    }

    @Override
    public Resource createResource(Model model, Resource parent) {
        addNsPrefix(model);
        Resource location = model.createResource(DCTerms.Location);
        for (Geometry geometry : getGeometries()) {
            geometry.addToResource(model, location);
        }
        return location;
    }

}
