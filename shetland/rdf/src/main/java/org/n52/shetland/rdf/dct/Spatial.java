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
package org.n52.shetland.rdf.dct;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.DCTerms;
import org.n52.shetland.rdf.AbstractResource;

public class Spatial extends AbstractResource implements DctRdfPrefix {

    private List<Location> locations = new LinkedList<Location>();

    public Spatial(String value) {
        super(value);
    }

    public Spatial(Location location) {
        super(null);
        addLocation(location);
    }

    public Spatial(Collection<Location> locations) {
        super(null);
        setLocations(locations);
    }

    public List<Location> getLocations() {
        return locations;
    }

    public Spatial setLocations(Collection<Location> locations) {
        this.getLocations().clear();
        if (locations != null) {
            this.getLocations().addAll(locations);
        }
        return this;
    }

    public Spatial addLocation(Location location) {
        if (location != null) {
            this.getLocations().add(location);
        }
        return this;
    }

    @Override
    public Property getProperty() {
        return DCTerms.spatial;
    }

    @Override
    public Resource addToResource(Model model, Resource parent) {
        for (Location location : locations) {
            parent.addProperty(getProperty(), location.createResource(model, parent));
            return parent;
        }
        return super.addToResource(model, parent);
    }
}
