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
package org.n52.svalbard.decode.json;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.request.UpdateSensorRequest;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.XmlNamespaceDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class UpdateSensorRequestDecoder
        extends AbstractSosRequestDecoder<UpdateSensorRequest> {
    public UpdateSensorRequestDecoder() {
        super(UpdateSensorRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.UpdateSensorDescription);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.UPDATE_SENSOR_DESCRIPTION;
    }

    @Override
    protected UpdateSensorRequest decodeRequest(JsonNode node)
            throws DecodingException {
        UpdateSensorRequest req = new UpdateSensorRequest();
        req.setProcedureIdentifier(node.path(JSONConstants.PROCEDURE).textValue());
        String pdf = node.path(JSONConstants.PROCEDURE_DESCRIPTION_FORMAT).textValue();
        req.setProcedureDescriptionFormat(pdf);
        JsonNode procedureDescriptionNode = node.path(JSONConstants.PROCEDURE_DESCRIPTION);
        if (procedureDescriptionNode.isArray()) {
            for (JsonNode n : procedureDescriptionNode) {
                req.addProcedureDescriptionString(decodeProcedureDescription(n, pdf));
            }
        } else {
            req.addProcedureDescriptionString(decodeProcedureDescription(procedureDescriptionNode, pdf));
        }
        return req;
    }

    private SosProcedureDescription<?> decodeProcedureDescription(JsonNode node, String pdf)
            throws DecodingException {
        if (node.isTextual()) {
            return parseProcedureDesciption(node.textValue(), pdf);
        } else {
            SosProcedureDescription<?> pd =
                    parseProcedureDesciption(node.path(JSONConstants.DESCRIPTION).textValue(), pdf);
            if (node.has(JSONConstants.VALID_TIME)) {
                pd.setValidTime(parseTime(node.path(JSONConstants.VALID_TIME)));
            }
            return pd;
        }
    }

    private SosProcedureDescription<?> parseProcedureDesciption(String xml, String pdf)
            throws DecodingException {
        try {
            final XmlObject xb = XmlObject.Factory.parse(xml);
            Decoder<?, XmlObject> decoder = getDecoder(new XmlNamespaceDecoderKey(pdf, xb.getClass()));
            if (decoder == null) {
                throw new DecodingException(JSONConstants.PROCEDURE_DESCRIPTION_FORMAT,
                        "The requested %s is not supported!", JSONConstants.PROCEDURE_DESCRIPTION_FORMAT);
            }
            Object decode = decoder.decode(xb);
            if (decode instanceof SosProcedureDescription<?>) {
                return (SosProcedureDescription<?>) decode;
            } else if (decode instanceof AbstractFeature) {
                return new SosProcedureDescription<AbstractFeature>((AbstractFeature) decode);
            } else {
                throw new DecodingException("The decoded element {} is not of type {}!", decode.getClass().getName(),
                        AbstractFeature.class.getName());
            }
        } catch (XmlException xmle) {
            throw new DecodingException("Error while parsing procedure description of InsertSensor request!", xmle);
        }
    }
}
