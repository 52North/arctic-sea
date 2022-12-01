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
package org.n52.shetland.inspire.base2;

import java.util.List;

import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.w3c.Nillable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @author Christian Autermann
 * @since
 *
 */
public class Contact {

    /**
     * 0..1
     */
    private Nillable<AddressRepresentation> address = Nillable.missing();

    /**
     * 0..1
     */
    private Nillable<PT_FreeText> contactInstructions = Nillable.missing();

    /**
     * 0..1
     */
    private Nillable<String> electronicMailAddress = Nillable.missing();

    /**
     * 0..1
     */
    private Nillable<PT_FreeText> hoursOfService = Nillable.missing();

    /**
     * 0..*
     */
    private Nillable<List<Nillable<String>>> telephoneFacsimile = Nillable.missing();

    /**
     * 0..*
     */
    private Nillable<List<Nillable<String>>> telephoneVoice = Nillable.missing();

    /**
     * 0..1
     */
    private Nillable<String> website;

    /**
     * @return the address
     */
    public Nillable<AddressRepresentation> getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public Contact setAddress(AddressRepresentation address) {
        return setAddress(Nillable.of(address));
    }

    public Contact setAddress(Nillable<AddressRepresentation> address) {
        this.address = Preconditions.checkNotNull(address);
        return this;
    }

    /**
     * @return the contactInstructions
     */
    public Nillable<PT_FreeText> getContactInstructions() {
        return contactInstructions;
    }

    /**
     * @param contactInstructions
     *            the contactInstructions to set
     */
    public Contact setContactInstructions(PT_FreeText contactInstructions) {
        return setContactInstructions(Nillable.of(contactInstructions));
    }

    /**
     * @param contactInstructions
     *            the contactInstructions to set
     */
    public Contact setContactInstructions(Nillable<PT_FreeText> contactInstructions) {
        this.contactInstructions = Preconditions.checkNotNull(contactInstructions);
        return this;
    }

    /**
     * @return the electronicMailAddress
     */
    public Nillable<String> getElectronicMailAddress() {
        return electronicMailAddress;
    }

    /**
     * @param electronicMailAddress
     *            the electronicMailAddress to set
     */
    public Contact setElectronicMailAddress(String electronicMailAddress) {
        return setElectronicMailAddress(Nillable.of(electronicMailAddress));
    }

    /**
     * @param electronicMailAddress
     *            the electronicMailAddress to set
     */
    public Contact setElectronicMailAddress(Nillable<String> electronicMailAddress) {
        this.electronicMailAddress = Preconditions.checkNotNull(electronicMailAddress);
        return this;
    }

    /**
     * @return the hoursOfService
     */
    public Nillable<PT_FreeText> getHoursOfService() {
        return hoursOfService;
    }

    /**
     * @param hoursOfService
     *            the hoursOfService to set
     */
    public Contact setHoursOfService(Nillable<PT_FreeText> hoursOfService) {
        this.hoursOfService = Preconditions.checkNotNull(hoursOfService);
        return this;
    }

    /**
     * @param hoursOfService
     *            the hoursOfService to set
     */
    public Contact setHoursOfService(PT_FreeText hoursOfService) {
        return setHoursOfService(Nillable.of(hoursOfService));
    }

    /**
     * @return the telephoneFacsimile
     */
    public Nillable<List<Nillable<String>>> getTelephoneFacsimile() {
        return telephoneFacsimile;
    }

    /**
     * @param telephoneFacsimile
     *            the telephoneFacsimile to set
     */
    public Contact setTelephoneFacsimile(List<Nillable<String>> telephoneFacsimile) {
        return setTelephoneFacsimile(Nillable.of(telephoneFacsimile));
    }

    /**
     * @param telephoneFacsimile
     *            the telephoneFacsimile to set
     */
    public Contact setTelephoneFacsimile(Nillable<List<Nillable<String>>> telephoneFacsimile) {
        this.telephoneFacsimile = Preconditions.checkNotNull(telephoneFacsimile);
        return this;
    }

