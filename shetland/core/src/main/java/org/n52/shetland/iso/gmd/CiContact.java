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
package org.n52.shetland.iso.gmd;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Reference;
import org.n52.shetland.w3c.xlink.Referenceable;

import com.google.common.collect.Lists;

/**
 * Internal representation of the ISO GMD Contact.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class CiContact extends AbstractObject {

    private Referenceable<CiTelephone> phone;
    private Referenceable<CiAddress> address;
    private Referenceable<CiOnlineResource> onlineResource;
    private Nillable<String> hoursOfService;
    private Nillable<String> contactInstructions;

    /**
     * @return the phone
     */
    public Referenceable<CiTelephone> getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(Referenceable<CiTelephone> phone) {
        this.phone = phone;
    }

    public boolean isSetPhone() {
        return getPhone() != null;
    }

    private boolean isSetPhoneInstance() {
        return getPhone() != null
                && getPhone().isInstance()
                && getPhone().getInstance().isPresent();
    }

    private CiTelephone getPhoneInstance() {
        return getPhone().getInstance().get();
    }

    public boolean isSetPhoneVoice() {
        return isSetPhoneInstance()
                && getPhoneInstance().isSetVoice();
    }

    public List<String> getPhoneVoice() {
        if (isSetPhoneVoice()) {
            return getPhoneInstance().getVoice();
        }
        return null;
    }

    public CiContact setPhoneVoice(final List<String> phoneVoice) {
        if (isSetPhoneInstance()) {
            getPhoneInstance().setVoice(phoneVoice);
        } else {
            setPhone(Referenceable.of(new CiTelephone().setVoice(phoneVoice)));
        }
        return this;
    }

    public CiContact addPhoneVoice(final String phoneVoice) {
        if (isSetPhoneInstance()) {
            getPhoneInstance().addVoice(phoneVoice);
        } else {
            setPhone(Referenceable.of(new CiTelephone().addVoice(phoneVoice)));
        }
        return this;
    }

    public boolean isSetPhoneFax() {
        return isSetPhoneInstance()
                && getPhoneInstance().isSetFacsimile();
    }

    public List<String> getPhoneFax() {
        if (isSetPhoneFax()) {
            return getPhoneInstance().getFacsimile();
        }
        return null;
    }

    public CiContact addPhoneFax(final String phoneFax) {
        if (isSetPhoneInstance()) {
            getPhoneInstance().addFacsimile(phoneFax);
        } else {
            setPhone(Referenceable.of(new CiTelephone().addFacsimile(phoneFax)));
        }
        return this;
    }

    public CiContact setPhoneFax(final List<String> phoneFax) {
        if (isSetPhoneInstance()) {
            getPhoneInstance().setFacsimile(phoneFax);
        } else {
            setPhone(Referenceable.of(new CiTelephone().setFacsimile(phoneFax)));
        }
        return this;
    }

    /**
     * @return the address
     */
    public Referenceable<CiAddress> getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(Referenceable<CiAddress> address) {
        this.address = address;
    }

    public boolean isSetAddress() {
        return getAddress() != null;
    }

    private boolean isSetAddressInstance() {
        return getAddress() != null
                && getAddress().isInstance()
                && getAddress().getInstance().isPresent();
    }

    private CiAddress getAddressInstance() {
        return getAddress().getInstance().get();
    }

    public boolean isSetDeliveryPoint() {
        return isSetAddressInstance()
                && getAddressInstance().hasDeliveryPoints();
    }

    public List<String> getDeliveryPoint() {
        if (isSetDeliveryPoint()) {
            return getAddressInstance().getDeliveryPoints();
        }
        return null;
    }

    public CiContact setDeliveryPoint(final List<String> deliveryPoints) {
        if (isSetAddressInstance()) {
            getAddressInstance().setDeliveryPoints(deliveryPoints);
        } else {
            setAddress(Referenceable.of(new CiAddress().setDeliveryPoints(deliveryPoints)));
        }
        return this;
    }

    public CiContact addDeliveryPoint(final String deliveryPoint) {
        if (isSetAddressInstance()) {
            getAddressInstance().addDeliveryPoints(deliveryPoint);
        } else {
            setAddress(Referenceable.of(new CiAddress().addDeliveryPoints(deliveryPoint)));
        }
        return this;
    }

    public boolean isSetCity() {
        return isSetAddressInstance() && !getAddressInstance().isSetCity();
    }

    public String getCity() {
        if (isSetCity()) {
            return getAddressInstance().getCity();
        }
        return null;
    }

    public CiContact setCity(final String city) {
        if (isSetAddressInstance()) {
            getAddressInstance().setCity(city);
        } else {
            setAddress(Referenceable.of(new CiAddress().setCity(city)));
        }
        return this;
    }

    public boolean isSetAdministrativeArea() {
        return isSetAddressInstance() && !getAddressInstance().isSetAdministrativeArea();
    }

    public String getAdministrativeArea() {
        if (isSetAdministrativeArea()) {
            return getAddressInstance().getAdministrativeArea();
        }
        return null;
    }

    public CiContact setAdministrativeArea(final String administrativeArea) {
        if (isSetAddressInstance()) {
            getAddressInstance().setAdministrativeArea(administrativeArea);
        } else {
            setAddress(Referenceable.of(new CiAddress().setAdministrativeArea(administrativeArea)));
        }
        return this;
    }

    public boolean isSetPostalCode() {
        return isSetAddressInstance() && !getAddressInstance().isSetPostalCode();
    }

    public String getPostalCode() {
        if (isSetAdministrativeArea()) {
            return getAddressInstance().getPostalCode();
        }
        return null;
    }

    public CiContact setPostalCode(final String postalCode) {
        if (isSetAddressInstance()) {
            getAddressInstance().setPostalCode(postalCode);
        } else {
            setAddress(Referenceable.of(new CiAddress().setPostalCode(postalCode)));
        }
        return this;
    }

    public boolean isSetCountry() {
        return isSetAddressInstance() && !getAddressInstance().isSetCountry();
    }

    public String getCountry() {
        if (isSetAdministrativeArea()) {
            return getAddressInstance().getCountry();
        }
        return null;
    }

    public CiContact setCountry(final String country) {
        if (isSetAddressInstance()) {
            getAddressInstance().setCountry(country);
        } else {
            setAddress(Referenceable.of(new CiAddress().setCountry(country)));
        }
        return this;
    }

    public boolean isSetEmail() {
        return isSetAddressInstance() && !getAddressInstance().hasElectronicMailAddresses();
    }

    public String getEmail() {
        if (isSetAdministrativeArea()) {
            return getAddressInstance().getElectronicMailAddresses().iterator().next();
        }
        return null;
    }

    public CiContact setEmail(final String email) {
        if (isSetAddressInstance()) {
            getAddressInstance().setElectronicMailAddresses(Lists.newArrayList(email));
        } else {
            setAddress(Referenceable.of(new CiAddress().setElectronicMailAddresses(Lists.newArrayList(email))));
        }
        return this;
    }

    public boolean isSetOnlineResource() {
        return onlineResource != null
                && ((onlineResource.isReference() && onlineResource.getReference().getHref().isPresent())
                        || onlineResource.isInstance());
    }

    public Referenceable<CiOnlineResource>  getOnlineResourceReferenceable() {
        return onlineResource;
    }

    public String getOnlineResource() {
        if (onlineResource.isReference() && onlineResource.getReference().getHref().isPresent()) {
            return onlineResource.getReference().getHref().get().toString();
        } else if (onlineResource.isInstance() && onlineResource.getInstance().isPresent()
                && onlineResource.getInstance().get().getLinkage().isPresent()) {
            return onlineResource.getInstance().get().getLinkage().get().toString();
        }
        return null;
    }

    public CiContact setOnlineResource(final Referenceable<CiOnlineResource> onlineResource) {
        this.onlineResource = onlineResource;
        return this;
    }

    public CiContact setOnlineResource(final String onlineResource) {
        try {
            this.onlineResource = Referenceable.of(new Reference().setHref(new URI(onlineResource)));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean isSetHoursOfService() {
        return hoursOfService != null && !hoursOfService.isNull();
    }

    public String getHoursOfService() {
        if (hoursOfService.isPresent()) {
            return hoursOfService.get();
        }
        return null;
    }

    public Nillable<String> getHoursOfServiceNillable() {
        return hoursOfService;
    }

    public CiContact setHoursOfService(final Nillable<String> hoursOfService) {
        this.hoursOfService = hoursOfService;
        return this;
    }

    public CiContact setHoursOfService(final String hoursOfService) {
        this.hoursOfService = Nillable.of(hoursOfService);
        return this;
    }

    public boolean isSetContactInstructions() {
        return contactInstructions != null && !contactInstructions.isNull();
    }

    public String getContactInstructions() {
        if (contactInstructions.isPresent()) {
            return contactInstructions.get();
        }
        return null;
    }

    public Nillable<String> getContactInstructionsNillable() {
        return contactInstructions;
    }

    public CiContact setContactInstructions(final Nillable<String> contactInstructions) {
        this.contactInstructions = contactInstructions;
        return this;
    }

    public CiContact setContactInstructions(final String contactInstructions) {
        this.contactInstructions = Nillable.of(contactInstructions);
        return this;
    }

    public boolean isSetContactInfo() {
        return isSetPhone() || isSetAddress() || isSetOnlineResource() || isSetHoursOfService()
                || isSetContactInstructions();
    }
}
