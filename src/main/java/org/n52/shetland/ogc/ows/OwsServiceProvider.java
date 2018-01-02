/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
public class OwsServiceProvider {

    private final String providerName;
    private final Optional<OwsOnlineResource> providerSite;
    private final OwsResponsibleParty serviceContact;

    public OwsServiceProvider(String providerName,
                              OwsOnlineResource providerSite,
                              OwsResponsibleParty serviceContact) {
        this.providerName = Objects.requireNonNull(Strings.emptyToNull(providerName));
        this.providerSite = Optional.ofNullable(providerSite);
        this.serviceContact = Objects.requireNonNull(serviceContact);
    }

    public String getProviderName() {
        return providerName;
    }

    public Optional<OwsOnlineResource> getProviderSite() {
        return providerSite;
    }

    public OwsResponsibleParty getServiceContact() {
        return serviceContact;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.providerName);
        hash = 83 * hash + Objects.hashCode(this.providerSite);
        hash = 83 * hash + Objects.hashCode(this.serviceContact);
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
        final OwsServiceProvider other = (OwsServiceProvider) obj;
        if (!Objects.equals(this.providerName, other.providerName)) {
            return false;
        }
        if (!Objects.equals(this.providerSite, other.providerSite)) {
            return false;
        }
        if (!Objects.equals(this.serviceContact, other.serviceContact)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OwsServiceProvider{" + "providerName=" + providerName +
               ", providerSite=" + providerSite + ", serviceContact=" +
               serviceContact + '}';
    }


}
