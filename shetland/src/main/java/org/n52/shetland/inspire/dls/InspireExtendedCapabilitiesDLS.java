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
package org.n52.shetland.inspire.dls;

import java.util.Collection;
import java.util.Set;

import org.n52.shetland.inspire.InspireConstants;
import org.n52.shetland.inspire.InspireLanguageISO6392B;
import org.n52.shetland.inspire.InspireSupportedCRS;
import org.n52.shetland.inspire.InspireSupportedLanguages;
import org.n52.shetland.inspire.InspireUniqueResourceIdentifier;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireExtendedCapabilitiesSpatialDataSetIdentifier;
import org.n52.shetland.inspire.dls.InspireCapabilities.InspireExtendedCapabilitiesSupportedCRS;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

/**
 * Abstract service internal representation of INSPIRE DLS ExtendedCapabilities.
 * Extends the {@link InspireExtendedCapabilities} with the DLS specific data
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class InspireExtendedCapabilitiesDLS
        extends InspireExtendedCapabilities
        implements InspireExtendedCapabilitiesSpatialDataSetIdentifier, InspireExtendedCapabilitiesSupportedCRS {

    /* SpatialDataSetIdentifier 1..* */
    private Set<InspireUniqueResourceIdentifier> spatialDataSetIdentifier = Sets.newHashSet();
    private InspireSupportedCRS supportedCRS;

    /**
     * constructor
     *
     * @param supportedLanguages
     *            Supported languages to set
     * @param responseLanguage
     *            Response language to set
     * @param spatialDataSetIdentifier
     *            Spatial dataset identifier to set
     * @param supportedCRS
     *            Supported CRSes to set
     */
    public InspireExtendedCapabilitiesDLS(
            InspireSupportedLanguages supportedLanguages, InspireLanguageISO6392B responseLanguage,
            InspireUniqueResourceIdentifier spatialDataSetIdentifier, InspireSupportedCRS supportedCRS) {
        super(supportedLanguages, responseLanguage);
        addSpatialDataSetIdentifier(spatialDataSetIdentifier);
        setSupportedCRS(supportedCRS);
    }

    /**
     * constructor
     *
     * @param supportedLanguages
     *            Supported languages to set
     * @param responseLanguage
     *            Response language to set
     * @param spatialDataSetIdentifiers
     *            Spatial dataset identifiers to set
     * @param supportedCRS
     *            Supported CRSes to set
     */
    public InspireExtendedCapabilitiesDLS(
            InspireSupportedLanguages supportedLanguages, InspireLanguageISO6392B responseLanguage,
            Collection<InspireUniqueResourceIdentifier> spatialDataSetIdentifiers, InspireSupportedCRS supportedCRS) {
        super(supportedLanguages, responseLanguage);
        setSpatialDataSetIdentifier(spatialDataSetIdentifiers);
        setSupportedCRS(supportedCRS);
    }

    @Override
    public String getNamespace() {
        return InspireConstants.NS_INSPIRE_DLS;
    }

    @Override
    public Set<InspireUniqueResourceIdentifier> getSpatialDataSetIdentifier() {
        return spatialDataSetIdentifier;
    }

    @Override
    public InspireExtendedCapabilitiesDLS setSpatialDataSetIdentifier(
            Collection<InspireUniqueResourceIdentifier> spatialDataSetIdentifier) {
        if (CollectionHelper.isNotEmpty(spatialDataSetIdentifier)) {
            getSpatialDataSetIdentifier().clear();
            getSpatialDataSetIdentifier().addAll(spatialDataSetIdentifier);
        }
        return this;
    }

    @Override
    public InspireExtendedCapabilitiesDLS addSpatialDataSetIdentifier(
            InspireUniqueResourceIdentifier spatialDataSetIdentifier) {
        getSpatialDataSetIdentifier().add(spatialDataSetIdentifier);
        return this;

    }

    @Override
    public boolean isSetSpatialDataSetIdentifier() {
        return CollectionHelper.isNotEmpty(getSpatialDataSetIdentifier());
    }

    @Override
    public InspireExtendedCapabilitiesSupportedCRS setSupportedCRS(InspireSupportedCRS supportedCRS) {
        this.supportedCRS = supportedCRS;
        return this;
    }

    @Override
    public InspireSupportedCRS getSupportedCRS() {
        return supportedCRS;
    }

    @Override
    public boolean isSetSupportedCRS() {
        return getSupportedCRS() != null;
    }

}
