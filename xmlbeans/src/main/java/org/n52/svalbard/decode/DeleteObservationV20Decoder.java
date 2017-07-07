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
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.filter.TemporalFilter;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationConstants;
import org.n52.shetland.ogc.sos.delobs.DeleteObservationRequest;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import net.opengis.sosdo.x20.DeleteObservationDocument;
import net.opengis.sosdo.x20.DeleteObservationType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class DeleteObservationV20Decoder
        extends AbstractXmlDecoder<XmlObject, DeleteObservationRequest> {

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(DeleteObservationConstants.NS_SOSDO_2_0,
                    DeleteObservationDocument.class),
            CodingHelper.xmlDecoderKeysForOperation(Sos2Constants.SOS, Sos2Constants.SERVICEVERSION,
                    DeleteObservationConstants.Operations.DeleteObservation));

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteObservationV20Decoder.class);

    public DeleteObservationV20Decoder() {
        LOGGER.info("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    public DeleteObservationRequest decode(XmlObject xmlObject) throws DecodingException {
        LOGGER.debug(String.format("REQUESTTYPE: %s", xmlObject != null ? xmlObject.getClass() : "null recevied"));
        // XmlHelper.validateDocument(xmlObject);
        if (xmlObject instanceof DeleteObservationDocument) {
            DeleteObservationDocument delObsDoc = (DeleteObservationDocument) xmlObject;
            DeleteObservationRequest decodedRequest = parseDeleteObservation(delObsDoc);
            LOGGER.debug(String.format("Decoded request: %s", decodedRequest));
            return decodedRequest;
        } else {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
    }

    private DeleteObservationRequest parseDeleteObservation(DeleteObservationDocument xbDelObsDoc)
            throws DecodingException {
        DeleteObservationRequest delObsRequest = null;

        DeleteObservationType xbDelObsType = xbDelObsDoc.getDeleteObservation();

        if (xbDelObsType != null) {
            delObsRequest = new DeleteObservationRequest(DeleteObservationConstants.NS_SOSDO_2_0);
            delObsRequest.setVersion(xbDelObsType.getVersion());
            delObsRequest.setService(xbDelObsType.getService());
            if (CollectionHelper.isNotNullOrEmpty(xbDelObsType.getObservationArray())) {
                delObsRequest.setObservationIdentifiers(Sets.newHashSet(xbDelObsType.getObservationArray()));
            } else {
                parseProcedure(xbDelObsType, delObsRequest);
                parseObservedProperty(xbDelObsType, delObsRequest);
                parseFeatureOfInterest(xbDelObsType, delObsRequest);
                parseOffering(xbDelObsType, delObsRequest);
                parseTemporalFilter(xbDelObsType, delObsRequest);
            }
        } else {
            throw new DecodingException(
                    "Received XML document is not valid. Set log level to debug to get more details");
        }

        return delObsRequest;
    }

    private void parseProcedure(DeleteObservationType dot, DeleteObservationRequest request) {
        if (CollectionHelper.isNotNullOrEmpty(dot.getProcedureArray())) {
            request.setProcedures(Sets.newHashSet(dot.getProcedureArray()));
        }
    }

    private void parseObservedProperty(DeleteObservationType dot, DeleteObservationRequest request) {
        if (CollectionHelper.isNotNullOrEmpty(dot.getObservedPropertyArray())) {
            request.setObservedProperties(Sets.newHashSet(dot.getObservedPropertyArray()));
        }
    }

    private void parseFeatureOfInterest(DeleteObservationType dot, DeleteObservationRequest request) {
        if (CollectionHelper.isNotNullOrEmpty(dot.getFeatureOfInterestArray())) {
            request.setFeatureIdentifiers(Sets.newHashSet(dot.getFeatureOfInterestArray()));
        }
    }

    private void parseOffering(DeleteObservationType dot, DeleteObservationRequest request) {
        if (CollectionHelper.isNotNullOrEmpty(dot.getOfferingArray())) {
            request.setOfferings(Sets.newHashSet(dot.getOfferingArray()));
        }
    }

    private void parseTemporalFilter(DeleteObservationType dot, DeleteObservationRequest request)
            throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(dot.getTemporalFilterArray())) {
            request.setTemporalFilters(parseTemporalFilters(dot.getTemporalFilterArray()));
        }
    }

    private Set<TemporalFilter> parseTemporalFilters(final DeleteObservationType.TemporalFilter[] temporalFilters)
            throws DecodingException {
        final Set<TemporalFilter> sosTemporalFilters = Sets.newHashSetWithExpectedSize(temporalFilters.length);
        for (final DeleteObservationType.TemporalFilter temporalFilter : temporalFilters) {
            final Object filter = decodeXmlElement(temporalFilter.getTemporalOps());
            if (filter instanceof TemporalFilter) {
                sosTemporalFilters.add((TemporalFilter) filter);
            }
        }
        return sosTemporalFilters;
    }

    public Set<SupportedType> getSupportedTypes() {
        return Collections.emptySet();
    }

}
