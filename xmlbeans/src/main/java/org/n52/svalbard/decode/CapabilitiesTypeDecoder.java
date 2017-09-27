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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import net.opengis.fes.x20.FilterCapabilitiesDocument;
import net.opengis.gml.x32.EnvelopeType;
import net.opengis.sos.x20.CapabilitiesType;
import net.opengis.sos.x20.CapabilitiesType.Contents;
import net.opengis.sos.x20.ContentsType;
import net.opengis.sos.x20.ObservationOfferingPropertyType;
import net.opengis.sos.x20.ObservationOfferingType;
import net.opengis.swes.x20.AbstractContentsType;
import net.opengis.swes.x20.AbstractOfferingType.RelatedFeature;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import org.n52.shetland.ogc.filter.FilterCapabilities;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.ows.OwsCapabilities;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosCapabilities;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.SosObservationOffering;
import org.n52.shetland.ogc.sos.SosOffering;
import org.n52.shetland.util.ReferencedEnvelope;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

import com.google.common.base.Joiner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author <a href="mailto:j.schulte@52north.org">Jan Schulte</a>
 */
public class CapabilitiesTypeDecoder extends
        AbstractCapabilitiesBaseTypeDecoder<CapabilitiesType, SosCapabilities> {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(CapabilitiesTypeDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS
            = CodingHelper.decoderKeysForElements(Sos2Constants.NS_SOS_20, CapabilitiesType.class);

    public CapabilitiesTypeDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public SosCapabilities decode(CapabilitiesType ct) throws DecodingException {
        if (ct != null) {
            OwsCapabilities owsCapabilities = parseCapabilitiesBaseType(SosConstants.SOS, ct);
            FilterCapabilities filterCapabilities = parseFilterCapabilities(ct.getFilterCapabilities());
            Collection<SosObservationOffering> contents = parseContents(ct.getContents());
            return new SosCapabilities(owsCapabilities, filterCapabilities, contents);
        }
        throw new UnsupportedDecoderInputException(this, ct);
    }

    private Collection<SosObservationOffering> parseContents(Contents contents) {
        if (contents == null) {
            return null;
        }
        return parseContents(contents.getContents());
    }

    private Collection<SosObservationOffering> parseContents(ContentsType contents) {
        return Optional.ofNullable(contents.getOfferingArray())
                .map(Arrays::stream).orElseGet(Stream::empty)
                .map(this::parseOffering)
                .filter(Objects::nonNull)
                .collect(toSet());
    }

    private SosObservationOffering parseOffering(AbstractContentsType.Offering offering) {
        SosObservationOffering observationOffering = new SosObservationOffering();
        if (offering.getDomNode().hasChildNodes()) {
            final Node node = XmlHelper.getNodeFromNodeList(offering.getDomNode().getChildNodes());
            try {
                ObservationOfferingPropertyType offeringType = ObservationOfferingPropertyType.Factory.parse(node);
                ObservationOfferingType obsOffPropType = offeringType.getObservationOffering();
                observationOffering.setOffering(parseOffering(obsOffPropType));
                observationOffering.setProcedures(parseProcedure(obsOffPropType));
                observationOffering.setProcedureDescriptionFormat(parseProcedureDescriptionFormat(obsOffPropType));
                observationOffering.setObservableProperties(parseObservableProperties(obsOffPropType));
                observationOffering.setRelatedFeatures(parseRelatedFeatures(obsOffPropType));
                observationOffering.setObservedArea(parseObservedArea(obsOffPropType));
                observationOffering.setPhenomenonTime(parsePhenomenonTime(obsOffPropType));
                observationOffering.setResultTime(parseResultTime(obsOffPropType));
                observationOffering.setResponseFormats(parseResponseFormats(obsOffPropType));
                observationOffering.setObservationTypes(parseObservationTypes(obsOffPropType));
                observationOffering.setFeatureOfInterestTypes(parseFeatureOfInterestTypes(obsOffPropType));
                observationOffering.setExtensions(parseOfferingExtension(obsOffPropType));
            } catch (XmlException | DecodingException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        }
        return observationOffering;
    }

    private SosOffering parseOffering(ObservationOfferingType obsOffPropType) throws DecodingException {
        String offeringId;
        if (obsOffPropType.getIdentifier() != null) {
            offeringId = obsOffPropType.getIdentifier();
        } else {
            offeringId = obsOffPropType.getId();
        }
        if (obsOffPropType.getNameArray() != null && obsOffPropType.getNameArray().length > 0) {
            CodeType codeType = decodeXmlElement(obsOffPropType.getNameArray(0));
            return new SosOffering(offeringId, codeType);
        }
        return new SosOffering(offeringId, "");
    }

    private FilterCapabilities parseFilterCapabilities(CapabilitiesType.FilterCapabilities filterCapabilities) {
        if (filterCapabilities == null) {
            return null;
        }
        return parseFilterCapabilities(filterCapabilities.getFilterCapabilities());
    }

    private FilterCapabilities parseFilterCapabilities(
            FilterCapabilitiesDocument.FilterCapabilities filterCapabilities) {
        // TOOD parse filter capabilities
        return null;
    }

    private Collection<String> parseProcedure(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getProcedure()).map(Collections::singleton).get();
    }

    private Collection<String> parseProcedureDescriptionFormat(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getProcedureDescriptionFormatArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toSet());
    }

    private Collection<String> parseObservableProperties(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getObservablePropertyArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toSet());
    }

    private Time parsePhenomenonTime(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getPhenomenonTime())
                .map((phenTime) -> {
                    try {
                        return (Time) decodeXmlElement(phenTime.getTimePeriod());
                    } catch (DecodingException ex) {
                        LOGGER.error(ex.getLocalizedMessage(), ex);
                    }
                    return null;
                })
                .orElse(null);
    }

    private Extensions parseOfferingExtension(ObservationOfferingType obsOff) throws DecodingException {
        Extensions extensions = new Extensions();
        for (XmlObject xmlObject : obsOff.getExtensionArray()) {
            try {
                Extension<?> extension = (Extension) decodeXmlElement(xmlObject);
                extensions.addExtension(extension);
            } catch (DecodingException ex) {
                LOGGER.warn(ex.getLocalizedMessage());
            }
        }
        return extensions;
    }

    private ReferencedEnvelope parseObservedArea(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getObservedArea()).map((obsArea) -> {
            try {
                EnvelopeType envelope = obsArea.getEnvelope();
                Object decodeXmlElement = decodeXmlElement(envelope);
                return (ReferencedEnvelope) decodeXmlElement;
            } catch (DecodingException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
            return null;
        }).orElse(null);
    }

    private Map<String, Set<String>> parseRelatedFeatures(ObservationOfferingType obsOff) {
        HashMap<String, Set<String>> map = new HashMap<>(obsOff.getRelatedFeatureArray().length);
        for (RelatedFeature releatedFeature : obsOff.getRelatedFeatureArray()) {
            String feature = releatedFeature.getFeatureRelationship().getTarget().getHref();
            String role = releatedFeature.getFeatureRelationship().getRole();
            Set<String> roles = map.computeIfAbsent(feature, (key) -> new HashSet<>(1));
            if (role != null) {
                roles.add(role);
            }
        }
        return map;
    }

    private Time parseResultTime(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getResultTime())
                .map((resultTime) -> {
                    try {
                        return (Time) decodeXmlElement(resultTime.getTimePeriod());
                    } catch (DecodingException ex) {
                        LOGGER.error(ex.getLocalizedMessage(), ex);
                    }
                    return null;
                })
                .orElse(null);
    }

    private Collection<String> parseResponseFormats(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getResponseFormatArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toSet());
    }

    private Collection<String> parseObservationTypes(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getObservationTypeArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toSet());
    }

    private Collection<String> parseFeatureOfInterestTypes(ObservationOfferingType obsOff) {
        return Optional.ofNullable(obsOff.getFeatureOfInterestTypeArray())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toSet());
    }


}
