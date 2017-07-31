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

import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.sos.response.DescribeSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.GmlHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.collect.Sets;

import net.opengis.swes.x20.DescribeSensorResponseDocument;
import net.opengis.swes.x20.DescribeSensorResponseType;
import net.opengis.swes.x20.SensorDescriptionType;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class DescribeSensorResponseEncoder
        extends AbstractSwesResponseEncoder<DescribeSensorResponse> {
    public DescribeSensorResponseEncoder() {
        super(SosConstants.Operations.DescribeSensor.name(), DescribeSensorResponse.class);
    }

    @Override
    protected XmlObject create(DescribeSensorResponse response)
            throws EncodingException {
        DescribeSensorResponseDocument doc = DescribeSensorResponseDocument.Factory.newInstance(getXmlOptions());
        DescribeSensorResponseType dsr = doc.addNewDescribeSensorResponse();
        dsr.setProcedureDescriptionFormat(response.getOutputFormat());
        for (SosProcedureDescription<?> sosProcedureDescription : response.getProcedureDescriptions()) {
            SensorDescriptionType sensorDescription = dsr.addNewDescription().addNewSensorDescription();
            sensorDescription.addNewData().set(getSensorDescription(response, sosProcedureDescription));
            if (sosProcedureDescription.isSetValidTime()) {
                XmlObject xoValidTime =
                        encodeObjectToXml(GmlConstants.NS_GML_32, sosProcedureDescription.getValidTime());
                XmlObject substitution = sensorDescription.addNewValidTime().addNewAbstractTimeGeometricPrimitive()
                        .substitute(GmlHelper.getGml321QnameForITime(sosProcedureDescription.getValidTime()),
                                xoValidTime.schemaType());
                substitution.set(xoValidTime);
            }
        }
        // in a single observation the gml:ids must be unique
        if (response.getProcedureDescriptions().size() > 1) {
            XmlHelper.makeGmlIdsUnique(doc.getDomNode());
        }
        return doc;
    }

    private XmlObject getSensorDescription(DescribeSensorResponse response, SosProcedureDescription<?> abstractFeature)
            throws EncodingException {
        if (abstractFeature instanceof SosProcedureDescriptionUnknownType && abstractFeature.isSetXml()) {
            try {
                return XmlHelper.parseXmlString(abstractFeature.getXml());
            } catch (DecodingException de) {
                throw new EncodingException("An xml error occured when parsing the request!", de);
            }
        }
        return encodeObjectToXml(response.getOutputFormat(), abstractFeature.getProcedureDescription());
    }

    @Override
    public Set<SchemaLocation> getConcreteSchemaLocations() {
        return Sets.newHashSet(SwesConstants.SWES_20_DESCRIBE_SENSOR_SCHEMA_LOCATION);
    }
}
