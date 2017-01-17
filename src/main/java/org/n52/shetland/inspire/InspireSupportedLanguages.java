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
package org.n52.shetland.inspire;

import java.util.Collection;
import java.util.Set;

import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

/**
 * Service internal representation of INSPIRE supported languages
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.1.0
 *
 */
public class InspireSupportedLanguages implements InspireObject {

    /* element DefaultLanguage 1..1 */
    private InspireLanguageISO6392B defaultLanguage;

    /* element SupportedLanguage 0..* */
    private Set<InspireLanguageISO6392B> supportedLanguages = Sets.newHashSet();

    /**
     * constructor
     *
     * @param defaultLanguage
     *            the mandatory default language
     */
    public InspireSupportedLanguages(InspireLanguageISO6392B defaultLanguage) {
        setDefaultLanguage(defaultLanguage);
    }

    /**
     * Get the default language
     *
     * @return the defaultLanguage
     */
    public InspireLanguageISO6392B getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Set the default language
     *
     * @param defaultLanguage
     *            the defaultLanguage to set
     */
    private void setDefaultLanguage(InspireLanguageISO6392B defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    /**
     * Get the supported languages
     *
     * @return the supportedLanguages
     */
    public Set<InspireLanguageISO6392B> getSupportedLanguages() {
        return supportedLanguages;
    }

    /**
     * Set the supported languages, clears the existing collection
     *
     * @param supportedLanguages
     *            the supportedLanguages to set
     */
    public void setSupportedLanguages(Collection<InspireLanguageISO6392B> supportedLanguages) {
        getSupportedLanguages().clear();
        if (CollectionHelper.isNotEmpty(supportedLanguages)) {
            getSupportedLanguages().addAll(supportedLanguages);
        }

    }

    /**
     * Add the supported language
     *
     * @param supportedLanguage
     *            the supported language to add
     * @return this
     */
    public InspireSupportedLanguages addSupportedLanguage(InspireLanguageISO6392B supportedLanguage) {
        getSupportedLanguages().add(supportedLanguage);
        return this;
    }

    /**
     * Check if supported languages are set
     *
     * @return <code>true</code>, if supported languages are set
     */
    public boolean isSetSupportedLanguages() {
        return CollectionHelper.isNotEmpty(getSupportedLanguages());
    }

    @Override
    public String toString() {
        return String.format("%s %n[%n defaultLanguage=%s,%n supportedLanguages=%s%n]", this.getClass()
                .getSimpleName(), getDefaultLanguage(), CollectionHelper.collectionToString(getSupportedLanguages()));
    }
}
