/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.util.LocalizedLazyThreadSafeProducer;
import org.n52.shetland.ogc.ows.OwsAddress;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsContact;
import org.n52.shetland.ogc.ows.OwsOnlineResource;
import org.n52.shetland.ogc.ows.OwsPhone;
import org.n52.shetland.ogc.ows.OwsResponsibleParty;
import org.n52.shetland.ogc.ows.OwsServiceProvider;

import com.google.common.base.Splitter;

/**
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@Configurable
public class OwsServiceProviderFactory extends LocalizedLazyThreadSafeProducer<OwsServiceProvider> {

    private String name;
    private URI site;
    private String individualName;
    private String positionName;
    private String phone;
    private String deliveryPoint;
    private String city;
    private String postalCode;
    private String country;
    private String electronicMailAddress;
    private String administrativeArea;
    private String facsimile;
    private String hoursOfService;
    private String contactInstructions;
    private String onlineResoureTitle;
    private URI onlineResoureHref;
    private String role;
    private URI roleCodespace;

    @Setting(OwsServiceProviderFactorySettings.NAME)
    public void setName(String name) throws ConfigurationError {
        this.name = name;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.SITE)
    public void setSite(URI site) {
        this.site = site;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.INDIVIDUAL_NAME)
    public void setIndividualName(String individualName) {
        this.individualName = individualName;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.POSITION_NAME)
    public void setPositionName(String positionName) {
        this.positionName = positionName;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.PHONE)
    public void setPhone(String phone) {
        this.phone = phone;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.FACSIMILE)
    public void setFacsimile(String facsimile) {
        this.facsimile = facsimile;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.ADDRESS)
    public void setDeliveryPoint(String deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.CITY)
    public void setCity(String city) {
        this.city = city;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.POSTAL_CODE)
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.COUNTRY)
    public void setCountry(String country) {
        this.country = country;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.EMAIL)
    public void setMailAddress(String mailAddress) {
        this.electronicMailAddress = mailAddress;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.STATE)
    public void setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.HOURS_OF_SERVICE)
    public void setHours(String hours) {
        this.hoursOfService = hours;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.CONTACT_INSTRUCTIONS)
    public void setContactInstructions(String contactInstructions) {
        this.contactInstructions = contactInstructions;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.ONLINE_RESOURCE)
    public void setOnlineResource(String onlineResource) {
        if (Optional.ofNullable(onlineResource).isPresent()) {
            if (onlineResource.contains("|")) {
                Iterable<String> split = Splitter.on("|").trimResults().split(onlineResource);
                Iterator<String> iterator = split.iterator();
                this.onlineResoureTitle = iterator.next();
                this.onlineResoureHref = URI.create(iterator.next());
            } else {
                this.onlineResoureTitle = onlineResource;
                this.onlineResoureHref = URI.create(onlineResource);
            }
            setRecreate();
        }
    }

    @Setting(OwsServiceProviderFactorySettings.ROLE_VALUE)
    public void setRole(String role) {
        this.role = role;
        setRecreate();
    }

    @Setting(OwsServiceProviderFactorySettings.ROLE_CODESPACE)
    public void setRoleCodespace(URI roleCodespace) {
        this.roleCodespace = roleCodespace;
        setRecreate();
    }

    @Override
    protected OwsServiceProvider create(Locale language) throws ConfigurationError {
        // TODO organisation name is missing
        String organisationName = null;
        OwsOnlineResource onlineResource = null;
        if (site != null) {
            onlineResource = new OwsOnlineResource(site);
        }
        OwsCode roleCode = null;
        if (role != null) {
            roleCode = new OwsCode(role, roleCodespace);
        }
        OwsOnlineResource providerSite = null;
        if (onlineResoureHref != null) {
            providerSite = new OwsOnlineResource(onlineResoureHref, onlineResoureTitle);
        }

        OwsAddress address = null;
        if (anyNonNull(deliveryPoint, city, administrativeArea, postalCode, country, electronicMailAddress)) {
            address = new OwsAddress(deliveryPoint, city, administrativeArea,
                                     postalCode, country, electronicMailAddress);
        }

        OwsPhone owsPhone = null;
        if (anyNonNull(phone, facsimile)) {
            owsPhone = new OwsPhone(phone, facsimile);
        }

        OwsContact contactInfo = null;
        if (anyNonNull(owsPhone, address, onlineResource, hoursOfService, contactInstructions)) {
            contactInfo = new OwsContact(owsPhone, address, onlineResource, hoursOfService, contactInstructions);
        }

        OwsResponsibleParty serviceContact = new OwsResponsibleParty(individualName, organisationName,
                                                                     positionName, contactInfo, roleCode);
        return new OwsServiceProvider(name, providerSite, serviceContact);

    }

    @Override
    public Set<Locale> getAvailableLocales() {
        return Collections.emptySet();
    }

    private static boolean anyNonNull(Object... values) {
        return Arrays.stream(values).anyMatch(Objects::nonNull);
    }
}
