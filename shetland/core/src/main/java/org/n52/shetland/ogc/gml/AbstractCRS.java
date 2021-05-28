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
package org.n52.shetland.ogc.gml;

import java.util.ArrayList;
import java.util.List;

import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Lists;

/**
 * Internal representation of the OGC GML AbstractCRS.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractCRS
        extends IdentifiedObject {

    /* 0..* */
    private List<Referenceable<DomainOfValidity>> domainOfValidity = new ArrayList<>();
    /* 1..* */
    private List<String> scope = new ArrayList<>();

    public AbstractCRS(CodeWithAuthority identifier, String scope) {
        this(identifier, Lists.newArrayList(scope));
    }

    public AbstractCRS(CodeWithAuthority identifier, List<String> scope) {
        super(identifier);
        setScope(scope);
    }

    public List<Referenceable<DomainOfValidity>> getDomainOfValidity() {
        return domainOfValidity;
    }

    public AbstractCRS setDomainOfValidity(List<Referenceable<DomainOfValidity>> domainOfValidity) {
        this.domainOfValidity.clear();
        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(domainOfValidity)) {
            this.domainOfValidity.addAll(domainOfValidity);
        }
        return this;
    }

    public AbstractCRS addDomainOfValidity(List<Referenceable<DomainOfValidity>> domainOfValidity) {
        this.domainOfValidity.addAll(domainOfValidity);
        return this;
    }

    public AbstractCRS addDomainOfValidity(Referenceable<DomainOfValidity> domainOfValidity) {
        this.domainOfValidity.add(domainOfValidity);
        return this;
    }

    public boolean hasDomainOfValidity() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(getDomainOfValidity());
    }

    public List<String> getScope() {
        return scope;
    }

    public AbstractCRS setScope(List<String> scope) {
        this.scope.clear();
        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(scope)) {
            this.scope.addAll(scope);
        }
        return this;
    }

    public AbstractCRS addScope(List<String> scope) {
        this.scope.addAll(scope);
        return this;
    }

    public AbstractCRS addScope(String scope) {
        this.scope.add(scope);
        return this;
    }

    public boolean hasScope() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(getScope());
    }

}
