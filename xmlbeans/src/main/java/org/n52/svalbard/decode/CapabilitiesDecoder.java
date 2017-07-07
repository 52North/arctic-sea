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
package org.n52.svalbard.decode;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import net.opengis.sos.x10.CapabilitiesDocument.Capabilities;
import net.opengis.sos.x10.ContentsDocument;
import net.opengis.sos.x10.FilterCapabilitiesDocument;
import net.opengis.sos.x10.ObservationOfferingType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.filter.FilterCapabilities;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosObservationOffering;
import org.n52.shetland.util.ReferencedEnvelope;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 */
public class CapabilitiesDecoder extends AbstractCapabilitiesBaseTypeDecoder<Capabilities, SosCapabilities> {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(CapabilitiesDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS
            = CodingHelper.decoderKeysForElements(Sos1Constants.NS_SOS, Capabilities.class);

    public CapabilitiesDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public SosCapabilities decode(Capabilities c) throws DecodingException {
        if (c != null) {
            OwsCapabilities owsCapabilities = parseCapabilitiesBaseType(SosConstants.SOS, c);
            FilterCapabilities filterCapabilities = parseFilterCapabilities(c.getFilterCapabilities());
            Collection<SosObservationOffering> contents = parseContents(c.getContents());
            return new SosCapabilities(owsCapabilities, filterCapabilities, contents);
        }
        throw new UnsupportedDecoderInputException(this, c);
    }

    private Collection<SosObservationOffering> parseContents(ContentsDocument.Contents contents) {
        if (contents == null) {
            return null;
        }
        return parseObservationOfferingList(contents.getObservationOfferingList());
    }

    private Collection<SosObservationOffering> parseObservationOfferingList(
            ContentsDocument.Contents.ObservationOfferingList obsOffList) {
        return Optional.ofNullable(obsOffList.getObservationOfferingArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map(this::parseOffering)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    private SosObservationOffering parseOffering(ObservationOfferingType obsOffType) {
        SosObservationOffering observationOffering = new SosObservationOffering();
        observationOffering.setOffering(obsOffType.getId());
        observationOffering.setProcedures(parseProcedure(obsOffType));
        observationOffering.setProcedureDescriptionFormat(parseProcedureDescriptionFormat(obsOffType));
        observationOffering.setObservableProperties(parseObservableProperties(obsOffType));
        observationOffering.setRelatedFeatures(parseRelatedFeatures(obsOffType));
        observationOffering.setObservedArea(parseObservedArea(obsOffType));
        observationOffering.setPhenomenonTime(parsePhenomenonTime(obsOffType));
        observationOffering.setResultTime(parseResultTime(obsOffType));
        observationOffering.setResponseFormats(parseResponseFormats(obsOffType));
        observationOffering.setObservationTypes(parseObservationTypes(obsOffType));
        observationOffering.setFeatureOfInterestTypes(parseFeatureOfInterestTypes(obsOffType));
        return observationOffering;
    }

    private FilterCapabilities parseFilterCapabilities(
            FilterCapabilitiesDocument.FilterCapabilities filterCapabilities) {
        // TODO parse filter capabilities
        LOGGER.warn("parseFilterCapabilities needs to be implemented");
        return null;
    }

    private Collection<String> parseProcedure(ObservationOfferingType obsOffType) {
        return Optional.ofNullable(obsOffType.getProcedureArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map((procedure) -> {
                    return procedure.getHref();
                })
                .collect(toSet());
    }

    private Collection<String> parseProcedureDescriptionFormat(ObservationOfferingType obsOffType) {
        LOGGER.warn("parseProcedureDescriptionFormat needs to be implemented");
        return Collections.emptySet();
    }

    private Collection<String> parseObservableProperties(ObservationOfferingType obsOffType) {
        return Optional.ofNullable(obsOffType.getObservedPropertyArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map((obsProp) -> {
                    return obsProp.getHref();
                })
                .collect(toSet());
    }

    private Time parsePhenomenonTime(ObservationOfferingType obsOffType) {
        LOGGER.warn("parsePhenomenonTime needs to be implemented");
        return null;
    }

    private Time parseResultTime(ObservationOfferingType obsOffType) {
        LOGGER.warn("parseResultTime needs to be implemented");
        return null;
    }

    private Collection<String> parseResponseFormats(ObservationOfferingType obsOffType) {
        return Optional.ofNullable(obsOffType.getResponseFormatArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toSet());
    }

    private Collection<String> parseObservationTypes(ObservationOfferingType obsOffType) {
        LOGGER.warn("parseObservationTypes needs to be implemented");
        return Collections.emptySet();
    }

    private Collection<String> parseFeatureOfInterestTypes(ObservationOfferingType obsOffType) {
        return Optional.ofNullable(obsOffType.getFeatureOfInterestArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .map((foi) -> {
                    return foi.getHref();
                })
                .collect(toSet());
    }

    private Map<String, Set<String>> parseRelatedFeatures(ObservationOfferingType obsOffType) {
        LOGGER.warn("parseRelatedFeatures needs to be implemented");
        return Collections.emptyMap();
    }

    private ReferencedEnvelope parseObservedArea(ObservationOfferingType obsOffType) {
        LOGGER.warn("parseObservedArea needs to be implemented");
        return null;
    }

}
