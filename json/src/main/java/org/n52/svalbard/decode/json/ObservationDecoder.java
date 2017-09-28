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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.om.NamedValue;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservableProperty;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.values.BooleanValue;
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.shetland.ogc.om.values.CountValue;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.shetland.ogc.om.values.HrefAttributeValue;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.shetland.ogc.om.values.ReferenceValue;
import org.n52.shetland.ogc.om.values.TextValue;
import org.n52.shetland.ogc.sensorML.SensorML;
import org.n52.shetland.w3c.xlink.W3CHrefAttribute;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class ObservationDecoder
        extends JSONDecoder<OmObservation> {

    private static final Set<SupportedType> SUPPORTED_TYPES = ImmutableSet.<SupportedType> builder()
            // .add(OBS_TYPE_SWE_ARRAY_OBSERVATION)
            // .add(OBS_TYPE_COMPLEX_OBSERVATION)
            .add(OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION_TYPE).add(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION_TYPE)
            .add(OmConstants.OBS_TYPE_COUNT_OBSERVATION_TYPE).add(OmConstants.OBS_TYPE_MEASUREMENT_TYPE)
            .add(OmConstants.OBS_TYPE_TEXT_OBSERVATION_TYPE).add(OmConstants.OBS_TYPE_TRUTH_OBSERVATION_TYPE)
            .add(OmConstants.OBS_TYPE_REFERENCE_OBSERVATION_TYPE).build();

    private final JSONDecoder<AbstractFeature> featureDecoder = new FeatureDecoder();

    private final JSONDecoder<Geometry> geometryDecoder = new GeoJSONDecoder();

    public ObservationDecoder() {
        super(OmObservation.class);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public OmObservation decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        if (node == null) {
            return null;
        }
        if (validate) {
            JSONValidator.getInstance().validateAndThrow(node, SchemaConstants.Observation.OBSERVATION);
        }
        return decodeJSON(node);
    }

    protected OmObservation decodeJSON(JsonNode node)
            throws DecodingException {
        if (node.isObject()) {
            OmObservation o = new OmObservation();
            o.setIdentifier(parseIdentifier(node));
            o.setValidTime(parseValidTime(node));
            o.setResultTime(parseResultTime(node));
            o.setValue(parseValue(node));
            o.setParameter(parseParameter(node));
            o.setObservationConstellation(parseObservationConstellation(node));
            return o;
        } else {
            return null;
        }
    }

    public OmObservationConstellation parseObservationConstellation(JsonNode node)
            throws DecodingException {
        OmObservationConstellation oc = new OmObservationConstellation();
        oc.setProcedure(parseProcedure(node));
        oc.setObservableProperty(parseObservableProperty(node));
        oc.setObservationType(parseObservationType(node));
        oc.setFeatureOfInterest(parseFeatureOfInterest(node));
        return oc;
    }

    protected AbstractFeature parseProcedure(JsonNode node) {
        return new SensorML().setIdentifier(node.path(JSONConstants.PROCEDURE).textValue());
    }

    private AbstractPhenomenon parseObservableProperty(JsonNode node) {
        return new OmObservableProperty(node.path(JSONConstants.OBSERVED_PROPERTY).textValue());
    }

    private CodeWithAuthority parseIdentifier(JsonNode node) {
        return parseCodeWithAuthority(node.path(JSONConstants.IDENTIFIER));
    }

    protected String parseObservationType(JsonNode node) {
        return node.path(JSONConstants.TYPE).textValue();
    }

    protected TimePeriod parseValidTime(JsonNode node)
            throws DecodingException {
        return parseTimePeriod(node.path(JSONConstants.VALID_TIME));
    }

    protected TimeInstant parseResultTime(JsonNode node)
            throws DecodingException {
        return parseTimeInstant(node.path(JSONConstants.RESULT_TIME));
    }

    private Time parsePhenomenonTime(JsonNode node)
            throws DecodingException {
        return parseTime(node.path(JSONConstants.PHENOMENON_TIME));
    }

    protected Collection<NamedValue<?>> parseParameter(JsonNode node)
            throws DecodingException {
        Set<NamedValue<?>> parameters = Sets.newTreeSet();
        JsonNode parameter = node.path(JSONConstants.PARAMETER);
        if (parameter.isArray()) {
            for (JsonNode jsonNode : parameter) {
                parameters.add(parseNamedValue(jsonNode));
            }
        } else if (parameter.isObject()) {
            parameters.add(parseNamedValue(parameter));
        }
        return parameters;
    }

    private NamedValue<?> parseNamedValue(JsonNode parameter)
            throws DecodingException {
        JsonNode namedValue = parameter.path(JSONConstants.NAMED_VALUE);
        NamedValue<?> nv = parseNamedValueValue(namedValue.path(JSONConstants.VALUE));
        ReferenceType referenceType = new ReferenceType(namedValue.path(JSONConstants.NAME).asText());
        nv.setName(referenceType);
        return nv;
    }

    private NamedValue<?> parseNamedValueValue(JsonNode value)
            throws DecodingException {
        if (value.isTextual()) {
            NamedValue<W3CHrefAttribute> nv = new NamedValue<>();
            nv.setValue(new HrefAttributeValue(new W3CHrefAttribute(value.asText())));
            return nv;
        } else if (value.isBoolean()) {
            NamedValue<Boolean> nv = new NamedValue<>();
            nv.setValue(new BooleanValue(value.asBoolean()));
            return nv;
        } else if (value.isInt()) {
            NamedValue<Integer> nv = new NamedValue<>();
            nv.setValue(new CountValue(value.asInt()));
            return nv;
        } else if (value.isObject()) {
            if (value.has(JSONConstants.CODESPACE)) {
                NamedValue<String> nv = new NamedValue<>();
                nv.setValue(parseCategroyValue(value));
                return nv;
            } else if (value.has(JSONConstants.UOM)) {
                NamedValue<Double> nv = new NamedValue<>();
                nv.setValue(parseQuantityValue(value));
                return nv;
            } else if (value.has(JSONConstants.COORDINATES)) {
                NamedValue<Geometry> nv = new NamedValue<>();
                nv.setValue(new GeometryValue(geometryDecoder.decodeJSON(value, false)));
                return nv;
            }
        }
        throw new DecodingException("%s is not yet supported", value.toString());
    }

    protected AbstractFeature parseFeatureOfInterest(JsonNode node)
            throws DecodingException {
        return featureDecoder.decodeJSON(node.path(JSONConstants.FEATURE_OF_INTEREST), false);
    }

    private ObservationValue<?> parseValue(JsonNode node)
            throws DecodingException {
        String type = parseObservationType(node);
        if (type.equals(OmConstants.OBS_TYPE_MEASUREMENT)) {
            return parseMeasurementValue(node);
        } else if (type.equals(OmConstants.OBS_TYPE_TEXT_OBSERVATION)) {
            return parseTextObservationValue(node);
        } else if (type.equals(OmConstants.OBS_TYPE_COUNT_OBSERVATION)) {
            return parseCountObservationValue(node);
        } else if (type.equals(OmConstants.OBS_TYPE_TRUTH_OBSERVATION)) {
            return parseTruthObservationValue(node);
        } else if (type.equals(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION)) {
            return parseCategoryObservationValue(node);
        } else if (type.equals(OmConstants.OBS_TYPE_REFERENCE_OBSERVATION)) {
            return parseReferenceObservationValue(node);
        } else if (type.equals(OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION)) {
            return parseGeometryObservation(node);
        } else {
            throw new JSONDecodingException("Unsupported observationType: " + type);
        }
    }

    protected ObservationValue<?> parseMeasurementValue(JsonNode node)
            throws DecodingException {
        final QuantityValue qv = parseQuantityValue(node.path(JSONConstants.RESULT));
        // new
        // QuantityValue(node.path(JSONConstants.RESULT).path(JSONConstants.VALUE).doubleValue(),
        // node
        // .path(JSONConstants.RESULT).path(JSONConstants.UOM).textValue());
        return new SingleObservationValue<>(parsePhenomenonTime(node), qv);
    }

    private QuantityValue parseQuantityValue(JsonNode node)
            throws DecodingException {
        return new QuantityValue(node.path(JSONConstants.VALUE).doubleValue(),
                node.path(JSONConstants.UOM).textValue());
    }

    private ObservationValue<?> parseTextObservationValue(JsonNode node)
            throws DecodingException {
        final TextValue v = new TextValue(node.path(JSONConstants.RESULT).textValue());
        return new SingleObservationValue<>(parsePhenomenonTime(node), v);
    }

    private ObservationValue<?> parseCountObservationValue(JsonNode node)
            throws DecodingException {
        final CountValue v = new CountValue(node.path(JSONConstants.RESULT).intValue());
        return new SingleObservationValue<>(parsePhenomenonTime(node), v);
    }

    private ObservationValue<?> parseTruthObservationValue(JsonNode node)
            throws DecodingException {
        final BooleanValue v = new BooleanValue(node.path(JSONConstants.RESULT).booleanValue());
        return new SingleObservationValue<>(parsePhenomenonTime(node), v);
    }

    private ObservationValue<?> parseCategoryObservationValue(JsonNode node)
            throws DecodingException {
        final CategoryValue v = parseCategroyValue(node.path(JSONConstants.RESULT));
        // new
        // CategoryValue(node.path(JSONConstants.RESULT).path(JSONConstants.VALUE).textValue(),
        // node
        // .path(JSONConstants.RESULT).path(JSONConstants.CODESPACE).textValue());
        return new SingleObservationValue<>(parsePhenomenonTime(node), v);
    }

    private CategoryValue parseCategroyValue(JsonNode node)
            throws DecodingException {
        return new CategoryValue(node.path(JSONConstants.VALUE).textValue(),
                node.path(JSONConstants.CODESPACE).textValue());
    }

    private ObservationValue<?> parseGeometryObservation(JsonNode node)
            throws DecodingException {
        GeometryValue v = new GeometryValue(geometryDecoder.decodeJSON(node.path(JSONConstants.RESULT), false));
        return new SingleObservationValue<Geometry>(parsePhenomenonTime(node), v);
    }

    private ObservationValue<?> parseReferenceObservationValue(JsonNode node) throws DecodingException {
        ReferenceValue v = new ReferenceValue(parseReferenceValue(node.path(JSONConstants.RESULT)));
        return new SingleObservationValue<ReferenceType>(parsePhenomenonTime(node), v);
    }

    private ReferenceType parseReferenceValue(JsonNode node) {
        ReferenceType ref = new ReferenceType();
        if (!node.path(JSONConstants.HREF).isMissingNode()) {
            ref.setHref(node.path(JSONConstants.HREF).asText());
        }
        if (!node.path(JSONConstants.TITLE).isMissingNode()) {
            ref.setTitle(node.path(JSONConstants.TITLE).asText());
        }
        if (!node.path(JSONConstants.ROLE).isMissingNode()) {
            ref.setRole(node.path(JSONConstants.ROLE).asText());
        }
        return ref;
    }

}
