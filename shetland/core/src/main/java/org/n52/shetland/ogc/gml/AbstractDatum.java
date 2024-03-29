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
package org.n52.shetland.ogc.gml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Lists;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Internal representation of the OGC GML AbstractDatum.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractDatum extends IdentifiedObject {

    /* 0..1 */
    private Referenceable<DomainOfValidity> domainOfValidity;
    /* 1..* */
    private List<String> scope = new ArrayList<>();
    /* 0..1 */
    private CodeType anchorDefinition;
    /* 0..1 */
    private DateTime realizationEpoch;

    public AbstractDatum(CodeWithAuthority identifier, String scope) {
        this(identifier, Lists.newArrayList(scope));
    }

    public AbstractDatum(CodeWithAuthority identifier, Collection<String> scope) {
        super(identifier);
        setScope(scope);
    }

    public Referenceable<DomainOfValidity> getDomainOfValidity() {
        return domainOfValidity;
    }

    public AbstractDatum setDomainOfValidity(Referenceable<DomainOfValidity> domainOfValidity) {
        this.domainOfValidity = domainOfValidity;
        return this;
    }

    public AbstractDatum setDomainOfValidity(DomainOfValidity domainOfValidity) {
        if (domainOfValidity != null) {
            this.domainOfValidity = Referenceable.of(domainOfValidity);
        } else {
            this.domainOfValidity = Referenceable.of(Nillable.<
                    DomainOfValidity> missing());
        }
        return this;
    }

    public boolean hasDomainOfValidity() {
        return getDomainOfValidity() != null;
    }

    public List<String> getScope() {
        return Collections.unmodifiableList(scope);
    }

    public AbstractDatum setScope(Collection<String> scope) {
        this.scope.clear();
        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(scope)) {
            this.scope.addAll(scope);
        }
        return this;
    }

    public AbstractDatum addScope(Collection<String> scope) {
        if (!CollectionHelper.nullEmptyOrContainsOnlyNulls(scope)) {
            this.scope.addAll(scope);
        }
        return this;
    }

    public AbstractDatum addScope(String scope) {
        if (scope != null) {
            this.scope.add(scope);
        }
        return this;
    }

    public boolean hasScope() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(getScope());
    }

    public CodeType getAnchorDefinition() {
        return anchorDefinition;
    }

    public AbstractDatum setAnchorDefinition(CodeType anchorDefinition) {
        this.anchorDefinition = anchorDefinition;
        return this;
    }

    public boolean hasAnchorDefinition() {
        return getAnchorDefinition() != null && getAnchorDefinition().isSetValue();
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public DateTime getRealizationEpoch() {
        return realizationEpoch;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public AbstractDatum setRealizationEpoch(DateTime realizationEpoch) {
        this.realizationEpoch = realizationEpoch;
        return this;
    }

    public boolean hasRealizationEpoch() {
        return getRealizationEpoch() != null;
    }

}
