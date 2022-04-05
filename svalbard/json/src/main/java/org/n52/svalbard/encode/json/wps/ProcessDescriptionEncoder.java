/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.encode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsAnyValue;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsRange;
import org.n52.shetland.ogc.ows.OwsValue;
import org.n52.shetland.ogc.ows.OwsValueRestriction;
import org.n52.shetland.ogc.ows.OwsValuesReference;
import org.n52.shetland.ogc.wps.Format;
import org.n52.shetland.ogc.wps.description.BoundingBoxDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxInputDescription;
import org.n52.shetland.ogc.wps.description.BoundingBoxOutputDescription;
import org.n52.shetland.ogc.wps.description.ComplexDescription;
import org.n52.shetland.ogc.wps.description.ComplexInputDescription;
import org.n52.shetland.ogc.wps.description.ComplexOutputDescription;
import org.n52.shetland.ogc.wps.description.Description;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralDescription;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;
import org.n52.shetland.ogc.wps.description.LiteralOutputDescription;
import org.n52.shetland.ogc.wps.description.ProcessDescription;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.json.JSONEncoder;
import org.n52.svalbard.encode.json.JSONEncoderKey;

import java.math.BigInteger;
import java.net.URI;
import java.util.Optional;
import java.util.stream.Stream;

public class ProcessDescriptionEncoder extends JSONEncoder<Object> {

    public ProcessDescriptionEncoder() {
        super(Stream.of(ProcessDescription.class,
                        BoundingBoxInputDescription.class,
                        BoundingBoxOutputDescription.class,
                        ComplexInputDescription.class,
                        ComplexOutputDescription.class,
                        LiteralInputDescription.class,
                        LiteralOutputDescription.class)
                    .map(JSONEncoderKey::new).toArray(EncoderKey[]::new));
    }

    @Override
    public JsonNode encodeJSON(Object object) throws EncodingException {
        if (object == null) {
            return nodeFactory().nullNode();
        } else if (object instanceof ProcessDescription) {
            return createProcessDescription((ProcessDescription) object);
        } else if (object instanceof LiteralInputDescription) {
            return createLiteralInput((LiteralInputDescription) object);
        } else if (object instanceof ComplexInputDescription) {
            return createComplexInput((ComplexInputDescription) object);
        } else if (object instanceof BoundingBoxInputDescription) {
            return createBoundingBoxInput((BoundingBoxInputDescription) object);
        } else if (object instanceof LiteralOutputDescription) {
            return createLiteralOutput((LiteralOutputDescription) object);
        } else if (object instanceof ComplexOutputDescription) {
            return createComplexOutput((ComplexOutputDescription) object);
        } else if (object instanceof BoundingBoxOutputDescription) {
            return createBoundingBoxOutput((BoundingBoxOutputDescription) object);
        } else {
            throw new EncodingException("not supported: " + object.getClass());
        }
    }

    private JsonNode createProcessDescription(ProcessDescription processDescription) throws EncodingException {
        ObjectNode root = createDescription(processDescription);
        root.put(JSONConstants.VERSION, processDescription.getVersion());
        root.set(JSONConstants.INPUTS, encodeObjectsToJson(processDescription.getInputDescriptions()));
        root.set(JSONConstants.OUTPUTS, encodeObjectsToJson(processDescription.getOutputDescriptions()));
        return root;
    }

    private ObjectNode createDescription(Description description) {
        ObjectNode root = nodeFactory().objectNode();
        root.put(JSONConstants.ID, description.getId().getValue());
        root.put(JSONConstants.TITLE, description.getTitle().getValue());
        description.getAbstract().map(OwsLanguageString::getValue)
                   .ifPresent(v -> root.put(JSONConstants.DESCRIPTION, v));
        root.set(JSONConstants.KEYWORDS, description.getKeywords().stream()
                                                    .map(OwsKeyword::getKeyword)
                                                    .map(OwsLanguageString::getValue)
                                                    .map(this::encodeAsString)
                                                    .collect(toJsonArray()));
        root.set(JSONConstants.METADATA, description.getMetadata().stream()
                                                    .map(this::createMetadata)
                                                    .collect(toJsonArray()));
        return root;
    }

