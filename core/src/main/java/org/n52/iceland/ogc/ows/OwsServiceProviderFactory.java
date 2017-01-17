/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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

import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.ADDRESS;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.CITY;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.CONTACT_INSTRUCTIONS;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.COUNTRY;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.EMAIL;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.FACSIMILE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.HOURS_OF_SERVICE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.INDIVIDUAL_NAME;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.NAME;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.ONLINE_RESOURCE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.PHONE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.POSITION_NAME;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.POSTAL_CODE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.ROLE_CODESPACE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.ROLE_VALUE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.SITE;
import static org.n52.iceland.ogc.ows.OwsServiceProviderFactorySettings.STATE;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.faroe.ConfigurationError;
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


    @Setting(NAME)
    public void setName(String name) throws ConfigurationError {
        this.name = name;
        setRecreate();
    }

    @Setting(SITE)
    public void setSite(URI site) {
        this.site = site;
        setRecreate();
    }

    @Setting(INDIVIDUAL_NAME)
    public void setIndividualName(String individualName) {
        this.individualName = individualName;
        setRecreate();
    }

    @Setting(POSITION_NAME)
    public void setPositionName(String positionName) {
        this.positionName = positionName;
        setRecreate();
    }

    @Setting(PHONE)
    public void setPhone(String phone) {
        this.phone = phone;
        setRecreate();
    }

    @Setting(FACSIMILE)
    public void setFacsimile(String facsimile) {
        this.facsimile = facsimile;
        setRecreate();
    }

    @Setting(ADDRESS)
    public void setDeliveryPoint(String deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
        setRecreate();
    }

    @Setting(CITY)
    public void setCity(String city) {
        this.city = city;
        setRecreate();
    }

    @Setting(POSTAL_CODE)
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        setRecreate();
    }

    @Setting(COUNTRY)
    public void setCountry(String country) {
        this.country = country;
        setRecreate();
    }

    @Setting(EMAIL)
    public void setMailAddress(String mailAddress) {
        this.electronicMailAddress = mailAddress;
        setRecreate();
    }

    @Setting(STATE)
    public void setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
        setRecreate();
    }

    @Setting(HOURS_OF_SERVICE)
    public void setHours(String hours) {
        this.hoursOfService = hours;
        setRecreate();
    }

    @Setting(CONTACT_INSTRUCTIONS)
    public void setContactInstructions(String contactInstructions) {
        this.contactInstructions = contactInstructions;
        setRecreate();
    }

    @Setting(ONLINE_RESOURCE)
    public void setOnlineResource(String onlineResource) {
        if (Optional.ofNullable(onlineResource).isPresent()) {
            Iterable<String> split = Splitter.on("|").trimResults().split(onlineResource);
            Iterator<String> iterator = split.iterator();
            this.onlineResoureTitle = iterator.next();
            this.onlineResoureHref = URI.create(iterator.next());
            setRecreate();
        }
    }

    @Setting(ROLE_VALUE)
    public void setRole(String role) {
        this.role = role;
        setRecreate();
    }

    @Setting(ROLE_CODESPACE)
    public void setRoleCodespace(URI roleCodespace) {
        this.roleCodespace = roleCodespace;
        setRecreate();
    }

    @Override
    protected OwsServiceProvider create(Locale language) throws ConfigurationError {
        OwsOnlineResource providerSite = new OwsOnlineResource(onlineResoureHref, onlineResoureTitle);
        OwsAddress address = new OwsAddress(deliveryPoint, city, administrativeArea, postalCode, country, electronicMailAddress);
        OwsContact contactInfo =new OwsContact(new OwsPhone(phone, facsimile), address, new OwsOnlineResource(site), hoursOfService, contactInstructions);
        // TODO organisation name is missing
        OwsResponsibleParty serviceContact = new OwsResponsibleParty(individualName, null, positionName, contactInfo, new OwsCode(role, roleCodespace));
        OwsServiceProvider serviceProvider = new OwsServiceProvider(name, providerSite, serviceContact);
        return serviceProvider;

    }

    @Override
    public Set<Locale> getAvailableLocales() {
        return Collections.emptySet();
    }
}
