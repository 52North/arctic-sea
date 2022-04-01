/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire.ad;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.inspire.GeographicalName;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class AddressRepresentation {
    /**
     * 1..*
     */
    private final List<GeographicalName> adminUnits = new LinkedList<>();

    /**
     * 0..*
     */
    private final List<String> locatorDesignators = new LinkedList<>();

    /**
     * 0..*
     */
    private final List<GeographicalName> locatorNames = new LinkedList<>();

    /**
     * 0..*
     */
    private final List<Nillable<GeographicalName>> addressAreas = new LinkedList<>();

    /**
     * 0..*
     */
    private final List<Nillable<GeographicalName>> postNames = new LinkedList<>();

    /**
     * 0..1
     */
    private Nillable<String> postCode = Nillable.missing();

    /**
     * 0..*
     */
    private final List<Nillable<GeographicalName>> thoroughfares = new LinkedList<>();

    /**
     * 0..1
     */
    private Nillable<Reference> addressFeature = Nillable.missing();

    public List<GeographicalName> getAdminUnits() {
        if (CollectionHelper.isEmpty(adminUnits)) {
            addAdminUnit(new GeographicalName());
        }
        return Collections.unmodifiableList(adminUnits);
    }

    public AddressRepresentation addAdminUnit(GeographicalName adminUnit) {
        this.adminUnits.add(Preconditions.checkNotNull(adminUnit));
        return this;
    }

    public List<String> getLocatorDesignators() {
        return Collections.unmodifiableList(locatorDesignators);
    }

    public AddressRepresentation addLocatorDesignator(String locatorDesignator) {
        this.locatorDesignators.add(Preconditions.checkNotNull(locatorDesignator));
        return this;
    }

    public List<GeographicalName> getLocatorNames() {
        return Collections.unmodifiableList(locatorNames);
    }

    public AddressRepresentation addLocatorName(GeographicalName locatorName) {
        this.locatorNames.add(Preconditions.checkNotNull(locatorName));
        return this;
    }

    public List<Nillable<GeographicalName>> getAddressAreas() {
        return Collections.unmodifiableList(addressAreas);
    }

    public AddressRepresentation addAddressArea(Nillable<GeographicalName> addressArea) {
        this.addressAreas.add(Preconditions.checkNotNull(addressArea));
        return this;
    }

    public AddressRepresentation addAddressArea(GeographicalName addressArea) {
        return addAddressArea(Nillable.of(addressArea));
    }

    public List<Nillable<GeographicalName>> getPostNames() {
        return Collections.unmodifiableList(postNames);
    }

    public AddressRepresentation addPostName(Nillable<GeographicalName> postName) {
        this.postNames.add(Preconditions.checkNotNull(postName));
        return this;
    }

    public AddressRepresentation addPostName(GeographicalName postName) {
        return addPostName(Nillable.of(postName));
    }

    public Nillable<String> getPostCode() {
        return postCode;
    }

    public AddressRepresentation setPostCode(Nillable<String> postCode) {
        this.postCode = postCode;
        return this;
    }

    public AddressRepresentation setPostCode(String postCode) {
        return setPostCode(Nillable.of(postCode));
    }

    public List<Nillable<GeographicalName>> getThoroughfares() {
        return Collections.unmodifiableList(thoroughfares);
    }

    public AddressRepresentation addThoroughfare(Nillable<GeographicalName> thoroughfare) {
        this.thoroughfares.add(Preconditions.checkNotNull(thoroughfare));
        return this;
    }

    public AddressRepresentation addThoroughfare(GeographicalName thoroughfare) {
        return addThoroughfare(Nillable.of(thoroughfare));
    }

    public Nillable<Reference> getAddressFeature() {
        return addressFeature;
    }

    public AddressRepresentation setAddressFeature(Nillable<Reference> addressFeature) {
        this.addressFeature = Preconditions.checkNotNull(addressFeature);
        return this;
    }

    public AddressRepresentation setAddressFeature(Reference addressFeature) {
        return setAddressFeature(Nillable.of(addressFeature));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAdminUnits(), getLocatorDesignators(), getLocatorNames(), getAddressAreas(),
                getPostNames(), getPostCode(), getThoroughfares(), getAddressFeature());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AddressRepresentation) {
            AddressRepresentation that = (AddressRepresentation) obj;
            return Objects.equal(getAdminUnits(), that.getAdminUnits())
                    && Objects.equal(getLocatorDesignators(), that.getLocatorDesignators())
                    && Objects.equal(getLocatorNames(), that.getLocatorNames())
                    && Objects.equal(getAddressAreas(), that.getAddressAreas())
                    && Objects.equal(getPostNames(), that.getPostNames())
                    && Objects.equal(getPostCode(), that.getPostCode())
                    && Objects.equal(getThoroughfares(), that.getThoroughfares())
                    && Objects.equal(getAddressFeature(), that.getAddressFeature());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("adminUnits", getAdminUnits())
                .add("locatorDesignator", getLocatorDesignators()).add("locatorNames", getLocatorNames())
                .add("addressAreas", getAddressAreas()).add("postNames", getPostNames()).add("postCode", getPostCode())
                .add("thoroughfares", getThoroughfares()).add("addressFeature", getAddressFeature()).toString();
    }
}
