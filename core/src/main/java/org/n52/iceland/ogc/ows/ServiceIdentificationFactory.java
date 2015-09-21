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

import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.ABSTRACT;
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.ACCESS_CONSTRAINTS;
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.FEES;
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.FILE;
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.KEYWORDS;
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.TITLE;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.i18n.I18NSettings;
import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.i18n.MultilingualString;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.FileIOHelper;
import org.n52.iceland.util.LocalizedLazyThreadSafeProducer;
import org.n52.iceland.util.StringHelper;
import org.n52.iceland.util.Validation;

@Configurable
public class ServiceIdentificationFactory
        extends LocalizedLazyThreadSafeProducer<OwsServiceIdentification> {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private final String service;
    private final ServiceOperatorRepository serviceOperatorRepository;

    private File file;
    private MultilingualString title = new MultilingualString();
    private MultilingualString abstrakt = new MultilingualString();
    private String fees = null;
    private String[] keywords = EMPTY_STRING_ARRAY;
    private String[] constraints = EMPTY_STRING_ARRAY;
    private Locale defaultLocale = Locale.ENGLISH;
    private boolean showAllLanguageValues;

    public ServiceIdentificationFactory(String service, ServiceOperatorRepository repository) {
        this.service = service;
        this.serviceOperatorRepository = repository;
    }

    @Setting(I18NSettings.I18N_SHOW_ALL_LANGUAGE_VALUES)
    public void setShowAllLanguageValues(final boolean showAllLanguageValues) {
        this.showAllLanguageValues = showAllLanguageValues;
    }

    @Setting(I18NSettings.I18N_DEFAULT_LANGUAGE)
    public void setDefaultLanguage(String lang) {
        this.defaultLocale = LocaleHelper.fromString(lang);
    }

    @Setting(FILE)
    public void setFile(File file) {
        this.file = file;
        setRecreate();
    }

    public void setKeywords(String[] keywords) {
        this.keywords = copyOf(keywords);
        setRecreate();
    }

    @Setting(KEYWORDS)
    public void setKeywords(String keywords) {
        setKeywords(StringHelper.splitToArray(keywords));
    }

    @Setting(TITLE)
    public void setTitle(Object title) throws ConfigurationError {
        Validation.notNull("Service Identification Title", title);
        if (title instanceof MultilingualString) {
            this.title = (MultilingualString) title;
        } else if (title instanceof String) {
            this.title = createFromString(title);
        } else {
            throw new ConfigurationError(
                    String.format("%s is not supported as title!", title.getClass().getName()));
        }
        setRecreate();
    }

    private MultilingualString createFromString(Object value) {
        return new MultilingualString().addLocalization(this.defaultLocale, (String) value);
    }

    @Setting(ABSTRACT)
    public void setAbstract(Object description) throws ConfigurationError {
        Validation.notNull("Service Identification Abstract", description);
        if (description instanceof MultilingualString) {
            this.abstrakt = (MultilingualString) description;
        } else if (description instanceof String) {
            this.abstrakt = createFromString(description);
        } else {
            throw new ConfigurationError(
                    String.format("%s is not supported as abstract!", description.getClass().getName()));
        }
        setRecreate();
    }

    @Setting(FEES)
    public void setFees(String fees) {
        // Validation.notNullOrEmpty("Service Identification Fees", fees);
        this.fees = fees;
        setRecreate();
    }

    public void setConstraints(String[] constraints) {
        this.constraints = copyOf(constraints);
        setRecreate();
    }

    @Setting(ACCESS_CONSTRAINTS)
    public void setConstraints(String constraints) {
        setConstraints(StringHelper.splitToArray(constraints));
    }

    @Override
    protected OwsServiceIdentification create(Locale language) throws ConfigurationError {
        if (this.file != null) {
            return createFromFile();
        } else {
            return createFromSettings(language);
        }
    }

    private OwsServiceIdentification createFromSettings(Locale locale) {
        OwsServiceIdentification serviceIdentification = new OwsServiceIdentification();
        serviceIdentification.setTitle(this.title.filter(locale, defaultLocale, showAllLanguageValues));
        serviceIdentification.setAbstract(this.abstrakt.filter(locale, defaultLocale, showAllLanguageValues));
        serviceIdentification.setAccessConstraints(Arrays.asList(this.constraints));
        serviceIdentification.setFees(this.fees);
        serviceIdentification.setServiceType(getServiceType());
        serviceIdentification.setServiceTypeCodeSpace(getServiceTypeCodespace());
        serviceIdentification.setVersions(getSupportedVersions());
        serviceIdentification.setKeywords(Arrays.asList(this.keywords));
        return serviceIdentification;
    }

    private String getServiceType() {
        return "OGC:" + this.service;
    }

    private String getServiceTypeCodespace() {
        return null;
    }

    private Set<String> getSupportedVersions() {
        return this.serviceOperatorRepository.getSupportedVersions(this.service);
    }

    private OwsServiceIdentification createFromFile() throws ConfigurationError {
        try {
            OwsServiceIdentification serviceIdentification = new OwsServiceIdentification();
            serviceIdentification.setServiceIdentification(StringHelper.convertStreamToString(FileIOHelper.loadInputStreamFromFile(this.file)));
            return serviceIdentification;
        } catch (OwsExceptionReport ex) {
            throw new ConfigurationError(ex);
        }
    }

    @Override
    public Set<Locale> getAvailableLocales() {
        return Stream.of(this.title, this.abstrakt)
                .filter(Objects::nonNull)
                .map(MultilingualString::getLocales)
                .collect(HashSet::new, Set::addAll, Set::addAll);
    }

    private static String[] copyOf(String[] a) {
        return a == null ? EMPTY_STRING_ARRAY : Arrays.copyOf(a, a.length);
    }
}
