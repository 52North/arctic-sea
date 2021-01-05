/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.sensorML.AbstractProcess;
import org.n52.shetland.ogc.sensorML.SensorML;
import org.n52.shetland.ogc.sensorML.SensorMLConstants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.DescribeSensorResponse;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class SosV1DescribeSensorResponseEncoder extends AbstractSosV1ResponseEncoder<DescribeSensorResponse> {
    public SosV1DescribeSensorResponseEncoder() {
        super(SosConstants.Operations.DescribeSensor.name(), DescribeSensorResponse.class);
    }

    @Override
    protected XmlObject create(DescribeSensorResponse response) throws EncodingException {
        if (response.isSetProcedureDescriptions()) {
            AbstractFeature procedureDescription =
                    response.getProcedureDescriptions().iterator().next().getProcedureDescription();
            if (checkFormat(response.getOutputFormat()) && !(procedureDescription instanceof SensorML)
                    && procedureDescription instanceof AbstractProcess) {
                procedureDescription = new SensorML().addMember((AbstractProcess) procedureDescription);
            }
            return encodeObjectToXml(response.getOutputFormat(), procedureDescription);
        }
        return null;
    }

    private boolean checkFormat(String outputFormat) {
        return checkForUrlVsMimeType(outputFormat).contains(SensorMLConstants.SENSORML_OUTPUT_FORMAT_MIME_TYPE);
    }

    private Set<String> checkForUrlVsMimeType(String procedureDescriptionFormat) {
        Set<String> possibleFormats = Sets.newHashSet();
        possibleFormats.add(procedureDescriptionFormat);
        if (SensorMLConstants.SENSORML_OUTPUT_FORMAT_MIME_TYPE.equalsIgnoreCase(procedureDescriptionFormat)) {
            possibleFormats.add(SensorMLConstants.SENSORML_OUTPUT_FORMAT_URL);
        } else if (SensorMLConstants.SENSORML_OUTPUT_FORMAT_URL.equalsIgnoreCase(procedureDescriptionFormat)) {
            possibleFormats.add(SensorMLConstants.SENSORML_OUTPUT_FORMAT_MIME_TYPE);
        }
        return possibleFormats;
    }

}