    private ObjectNode createMetadata(OwsMetadata x) {
        ObjectNode root = nodeFactory().objectNode();
        x.getRole().map(URI::toString).ifPresent(v -> root.put(JSONConstants.ROLE, v));
        x.getHref().map(URI::toString).ifPresent(v -> root.put(JSONConstants.HREF, v));
        return root;
    }

    private ObjectNode createBoundingBoxOutput(BoundingBoxOutputDescription description) {
        ObjectNode root = createDescription(description);
        root.putObject(JSONConstants.OUTPUT)
            .set(JSONConstants.SUPPORTED_CRS, createSupportedCRS(description));
        return root;
    }

    private ObjectNode createLiteralOutput(LiteralOutputDescription description) {
        ObjectNode root = createDescription(description);
        root.putObject(JSONConstants.OUTPUT)
            .set(JSONConstants.LITERAL_DATA_DOMAINS, createLiteralDataDomains(description));
        return root;
    }

    private ObjectNode createComplexOutput(ComplexOutputDescription description) {
        ObjectNode root = createDescription(description);
        root.putObject(JSONConstants.OUTPUT)
            .set(JSONConstants.FORMATS, createFormats(description));
        return root;
    }

    private ArrayNode createLiteralDataDomains(LiteralDescription description) {
        ArrayNode root = nodeFactory().arrayNode();
        root.add(createLiteralDataDomain(description.getDefaultLiteralDataDomain()).put(JSONConstants.DEFAULT, true));

        description.getSupportedLiteralDataDomains().stream()
                   .filter(v -> !description.getDefaultLiteralDataDomain().equals(v))
                   .map(this::createLiteralDataDomain)
                   .map(v -> v.put(JSONConstants.DEFAULT, false))
                   .forEach(root::add);

        return root;
    }

    private ObjectNode createInputDescription(ProcessInputDescription processInputDescription) {
        ObjectNode root = createDescription(processInputDescription);
        root.put(JSONConstants.MIN_OCCURS, processInputDescription.getOccurence().getMin());
        Optional<BigInteger> max = processInputDescription.getOccurence().getMax();
        if (max.isPresent()) {
            root.put(JSONConstants.MAX_OCCURS, max.get());
        } else {
            root.put(JSONConstants.MAX_OCCURS, "unbounded");
        }
        return root;
    }

    private ObjectNode createBoundingBoxInput(BoundingBoxInputDescription description) {
        ObjectNode root = createInputDescription(description);
        root.putObject(JSONConstants.INPUT).set(JSONConstants.SUPPORTED_CRS, createSupportedCRS(description));
        return root;
    }

    private ObjectNode createLiteralInput(LiteralInputDescription description) {
        ObjectNode root = createInputDescription(description);
        root.putObject(JSONConstants.INPUT).set(JSONConstants.LITERAL_DATA_DOMAINS,
                                                createLiteralDataDomains(description));
        return root;
    }

    private ObjectNode createComplexInput(ComplexInputDescription description) {
        ObjectNode root = createInputDescription(description);
        root.putObject(JSONConstants.INPUT).set(JSONConstants.FORMATS, createFormats(description));
        return root;
    }

    private ArrayNode createSupportedCRS(BoundingBoxDescription description) {
        ArrayNode root = nodeFactory().arrayNode();

        ObjectNode def = root.addObject();
        def.put(JSONConstants.CRS, description.getDefaultCRS().getValue().toString());
        def.put(JSONConstants.DEFAULT, true);

        description.getSupportedCRS().stream()
                   .filter(v -> !description.getDefaultCRS().equals(v))
                   .map(OwsCRS::getValue).map(URI::toString)
                   .map(v -> nodeFactory().objectNode().put(JSONConstants.CRS, v).put(JSONConstants.DEFAULT, false))
                   .forEach(root::add);
        return root;
    }

    private ObjectNode createLiteralDataDomain(LiteralDataDomain literalDataDomain) {
        ObjectNode root = nodeFactory().objectNode();
        literalDataDomain.getDataType().map(this::createOwsDomainMetadata)
                         .ifPresent(v -> root.set(JSONConstants.DATA_TYPE, v));
        literalDataDomain.getUOM().map(this::createOwsDomainMetadata)
                         .ifPresent(v -> root.set(JSONConstants.UOM, v));
        root.set(JSONConstants.VALUE_DEFINITION, createPossibleValues(literalDataDomain.getPossibleValues()));

        literalDataDomain.getDefaultValue().map(OwsValue::getValue)
                         .ifPresent(v -> root.put(JSONConstants.DEFAULT_VALUE, v));
        return root;
    }

