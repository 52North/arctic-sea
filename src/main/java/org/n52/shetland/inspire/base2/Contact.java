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

import java.util.List;

import org.n52.shetland.inspire.ad.AddressRepresentation;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.shetland.w3c.Nillable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

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
     * 0..*
     */
    private Nillable<List<String>> telephoneFacsimile = Nillable.missing();

    /**
     * 0..*
     */
    private Nillable<List<String>> telephoneVoice = Nillable.missing();

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
     * @return the telephoneFacsimile
     */
    public Nillable<List<String>> getTelephoneFacsimile() {
        return telephoneFacsimile;
    }

    /**
     * @param telephoneFacsimile
     *            the telephoneFacsimile to set
     */
    public Contact setTelephoneFacsimile(List<String> telephoneFacsimile) {
        return setTelephoneFacsimile(Nillable.of(telephoneFacsimile));
    }

    /**
     * @param telephoneFacsimile
     *            the telephoneFacsimile to set
     */
    public Contact setTelephoneFacsimile(Nillable<List<String>> telephoneFacsimile) {
        this.telephoneFacsimile = Preconditions.checkNotNull(telephoneFacsimile);
        return this;
    }

    /**
     * @param telephoneFacsimile
     *            the telephoneFacsimile to add
     */
    public Contact addTelephoneFacsimile(String telephoneFacsimile) {
        if (this.telephoneFacsimile.isAbsent()) {
            this.telephoneFacsimile = Nillable.of((List<String>)Lists.<String>newArrayList());
        }
        this.telephoneFacsimile.get().add(Preconditions.checkNotNull(telephoneFacsimile));
        return this;
    }

    /**
     * @return the telephoneVoice
     */
    public Nillable<List<String>> getTelephoneVoice() {
        return telephoneVoice;
    }

    /**
     * @param telephoneVoice
     *            the telephoneVoice to set
     */
    public Contact setTelephoneVoice(List<String> telephoneVoice) {
        return setTelephoneVoice(Nillable.of(telephoneVoice));
    }

    /**
     * @param telephoneVoice
     *            the telephoneVoice to set
     */
    public Contact setTelephoneVoice(Nillable<List<String>> telephoneVoice) {
        this.telephoneVoice = Preconditions.checkNotNull(telephoneVoice);
        return this;
    }

    /**
     * @param telephoneVoice
     *            the telephoneVoice to add
     */
    public Contact addTelephoneVoice(String telephoneVoice) {
        if (this.telephoneVoice.isAbsent()) {
            this.telephoneVoice = Nillable.of((List<String>)Lists.<String>newArrayList());
        }
        this.telephoneVoice.get().add(Preconditions.checkNotNull(telephoneVoice));
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

}
