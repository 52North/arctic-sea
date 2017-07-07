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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import net.opengis.waterml.x20.ObservationProcessDocument;
import net.opengis.waterml.x20.ObservationProcessPropertyType;
import net.opengis.waterml.x20.ObservationProcessType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.om.series.wml.ObservationProcess;
import org.n52.shetland.ogc.om.series.wml.WaterMLConstants;
import org.n52.shetland.ogc.sos.ProcedureDescriptionFormat;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class WmlObservationProcessDecoderv20
        extends AbstractWmlDecoderv20
        implements ProcedureDecoder<Object, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WmlObservationProcessDecoderv20.class);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(WaterMLConstants.NS_WML_20_PROCEDURE_ENCODING,
                    ObservationProcessDocument.class, ObservationProcessPropertyType.class,
                    ObservationProcessType.class),
            CodingHelper.decoderKeysForElements(WaterMLConstants.NS_WML_20, ObservationProcessDocument.class,
                    ObservationProcessPropertyType.class, ObservationProcessType.class));

    private static final Map<String, Map<String, Set<String>>> SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS =
            ImmutableMap.of(SosConstants.SOS, ImmutableMap.of(Sos2Constants.SERVICEVERSION,
                    ImmutableSet.of(WaterMLConstants.NS_WML_20_PROCEDURE_ENCODING)));

    private static final Set<SupportedType> SUPPORTED_TYPES =
            ImmutableSet.of(new ProcedureDescriptionFormat(WaterMLConstants.NS_WML_20_PROCEDURE_ENCODING));

    public WmlObservationProcessDecoderv20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.unmodifiableSet(SUPPORTED_TYPES);
    }

    @Override
    public Set<String> getSupportedProcedureDescriptionFormats(String service, String version) {
        return SUPPORTED_TRANSACTIONAL_PROCEDURE_DESCRIPTION_FORMATS.getOrDefault(service, Collections.emptyMap())
                .getOrDefault(version, Collections.emptySet());
    }

    @Override
    public Object decode(Object object) throws DecodingException {
        if (object instanceof ObservationProcessDocument) {
            return parseObservationProcess(((ObservationProcessDocument) object).getObservationProcess());
        } else if (object instanceof ObservationProcessPropertyType) {
            return parseObservationProcess(((ObservationProcessPropertyType) object).getObservationProcess());
        } else if (object instanceof ObservationProcessType) {
            return parseObservationProcess((ObservationProcessType) object);
        }
        return super.decode(object);
    }

    private Object parseObservationProcess(ObservationProcessType opt) throws DecodingException {
        ObservationProcess observationProcess = new ObservationProcess();
        observationProcess.setGmlId(opt.getId());
        // parse identifier, names, description, locations
        parseAbstractFeatureType(opt, observationProcess);
        parseProcessType(opt, observationProcess);
        parseOriginatingProcess(opt, observationProcess);
        parseAggregatingDuration(opt, observationProcess);
        parseVerticalDatum(opt, observationProcess);
        parseComment(opt, observationProcess);
        parseProcessReference(opt, observationProcess);
        parseInput(opt, observationProcess);
        parseParameter(opt, observationProcess);
        setDescriptionXml(opt, observationProcess);
        return observationProcess;
    }

    private void setDescriptionXml(ObservationProcessType opt, ObservationProcess observationProcess) {
        ObservationProcessDocument doc = ObservationProcessDocument.Factory.newInstance(getXmlOptions());
        doc.setObservationProcess(opt);
        observationProcess.setXml(doc.xmlText(getXmlOptions()));
    }

    private void parseProcessType(ObservationProcessType opt, ObservationProcess observationProcess) {
        observationProcess.setProcessType(parseReferenceType(opt.getProcessType()));
    }

    private void parseOriginatingProcess(ObservationProcessType opt, ObservationProcess observationProcess)
            throws DecodingException {
        if (opt.isSetOriginatingProcess()) {
            observationProcess.setOriginatingProcess(parseReferenceType(opt.getOriginatingProcess()));
        }
    }

    private void parseAggregatingDuration(ObservationProcessType opt, ObservationProcess observationProcess) {
        if (opt.isSetAggregationDuration()) {
            observationProcess.setAggregationDuration(opt.getAggregationDuration().toString());
        }
    }

    private void parseVerticalDatum(ObservationProcessType opt, ObservationProcess observationProcess)
            throws DecodingException {
        if (opt.isSetVerticalDatum()) {
            Object decodeXmlElement = decodeXmlElement(opt.getVerticalDatum());
            if (decodeXmlElement instanceof ReferenceType) {
                observationProcess.setVerticalDatum((ReferenceType) decodeXmlElement);
            }
        }
    }

    private void parseComment(ObservationProcessType opt, ObservationProcess observationProcess) {
        if (CollectionHelper.isNotNullOrEmpty(opt.getCommentArray())) {
            observationProcess.setComments(Lists.newArrayList(opt.getCommentArray()));
        }
    }

    private void parseProcessReference(ObservationProcessType opt, ObservationProcess observationProcess) {
        if (opt.isSetProcessReference()) {
            observationProcess.setProcessReference(parseReferenceType(opt.getProcessReference()));
        }
    }

    private void parseInput(ObservationProcessType opt, ObservationProcess observationProcess) {
        if (CollectionHelper.isNotNullOrEmpty(opt.getInputArray())) {
            parseReferenceType(opt.getInputArray());
        }
    }

    private void parseParameter(ObservationProcessType opt, ObservationProcess observationProcess)
            throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(opt.getParameterArray())) {
            observationProcess.setParameters(parseNamedValueTypeArray(opt.getParameterArray()));
        }
    }

}
