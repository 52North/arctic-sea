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
package org.n52.shetland.ogc.sos.ro;

import java.util.Set;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.ows.extension.Extension;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class RelatedOfferings implements Extension<Set<OfferingContext>> {
    private String identifier;
    private String definition;
    private Set<OfferingContext> offeringRelations = Sets.newHashSet();


    @Override
    public String getNamespace() {
        return RelatedOfferingConstants.NS_RO;
    }

    @Override
    public Extension<Set<OfferingContext>> setNamespace(String namespace) {
        return this;
    }

    @Override
    public boolean isSetNamespace() {
        return !Strings.isNullOrEmpty(getNamespace());
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public Extension<Set<OfferingContext>> setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public boolean isSetIdentifier() {
        return !Strings.isNullOrEmpty(getIdentifier());
    }

    @Override
    public String getDefinition() {
        return definition;
    }

    @Override
    public Extension<Set<OfferingContext>> setDefinition(String definition) {
        this.definition = definition;
        return this;
    }

    @Override
    public boolean isSetDefinition() {
        return !Strings.isNullOrEmpty(getDefinition());
    }

    @Override
    public Set<OfferingContext> getValue() {
        return offeringRelations;
    }

    @Override
    public Extension<Set<OfferingContext>> setValue(Set<OfferingContext> value) {
        this.offeringRelations.clear();
        this.offeringRelations.addAll(value);
        return this;
    }

    public Extension<Set<OfferingContext>> addValue(String role, String offering) {
        this.offeringRelations.add(new OfferingContext(new ReferenceType(role), new ReferenceType(offering)));
        return this;
    }

    public Extension<Set<OfferingContext>> addValue(ReferenceType role, ReferenceType offering) {
        this.offeringRelations.add(new OfferingContext(role, offering));
        return this;
    }

    public Extension<Set<OfferingContext>> addValue(OfferingContext offeringContext) {
        this.offeringRelations.add(offeringContext);
        return this;
    }

}