    private ObjectNode createOwsDomainMetadata(OwsDomainMetadata domainMetadata) {
        ObjectNode root = nodeFactory().objectNode();
        domainMetadata.getValue().ifPresent(v -> root.put(JSONConstants.NAME, v));
        domainMetadata.getReference().map(URI::toString).ifPresent(v -> root.put(JSONConstants.REFERENCE, v));
        return root;
    }

    private JsonNode createPossibleValues(OwsPossibleValues possibleValues) {
        if (possibleValues.isAnyValue()) {
            return createAnyValue(possibleValues.asAnyValues());
        } else if (possibleValues.isAllowedValues()) {
            return createAllowedValues(possibleValues.asAllowedValues());
        } else if (possibleValues.isValuesReference()) {
            return createValuesReference(possibleValues.asValuesReference());
        } else {
            throw new IllegalArgumentException("unsupported possible values: " + possibleValues);
        }
    }

    private JsonNode createAnyValue(OwsAnyValue owsAnyValue) {
        return nodeFactory().objectNode().put(JSONConstants.ANY_VALUE, true);
    }

    private ObjectNode createValuesReference(OwsValuesReference valuesReference) {
        ObjectNode root = nodeFactory().objectNode();
        root.put(JSONConstants.VALUE_REFERENCE, valuesReference.getReference().toString());
        if (!valuesReference.getValue().isEmpty()) {
            root.put(JSONConstants.VALUE, valuesReference.getValue());
        }
        return root;
    }

    private ArrayNode createAllowedValues(OwsAllowedValues allowedValues) {
        return allowedValues.stream().map(this::createAllowedValue).collect(toJsonArray());
    }

    private JsonNode createAllowedValue(OwsValueRestriction allowedValue) {
        if (allowedValue.isValue()) {
            return createValue(allowedValue.asValue());
        } else if (allowedValue.isRange()) {
            return createRange(allowedValue.asRange());
        } else {
            throw new IllegalArgumentException("unsupported allowed value " + allowedValue);
        }
    }

    private JsonNode createValue(OwsValue owsValue) {
        return encodeAsString(owsValue.getValue());
    }

    private JsonNode createRange(OwsRange owsRange) {
        ObjectNode root = nodeFactory().objectNode();
        owsRange.getLowerBound().map(OwsValue::getValue).ifPresent(v -> root.put(JSONConstants.MINIMUM_VALUE, v));
        owsRange.getUpperBound().map(OwsValue::getValue).ifPresent(v -> root.put(JSONConstants.MAXIMUM_VALUE, v));
        owsRange.getSpacing().map(OwsValue::getValue).ifPresent(v -> root.put(JSONConstants.SPACING, v));
        root.put(JSONConstants.RANGE_CLOSURE, owsRange.getType());
        return root;
    }

    private ArrayNode createFormats(ComplexDescription description) {
        ArrayNode formats = nodeFactory().arrayNode();
        formats.add(createFormat(description.getDefaultFormat(), description.getMaximumMegabytes())
                            .put(JSONConstants.DEFAULT, true));
        description.getSupportedFormats().stream()
                   .filter(v -> !description.getDefaultFormat().equals(v))
                   .map(format -> createFormat(format, description.getMaximumMegabytes()))
                   .map(n -> n.put(JSONConstants.DEFAULT, false))
                   .forEach(formats::add);
        return formats;
    }

    private ObjectNode createFormat(Format format, Optional<BigInteger> maximumMegabytes) {
        ObjectNode formatDescription = nodeFactory().objectNode();
        format.getMimeType().ifPresent(v -> formatDescription.put(JSONConstants.MIME_TYPE, v));
        format.getSchema().ifPresent(v -> formatDescription.put(JSONConstants.SCHEMA, v));
        format.getEncoding().ifPresent(v -> formatDescription.put(JSONConstants.ENCODING, v));
        maximumMegabytes.ifPresent(v -> formatDescription.put(JSONConstants.MAXIMUM_MEGABYTES, v));
        return formatDescription;
    }
}
