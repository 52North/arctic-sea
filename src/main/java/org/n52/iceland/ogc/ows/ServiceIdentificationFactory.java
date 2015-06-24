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
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.SERVICE_TYPE;
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.SERVICE_TYPE_CODE_SPACE;
import static org.n52.iceland.ogc.ows.ServiceIdentificationFactorySettings.TITLE;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.ConfigurationError;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.i18n.I18NSettings;
import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.i18n.MultilingualString;
import org.n52.iceland.util.FileIOHelper;
import org.n52.iceland.util.LazyThreadSafeProducer;
import org.n52.iceland.util.StringHelper;
import org.n52.iceland.util.Validation;

import com.google.common.collect.Sets;

@Configurable
public class ServiceIdentificationFactory extends LazyThreadSafeProducer<OwsServiceIdentification> {

    private File file;
    private String[] keywords;
    private MultilingualString title;
    private MultilingualString abstrakt;
    private String serviceType;
    private String serviceTypeCodeSpace;
    private String fees;
    private String[] constraints;
    private Locale defaultLocale = Locale.ENGLISH;

    @Inject
    private ServiceOperatorRepository serviceOperatorRepository;

    private boolean showAllLanguageValues;

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
            this.title = new MultilingualString()
                    .addLocalization(this.defaultLocale, (String) title);
        } else {
            throw new ConfigurationError(
                    String.format("%s is not supported as title!", title.getClass().getName()));
        }
        setRecreate();
    }

    @Setting(ABSTRACT)
    public void setAbstract(Object description) throws ConfigurationError {
        Validation.notNull("Service Identification Abstract", description);
        if (description instanceof MultilingualString) {
            this.abstrakt = (MultilingualString) description;
        } else if (description instanceof String) {
            this.abstrakt = new MultilingualString()
                    .addLocalization(this.defaultLocale, (String) description);
        } else {
            throw new ConfigurationError(
                    String.format("%s is not supported as abstract!", description.getClass().getName()));
        }
        setRecreate();
    }

    @Setting(SERVICE_TYPE)
    public void setServiceType(String serviceType) throws ConfigurationError {
        Validation.notNullOrEmpty("Service Identification Service Type", serviceType);
        this.serviceType = serviceType;
        setRecreate();
    }

    @Setting(SERVICE_TYPE_CODE_SPACE)
    public void setServiceTypeCodeSpace(String serviceTypeCodeSpace) throws ConfigurationError {
        this.serviceTypeCodeSpace = serviceTypeCodeSpace;
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
        if (this.title != null) {
            serviceIdentification.setTitle(this.title.filter(locale, defaultLocale, showAllLanguageValues));
        }
        if (this.abstrakt != null) {
            serviceIdentification.setAbstract(this.abstrakt.filter(locale, defaultLocale, showAllLanguageValues));
        }
        if (this.constraints != null) {
            serviceIdentification.setAccessConstraints(Arrays.asList(this.constraints));
        }
        serviceIdentification.setFees(this.fees);
        serviceIdentification.setServiceType(this.serviceType);
        serviceIdentification.setServiceTypeCodeSpace(this.serviceTypeCodeSpace);
        Set<String> supportedVersions = this.serviceOperatorRepository.getSupportedVersions(SosConstants.SOS);
        serviceIdentification.setVersions(supportedVersions);
        if (this.keywords != null) {
            serviceIdentification.setKeywords(Arrays.asList(this.keywords));
        }
        return serviceIdentification;
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

    public Set<Locale> getAvailableLocales() {
        if (this.title == null) {
            if (this.abstrakt == null) {
                return Collections.emptySet();
            } else {
                return this.abstrakt.getLocales();
            }
        } else {
            if (this.abstrakt == null) {
                return this.title.getLocales();
            } else {
                return Sets.union(this.title.getLocales(), this.abstrakt.getLocales());
            }
        }
    }

    private static String[] copyOf(String[] a) {
        return a == null ? new String[0] : Arrays.copyOf(a, a.length);
    }
}
