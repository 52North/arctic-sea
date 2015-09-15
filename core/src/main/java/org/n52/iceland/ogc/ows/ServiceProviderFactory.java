/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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

import com.google.common.base.Splitter;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.ADDRESS;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.CITY;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.COUNTRY;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.EMAIL;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.FILE;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.INDIVIDUAL_NAME;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.NAME;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.PHONE;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.POSITION_NAME;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.POSTAL_CODE;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.SITE;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.STATE;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.CONTACT_INSTRUCTIONS;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.FACSIMILE;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.HOURS_OF_SERVICE;
import static org.n52.iceland.ogc.ows.ServiceProviderFactorySettings.ONLINE_RESOURCE;
import org.n52.iceland.util.FileIOHelper;
import org.n52.iceland.util.LocalizedLazyThreadSafeProducer;
import org.n52.iceland.util.StringHelper;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 1.0.0
 */
@Configurable
public class ServiceProviderFactory extends LocalizedLazyThreadSafeProducer<OwsServiceProvider> {

    private File file;
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
    private String onlineResoureHref;

    @Setting(FILE)
    public void setFile(File file) {
        this.file = file;
        setRecreate();
    }

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
            this.onlineResoureHref = iterator.next();
            setRecreate();
        }
    }

    @Override
    protected OwsServiceProvider create(Locale language) throws ConfigurationError {
        OwsServiceProvider serviceProvider = new OwsServiceProvider();
        if (this.file != null) {
            createFromFile(serviceProvider);
        } else {
            createFromSettings(serviceProvider);
        }
        return serviceProvider;
    }

    private void createFromSettings(OwsServiceProvider serviceProvider) {
        serviceProvider.setAdministrativeArea(this.administrativeArea);
        serviceProvider.setCity(this.city);
        serviceProvider.setContactInstructions(this.contactInstructions);
        serviceProvider.setCountry(this.country);
        serviceProvider.setDeliveryPoint(this.deliveryPoint);
        serviceProvider.setFacsimile(this.facsimile);
        serviceProvider.setHoursOfService(this.hoursOfService);
        serviceProvider.setIndividualName(this.individualName);
        serviceProvider.setElectronicMailAddress(this.electronicMailAddress);
        serviceProvider.setName(this.name);
        serviceProvider.setOnlineResourceHref(this.onlineResoureHref);
        serviceProvider.setOnlineResourceTitle(this.onlineResoureTitle);
        serviceProvider.setPhone(this.phone);
        serviceProvider.setPositionName(this.positionName);
        serviceProvider.setPostalCode(this.postalCode);
        serviceProvider.setSite(this.site == null ? null : this.site.toString());
    }

    private void createFromFile(OwsServiceProvider serviceProvider)
            throws ConfigurationError {
        try {
            serviceProvider.setServiceProvider(StringHelper.convertStreamToString(FileIOHelper.loadInputStreamFromFile(this.file)));
        } catch (OwsExceptionReport ex) {
            throw new ConfigurationError(ex);
        }
    }

    @Override
    public Set<Locale> getAvailableLocales() {
        return Collections.emptySet();
    }
}
