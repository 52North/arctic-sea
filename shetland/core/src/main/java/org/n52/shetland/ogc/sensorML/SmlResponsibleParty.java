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
package org.n52.shetland.ogc.sensorML;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.util.CollectionHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Implementation for sml:ResponsibleParty
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SmlResponsibleParty extends SmlContact {

    private String individualName;

    private String organizationName;

    private String positionName;

    private List<String> phoneVoice = new LinkedList<>();

    private List<String> phoneFax = new LinkedList<>();

    private List<String> deliveryPoints = new LinkedList<>();

    private String city;

    private String administrativeArea;

    private String postalCode;

    private String country;

    private String email;

    private List<String> onlineResources = new LinkedList<>();

    private String hoursOfService;

    private String contactInstructions;

    public SmlResponsibleParty() {
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlResponsibleParty(final String individualName, final String organizationName, final String positionName,
            final Collection<String> phoneVoice, final Collection<String> phoneFax,
            final Collection<String> deliveryPoint, final String city, final String administrativeArea,
            final String postalCode, final String country, final String email, final Collection<String> onlineResource,
            final String hoursOfService, final String contactInstructions) {
        this.individualName = individualName;
        this.organizationName = organizationName;
        this.positionName = positionName;
        setPhoneVoice(phoneVoice);
        setPhoneFax(phoneFax);
        setDeliveryPoint(deliveryPoint);
        this.city = city;
        this.administrativeArea = administrativeArea;
        this.postalCode = postalCode;
        this.country = country;
        this.email = email;
        setOnlineResource(onlineResource);
        this.hoursOfService = hoursOfService;
        this.contactInstructions = contactInstructions;
    }

    public boolean isSetIndividualName() {
        return individualName != null && !individualName.isEmpty();
    }

    public String getIndividualName() {
        return individualName;
    }

    public SmlResponsibleParty setIndividualName(final String invidualName) {
        individualName = invidualName;
        return this;
    }

    public boolean isSetOrganizationName() {
        return organizationName != null && !organizationName.isEmpty();
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public SmlResponsibleParty setOrganizationName(final String organizationName) {
        this.organizationName = organizationName;
        return this;
    }

    public boolean isSetPositionName() {
        return positionName != null && !positionName.isEmpty();
    }

    public String getPositionName() {
        return positionName;
    }

    public SmlResponsibleParty setPositionName(final String positionName) {
        this.positionName = positionName;
        return this;
    }

    public boolean isSetPhoneVoice() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(phoneVoice);
    }

    public List<String> getPhoneVoice() {
        return Collections.unmodifiableList(phoneVoice);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlResponsibleParty setPhoneVoice(final Collection<String> phoneVoice) {
        this.phoneVoice.clear();
        if (CollectionHelper.isNotEmpty(phoneVoice)) {
            this.phoneVoice.addAll(phoneVoice);
        }
        return this;
    }

    public SmlResponsibleParty addPhoneVoice(final String phoneVoice) {
        if (phoneVoice != null) {
            this.phoneVoice.add(phoneVoice);
        }
        return this;
    }

    public boolean isSetPhoneFax() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(phoneFax);
    }

    public List<String> getPhoneFax() {
        return Collections.unmodifiableList(phoneFax);
    }

    public SmlResponsibleParty addPhoneFax(final String phoneFax) {
        if (phoneFax != null) {
            this.phoneFax.add(phoneFax);
        }
        return this;
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlResponsibleParty setPhoneFax(final Collection<String> phoneFax) {
        this.phoneFax.clear();
        if (CollectionHelper.isNotEmpty(phoneFax)) {
            this.phoneFax.addAll(phoneFax);
        }
        return this;
    }

    public boolean isSetDeliveryPoint() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(deliveryPoints);
    }

    public List<String> getDeliveryPoint() {
        return Collections.unmodifiableList(deliveryPoints);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlResponsibleParty setDeliveryPoint(final Collection<String> deliveryPoints) {
        this.deliveryPoints.clear();
        if (CollectionHelper.isNotEmpty(deliveryPoints)) {
            this.deliveryPoints.addAll(deliveryPoints);
        }
        return this;
    }

    public SmlResponsibleParty addDeliveryPoint(final String deliveryPoint) {
        if (deliveryPoint != null) {
            deliveryPoints.add(deliveryPoint);
        }
        return this;
    }

    public boolean isSetCity() {
        return city != null && !city.isEmpty();
    }

    public String getCity() {
        return city;
    }

    public SmlResponsibleParty setCity(final String city) {
        this.city = city;
        return this;
    }

    public boolean isSetAdministrativeArea() {
        return administrativeArea != null && !administrativeArea.isEmpty();
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }

    public SmlResponsibleParty setAdministrativeArea(final String administrativeArea) {
        this.administrativeArea = administrativeArea;
        return this;
    }

    public boolean isSetPostalCode() {
        return postalCode != null && !postalCode.isEmpty();
    }

    public String getPostalCode() {
        return postalCode;
    }

    public SmlResponsibleParty setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public boolean isSetCountry() {
        return country != null && !country.isEmpty();
    }

    public String getCountry() {
        return country;
    }

    public SmlResponsibleParty setCountry(final String country) {
        this.country = country;
        return this;
    }

    public boolean isSetEmail() {
        return email != null && !email.isEmpty();
    }

    public String getEmail() {
        return email;
    }

    public SmlResponsibleParty setEmail(final String email) {
        this.email = email;
        return this;
    }

    public boolean isSetOnlineResources() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(onlineResources);
    }

    public List<String> getOnlineResources() {
        return Collections.unmodifiableList(onlineResources);
    }

    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public SmlResponsibleParty setOnlineResource(final Collection<String> onlineResources) {
        this.onlineResources.clear();
        if (CollectionHelper.isNotEmpty(onlineResources)) {
            this.onlineResources.addAll(onlineResources);
        }
        return this;
    }

    public SmlResponsibleParty addOnlineResource(final String onlineResource) {
        if (onlineResource != null) {
            onlineResources.add(onlineResource);
        }
        return this;
    }

    public boolean isSetHoursOfService() {
        return hoursOfService != null && !hoursOfService.isEmpty();
    }

    public String getHoursOfService() {
        return hoursOfService;
    }

    public SmlResponsibleParty setHoursOfService(final String hoursOfService) {
        this.hoursOfService = hoursOfService;
        return this;
    }

    public boolean isSetContactInstructions() {
        return contactInstructions != null && !contactInstructions.isEmpty();
    }

    public String getContactInstructions() {
        return contactInstructions;
    }

    public SmlResponsibleParty setContactInstructions(final String contactInstructions) {
        this.contactInstructions = contactInstructions;
        return this;
    }

    public boolean isSetContactInfo() {
        return isSetPhone() || isSetAddress() || isSetOnlineResources() || isSetHoursOfService()
                || isSetContactInstructions();
    }

    public boolean isSetAddress() {
        return isSetDeliveryPoint() || isSetCity() || isSetAdministrativeArea() || isSetPostalCode() || isSetCountry()
                || isSetEmail();
    }

    public boolean isSetPhone() {
        return isSetPhoneFax() || isSetPhoneVoice();
    }
}
