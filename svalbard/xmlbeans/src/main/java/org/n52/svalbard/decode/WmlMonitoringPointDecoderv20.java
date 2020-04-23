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
package org.n52.svalbard.decode;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.OGCConstants;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.om.features.SfConstants;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.om.series.wml.WmlMonitoringPoint;
import org.n52.shetland.ogc.sos.FeatureType;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.ConformanceClasses;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderXmlInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.opengis.waterml.x20.MonitoringPointDocument;
import net.opengis.waterml.x20.MonitoringPointPropertyType;
import net.opengis.waterml.x20.MonitoringPointType;

public class WmlMonitoringPointDecoderv20 extends SamplingDecoderv20 {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmlMonitoringPointDecoderv20.class);

    private static final Set<SupportedType> SUPPORTED_TYPES =
            ImmutableSet.<SupportedType> builder().add(new FeatureType(OGCConstants.UNKNOWN))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_POINT))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_CURVE))
                    .add(new FeatureType(SfConstants.SAMPLING_FEAT_TYPE_SF_SAMPLING_SURFACE)).build();

    private static final Set<String> CONFORMANCE_CLASSES =
            Sets.newHashSet(ConformanceClasses.OM_V2_SPATIAL_SAMPLING, ConformanceClasses.OM_V2_SAMPLING_POINT,
                    ConformanceClasses.OM_V2_SAMPLING_CURVE, ConformanceClasses.OM_V2_SAMPLING_SURFACE);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(WaterMLConstants.NS_WML_20, MonitoringPointDocument.class,
                    MonitoringPointPropertyType.class, MonitoringPointType.class));

    public WmlMonitoringPointDecoderv20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }


    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();

    }

    @Override
    public AbstractFeature decode(XmlObject element) throws DecodingException {
        // validate XmlObject
        XmlHelper.validateDocument(element);
        if (element instanceof MonitoringPointDocument) {
            return decode(((MonitoringPointDocument) element).getMonitoringPoint());
        } else if (element instanceof MonitoringPointPropertyType) {
            if (((MonitoringPointPropertyType) element).getMonitoringPoint() != null) {
                return decode(((MonitoringPointPropertyType) element).getMonitoringPoint());
            }
            return parseMonitoringPointPropertyType((MonitoringPointPropertyType) element);
        } else if (element instanceof MonitoringPointType) {
            return parseMonitoringPoint((MonitoringPointType) element);
        }
        throw new UnsupportedDecoderXmlInputException(this, element);
    }


    private AbstractFeature parseMonitoringPoint(MonitoringPointType mpt) throws DecodingException {
        WmlMonitoringPoint monitoingPoint = new WmlMonitoringPoint(null, mpt.getId());
        // TODO parse special monitoring point elements
        return parseSpatialSamplingFeature(mpt, monitoingPoint);
    }


    private AbstractFeature parseMonitoringPointPropertyType(MonitoringPointPropertyType element) {
        // TODO Auto-generated method stub
        return null;
    }
}
