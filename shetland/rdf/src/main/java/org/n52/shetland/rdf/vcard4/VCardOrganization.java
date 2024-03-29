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
package org.n52.shetland.rdf.vcard4;

import org.apache.jena.rdf.model.Resource;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.vocabulary.VCARD4;
import org.n52.shetland.rdf.ResourceCreator;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class VCardOrganization implements ResourceCreator, Vcard4RdfPrefix {

    private FN fn;

    private Email email;

    private HasEmail hasEmail;

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public FN getFn() {
        return fn;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setFn(FN fn) {
        this.fn = fn;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public Email getEmail() {
        return email;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setEmail(Email email) {
        this.email = email;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public HasEmail getHasEmail() {
        return hasEmail;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setHasEmail(HasEmail hasEmail) {
        this.hasEmail = hasEmail;
    }

    @Override
    public Resource createResource(Model model, Resource parent) {
        addNsPrefix(model);
        Resource organization = model.createResource(VCARD4.Organization);
        if (getFn() != null) {
            getFn().addToResource(model, organization);
        }
        if (getEmail() != null) {
            getEmail().addToResource(model, organization);
        }
        if (getHasEmail() != null) {
            getHasEmail().addToResource(model, organization);
        }
        return organization;
    }

}
