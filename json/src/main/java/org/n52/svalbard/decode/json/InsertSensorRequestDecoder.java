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

import java.util.Collections;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.shetland.ogc.sos.SosProcedureDescription;
import org.n52.shetland.ogc.sos.request.InsertSensorRequest;
import org.n52.shetland.ogc.swes.SwesFeatureRelationship;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.XmlNamespaceDecoderKey;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class InsertSensorRequestDecoder
        extends AbstractSosRequestDecoder<InsertSensorRequest> {
    private final JSONDecoder<AbstractFeature> featureDecoder = new FeatureDecoder();

    public InsertSensorRequestDecoder() {
        super(InsertSensorRequest.class, SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.InsertSensor);
    }

    @Override
    protected String getSchemaURI() {
        return SchemaConstants.Request.INSERT_SENSOR;
    }

    @Override
    protected InsertSensorRequest decodeRequest(JsonNode node)
            throws DecodingException {
        final InsertSensorRequest r = new InsertSensorRequest();
        final SosInsertionMetadata meta = new SosInsertionMetadata();
        meta.setFeatureOfInterestTypes(parseStringOrStringList(node.path(JSONConstants.FEATURE_OF_INTEREST_TYPE)));
        meta.setObservationTypes(parseStringOrStringList(node.path(JSONConstants.OBSERVATION_TYPE)));
        r.setMetadata(meta);
        r.setObservableProperty(parseStringOrStringList(node.path(JSONConstants.OBSERVABLE_PROPERTY)));
        r.setProcedureDescriptionFormat(node.path(JSONConstants.PROCEDURE_DESCRIPTION_FORMAT).textValue());
        r.setRelatedFeature(parseFeatureRelationships(node.path(JSONConstants.RELATED_FEATURE)));
        r.setProcedureDescription(parseProcedureDescription(node.path(JSONConstants.PROCEDURE_DESCRIPTION),
                r.getProcedureDescriptionFormat()));
        return r;
    }

    protected List<SwesFeatureRelationship> parseFeatureRelationships(JsonNode node)
            throws DecodingException {
        if (node.isArray()) {
            List<SwesFeatureRelationship> list = Lists.newArrayListWithExpectedSize(node.size());
            for (JsonNode n : node) {
                if (n.isObject()) {
                    list.add(parseFeatureRelationship(n));
                }
            }
            return list;
        } else if (node.isObject()) {
            return Collections.singletonList(parseFeatureRelationship(node));
        } else {
            return null;
        }
    }

    protected SwesFeatureRelationship parseFeatureRelationship(JsonNode node)
            throws DecodingException {
        return new SwesFeatureRelationship(node.path(JSONConstants.ROLE).textValue(),
                featureDecoder.decodeJSON(node.path(JSONConstants.TARGET), false));
    }

    private SosProcedureDescription<?> parseProcedureDescription(JsonNode path, String pdf)
            throws DecodingException {
        try {
            final XmlObject xb = XmlObject.Factory.parse(path.textValue());
            Decoder<?, XmlObject> decoder = getProcedureDescriptionDecoder(pdf, xb);
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
        } catch (final XmlException xmle) {
            throw new DecodingException("Error while parsing procedure description of InsertSensor request!", xmle);
        }
    }

    protected Decoder<?, XmlObject> getProcedureDescriptionDecoder(String pdf, XmlObject xb) {
        return getDecoder(new XmlNamespaceDecoderKey(pdf, xb.getClass()));
    }
}
