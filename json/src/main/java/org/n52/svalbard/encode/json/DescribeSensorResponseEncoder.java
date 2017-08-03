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
package org.n52.svalbard.encode.json;

import java.util.List;
import java.util.function.Supplier;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.SosProcedureDescriptionUnknownType;
import org.n52.shetland.ogc.sos.response.DescribeSensorResponse;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class DescribeSensorResponseEncoder
        extends AbstractSosResponseEncoder<DescribeSensorResponse> {

    private EncoderRepository encoderRepository;
    private Supplier<XmlOptions> xmlOptions;

    public DescribeSensorResponseEncoder() {
        super(DescribeSensorResponse.class, SosConstants.Operations.DescribeSensor);
    }

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    @Inject
    public void setXmlOptions(Supplier<XmlOptions> xmlOptions) {
        this.xmlOptions = xmlOptions;
    }

    @Override
    protected void encodeResponse(ObjectNode json, DescribeSensorResponse t)
            throws EncodingException {
        json.put(JSONConstants.PROCEDURE_DESCRIPTION_FORMAT, t.getOutputFormat());
        json.set(JSONConstants.PROCEDURE_DESCRIPTION,
                encodeDescriptions(t.getProcedureDescriptions(), t.getOutputFormat()));

    }

    private String toString(AbstractFeature desc, String format)
            throws EncodingException {
        if (desc instanceof SosProcedureDescriptionUnknownType && desc.isSetXml()) {
            return desc.getXml();
        }
        return ((XmlObject) encoderRepository.getEncoder(CodingHelper.getEncoderKey(format, desc)).encode(desc))
                .xmlText(xmlOptions.get());
    }

    private JsonNode encodeDescription(SosProcedureDescription<?> desc, String format)
            throws EncodingException {
        String xml = toString(desc, format);
        if (desc.isSetValidTime()) {
            ObjectNode j = nodeFactory().objectNode();
            j.set(JSONConstants.VALID_TIME, encodeObjectToJson(desc.getValidTime()));
            j.put(JSONConstants.DESCRIPTION, xml);
            return j;
        } else {
            return nodeFactory().textNode(xml);
        }
    }

    private JsonNode encodeDescriptions(List<SosProcedureDescription<?>> descs, String format)
            throws EncodingException {
        if (descs.size() == 1) {
            return encodeDescription(descs.get(0), format);
        } else {
            ArrayNode a = nodeFactory().arrayNode();
            for (SosProcedureDescription<?> desc : descs) {
                a.add(encodeDescription(desc, format));
            }
            return a;
        }
    }
}