    /**
     * @param telephoneFacsimile
     *            the telephoneFacsimile to add
     */
    public Contact addTelephoneFacsimile(String telephoneFacsimile) {
        return addTelephoneFacsimile(Nillable.of(Preconditions.checkNotNull(telephoneFacsimile)));
    }

    public Contact addTelephoneFacsimile(Nillable<String> telephoneFacsimile) {
        if (this.telephoneFacsimile.isAbsent() || this.telephoneFacsimile.isNil()) {
            this.telephoneFacsimile = Nillable.of((List<Nillable<String>>) Lists.<Nillable<String>> newArrayList());
        }
        if (this.telephoneFacsimile.isPresent()) {
            this.telephoneFacsimile.get().add(telephoneFacsimile);
        }
        return this;
    }

    /**
     * @return the telephoneVoice
     */
    public Nillable<List<Nillable<String>>> getTelephoneVoice() {
        return telephoneVoice;
    }

    /**
     * @param telephoneVoice
     *            the telephoneVoice to set
     */
    public Contact setTelephoneVoice(List<Nillable<String>> telephoneVoice) {
        return setTelephoneVoice(Nillable.of(telephoneVoice));
    }

    /**
     * @param telephoneVoice
     *            the telephoneVoice to set
     */
    public Contact setTelephoneVoice(Nillable<List<Nillable<String>>> telephoneVoice) {
        this.telephoneVoice = Preconditions.checkNotNull(telephoneVoice);
        return this;
    }

    /**
     * @param telephoneVoice
     *            the telephoneVoice to add
     */
    public Contact addTelephoneVoice(String telephoneVoice) {
        return addTelephoneVoice(Nillable.of(Preconditions.checkNotNull(telephoneVoice)));
    }

    public Contact addTelephoneVoice(Nillable<String> telephoneVoice) {
        if (this.telephoneVoice.isAbsent() || this.telephoneVoice.isNil()) {
            this.telephoneVoice = Nillable.of((List<Nillable<String>>) Lists.<Nillable<String>> newArrayList());
        }
        if (telephoneVoice.isPresent()) {
            this.telephoneVoice.get().add(telephoneVoice);
        }
        return this;
    }

    /**
     * @return the website
     */
    public Nillable<String> getWebsite() {
        return website;
    }

    /**
     * @param website
     *            the website to set
     */
    public Contact setWebsite(String website) {
        return setWebsite(Nillable.of(website));
    }

    /**
     * @param website
     *            the website to set
     */
    public Contact setWebsite(Nillable<String> website) {
        this.website = Preconditions.checkNotNull(website);
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getAddress(), getContactInstructions(), getElectronicMailAddress(),
                getHoursOfService(), getTelephoneFacsimile(), getTelephoneVoice(), getWebsite());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Contact) {
            Contact that = (Contact) obj;
            return Objects.equal(getAddress(), that.getAddress())
                    && Objects.equal(getContactInstructions(), that.getContactInstructions())
                    && Objects.equal(getElectronicMailAddress(), that.getElectronicMailAddress())
                    && Objects.equal(getHoursOfService(), that.getHoursOfService())
                    && Objects.equal(getTelephoneFacsimile(), that.getTelephoneFacsimile())
                    && Objects.equal(getTelephoneVoice(), that.getTelephoneVoice())
                    && Objects.equal(getWebsite(), getWebsite());
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("AddressRepresentation", getAddress())
                .add("contactInstructions", getContactInstructions())
                .add("electronicMailAddressRepresentation", getElectronicMailAddress())
                .add("hoursOfService", getHoursOfService()).add("telephoneFacsimile", getTelephoneFacsimile())
                .add("telephoneVoice", getTelephoneVoice()).add("website", getWebsite()).toString();
    }
}
