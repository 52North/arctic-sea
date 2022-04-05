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
package org.n52.shetland.inspire.base2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class RelatedParty {

    /**
     * 0..1
     */
    private Nillable<PT_FreeText> individualName = Nillable.missing();

    /**
     * 0..1
     */
    private Nillable<PT_FreeText> organisationName = Nillable.missing();

    /**
     * 0..1
     */
    private Nillable<PT_FreeText> positionName = Nillable.missing();

    /**
     * 0..1
     */
    private Nillable<Contact> contact = Nillable.missing();

    /**
     * 0..*
     */
    private final List<Nillable<Reference>> roles = new LinkedList<>();

    /**
     * @return the individualName
     */
    public Nillable<PT_FreeText> getIndividualName() {
        return individualName;
    }

    /**
     * @param individualName
     *            the individualName to set
     * @return this {@link RelatedParty}
     */
    public RelatedParty setIndividualName(PT_FreeText individualName) {
        return setIndividualName(Nillable.of(individualName));
    }

    public RelatedParty setIndividualName(String individualName) {
        return setIndividualName(
                Nillable.of(new PT_FreeText().addTextGroup(new LocalisedCharacterString(individualName))));
    }

    public RelatedParty setIndividualName(Nillable<PT_FreeText> individualName) {
        this.individualName = Preconditions.checkNotNull(individualName);
        return this;
    }

    public boolean isSetIndividualName() {
        return getIndividualName() != null && getIndividualName().isPresent();
    }

    /**
     * @return the organisationName
     */
    public Nillable<PT_FreeText> getOrganisationName() {
        return organisationName;
    }

    /**
     * @param organisationName
     *            the organisationName to set
     * @return this {@link RelatedParty}
     */
    public RelatedParty setOrganisationName(PT_FreeText organisationName) {
        return setOrganisationName(Nillable.of(organisationName));
    }

    public RelatedParty setOrganisationName(String organisationName) {
        return setOrganisationName(
                Nillable.of(new PT_FreeText().addTextGroup(new LocalisedCharacterString(organisationName))));
    }

    public RelatedParty setOrganisationName(Nillable<PT_FreeText> organisationName) {
        this.organisationName = Preconditions.checkNotNull(organisationName);
        return this;
    }

    public boolean isSetOrganisationName() {
        return getOrganisationName() != null && getIndividualName().isPresent();
    }

    /**
     * @return the positionName
     */
    public Nillable<PT_FreeText> getPositionName() {
        return positionName;
    }

    /**
     * @param positionName
     *            the positionName to set
     * @return this {@link RelatedParty}
     */
    public RelatedParty setPositionName(PT_FreeText positionName) {
        return setPositionName(Nillable.of(positionName));
    }

    public RelatedParty setPositionName(String positionName) {
        return setPositionName(
                Nillable.of(new PT_FreeText().addTextGroup(new LocalisedCharacterString(positionName))));
    }

    public RelatedParty setPositionName(Nillable<PT_FreeText> positionName) {
        this.positionName = Preconditions.checkNotNull(positionName);
        return this;
    }

    public boolean isSetPositionName() {
        return getPositionName() != null && getPositionName().isPresent();
    }

    /**
     * @return the contact
     */
    public Nillable<org.n52.shetland.inspire.base2.Contact> getContact() {
        return contact;
    }

    /**
     * @param contact
     *            the contact to set
     */
    public RelatedParty setContact(Contact contact) {
        return setContact(Nillable.of(contact));
    }

    public RelatedParty setContact(Nillable<Contact> contact) {
        this.contact = Preconditions.checkNotNull(contact);
        return this;
    }

    public boolean isSetContact() {
        return getContact() != null && getContact().isPresent();
    }

    /**
     * @return the role
     */
    public List<Nillable<Reference>> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    /**
     * @param roles
     *            the roles to set
     */
    public void setRoles(List<Nillable<Reference>> roles) {
        this.roles.clear();
        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public RelatedParty addRole(Nillable<Reference> role) {
        this.roles.add(Preconditions.checkNotNull(role));
        return this;
    }

    public RelatedParty addRole(Reference role) {
        return addRole(Nillable.of(role));
    }

    public boolean isSetRole() {
        return CollectionHelper.isNotEmpty(getRoles());
    }
    // }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIndividualName(), getOrganisationName(), getPositionName(), getContact(),
                getRoles());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RelatedParty) {
            RelatedParty that = (RelatedParty) obj;
            return Objects.equal(this.getIndividualName(), that.getIndividualName())
                    && Objects.equal(this.getOrganisationName(), that.getOrganisationName())
                    && Objects.equal(this.getPositionName(), that.getPositionName())
                    && Objects.equal(this.getContact(), that.getContact())
                    && Objects.equal(this.getRoles(), that.getRoles());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("individualName", getIndividualName())
                .add("organisationName", getOrganisationName()).add("positionName", getPositionName())
                .add("contact", getContact()).add("roles", getRoles()).toString();
    }
}
