/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.DCAT;
import org.n52.shetland.rdf.AbstractResource;
import org.n52.shetland.rdf.vcard4.VCardOrganization;

public class ContactPoint extends AbstractResource implements DcatRdfPrefix {

    private List<VCardOrganization> organizations = new LinkedList<VCardOrganization>();

    public ContactPoint(String value) {
        super(value);
    }

    public ContactPoint(VCardOrganization organization) {
        super(null);
        addOrganization(organization);
    }

    public ContactPoint(Collection<VCardOrganization> organizations) {
        super(null);
        setOrganizations(organizations);
    }

    public List<VCardOrganization> getOrganizations() {
        return organizations;
    }

    private ContactPoint setOrganizations(Collection<VCardOrganization> organizations) {
        this.getOrganizations().clear();
        if (organizations != null) {
            this.getOrganizations().addAll(organizations);
        }
        return this;
    }

    private ContactPoint addOrganization(VCardOrganization organization) {
        if (organization != null) {
            this.getOrganizations().add(organization);
        }
        return this;
    }

    @Override
    public Property getProperty() {
        return DCAT.contactPoint;
    }

    @Override
    public Resource addToResource(Model model, Resource parent) {
        addNsPrefix(model);
        for (VCardOrganization organization : getOrganizations()) {
            parent.addProperty(getProperty(), organization.createResource(model, parent));
            return parent;
        }
        return super.addToResource(model, parent);
    }

}
