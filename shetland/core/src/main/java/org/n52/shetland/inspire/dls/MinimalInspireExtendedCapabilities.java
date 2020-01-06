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
package org.n52.shetland.inspire.dls;

import java.util.Collection;

import org.n52.shetland.inspire.InspireLanguageISO6392B;
import org.n52.shetland.inspire.InspireResourceLocator;
import org.n52.shetland.inspire.InspireSupportedLanguages;
import org.n52.shetland.inspire.InspireUniqueResourceIdentifier;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireExtendedCapabilitiesMetadataURL;

/**
 * Service internal object to represent the minimal INSPIRE DLS ExtendedCapabilities
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class MinimalInspireExtendedCapabilities extends InspireExtendedCapabilitiesDLS implements
        InspireExtendedCapabilitiesMetadataURL {

    /* MetadataUrl 1..1 */
    private InspireResourceLocator metadataUrl;

    /**
     * constructor
     *
     * @param metadataUrl
     *            Metadata URL to set
     * @param supportedLanguages
     *            Supported languages to set
     * @param responseLanguage
     *            Response language to set
     * @param spatialDataSetIdentifier
     *            Spatial dataset identifier to set
     */
    public MinimalInspireExtendedCapabilities(InspireResourceLocator metadataUrl,
            InspireSupportedLanguages supportedLanguages, InspireLanguageISO6392B responseLanguage,
            Collection<InspireUniqueResourceIdentifier> spatialDataSetIdentifier) {
        super(supportedLanguages, responseLanguage, spatialDataSetIdentifier);
        setMetadataUrl(metadataUrl);
    }

    @Override
    public InspireResourceLocator getMetadataUrl() {
        return metadataUrl;
    }

    @Override
    public MinimalInspireExtendedCapabilities setMetadataUrl(InspireResourceLocator metadataUrl) {
        this.metadataUrl = metadataUrl;
        return this;
    }

    @Override
    public boolean isSetMetadataUrl() {
        return getMetadataUrl() != null;
    }

    @Override
    public String toString() {
        return String.format("%s %n[%n \tn supportedLanguages=%s," + "%n responseLanguage=%s,"
                + "%n metadataUrl=%s%n]", this.getClass().getSimpleName(), getSupportedLanguages(),
                getResponseLanguage(), getMetadataUrl());
    }
}
