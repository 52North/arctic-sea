/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import org.n52.faroe.ConfigurationError;
import org.n52.faroe.Validation;
import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.i18n.I18NSettings;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.LocalizedLazyThreadSafeProducer;
import org.n52.janmayen.i18n.LocaleHelper;
import org.n52.janmayen.i18n.LocalizedString;
import org.n52.janmayen.i18n.MultilingualString;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsServiceIdentification;
import org.n52.shetland.util.StringHelper;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Configurable
public class OwsServiceIdentificationFactory
        extends LocalizedLazyThreadSafeProducer<OwsServiceIdentification> {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private final String service;
    private final ServiceOperatorRepository serviceOperatorRepository;
    private MultilingualString title = new MultilingualString();
    private MultilingualString abstrakt = new MultilingualString();
    private String fees;
    private String[] keywords = EMPTY_STRING_ARRAY;
    private String[] constraints = EMPTY_STRING_ARRAY;
    private Locale defaultLocale = Locale.ENGLISH;
    private boolean showAllLanguageValues;

    public OwsServiceIdentificationFactory(String service, ServiceOperatorRepository repository) {
        this.service = service;
        this.serviceOperatorRepository = repository;
    }

    @Setting(I18NSettings.I18N_SHOW_ALL_LANGUAGE_VALUES)
    public void setShowAllLanguageValues(final boolean showAllLanguageValues) {
        this.showAllLanguageValues = showAllLanguageValues;
    }

    @Setting(I18NSettings.I18N_DEFAULT_LANGUAGE)
    public void setDefaultLanguage(String lang) {
        this.defaultLocale = LocaleHelper.decode(lang);
    }

    @Setting(OwsServiceIdentificationFactorySettings.KEYWORDS)
    public void setKeywords(String keywords) {
        setKeywords(StringHelper.splitToArray(keywords));
    }

    public void setKeywords(String[] keywords) {
        this.keywords = copyOf(keywords);
        setRecreate();
    }

    private Set<OwsKeyword> getKeywords() {
        return Arrays.stream(this.keywords).map(OwsKeyword::new).collect(toSet());
    }

    @Setting(OwsServiceIdentificationFactorySettings.TITLE)
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

    @Setting(OwsServiceIdentificationFactorySettings.ABSTRACT)
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

    @Setting(OwsServiceIdentificationFactorySettings.FEES)
    public void setFees(String fees) {
        this.fees = fees;
        setRecreate();
    }

    private Set<String> getFees() {
        return Optional.ofNullable(this.fees).map(Collections::singleton).orElseGet(Collections::emptySet);
    }

    public void setConstraints(String[] constraints) {
        this.constraints = copyOf(constraints);
        setRecreate();
    }

    @Setting(OwsServiceIdentificationFactorySettings.ACCESS_CONSTRAINTS)
    public void setConstraints(String constraints) {
        setConstraints(StringHelper.splitToArray(constraints));
    }

    @Override
    protected OwsServiceIdentification create(Locale language) throws ConfigurationError {
        return createFromSettings(language);
    }

    private OwsServiceIdentification createFromSettings(Locale locale) {
        // TODO
        Set<URI> profiles = null;
        return new OwsServiceIdentification(
                getServiceType(),
                getSupportedVersions(),
                profiles,
                getFees(),
                getContraints(),
                getTitle(locale),
                getAbstract(locale),
                getKeywords());
    }

    private OwsCode getServiceType() {
        return new OwsCode("OGC:" + this.service);
    }

    private Set<String> getSupportedVersions() {
        return this.serviceOperatorRepository.getSupportedVersions(this.service);
    }

    @Override
    public Set<Locale> getAvailableLocales() {
        return Stream.of(this.title, this.abstrakt)
                     .filter(Objects::nonNull)
                     .map(MultilingualString::getLocales)
                     .collect(HashSet::new, Set::addAll, Set::addAll);
    }

    private MultilingualString getAbstract(Locale locale) {
        if (this.abstrakt.hasLocale(locale)) {
            return this.abstrakt.filter(locale, defaultLocale, showAllLanguageValues);
        } else {
            MultilingualString multilingualString = new MultilingualString();
            for (Locale eqLocale : LocaleHelper.getEquivalents(locale)) {
                if (this.abstrakt.hasLocale(eqLocale)) {
                    for (LocalizedString ls : this.abstrakt.filter(eqLocale, defaultLocale, showAllLanguageValues)) {
                        multilingualString.addLocalization(new LocalizedString(locale, ls.getText()));
                    }
                    return multilingualString;
                }
            }
        }
        return this.abstrakt.filter(locale, defaultLocale, showAllLanguageValues);
    }

    private MultilingualString getTitle(Locale locale) {
        if (this.title.hasLocale(locale)) {
            return this.title.filter(locale, defaultLocale, showAllLanguageValues);
        } else {
            MultilingualString multilingualString = new MultilingualString();
            for (Locale eqLocale : LocaleHelper.getEquivalents(locale)) {
                if (this.title.hasLocale(eqLocale)) {
                    for (LocalizedString ls : this.title.filter(eqLocale, defaultLocale, showAllLanguageValues)) {
                        multilingualString.addLocalization(new LocalizedString(locale, ls.getText()));
                    }
                    return multilingualString;
                }
            }
        }
        return this.title.filter(locale, defaultLocale, showAllLanguageValues);
    }

    private Set<String> getContraints() {
        return new HashSet<>(Arrays.asList(this.constraints));
    }

    private static String[] copyOf(String[] a) {
        return a == null ? EMPTY_STRING_ARRAY : Arrays.copyOf(a, a.length);
    }
}
