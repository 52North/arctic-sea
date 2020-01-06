/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.decode.json.wps;

import com.fasterxml.jackson.databind.JsonNode;
import org.n52.shetland.ogc.ows.OwsAllowedValues;
import org.n52.shetland.ogc.ows.OwsAnyValue;
import org.n52.shetland.ogc.ows.OwsCRS;
import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsRange;
import org.n52.shetland.ogc.ows.OwsValue;
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
import org.n52.shetland.ogc.wps.description.ProcessDescriptionBuilderFactory;
import org.n52.shetland.ogc.wps.description.ProcessInputDescription;
import org.n52.shetland.ogc.wps.description.ProcessOutputDescription;
import org.n52.shetland.ogc.wps.description.impl.ProcessDescriptionFactory;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.JSONDecoder;

import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ProcessDescriptionDecoder extends JSONDecoder<ProcessDescription> {
    private final ProcessDescriptionBuilderFactory<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> factory;

    public ProcessDescriptionDecoder() {
        this(null);
    }

    public ProcessDescriptionDecoder(ProcessDescriptionBuilderFactory factory) {
        super(ProcessDescription.class);
        this.factory = Optional.ofNullable(factory).orElseGet(ProcessDescriptionFactory::instance);
    }

    @Override
    public ProcessDescription decodeJSON(JsonNode node, boolean validate) throws DecodingException {
        if (node == null || node.isNull()) {
            return null;
        }
        return decodeProcess(node);
    }

    private ProcessDescription decodeProcess(JsonNode node) throws DecodingException {
        if (!node.isObject()) {
            throw new DecodingException("expected object");
        }
        ProcessDescription.Builder<?, ?> builder = factory.process();
        decodeDescription(builder, node);
        for (JsonNode input : node.path(JSONConstants.INPUTS)) {
            builder.withInput(decodeInput(input));
        }
        for (JsonNode output : node.path(JSONConstants.OUTPUTS)) {
            builder.withOutput(decodeOutput(output));
        }
        if (node.path(JSONConstants.VERSION).isValueNode()) {
            builder.withVersion(node.path(JSONConstants.VERSION).textValue());
        }
        return builder.build();
    }

    private ProcessInputDescription decodeInput(JsonNode node) throws DecodingException {
        if (node.path(JSONConstants.INPUT).has(JSONConstants.LITERAL_DATA_DOMAINS)) {
            return decodeLiteralInput(node);
        } else if (node.path(JSONConstants.INPUT).has(JSONConstants.FORMATS)) {
            return decodeComplexInput(node);
        } else if (node.path(JSONConstants.INPUT).has(JSONConstants.SUPPORTED_CRS)) {
            return decodeBoundingBoxInput(node);
        }
        throw new DecodingException("unsupported input" + node);
    }

    private LiteralInputDescription decodeLiteralInput(JsonNode node) throws DecodingException {
        LiteralInputDescription.Builder<?, ?> builder = factory.literalInput();
        decodeInputDescription(builder, node);
        decodeSupportedLiteralDataDomains(builder, node.path(JSONConstants.INPUT));
        return builder.build();
    }

    private void decodeSupportedLiteralDataDomains(LiteralDescription.Builder<?, ?> builder, JsonNode node)
            throws DecodingException {
        JsonNode domains = node.path(JSONConstants.LITERAL_DATA_DOMAINS);
        LiteralDataDomain defaultLiteralDataDomain = null;
        List<LiteralDataDomain> literalDataDomains = new ArrayList<>(domains.size());
        for (JsonNode domainNode : domains) {
            LiteralDataDomain literalDataDomain = decodeLiteralDataDomain(domainNode);
            literalDataDomains.add(literalDataDomain);
            if (domainNode.path(JSONConstants.DEFAULT).asBoolean(false)) {
                defaultLiteralDataDomain = literalDataDomain;
            }
        }
        if (defaultLiteralDataDomain == null && !literalDataDomains.isEmpty()) {
            defaultLiteralDataDomain = literalDataDomains.iterator().next();
        }
        if (defaultLiteralDataDomain == null) {
            throw new DecodingException("missing literal data domain");
        }
        builder.withDefaultLiteralDataDomain(defaultLiteralDataDomain);
        builder.withSupportedLiteralDataDomain(literalDataDomains);

    }

    private LiteralDataDomain decodeLiteralDataDomain(JsonNode node) {
        LiteralDataDomain.Builder<?, ?> builder = factory.literalDataDomain();
        if (!node.path(JSONConstants.DEFAULT_VALUE).isMissingNode()) {
            builder.withDefaultValue(node.path(JSONConstants.DEFAULT_VALUE).textValue());
        }
        JsonNode dataTypeNode = node.path(JSONConstants.DATA_TYPE);
        if (!dataTypeNode.isMissingNode()) {
            builder.withDataType(decodeDomainMetadata(dataTypeNode));
        }
        JsonNode uomNode = node.path(JSONConstants.UOM);
        if (!uomNode.isMissingNode()) {
            builder.withUOM(decodeDomainMetadata(uomNode));
        }
        builder.withValueDescription(decodeValueDefinition(node.path(JSONConstants.VALUE_DEFINITION)));
        return builder.build();
    }

    private OwsPossibleValues decodeValueDefinition(JsonNode node) {
        if (node.isObject()) {
            return OwsAnyValue.instance();
        } else if (node.isArray()) {
            return new OwsAllowedValues(StreamSupport.stream(node.spliterator(), false)
                                                     .map(n -> n.isObject() ? decodeRange(n) : decodeValue(n))
                                                     .filter(Objects::nonNull));
        } else {
            return new OwsValuesReference(URI.create(node.asText()));
        }
    }

    private OwsValue decodeValue(JsonNode node) {
        if (!node.isValueNode()) {
            return null;
        }
        String text = node.asText();
        return new OwsValue(text);
    }

    private OwsRange decodeRange(JsonNode node) {
        OwsValue min = decodeValue(node.path(JSONConstants.MINIMUM_VALUE));
        OwsValue max = decodeValue(node.path(JSONConstants.MAXIMUM_VALUE));
        OwsValue spacing = decodeValue(node.path(JSONConstants.SPACING));
        String rangeClosure = node.path(JSONConstants.RANGE_CLOSURE).textValue();
        return new OwsRange(min, max, rangeClosure, spacing);
    }

    private OwsDomainMetadata decodeDomainMetadata(JsonNode dataTypeNode) {
        String name = dataTypeNode.path(JSONConstants.NAME).textValue();
        URI reference = Optional.ofNullable(dataTypeNode.path(JSONConstants.REFERENCE).textValue())
                                .map(URI::create).orElse(null);
        return new OwsDomainMetadata(reference, name);
    }

    private ComplexInputDescription decodeComplexInput(JsonNode node) throws DecodingException {
        ComplexInputDescription.Builder<?, ?> builder = factory.complexInput();
        decodeInputDescription(builder, node);
        BigInteger maximumMegabytes = decodeSupportedFormats(builder, node.path(JSONConstants.INPUT));
        return builder.withMaximumMegabytes(maximumMegabytes).build();
    }

    private BigInteger getMaximumMegabytes(BigInteger currentMaximum, JsonNode formatNode) {
        if (formatNode.path(JSONConstants.MAXIMUM_MEGABYTES).isNumber()) {
            BigInteger value = formatNode.path(JSONConstants.MAXIMUM_MEGABYTES).bigIntegerValue();
            if (currentMaximum == null || currentMaximum.compareTo(value) > 0) {
                return value;
            }
        }
        return currentMaximum;
    }

    private Format decodeFormat(JsonNode node) {
        return new Format(node.path(JSONConstants.MIME_TYPE).textValue(),
                          node.path(JSONConstants.ENCODING).textValue(),
                          node.path(JSONConstants.SCHEMA).textValue());
    }

    private BoundingBoxInputDescription decodeBoundingBoxInput(JsonNode node) throws DecodingException {
        BoundingBoxInputDescription.Builder<?, ?> builder = factory.boundingBoxInput();
        decodeInputDescription(builder, node);
        decodeSupportedCRS(builder, node.path(JSONConstants.INPUT));
        return builder.build();
    }

    private ProcessOutputDescription decodeOutput(JsonNode node) throws DecodingException {
        if (node.path(JSONConstants.OUTPUT).has(JSONConstants.LITERAL_DATA_DOMAINS)) {
            return decodeLiteralOutput(node);
        } else if (node.path(JSONConstants.OUTPUT).has(JSONConstants.FORMATS)) {
            return decodeComplexOutput(node);
        } else if (node.path(JSONConstants.OUTPUT).has(JSONConstants.SUPPORTED_CRS)) {
            return decodeBoundingBoxOutput(node);
        }
        throw new DecodingException("unsupported output" + node);
    }

    private BoundingBoxOutputDescription decodeBoundingBoxOutput(JsonNode node) throws DecodingException {
        BoundingBoxOutputDescription.Builder<?, ?> builder = factory.boundingBoxOutput();
        decodeDescription(builder, node);
        decodeSupportedCRS(builder, node.path(JSONConstants.OUTPUT));
        return builder.build();
    }

    private ComplexOutputDescription decodeComplexOutput(JsonNode node) throws DecodingException {
        ComplexOutputDescription.Builder<?, ?> builder = factory.complexOutput();
        decodeDescription(builder, node);
        BigInteger maximumMegabytes = decodeSupportedFormats(builder, node.path(JSONConstants.OUTPUT));
        return builder.withMaximumMegabytes(maximumMegabytes).build();
    }

    private BigInteger decodeSupportedFormats(ComplexDescription.Builder<?, ?> builder, JsonNode node)
            throws DecodingException {
        JsonNode formatsNode = node.path(JSONConstants.FORMATS);
        BigInteger maximumMegabytes = null;
        Format defaultFormat = null;
        List<Format> formats = new ArrayList<>(formatsNode.size());
        for (JsonNode formatNode : formatsNode) {
            Format format = decodeFormat(formatNode);
            formats.add(format);
            if (formatNode.path(JSONConstants.DEFAULT).asBoolean(false)) {
                defaultFormat = format;
            }
            maximumMegabytes = getMaximumMegabytes(maximumMegabytes, formatNode);
        }

        if (defaultFormat == null && !formats.isEmpty()) {
            defaultFormat = formats.iterator().next();
        }
        if (defaultFormat == null) {
            throw new DecodingException("missing default format");
        }
        builder.withDefaultFormat(defaultFormat);
        builder.withSupportedFormat(formats);
        return maximumMegabytes;
    }

    private LiteralOutputDescription decodeLiteralOutput(JsonNode node) throws DecodingException {
        LiteralOutputDescription.Builder<?, ?> builder = factory.literalOutput();
        decodeDescription(builder, node);
        decodeSupportedLiteralDataDomains(builder, node.path(JSONConstants.OUTPUT));
        return builder.build();
    }

    private void decodeInputDescription(ProcessInputDescription.Builder<?, ?> builder, JsonNode node) {
        decodeDescription(builder, node);
        if (node.path(JSONConstants.MIN_OCCURS).isNumber()) {
            builder.withMinimalOccurence(node.path(JSONConstants.MIN_OCCURS).bigIntegerValue());
        }
        if (node.path(JSONConstants.MAX_OCCURS).isNumber()) {
            builder.withMaximalOccurence(node.path(JSONConstants.MAX_OCCURS).bigIntegerValue());
        }
    }

    private void decodeSupportedCRS(BoundingBoxDescription.Builder<?, ?> builder, JsonNode node)
            throws DecodingException {
        JsonNode crsNodes = node.path(JSONConstants.SUPPORTED_CRS);
        OwsCRS defaultCRS = null;
        List<OwsCRS> supportedCRS = new ArrayList<>(crsNodes.size());
        for (JsonNode crsNode : crsNodes) {
            OwsCRS crs = new OwsCRS(URI.create(crsNode.path(JSONConstants.CRS).textValue()));
            supportedCRS.add(crs);
            if (crsNode.path(JSONConstants.DEFAULT).asBoolean(false)) {
                defaultCRS = crs;
            }
        }
        if (defaultCRS == null && !supportedCRS.isEmpty()) {
            defaultCRS = supportedCRS.iterator().next();
        }
        if (defaultCRS == null) {
            throw new DecodingException("missing default crs");
        }
        builder.withDefaultCRS(defaultCRS);
        builder.withSupportedCRS(supportedCRS);
    }

    private void decodeDescription(Description.Builder<?, ?> builder, JsonNode node) {
        builder.withIdentifier(node.path(JSONConstants.ID).textValue());
        builder.withTitle(node.path(JSONConstants.TITLE).textValue());
        builder.withAbstract(node.path(JSONConstants.DESCRIPTION).textValue());

        for (JsonNode metadata : node.path(JSONConstants.METADATA)) {
            URI role = Optional.ofNullable(metadata.path(JSONConstants.ROLE).textValue()).map(URI::create).orElse(null);
            URI href = Optional.ofNullable(metadata.path(JSONConstants.HREF).textValue()).map(URI::create).orElse(null);
            builder.withMetadata(new OwsMetadata(href, role, null, null, null, null, null));
        }

        for (JsonNode keyword : node.path(JSONConstants.KEYWORDS)) {
            builder.withKeyword(keyword.textValue());
        }
    }
}
