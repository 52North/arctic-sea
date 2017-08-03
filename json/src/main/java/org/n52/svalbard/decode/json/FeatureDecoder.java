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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class FeatureDecoder
        extends JSONDecoder<AbstractFeature> {
    private final JSONDecoder<Geometry> geometryDecoder = new GeoJSONDecoder();

    public FeatureDecoder() {
        super(AbstractFeature.class);
    }

    @Override
    public AbstractFeature decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        if (validate) {
            JSONValidator.getInstance().validateAndThrow(node, SchemaConstants.Common.FEATURE_OF_INTEREST);
        }
        return decodeJSON(node);
    }

    protected AbstractFeature decodeJSON(JsonNode node)
            throws DecodingException {
        if (node.isArray()) {
            return parseFeatureCollection(node);
        } else {
            return parseSamplingFeature(node);
        }
    }

    protected SamplingFeature parseSamplingFeature(JsonNode node)
            throws DecodingException {
        if (node.isTextual()) {
            return new SamplingFeature(parseCodeWithAuthority(node));
        } else if (node.isObject()) {
            SamplingFeature foi = new SamplingFeature(parseCodeWithAuthority(node.path(JSONConstants.IDENTIFIER)));
            foi.setGeometry(parseGeometry(node));
            foi.setSampledFeatures(parseSampledFeatures(node));
            foi.setName(parseNames(node));
            return foi;
        } else {
            return null;
        }
    }

    private FeatureCollection parseFeatureCollection(JsonNode node)
            throws DecodingException {
        if (node.isArray()) {
            FeatureCollection collection = new FeatureCollection();
            for (JsonNode n : node) {
                collection.addMember(parseSamplingFeature(n));
            }
            return collection;
        } else {
            return null;
        }
    }

    protected List<AbstractFeature> parseSampledFeatures(JsonNode node)
            throws DecodingException {
        final JsonNode sfnode = node.path(JSONConstants.SAMPLED_FEATURE);
        if (sfnode.isArray()) {
            ArrayList<AbstractFeature> features = new ArrayList<>(sfnode.size());
            for (JsonNode n : sfnode) {
                features.add(parseSamplingFeature(n));
            }
            return features;
        } else {
            final SamplingFeature sff = parseSamplingFeature(sfnode);
            if (sff == null) {
                return Collections.emptyList();
            } else {
                return Collections.<AbstractFeature> singletonList(sff);
            }
        }
    }

    protected Geometry parseGeometry(JsonNode node)
            throws DecodingException {
        return geometryDecoder.decodeJSON(node.path(JSONConstants.GEOMETRY), false);
    }

    private List<CodeType> parseNames(JsonNode node) {
        JsonNode name = node.path(JSONConstants.NAME);
        if (name.isArray()) {
            return Streams.stream(name).map(this::parseCodeType).collect(toList());
        } else {
            return Collections.singletonList(parseCodeType(name));
        }
    }
}
