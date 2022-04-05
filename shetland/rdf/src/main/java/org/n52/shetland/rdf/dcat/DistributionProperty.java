/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.shetland.rdf.dcat;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.DCAT;
import org.n52.shetland.rdf.AbstractResource;

public class DistributionProperty extends AbstractResource implements DcatRdfPrefix {

    private Distribution distribution;

    public DistributionProperty(String value) {
        super(value);
    }

    public DistributionProperty(Distribution distribution) {
        super(null);
        this.distribution = distribution;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    @Override
    public Property getProperty() {
        return DCAT.distribution;
    }

    @Override
    public Resource addToResource(Model model, Resource parent) {
        if (getDistribution() != null) {
            parent.addProperty(getProperty(), getDistribution().getResource(model));
            return parent;
        }
        return super.addToResource(model, parent);
    }

}
