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
package org.n52.svalbard.encode.inspire.ef;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.encode.ClassToClassEncoderKey;
import org.n52.sos.encode.EncoderKey;
import org.n52.sos.encode.XmlPropertyTypeEncoderKey;
import org.n52.sos.exception.ows.concrete.UnsupportedEncoderInputException;
import org.n52.sos.ogc.gml.AbstractFeature;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.SosConstants.HelperValues;
import org.n52.svalbard.inspire.ef.EnvironmentalMonitoringFacility;
import org.n52.svalbard.inspire.ef.InspireEfConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.ef.x40.EnvironmentalMonitoringFacilityPropertyType;

public class EnvironmentalMonitoringFaciltityPropertyTypeEncoder
        extends AbstractEnvironmentalMonitoringFaciltityEncoder {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EnvironmentalMonitoringFaciltityPropertyTypeEncoder.class);

    protected static final Set<EncoderKey> ENCODER_KEYS = Sets.newHashSet(
            new ClassToClassEncoderKey(EnvironmentalMonitoringFacility.class, EnvironmentalMonitoringFacilityPropertyType.class),
            new XmlPropertyTypeEncoderKey(InspireEfConstants.NS_EF, EnvironmentalMonitoringFacility.class));

    public EnvironmentalMonitoringFaciltityPropertyTypeEncoder() {
        LOGGER.debug("Encoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(ENCODER_KEYS));
    }

    @Override
    public Set<EncoderKey> getEncoderKeyType() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(final AbstractFeature abstractFeature, final Map<HelperValues, String> additionalValues)
            throws OwsExceptionReport {
        if (abstractFeature instanceof EnvironmentalMonitoringFacility) {
            EnvironmentalMonitoringFacilityPropertyType emfpt =
                    EnvironmentalMonitoringFacilityPropertyType.Factory.newInstance();
            emfpt.setEnvironmentalMonitoringFacility(
                    createEnvironmentalMonitoringFaciltityType((EnvironmentalMonitoringFacility) abstractFeature));
            return emfpt;
        }
        throw new UnsupportedEncoderInputException(this, abstractFeature);
    }

}
