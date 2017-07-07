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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gwml.GWMLConstants;
import org.n52.shetland.ogc.om.ObservationType;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.values.ProfileValue;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.collect.Sets;

import net.opengis.gwmlWell.x22.GWGeologyLogType;
import net.opengis.gwmlWell.x22.GWGeologyLogType.EndDepth;
import net.opengis.gwmlWell.x22.GWGeologyLogType.StartDepth;
import net.opengis.om.x20.OMObservationType;

public class GWGeologyLogObservationTypeEncoder
        extends OmEncoderv20 {

    private static final Set<EncoderKey> ENCODER_KEYS =
            CollectionHelper.union(CodingHelper.encoderKeysForElements(GWMLConstants.NS_GWML_22, OmObservation.class),
                    CodingHelper.encoderKeysForElements(GWMLConstants.NS_GWML_WELL_22, OmObservation.class));

    private static final Set<SupportedType> SUPPORTED_TYPES =
            Sets.newHashSet(new ObservationType(GWMLConstants.OBS_TYPE_GEOLOGY_LOG),
                    new ObservationType(GWMLConstants.OBS_TYPE_GEOLOGY_LOG),
                    new ObservationType(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION),
                    new ObservationType(OmConstants.OBS_TYPE_TEXT_OBSERVATION),
                    new ObservationType(OmConstants.OBS_TYPE_PROFILE_OBSERVATION));

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_RESPONSE_FORMATS = Collections.singletonMap(
            SosConstants.SOS,
            Collections.singletonMap(Sos2Constants.SERVICEVERSION, Collections.singleton(GWMLConstants.NS_GWML_22)));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Map<String, Set<SupportedType>> getSupportedResponseFormatObservationTypes() {
        return Collections.singletonMap(GWMLConstants.NS_GWML_22, getSupportedTypes());
    }

    @Override
    public boolean isObservationAndMeasurmentV20Type() {
        return true;
    }

    @Override
    protected XmlObject createResult(OmObservation sosObservation) throws EncodingException {
        return super.createResult(sosObservation);
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
        return super.getDefaultFeatureEncodingNamespace();
    }

    @Override
    protected String getDefaultProcedureEncodingNamspace() {
        return super.getDefaultProcedureEncodingNamspace();
    }

    @Override
    protected boolean convertEncodedProcedure() {
        return false;
    }

    @Override
    protected OMObservationType createOmObservationType() {
        return GWGeologyLogType.Factory.newInstance(getXmlOptions());
    }

    @Override
    protected void addObservationType(OMObservationType xbObservation, String observationType) {
        xbObservation.addNewType().setHref(GWMLConstants.OBS_TYPE_GEOLOGY_LOG);
    }

    @Override
    protected void addAddtitionalInformation(OMObservationType omot, OmObservation observation)
            throws EncodingException {
        if (omot instanceof GWGeologyLogType && observation.getValue().getValue() instanceof ProfileValue) {
            ProfileValue value = (ProfileValue) observation.getValue().getValue();
            if (value.isSetFromLevel()) {
                encodeStartDepth(((GWGeologyLogType) omot) .addNewStartDepth(), value.getFromLevel());
            }
            if (value.isSetToLevel()) {
                encodeEndDepth(((GWGeologyLogType) omot) .addNewEndDepth(), value.getToLevel());
            }
        }
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(GWMLConstants.NS_GWML_22, GWMLConstants.NS_GWML_2_PREFIX);
        nameSpacePrefixMap.put(GWMLConstants.NS_GWML_WELL_22, GWMLConstants.NS_GWML_WELL_2_PREFIX);
    }

    @Override
    public Set<SchemaLocation> getSchemaLocations() {
        Set<SchemaLocation> schemaLocations =
                Sets.newHashSet(GWMLConstants.GWML_22_SCHEMA_LOCATION, GWMLConstants.GWML_WELL_22_SCHEMA_LOCATION);
        schemaLocations.addAll(super.getSchemaLocations());
        return schemaLocations;
    }

    private void encodeStartDepth(StartDepth sd, SweQuantity sweQuantity) throws EncodingException {
        sd.addNewQuantity().set(encodeSweCommon(sweQuantity));
    }

    private void encodeEndDepth(EndDepth ed, SweQuantity sweQuantity) throws EncodingException {
        ed.addNewQuantity().set(encodeSweCommon(sweQuantity));
    }

}
