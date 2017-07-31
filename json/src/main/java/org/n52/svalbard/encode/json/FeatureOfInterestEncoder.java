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

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.AbstractSamplingFeature;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

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
public class FeatureOfInterestEncoder extends JSONEncoder<AbstractFeature> {
    public FeatureOfInterestEncoder() {
        super(AbstractFeature.class);
    }

    @Override
    public JsonNode encodeJSON(AbstractFeature t) throws EncodingException {
        if (t instanceof FeatureCollection) {
            return encodeFeatureCollection(t);
        } else if (t instanceof AbstractSamplingFeature) {
            return encodeAbstractSamplingFeature(t);
        } else {
            throw new UnsupportedEncoderInputException(this, t);
        }
    }

    private JsonNode encodeAbstractSamplingFeature(AbstractFeature t) throws EncodingException {
        AbstractSamplingFeature sf = (AbstractSamplingFeature) t;
        if (sf.isSetUrl()) {
            return nodeFactory().textNode(sf.getUrl());
        } else if (!sf.isSetGeometry()) {
            return nodeFactory().textNode(sf.getIdentifierCodeWithAuthority().getValue());
        } else {
            ObjectNode json = nodeFactory().objectNode();
            encodeIdentifier(sf, json);
            encodeNames(sf, json);
            encodeSampledFeatures(sf, json);
            encodeGeometry(sf, json);
            return json;
        }
    }

    private JsonNode encodeFeatureCollection(AbstractFeature t) throws EncodingException {
        FeatureCollection featureCollection = (FeatureCollection) t;
        ArrayNode a = nodeFactory().arrayNode();
        for (AbstractFeature af : featureCollection) {
            a.add(encodeObjectToJson(af));
        }
        return a;
    }

    private void encodeIdentifier(AbstractSamplingFeature sf, ObjectNode json) {
        if (sf.isSetIdentifier()) {
            json.set(JSONConstants.IDENTIFIER, encodeCodeWithAuthority(sf.getIdentifierCodeWithAuthority()));
        }

    }

    private void encodeNames(AbstractSamplingFeature samplingFeature, ObjectNode json) {
        if (samplingFeature.isSetName()) {
            if (samplingFeature.getName().size() == 1) {
                json.set(JSONConstants.NAME, encodeCodeType(samplingFeature.getName().iterator().next()));
            } else {
                ArrayNode names = json.putArray(JSONConstants.NAME);
                for (CodeType name : samplingFeature.getName()) {
                    names.add(encodeCodeType(name));
                }
            }
        }
    }

    private void encodeSampledFeatures(AbstractSamplingFeature sf, ObjectNode json) throws EncodingException {
        if (sf.isSetSampledFeatures()) {
            if (sf.getSampledFeatures().size() == 1) {
                json.set(JSONConstants.SAMPLED_FEATURE, encodeObjectToJson(sf.getSampledFeatures().iterator().next()));
            } else {
                ArrayNode sampledFeatures = json.putArray(JSONConstants.SAMPLED_FEATURE);
                for (AbstractFeature sampledFeature : sf.getSampledFeatures()) {
                    sampledFeatures.add(encodeObjectToJson(sampledFeature));
                }
            }
        }
    }

    private void encodeGeometry(AbstractSamplingFeature sf, ObjectNode json) throws EncodingException {
        if (sf.isSetGeometry()) {
            json.set(JSONConstants.GEOMETRY, encodeObjectToJson(sf.getGeometry()));
        }
    }
}
