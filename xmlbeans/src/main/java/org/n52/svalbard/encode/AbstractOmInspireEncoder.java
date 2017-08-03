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
package org.n52.svalbard.encode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.inspire.base.InspireBaseConstants;
import org.n52.shetland.inspire.omor.InspireOMORConstants;
import org.n52.shetland.inspire.omso.InspireOMSOConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.om.ObservationType;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;

import com.google.common.collect.Sets;

import net.opengis.om.x20.OMObservationType;

/**
 * Abstract {@link Encoder} implementation for INSPIRES OM
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public abstract class AbstractOmInspireEncoder extends AbstractWmlEncoderv20 {

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS =
            Collections.singletonMap(SosConstants.SOS, Collections.singletonMap(Sos2Constants.SERVICEVERSION,
                    Collections.singleton(InspireOMSOConstants.NS_OMSO_30)));

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return true;
    }

    @Override
    public boolean shouldObservationsWithSameXBeMerged() {
        return false;
    }

    @Override
    public boolean supportsResultStreamingForMergedValues() {
        return false;
    }

    @Override
    public Set<String> getSupportedResponseFormats(String service, String version) {
        if (SUPPORTED_RESPONSE_FORMATS.get(service) != null
                && SUPPORTED_RESPONSE_FORMATS.get(service).get(version) != null) {
            return SUPPORTED_RESPONSE_FORMATS.get(service).get(version);
        }
        return new HashSet<>(0);
    }

    @Override
    public String getDefaultFeatureEncodingNamespace() {
        return SfConstants.NS_SAMS;
    }

    @Override
    protected String getDefaultProcedureEncodingNamspace() {
        return SensorMLConstants.NS_SML;
    }

    @Override
    protected boolean convertEncodedProcedure() {
        return false;
    }

    @Override
    protected void addObservationType(OMObservationType xbObservation, String observationType) {
        xbObservation.addNewType().setHref(getObservationType());
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        super.addNamespacePrefixToMap(nameSpacePrefixMap);
        nameSpacePrefixMap.put(InspireBaseConstants.NS_BASE, InspireBaseConstants.NS_BASE_PREFIX);
        nameSpacePrefixMap.put(InspireOMORConstants.NS_OMOR_30, InspireOMORConstants.NS_OMOR_PREFIX);
        nameSpacePrefixMap.put(InspireOMSOConstants.NS_OMSO_30, InspireOMSOConstants.NS_OMSO_PREFIX);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.singletonMap(InspireOMSOConstants.NS_OMSO_30, getSupportedTypes());
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(Sets.newHashSet(new ObservationType(getObservationType())));
    }

    protected abstract String getObservationType();

}
