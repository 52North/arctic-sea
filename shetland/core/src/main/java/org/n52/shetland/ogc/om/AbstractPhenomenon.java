/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.om;


import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * Abstract class for phenomena
 *
 * @since 1.0.0
 */
public abstract class AbstractPhenomenon extends AbstractFeature implements Comparable<AbstractPhenomenon> {

    /**
     * constructor
     *
     * @param identifier
     *            Phenomenon identifier
     */
    public AbstractPhenomenon(final String identifier) {
        super(new CodeWithAuthority(identifier));
    }

    /**
     * constructor
     *
     * @param identifier
     *            Phenomenon identifier
     * @param description
     *            Phenomenon description
     */
    public AbstractPhenomenon(final String identifier, final String description) {
        super(new CodeWithAuthority(identifier));
        setDescription(description);
    }

    @Override
    public boolean equals(final Object paramObject) {
        if (paramObject instanceof AbstractPhenomenon) {
            final AbstractPhenomenon phen = (AbstractPhenomenon) paramObject;
            return getIdentifierCodeWithAuthority().equals(phen.getIdentifierCodeWithAuthority());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIdentifierCodeWithAuthority());
    }

    @Override
    public int compareTo(final AbstractPhenomenon o) {
        return getIdentifierCodeWithAuthority().compareTo(o
                .getIdentifierCodeWithAuthority());
    }

    public abstract boolean isComposite();

    public abstract boolean isObservableProperty();

    public OmObservableProperty asObservableProperty() {
        return (OmObservableProperty) this;
    }

    public OmCompositePhenomenon asCompositePhenomenon() {
        return (OmCompositePhenomenon) this;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("identifier", getIdentifier())
                .add("description", getDescription())
                .toString();
    }
}
