/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import org.n52.shetland.inspire.InspireConstants;
import org.n52.shetland.inspire.InspireLanguageISO6392B;
import org.n52.shetland.inspire.InspireObject;
import org.n52.shetland.inspire.InspireSupportedLanguages;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireExtendedCapabilitiesResponseLanguage;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireExtendedCapabilitiesSupportedLanguage;
import org.n52.shetland.ogc.ows.OwsOperationMetadataExtension;
import org.n52.shetland.ogc.sos.SosConstants;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Abstract service internal representation of INSPIRE ExtendedCapabilities.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class InspireExtendedCapabilities implements OwsOperationMetadataExtension,
        InspireExtendedCapabilitiesSupportedLanguage, InspireExtendedCapabilitiesResponseLanguage, InspireObject {

    private String inspireId;

    /* SupportedLanguages 1..1 */
    private InspireSupportedLanguages supportedLanguages;

    /* ResponseLanguage 1..1 */
    private InspireLanguageISO6392B responseLanguage;

    /**
     * Constructor
     *
     * @param supportedLanguages
     *            Supported languages to set
     * @param responseLanguage
     *            Response language to set
     */
    public InspireExtendedCapabilities(InspireSupportedLanguages supportedLanguages,
            InspireLanguageISO6392B responseLanguage) {
        setSupportedLanguages(supportedLanguages);
        setResponseLanguage(responseLanguage);
    }

    @Override
    public String getService() {
        return SosConstants.SOS;
    }

    @Override
    public String getNamespace() {
        return InspireConstants.NS_INSPIRE_COMMON;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public InspireSupportedLanguages getSupportedLanguages() {
        return supportedLanguages;
    }

    @Override
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public InspireExtendedCapabilities setSupportedLanguages(InspireSupportedLanguages supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
        return this;
    }

    @Override
    public boolean isSetSupportedLanguages() {
        return getSupportedLanguages() != null;
    }

    @Override
    public InspireLanguageISO6392B getResponseLanguage() {
        return responseLanguage;
    }

    @Override
    public InspireExtendedCapabilities setResponseLanguage(InspireLanguageISO6392B responseLanguage) {
        this.responseLanguage = responseLanguage;
        return this;
    }

    @Override
    public boolean isSetResponseLanguage() {
        return getResponseLanguage() != null;
    }

    /**
     * Set the INSPIRE id
     *
     * @param inspireId
     *            INSPIRE id to set
     * @return this
     */
    public InspireExtendedCapabilities setInspireId(String inspireId) {
        this.inspireId = inspireId;
        return this;
    }

    /**
     * Get the INSPIRE id
     *
     * @return the INSPIRE id
     */
    public String getInspireId() {
        return inspireId;
    }

}
