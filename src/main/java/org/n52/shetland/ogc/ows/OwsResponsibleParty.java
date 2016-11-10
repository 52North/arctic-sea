/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsResponsibleParty {

    private final Optional<String> individualName;
    private final Optional<String> organisationName;
    private final Optional<String> positionName;
    private final Optional<OwsContact> contactInfo;
    private final Optional<OwsCode> role;

    public OwsResponsibleParty(String individualName, String organisationName,
                               String positionName, OwsContact contactInfo,
                               OwsCode role) {
        this.individualName
                = Optional.ofNullable(Strings.emptyToNull(individualName));
        this.organisationName
                = Optional.ofNullable(Strings.emptyToNull(organisationName));
        this.positionName
                = Optional.ofNullable(Strings.emptyToNull(positionName));
        this.contactInfo = Optional.ofNullable(contactInfo);
        this.role = Optional.ofNullable(role);
    }

    public Optional<String> getIndividualName() {
        return individualName;
    }

    public Optional<String> getOrganisationName() {
        return organisationName;
    }

    public Optional<String> getPositionName() {
        return positionName;
    }

    public Optional<OwsContact> getContactInfo() {
        return contactInfo;
    }

    public Optional<OwsCode> getRole() {
        return role;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.individualName);
        hash = 79 * hash + Objects.hashCode(this.organisationName);
        hash = 79 * hash + Objects.hashCode(this.positionName);
        hash = 79 * hash + Objects.hashCode(this.contactInfo);
        hash = 79 * hash + Objects.hashCode(this.role);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsResponsibleParty other = (OwsResponsibleParty) obj;
        if (!Objects.equals(this.individualName, other.individualName)) {
            return false;
        }
        if (!Objects.equals(this.organisationName, other.organisationName)) {
            return false;
        }
        if (!Objects.equals(this.positionName, other.positionName)) {
            return false;
        }
        if (!Objects.equals(this.contactInfo, other.contactInfo)) {
            return false;
        }
        if (!Objects.equals(this.role, other.role)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsResponsibleParty{" + "individualName=" + individualName +
               ", organisationName=" + organisationName + ", positionName=" +
               positionName + ", contactInfo=" + contactInfo + ", role=" + role +
               '}';
    }


}
