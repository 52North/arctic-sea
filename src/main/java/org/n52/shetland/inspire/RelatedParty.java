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
package org.n52.shetland.inspire;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public class RelatedParty {
    private Nillable<String> individualName = Nillable.missing();
    private Nillable<String> organisationName = Nillable.missing();
    private Nillable<String> positionName = Nillable.missing();
    private Nillable<Contact> contact = Nillable.missing();
    private final List<Nillable<Reference>> roles = new LinkedList<>();

    public Nillable<String> getIndividualName() {
        return individualName;
    }

    public RelatedParty setIndividualName(Nillable<String> individualName) {
        this.individualName = Preconditions.checkNotNull(individualName);
        return this;
    }

    public RelatedParty setIndividualName(String individualName) {
        return setIndividualName(Nillable.of(individualName));
    }

    public Nillable<String> getOrganisationName() {
        return organisationName;
    }

    public RelatedParty setOrganisationName(Nillable<String> organisationName) {
        this.organisationName = Preconditions.checkNotNull(organisationName);
        return this;
    }

    public RelatedParty setOrganisationName(String organisationName) {
        return setOrganisationName(Nillable.of(organisationName));
    }

    public Nillable<String> getPositionName() {
        return positionName;
    }

    public RelatedParty setPositionName(Nillable<String> positionName) {
        this.positionName = Preconditions.checkNotNull(positionName);
        return this;
    }

    public RelatedParty setPositionName(String positionName) {
        return setPositionName(Nillable.of(positionName));
    }

    public Nillable<Contact> getContact() {
        return contact;
    }

    public RelatedParty setContact(Nillable<Contact> contact) {
        this.contact = Preconditions.checkNotNull(contact);
        return this;
    }

    public RelatedParty setContact(Contact contact) {
        return setContact(Nillable.of(contact));
    }

    public List<Nillable<Reference>> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public RelatedParty addRole(Nillable<Reference> role) {
        this.roles.add(Preconditions.checkNotNull(role));
        return this;
    }

    public RelatedParty addRole(Reference role) {
        return addRole(Nillable.of(role));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIndividualName(), getOrganisationName(),
                                getPositionName(), getContact(), getRoles());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RelatedParty) {
            RelatedParty that = (RelatedParty) obj;
            return Objects.equal(this.getIndividualName(), that.getIndividualName()) &&
                   Objects.equal(this.getOrganisationName(), that.getOrganisationName()) &&
                   Objects.equal(this.getPositionName(), that.getPositionName()) &&
                   Objects.equal(this.getContact(), that.getContact()) &&
                   Objects.equal(this.getRoles(), that.getRoles());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("individualName", getIndividualName())
                .add("organisationName", getOrganisationName())
                .add("positionName", getPositionName())
                .add("contact", getContact())
                .add("roles", getRoles())
                .toString();
    }
}
