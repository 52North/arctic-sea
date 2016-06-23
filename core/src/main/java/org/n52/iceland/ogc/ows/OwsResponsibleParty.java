/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

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

}
