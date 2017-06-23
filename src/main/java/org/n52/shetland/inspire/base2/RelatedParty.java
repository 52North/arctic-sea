/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire.base2;

import java.util.Set;

import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

public class RelatedParty {

    /**
     * 0..1
     */
    private PT_FreeText individualName;

    /**
     * 0..1
     */
    private PT_FreeText organisationName;

    /**
     * 0..1
     */
    private PT_FreeText positionName;

    /**
     * 0..1
     */
    private Contact contact;

    /**
     * 0..*
     */
    private Set<ReferenceType> role = Sets.newHashSet();

    /**
     * @return the individualName
     */
    public PT_FreeText getIndividualName() {
        return individualName;
    }

    /**
     * @param individualName the individualName to set
     */
    public void setIndividualName(PT_FreeText individualName) {
        this.individualName = individualName;
    }

    public boolean isSetIndividualName() {
        return getIndividualName() != null;
    }

    /**
     * @return the organisationName
     */
    public PT_FreeText getOrganisationName() {
        return organisationName;
    }

    /**
     * @param organisationName the organisationName to set
     */
    public void setOrganisationName(PT_FreeText organisationName) {
        this.organisationName = organisationName;
    }

    public boolean isSetOrganisationName() {
        return getOrganisationName() != null;
    }

    /**
     * @return the positionName
     */
    public PT_FreeText getPositionName() {
        return positionName;
    }

    /**
     * @param positionName the positionName to set
     */
    public void setPositionName(PT_FreeText positionName) {
        this.positionName = positionName;
    }

    public boolean isSetPositionName() {
        return getPositionName() != null;
    }

    /**
     * @return the contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public boolean isSetContact() {
        return getContact() != null;
    }

    /**
     * @return the role
     */
    public Set<ReferenceType> getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Set<ReferenceType> role) {
        this.role = role;
    }

    public boolean isSetRole() {
        return CollectionHelper.isNotEmpty(getRole());
    }

}
